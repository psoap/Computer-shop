package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Order;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.SQLQueriesStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements Dao<Order> {
    @Override
    public Order insert(Order entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            insertPreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getUserId());
            insertPreparedStatement.setNull(ConstantStorage.INDEX_2, Types.NULL);
            insertPreparedStatement.setShort(ConstantStorage.INDEX_3, entity.getStatusId());
            insertPreparedStatement.setBigDecimal(ConstantStorage.INDEX_4, entity.getTotalPrice());
            insertPreparedStatement.setTimestamp(ConstantStorage.INDEX_5, entity.getChangeDate());
            insertPreparedStatement.execute();
            try (ResultSet keysResultSet = insertPreparedStatement.getGeneratedKeys()) {
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
    public Order update(Order entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.UPDATE_ORDER)) {
            updatePreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getDeliveryProfileId());
            updatePreparedStatement.setShort(ConstantStorage.INDEX_2, entity.getStatusId());
            updatePreparedStatement.setBigDecimal(ConstantStorage.INDEX_3, entity.getTotalPrice());
            updatePreparedStatement.setTimestamp(ConstantStorage.INDEX_4, entity.getChangeDate());
            updatePreparedStatement.setLong(ConstantStorage.INDEX_5, entity.getId());
            updatePreparedStatement.execute();
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void remove(Order entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(SQLQueriesStorage.DELETE_ORDER)) {
            deletePreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getId());
            deletePreparedStatement.setLong(ConstantStorage.INDEX_2, entity.getId());
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public Order findById(long id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Order order = null;
        try (PreparedStatement findOnePreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ORDER_BY_ID)) {
            findOnePreparedStatement.setLong(ConstantStorage.INDEX_1, id);
            try (ResultSet ordersResultSet = findOnePreparedStatement.executeQuery()) {
                if (ordersResultSet.next()) {
                    order = new Order();
                    order.setId(ordersResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
                    order.setUserId(ordersResultSet.getLong(SQLQueriesStorage.COLUMN_USER_ID));
                    order.setStatusId(ordersResultSet.getShort(SQLQueriesStorage.COLUMN_STATUS_ID));
                    order.setDeliveryProfileId(ordersResultSet.getLong(SQLQueriesStorage.COLUMN_DELIVERY_PROFILE_ID));
                    order.setTotalPrice(ordersResultSet.getBigDecimal(SQLQueriesStorage.COLUMN_TOTAL_PRICE));
                    order.setChangeDate(ordersResultSet.getTimestamp(SQLQueriesStorage.COLUMN_CHANGE_DATE));
                }
            }
            return order;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }

    }

    public List<Order> findAllByUserId(long userId) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ORDERS)) {
            findAllPreparedStatement.setLong(ConstantStorage.INDEX_1, userId);
            fillListFromResultSet(findAllPreparedStatement, orders);
            return orders;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Order> findUserOrdersByStatus(long userId, short statusId) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ORDERS_BY_STATUS_AND_USER_ID)) {
            findAllPreparedStatement.setLong(ConstantStorage.INDEX_1, userId);
            findAllPreparedStatement.setShort(ConstantStorage.INDEX_2, statusId);
            fillListFromResultSet(findAllPreparedStatement, orders);
            return orders;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Order> findAllUsersOrdersByStatus(short statusId) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ORDERS_BY_STATUS)) {
            findAllPreparedStatement.setShort(ConstantStorage.INDEX_1, statusId);
            fillListFromResultSet(findAllPreparedStatement, orders);
            return orders;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private static void fillListFromResultSet(PreparedStatement findAllPreparedStatement, List<Order> orders) throws SQLException {
        try (ResultSet ordersResultSet = findAllPreparedStatement.executeQuery()) {
            while (ordersResultSet.next()) {
                Order order = new Order();
                order.setId(ordersResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
                order.setUserId(ordersResultSet.getLong(SQLQueriesStorage.COLUMN_USER_ID));
                order.setStatusId(ordersResultSet.getShort(SQLQueriesStorage.COLUMN_STATUS_ID));
                order.setDeliveryProfileId(ordersResultSet.getLong(SQLQueriesStorage.COLUMN_DELIVERY_PROFILE_ID));
                order.setTotalPrice(ordersResultSet.getBigDecimal(SQLQueriesStorage.COLUMN_TOTAL_PRICE));
                order.setChangeDate(ordersResultSet.getTimestamp(SQLQueriesStorage.COLUMN_CHANGE_DATE));
                orders.add(order);
            }
        }
    }
}
