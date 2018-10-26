package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.SQLQueriesStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements Dao<Product> {
    @Override
    public Product insert(Product entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            fillStatementFromEntity(insertPreparedStatement, entity);
            insertPreparedStatement.execute();
            try (ResultSet keysRS = insertPreparedStatement.getGeneratedKeys()) {
                if (keysRS.next()) {
                    entity.setId(keysRS.getLong(SQLQueriesStorage.COLUMN_ID));
                }
            }
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public Product update(Product entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.UPDATE_PRODUCT)) {
            fillStatementFromEntity(updatePreparedStatement, entity);
            updatePreparedStatement.setLong(ConstantStorage.INDEX_7, entity.getId());
            updatePreparedStatement.execute();
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void remove(Product entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(SQLQueriesStorage.DELETE_PRODUCT)) {
            deletePreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getId());
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Product> findAllByCategory(short id, short limit, int offset) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Product> products = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_PRODUCTS_BY_CATEGORY)) {
            findAllPreparedStatement.setShort(ConstantStorage.INDEX_1, id);
            findAllPreparedStatement.setShort(ConstantStorage.INDEX_2, limit);
            findAllPreparedStatement.setInt(ConstantStorage.INDEX_3, offset);
            fillListFromResultSet(findAllPreparedStatement, products);
            return products;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public Product findById(long id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Product product = null;
        try (PreparedStatement findOnePreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_PRODUCT_BY_ID)) {
            findOnePreparedStatement.setLong(ConstantStorage.INDEX_1, id);
            try (ResultSet productResultSet = findOnePreparedStatement.executeQuery()) {
                if (productResultSet.next()) {
                    product = fillProductFromResultSet(productResultSet);
                }
            }
            return product;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Product> findBySearchQuery(String query) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Product> products = new ArrayList<>();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_PRODUCTS_BY_SEARCH_QUERY)) {
            findAllPreparedStatement.setString(ConstantStorage.INDEX_1, "%" + query + "%");
            fillListFromResultSet(findAllPreparedStatement, products);
            return products;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private static void fillStatementFromEntity(PreparedStatement preparedStatement, Product product) throws SQLException {
        preparedStatement.setString(ConstantStorage.INDEX_1, product.getName());
        preparedStatement.setString(ConstantStorage.INDEX_2, product.getShortDescription());
        preparedStatement.setString(ConstantStorage.INDEX_3, product.getDescription());
        preparedStatement.setBigDecimal(ConstantStorage.INDEX_4, product.getPrice());
        preparedStatement.setShort(ConstantStorage.INDEX_5, product.getCategoryId());
        if (product.getImageUrl() == null) {
            preparedStatement.setNull(ConstantStorage.INDEX_6, Types.NULL);
        } else {
            preparedStatement.setString(ConstantStorage.INDEX_6, product.getImageUrl());
        }
    }

    private static Product fillProductFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong(SQLQueriesStorage.COLUMN_ID));
        product.setName(resultSet.getString(SQLQueriesStorage.COLUMN_NAME));
        product.setShortDescription(resultSet.getString(SQLQueriesStorage.COLUMN_SHORT_DESCRIPTION));
        product.setDescription(resultSet.getString(SQLQueriesStorage.COLUMN_DESCRIPTION));
        product.setPrice(resultSet.getBigDecimal(SQLQueriesStorage.COLUMN_PRICE));
        product.setCategoryId(resultSet.getShort(SQLQueriesStorage.COLUMN_CATEGORY_ID));
        product.setImageUrl(resultSet.getString(SQLQueriesStorage.COLUMN_IMAGE_URL));
        return product;
    }

    private static void fillListFromResultSet(PreparedStatement findAllPreparedStatement, List<Product> products) throws SQLException {
        try (ResultSet productsResultSet = findAllPreparedStatement.executeQuery()) {
            while (productsResultSet.next()) {
                products.add(fillProductFromResultSet(productsResultSet));
            }
        }
    }

    public int getCountOfRowsByCategory(short id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_PRODUCTS_COUNT_BY_CATEGORY)) {
            findAllPreparedStatement.setShort(ConstantStorage.INDEX_1, id);
            int count = ConstantStorage.ZERO;
            try (ResultSet productsResultSet = findAllPreparedStatement.executeQuery()) {
                if (productsResultSet.next()) {
                    count = productsResultSet.getInt(ConstantStorage.INDEX_1);
                }
            }
            return count;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}