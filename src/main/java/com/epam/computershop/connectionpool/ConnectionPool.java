package com.epam.computershop.connectionpool;

import com.epam.computershop.exception.ConnectionPoolException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final String PROPERTY_FILE_NAME = "database.properties";
    private static final String JDBC_DRIVER_PROPERTY = "jdbc.driver";
    private static final String JDBC_URL_PROPERTY = "jdbc.url";
    private static final String JDBC_USERNAME_PROPERTY = "jdbc.username";
    private static final String JDBC_PASSWORD_PROPERTY = "jdbc.password";
    private static final String JDBC_POOL_SIZE_PROPERTY = "jdbc.pool_size";

    private static volatile ConnectionPool currentConnectionPool;

    private List<Connection> allConnections;
    private BlockingQueue<Connection> freeConnections;
    private String driverName;
    private String userName;
    private String password;
    private String url;
    private int poolSize;

    private ConnectionPool() throws ConnectionPoolException {
        loadProperties();
        loadJdbcDriver();
        allConnections = new ArrayList<>(poolSize);
        initConnections();
        freeConnections = new ArrayBlockingQueue<>(poolSize);
        freeConnections.addAll(allConnections);
    }

    public static ConnectionPool getInstance() throws ConnectionPoolException {
        ConnectionPool connectionPool = currentConnectionPool;
        if (connectionPool == null) {
            Lock lock = new ReentrantLock();
            lock.lock();
            connectionPool = currentConnectionPool;
            if (connectionPool == null) {
                connectionPool = currentConnectionPool = new ConnectionPool();
            }
            lock.unlock();
        }
        return connectionPool;
    }

    public Connection getConnection() throws ConnectionPoolException {
        try {
            return freeConnections.take();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Failed to get connection.", ex);
        }
    }

    public void returnConnection(Connection connection) {
        if ((freeConnections.size() < poolSize) && (connection != null)) {
            freeConnections.add(connection);
        }
    }

    public void close() throws ConnectionPoolException {
        for (Connection connection : allConnections) {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new ConnectionPoolException("Failed to close connections.", ex);
            }
        }
    }

    private void initConnections() throws ConnectionPoolException {
        for (int i = 0; i < poolSize; i++) {
            try {
                allConnections.add(DriverManager.getConnection(url, userName, password));
            } catch (SQLException ex) {
                throw new ConnectionPoolException("Failed to init connections.", ex);
            }
        }
    }

    private void loadJdbcDriver() throws ConnectionPoolException {
        try {
            Driver driver = (Driver) Class.forName(driverName).newInstance();
            DriverManager.registerDriver(driver);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            throw new ConnectionPoolException("Failed to load JDBC driver.", ex);
        }
    }

    private void loadProperties() throws ConnectionPoolException {
        try (InputStream connectionPoolProperties =
                     ConnectionPool.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            if (connectionPoolProperties != null) {
                Properties properties = new Properties();
                properties.load(connectionPoolProperties);
                driverName = properties.getProperty(JDBC_DRIVER_PROPERTY);
                userName = properties.getProperty(JDBC_USERNAME_PROPERTY);
                password = properties.getProperty(JDBC_PASSWORD_PROPERTY);
                url = properties.getProperty(JDBC_URL_PROPERTY);
                poolSize = Integer.parseInt(properties.getProperty(JDBC_POOL_SIZE_PROPERTY));
            } else {
                throw new ConnectionPoolException("Property file not found.");
            }
        } catch (IOException ex) {
            throw new ConnectionPoolException("Failed to load property file.", ex);
        }
    }
}