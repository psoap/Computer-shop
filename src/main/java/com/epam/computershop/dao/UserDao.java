package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class UserDao extends Dao<User> {
    @Override
    public void insert(User entity) throws SQLException, ConnectionPoolException {
        insertDefault(entity, INSERT_USER);
    }

    @Override
    public void update(User entity) throws SQLException, ConnectionPoolException {
        updateDefault(entity, UPDATE_USER);
    }

    @Override
    public void remove(User entity) throws SQLException, ConnectionPoolException {
        removeDefault(entity, DELETE_USER);
    }

    public List<User> findAll() throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_ALL_USERS);
             ResultSet usersResultSet = findAllPreparedStatement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (usersResultSet.next()) {
                users.add(getUser(usersResultSet));
            }
            return users;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public User findUserByLogin(String login) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findOnePreparedStatement =
                     connection.prepareStatement(SELECT_USER_BY_LOGIN)) {
            findOnePreparedStatement.setString(INDEX_1, login);
            try (ResultSet usersResultSet = findOnePreparedStatement.executeQuery()) {
                return findUser(usersResultSet);
            }
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public User findUserById(long id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findOnePreparedStatement =
                     connection.prepareStatement(SELECT_USER_BY_ID)) {
            findOnePreparedStatement.setLong(INDEX_1, id);
            try (ResultSet usersResultSet = findOnePreparedStatement.executeQuery()) {
                return findUser(usersResultSet);
            }
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private void fillStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(INDEX_1, user.getEmail());
        preparedStatement.setString(INDEX_2, user.getPassword());
        preparedStatement.setShort(INDEX_3, user.getRole().getId());
        preparedStatement.setBigDecimal(INDEX_4, user.getBalance());
    }

    @Override
    protected void fillInsertStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setString(INDEX_5, entity.getLogin());
    }

    @Override
    protected void fillUpdateStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setLong(INDEX_5, entity.getId());
    }

    @Override
    protected void fillRemoveStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        preparedStatement.setLong(INDEX_1, entity.getId());
    }

    @Override
    protected void setEntityPrimaryKeyField(PreparedStatement preparedStatement, User entity) throws SQLException {
        entity.setId(getGeneratedPrimaryKey(preparedStatement));
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(COLUMN_ID));
        user.setEmail(resultSet.getString(COLUMN_EMAIL));
        user.setLogin(resultSet.getString(COLUMN_LOGIN));
        user.setPassword(resultSet.getString(COLUMN_PASSWORD));
        user.setRole(UserRole.getRoleById(resultSet.getShort(COLUMN_ROLE_ID)));
        user.setBalance(resultSet.getBigDecimal(COLUMN_BALANCE));
        return user;
    }

    private User findUser(ResultSet usersResultSet) throws SQLException {
        return usersResultSet.next() ? getUser(usersResultSet) : null;
    }
}
