package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Entity;
import com.epam.computershop.exception.ConnectionPoolException;

import java.sql.*;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

abstract class Dao<T extends Entity> {
    public abstract void insert(T entity) throws SQLException, ConnectionPoolException;

    public abstract void update(T entity) throws SQLException, ConnectionPoolException;

    public abstract void remove(T entity) throws SQLException, ConnectionPoolException;

    protected void defaultInsert(T entity, String sql) throws ConnectionPoolException, SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement =
                     connection.prepareStatement(sql,
                             Statement.RETURN_GENERATED_KEYS)) {
            fillInsertStatement(insertPreparedStatement, entity);
            insertPreparedStatement.execute();
            setEntityPrimaryKeyField(insertPreparedStatement, entity);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    protected void defaultUpdate(T entity, String sql) throws ConnectionPoolException, SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement updatePreparedStatement =
                     connection.prepareStatement(sql)) {
            fillUpdateStatement(updatePreparedStatement, entity);
            updatePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    protected void defaultRemove(T entity, String sql) throws ConnectionPoolException, SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement =
                     connection.prepareStatement(sql)) {
            fillRemoveStatement(deletePreparedStatement, entity);
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    protected static long getGeneratedPrimaryKey(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet keysResultSet = preparedStatement.getGeneratedKeys()) {
            return (keysResultSet != null && keysResultSet.next()) ? keysResultSet.getLong(COLUMN_ID) : ZERO;
        }
    }

    protected abstract void fillInsertStatement(PreparedStatement preparedStatement, T entity)
            throws SQLException;

    protected abstract void fillUpdateStatement(PreparedStatement preparedStatement, T entity)
            throws SQLException;

    protected abstract void fillRemoveStatement(PreparedStatement preparedStatement, T entity)
            throws SQLException;

    protected abstract void setEntityPrimaryKeyField(PreparedStatement preparedStatement, T entity)
            throws SQLException;

}