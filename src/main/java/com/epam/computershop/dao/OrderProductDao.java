package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class OrderProductDao {
    public void insertLarge(long userId, OrderStatus status, Map<Long, Short> products)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        connection.setAutoCommit(false);
        try (PreparedStatement insertPreparedStatement =
                     connection.prepareStatement(INSERT_ORDER_PRODUCT)) {
            for (Map.Entry productIdQuantity : products.entrySet()) {
                fillStatement(insertPreparedStatement, userId, status,
                        (Long) productIdQuantity.getKey(), (Short) productIdQuantity.getValue());
                insertPreparedStatement.execute();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public boolean insert(long userId, OrderStatus status, long productId, short quantity)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement =
                     connection.prepareStatement(INSERT_ORDER_PRODUCT)) {
            fillStatement(insertPreparedStatement, userId, status, productId, quantity);
            return insertPreparedStatement.executeUpdate() == ZERO;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public void update(long userId, OrderStatus status, long productId, Short quantity)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement =
                     connection.prepareStatement(UPDATE_ORDER_PRODUCT)) {
            insertPreparedStatement.setShort(INDEX_1, quantity);
            insertPreparedStatement.setLong(INDEX_2, userId);
            insertPreparedStatement.setShort(INDEX_3, status.getStatusId());
            insertPreparedStatement.setLong(INDEX_4, productId);
            insertPreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public void remove(long userId, OrderStatus status, long productId)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement =
                     connection.prepareStatement(DELETE_ORDER_PRODUCT)) {
            deletePreparedStatement.setLong(INDEX_1, userId);
            deletePreparedStatement.setShort(INDEX_2, status.getStatusId());
            deletePreparedStatement.setLong(INDEX_3, productId);
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public Map<Product, Short> findAllByOrderId(long orderId)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_ORDER_PRODUCT)) {
            findAllPreparedStatement.setLong(INDEX_1, orderId);
            return getOrderedProducts(findAllPreparedStatement);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private Map<Product, Short> getOrderedProducts(PreparedStatement preparedStatement)
            throws SQLException {
        Map<Product, Short> orderProducts = new HashMap<>();
        try (ResultSet orderProductsResultSet = preparedStatement.executeQuery()) {
            while (orderProductsResultSet.next()) {
                Product product = new Product();
                product.setId(orderProductsResultSet.getLong(COLUMN_ID));
                product.setName(orderProductsResultSet.getString(COLUMN_NAME));
                product.setShortDescription(orderProductsResultSet.getString(COLUMN_SHORT_DESCRIPTION));
                product.setPrice(orderProductsResultSet.getBigDecimal(COLUMN_PRICE));
                product.setImageUrl(orderProductsResultSet.getString(COLUMN_IMAGE_URL));
                Short quantity = orderProductsResultSet.getShort(COLUMN_QUANTITY);
                orderProducts.put(product, quantity);
            }
        }
        return orderProducts;
    }

    private void fillStatement(PreparedStatement preparedStatement, long userId,
                               OrderStatus status, Long productId, Short quantity)
            throws SQLException {
        preparedStatement.setLong(INDEX_1, productId);
        preparedStatement.setShort(INDEX_2, quantity);
        preparedStatement.setLong(INDEX_3, userId);
        preparedStatement.setShort(INDEX_4, status.getStatusId());
        preparedStatement.setShort(INDEX_5, quantity);
    }
}
