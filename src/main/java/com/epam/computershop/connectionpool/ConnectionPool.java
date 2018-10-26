package com.epam.computershop.connectionpool;

import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPool.class);

    private static final String PROPERTY_FILE_NAME = "database.properties";
    private static final String JDBC_DRIVER_PROPERTY = "jdbc.driver";
    private static final String JDBC_URL_PROPERTY = "jdbc.url";
    private static final String JDBC_USERNAME_PROPERTY = "jdbc.username";
    private static final String JDBC_PASSWORD_PROPERTY = "jdbc.password";
    private static final String JDBC_POOL_SIZE_PROPERTY = "jdbc.poolsize";

    private static volatile ConnectionPool currentConnectionPool;

    private BlockingQueue<Connection> freeConnections;
    private List<Connection> busyConnections;
    private String driverName;
    private String userName;
    private String password;
    private String url;
    private int poolSize;


    private ConnectionPool() throws ConnectionPoolException {
        loadProperties();
        freeConnections = new ArrayBlockingQueue<>(poolSize);
        busyConnections = new ArrayList<>(poolSize);
        loadJDBCDriver();
        initFreeConnections();
    }

    public static ConnectionPool getInstance() throws ConnectionPoolException {
        ConnectionPool connectionPool = currentConnectionPool;
        if (connectionPool == null) {
            synchronized (ConnectionPool.class) {
                connectionPool = currentConnectionPool;
                if (connectionPool == null) {
                    connectionPool = currentConnectionPool = new ConnectionPool();
                }
            }
        }
        return connectionPool;
    }

    private void initFreeConnections() throws ConnectionPoolException {
        for (int i = 0; i < poolSize; i++) {
            try {
                freeConnections.add(DriverManager.getConnection(url, userName, password));
            } catch (SQLException ex) {
                LOGGER.error("Failed to init free connections list");
                throw new ConnectionPoolException();
            }
        }
    }

    private void loadJDBCDriver() throws ConnectionPoolException {
        try {
            Driver driver = (Driver) Class.forName(driverName).newInstance();
            DriverManager.registerDriver(driver);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            LOGGER.error("Failed to load JDBC driver");
            throw new ConnectionPoolException();
        }
    }

    public Connection getConnection() throws ConnectionPoolException {
        try {
            if (freeConnections.size() > ConstantStorage.ZERO) {
                Connection connection = freeConnections.take();
                busyConnections.add(connection);
                return connection;
            } else {
                Iterator<Connection> it = busyConnections.iterator();
                while (it.hasNext()) {
                    if (!it.next().isValid(ConstantStorage.ZERO)) {
                        it.remove();
                        Connection connection = DriverManager.getConnection(url, userName, password);
                        if (freeConnections.add(connection)) {
                            return connection;
                        }
                    }
                }
            }
        } catch (InterruptedException | SQLException ex) {
            LOGGER.error("Failed to get free connection");
        }
        throw new ConnectionPoolException();
    }

    public void returnConnection(Connection connection) {
        if (freeConnections.size() < poolSize) {
            if (busyConnections.remove(connection)) {
                freeConnections.add(connection);
            }
        }
    }

    private void loadProperties() throws ConnectionPoolException {
        try (InputStream cpProps = ConnectionPool.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            if (cpProps != null) {
                Properties properties = new Properties();
                properties.load(cpProps);
                driverName = properties.getProperty(JDBC_DRIVER_PROPERTY);
                userName = properties.getProperty(JDBC_USERNAME_PROPERTY);
                password = properties.getProperty(JDBC_PASSWORD_PROPERTY);
                url = properties.getProperty(JDBC_URL_PROPERTY);
                poolSize = Integer.parseInt(properties.getProperty(JDBC_POOL_SIZE_PROPERTY));
            } else {
                LOGGER.error("Property file is not found");
                throw new ConnectionPoolException();
            }
        } catch (IOException ex) {
            LOGGER.error("Failed to load property file");
            throw new ConnectionPoolException();
        }
    }

    public void close() {
        freeConnections.forEach(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close free connections list");
            }
        });
        busyConnections.forEach(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close busy connections list");
            }
        });
    }
}