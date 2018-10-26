package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.SQLQueriesStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LangDao {
    public void insert(String code, String name) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_LANG)) {
            insertPreparedStatement.setString(ConstantStorage.INDEX_1, code);
            insertPreparedStatement.setString(ConstantStorage.INDEX_2, name);
            insertPreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Locale> findAll() throws SQLException, ConnectionPoolException {
        List<Locale> langs = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ALL_LANGS);
             ResultSet langsResultSet = findAllPreparedStatement.executeQuery()) {
            while (langsResultSet.next()) {
                Locale lang = new Locale(langsResultSet.getString(SQLQueriesStorage.COLUMN_CODE));
                langs.add(lang);
            }
            return langs;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}
