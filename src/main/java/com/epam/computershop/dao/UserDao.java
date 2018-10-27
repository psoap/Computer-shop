package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.SQLQueriesStorage;
import com.epam.computershop.util.ConstantStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {

    @Override
    public User insert(User entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            insertPreparedStatement.setString(ConstantStorage.INDEX_1, entity.getEmail());
            insertPreparedStatement.setString(ConstantStorage.INDEX_2, entity.getLogin());
            insertPreparedStatement.setString(ConstantStorage.INDEX_3, entity.getPassword());
            insertPreparedStatement.setShort(ConstantStorage.INDEX_4, entity.getRoleId());
            insertPreparedStatement.setBigDecimal(ConstantStorage.INDEX_5, entity.getBalance());
            insertPreparedStatement.execute();
            try(ResultSet keysResultSet = insertPreparedStatement.getGeneratedKeys()){
                if (keysResultSet.next()) {
                    entity.setId(keysResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
                }
            }
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public User update(User entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.UPDATE_USER)) {
            updatePreparedStatement.setString(ConstantStorage.INDEX_1, entity.getEmail());
            updatePreparedStatement.setString(ConstantStorage.INDEX_2, entity.getPassword());
            updatePreparedStatement.setShort(ConstantStorage.INDEX_3, entity.getRoleId());
            updatePreparedStatement.setBigDecimal(ConstantStorage.INDEX_4, entity.getBalance());
            updatePreparedStatement.setLong(ConstantStorage.INDEX_5, entity.getId());
            updatePreparedStatement.execute();
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void remove(User entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(SQLQueriesStorage.DELETE_USER)) {
            deletePreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getId());
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<User> findAll() throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ALL_USERS);
             ResultSet usersResultSet = findAllPreparedStatement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (usersResultSet.next()) {
                User user = new User();
                user.setId(usersResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
                user.setEmail(usersResultSet.getString(SQLQueriesStorage.COLUMN_EMAIL));
                user.setLogin(usersResultSet.getString(SQLQueriesStorage.COLUMN_LOGIN));
                user.setPassword(usersResultSet.getString(SQLQueriesStorage.COLUMN_PASSWORD));
                user.setRoleId(usersResultSet.getShort(SQLQueriesStorage.COLUMN_ROLE_ID));
                user.setBalance(usersResultSet.getBigDecimal(SQLQueriesStorage.COLUMN_BALANCE));
                users.add(user);
            }
            return users;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public User findUserByLogin(String login) throws SQLException, ConnectionPoolException {
        User user;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findOnePreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_USER_BY_LOGIN)) {
            findOnePreparedStatement.setString(ConstantStorage.INDEX_1, login);
            try (ResultSet usersResultSet = findOnePreparedStatement.executeQuery()) {
                user = fillUserFromResultSet(usersResultSet);
            }
            return user;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public User findUserById(long id) throws SQLException, ConnectionPoolException {
        User user;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findOnePreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_USER_BY_ID)) {
            findOnePreparedStatement.setLong(ConstantStorage.INDEX_1, id);
            try (ResultSet usersResultSet = findOnePreparedStatement.executeQuery()) {
                user = fillUserFromResultSet(usersResultSet);
            }
            return user;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private static User fillUserFromResultSet(ResultSet usersResultSet) throws SQLException {
        if (usersResultSet.next()) {
            User user = new User();
            user.setId(usersResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
            user.setEmail(usersResultSet.getString(SQLQueriesStorage.COLUMN_EMAIL));
            user.setLogin(usersResultSet.getString(SQLQueriesStorage.COLUMN_LOGIN));
            user.setPassword(usersResultSet.getString(SQLQueriesStorage.COLUMN_PASSWORD));
            user.setRoleId(usersResultSet.getShort(SQLQueriesStorage.COLUMN_ROLE_ID));
            user.setBalance(usersResultSet.getBigDecimal(SQLQueriesStorage.COLUMN_BALANCE));
            return user;
        } else {
            return null;
        }
    }
}
