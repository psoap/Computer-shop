package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.SQLQueriesStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProductDao {
    public void insertLarge(long userId, short orderStatusId, Map<Long, Short> products) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_ORDER_PRODUCT)) {
            for (Long productId : products.keySet()) {
                insertPreparedStatement.setLong(ConstantStorage.INDEX_1, productId);
                insertPreparedStatement.setShort(ConstantStorage.INDEX_2, products.get(productId));
                insertPreparedStatement.setLong(ConstantStorage.INDEX_3, userId);
                insertPreparedStatement.setShort(ConstantStorage.INDEX_4, orderStatusId);
                insertPreparedStatement.setShort(ConstantStorage.INDEX_5, orderStatusId);
                insertPreparedStatement.execute();
            }
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public boolean insert(long userId, short orderStatusId, long productId, Short quantity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_ORDER_PRODUCT)) {
            insertPreparedStatement.setLong(ConstantStorage.INDEX_1, productId);
            insertPreparedStatement.setShort(ConstantStorage.INDEX_2, quantity);
            insertPreparedStatement.setLong(ConstantStorage.INDEX_3, userId);
            insertPreparedStatement.setShort(ConstantStorage.INDEX_4, orderStatusId);
            insertPreparedStatement.setShort(ConstantStorage.INDEX_5, quantity);
            return insertPreparedStatement.executeUpdate() == ConstantStorage.ZERO;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public void update(long userId, short orderStatusId, long productId, Short quantity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.UPDATE_ORDER_PRODUCT)) {
            insertPreparedStatement.setShort(ConstantStorage.INDEX_1, quantity);
            insertPreparedStatement.setLong(ConstantStorage.INDEX_2, userId);
            insertPreparedStatement.setShort(ConstantStorage.INDEX_3, orderStatusId);
            insertPreparedStatement.setLong(ConstantStorage.INDEX_4, productId);
            insertPreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public void remove(long userId, short orderStatusId, long productId) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(SQLQueriesStorage.DELETE_ORDER_PRODUCT)) {
            deletePreparedStatement.setLong(ConstantStorage.INDEX_1, userId);
            deletePreparedStatement.setLong(ConstantStorage.INDEX_2, orderStatusId);
            deletePreparedStatement.setLong(ConstantStorage.INDEX_3, productId);
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public Map<Product, Short> findAllByOrderId(long orderId) throws SQLException, ConnectionPoolException {
        Map<Product, Short> orderProducts = new HashMap<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ORDER_PRODUCT)) {
            findAllPreparedStatement.setLong(ConstantStorage.INDEX_1, orderId);
            try (ResultSet orderProductsResultSet = findAllPreparedStatement.executeQuery()) {
                while (orderProductsResultSet.next()) {
                    Product product = new Product();
                    product.setId(orderProductsResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
                    product.setName(orderProductsResultSet.getString(SQLQueriesStorage.COLUMN_NAME));
                    product.setShortDescription(orderProductsResultSet.getString(SQLQueriesStorage.COLUMN_SHORT_DESCRIPTION));
                    product.setPrice(orderProductsResultSet.getBigDecimal(SQLQueriesStorage.COLUMN_PRICE));
                    product.setImageUrl(orderProductsResultSet.getString(SQLQueriesStorage.COLUMN_IMAGE_URL));
                    Short quantity = orderProductsResultSet.getShort(SQLQueriesStorage.COLUMN_QUANTITY);
                    orderProducts.put(product, quantity);
                }
            }
            return orderProducts;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Long> findAllProductsIdsByOrder(long orderId) throws SQLException, ConnectionPoolException {
        List<Long> orderProductsIds = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_ORDER_PRODUCTS_IDS)) {
            findAllPreparedStatement.setLong(ConstantStorage.INDEX_1, orderId);
            try (ResultSet orderProductsIdsResultSet = findAllPreparedStatement.executeQuery()) {
                while (orderProductsIdsResultSet.next()) {
                    orderProductsIds.add(orderProductsIdsResultSet.getLong(SQLQueriesStorage.COLUMN_PRODUCT_ID));
                }
            }
            return orderProductsIds;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}
