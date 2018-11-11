package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.exception.ConnectionPoolException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class LangDao {
    public void insert(String code, String name) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement =
                     connection.prepareStatement(INSERT_LOCALE)) {
            insertPreparedStatement.setString(INDEX_1, code);
            insertPreparedStatement.setString(INDEX_2, name);
            insertPreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Locale> findAll() throws SQLException, ConnectionPoolException {
        List<Locale> locales = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_ALL_LOCALES);
             ResultSet localesResultSet = findAllPreparedStatement.executeQuery()) {
            while (localesResultSet.next()) {
                Locale locale = new Locale(localesResultSet.getString(COLUMN_CODE));
                locales.add(locale);
            }
            return locales;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}
