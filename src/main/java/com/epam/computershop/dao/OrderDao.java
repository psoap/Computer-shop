package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Order;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class OrderDao extends Dao<Order> {
    @Override
    public void insert(Order entity) throws SQLException, ConnectionPoolException {
        insertDefault(entity, INSERT_ORDER);
    }

    @Override
    public void update(Order entity) throws SQLException, ConnectionPoolException {
        updateDefault(entity, UPDATE_ORDER);
    }

    @Override
    public void remove(Order entity) throws SQLException, ConnectionPoolException {
        removeDefault(entity, DELETE_ORDER);
    }

    public Order findById(long id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Order order = null;
        try (PreparedStatement findOnePreparedStatement =
                     connection.prepareStatement(SELECT_ORDER_BY_ID)) {
            findOnePreparedStatement.setLong(INDEX_1, id);
            try (ResultSet ordersResultSet = findOnePreparedStatement.executeQuery()) {
                if (ordersResultSet.next()) {
                    order = getFilledOrder(ordersResultSet);
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
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_ORDERS)) {
            findAllPreparedStatement.setLong(INDEX_1, userId);
            fillOrders(findAllPreparedStatement, orders);
            return orders;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Order> findUserOrdersByStatus(long userId, OrderStatus status)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_ORDERS_BY_STATUS_AND_USER_ID)) {
            findAllPreparedStatement.setLong(INDEX_1, userId);
            findAllPreparedStatement.setShort(INDEX_2, status.getStatusId());
            fillOrders(findAllPreparedStatement, orders);
            return orders;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Order> findAllUsersOrdersByStatus(OrderStatus status)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_ORDERS_BY_STATUS)) {
            findAllPreparedStatement.setShort(INDEX_1, status.getStatusId());
            fillOrders(findAllPreparedStatement, orders);
            return orders;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private void fillStatement(PreparedStatement preparedStatement, Order order) throws SQLException {
        if(order.getDeliveryProfileId() == null){
            preparedStatement.setNull(INDEX_1, Types.NULL);
        } else {
            preparedStatement.setLong(INDEX_1, order.getDeliveryProfileId());
        }
        preparedStatement.setShort(INDEX_2, order.getStatus().getStatusId());
        preparedStatement.setBigDecimal(INDEX_3, order.getTotalPrice());
        preparedStatement.setTimestamp(INDEX_4, order.getChangeDate());
    }

    @Override
    protected void fillInsertStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setLong(INDEX_5, entity.getUserId());
    }

    @Override
    protected void fillUpdateStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setLong(INDEX_5, entity.getId());
    }

    @Override
    protected void fillRemoveStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        preparedStatement.setLong(INDEX_1, entity.getId());
        preparedStatement.setLong(INDEX_2, entity.getId());
    }

    @Override
    protected void setEntityPrimaryKeyField(PreparedStatement preparedStatement, Order entity) throws SQLException {
        entity.setId(getGeneratedPrimaryKey(preparedStatement));
    }

    private void fillOrders(PreparedStatement findAllPreparedStatement, List<Order> orders)
            throws SQLException {
        try (ResultSet ordersResultSet = findAllPreparedStatement.executeQuery()) {
            while (ordersResultSet.next()) {
                orders.add(getFilledOrder(ordersResultSet));
            }
        }
    }

    private Order getFilledOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong(COLUMN_ID));
        order.setUserId(resultSet.getLong(COLUMN_USER_ID));
        order.setStatus(OrderStatus.getStatusById(resultSet.getShort(COLUMN_STATUS_ID)));
        order.setDeliveryProfileId(resultSet.getLong(COLUMN_DELIVERY_PROFILE_ID));
        order.setTotalPrice(resultSet.getBigDecimal(COLUMN_TOTAL_PRICE));
        order.setChangeDate(resultSet.getTimestamp(COLUMN_CHANGE_DATE));
        return order;
    }
}