package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class ProductDao extends Dao<Product> {
    @Override
    public void insert(Product entity) throws SQLException, ConnectionPoolException {
        insertDefault(entity, INSERT_PRODUCT);
    }

    @Override
    public void update(Product entity) throws SQLException, ConnectionPoolException {
        updateDefault(entity, UPDATE_PRODUCT);
    }

    @Override
    public void remove(Product entity) throws SQLException, ConnectionPoolException {
        removeDefault(entity, DELETE_PRODUCT);
    }

    public List<Product> findAllByCategory(short id, short limit, int offset)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_PRODUCTS_BY_CATEGORY)) {
            findAllPreparedStatement.setShort(INDEX_1, id);
            findAllPreparedStatement.setShort(INDEX_2, limit);
            findAllPreparedStatement.setInt(INDEX_3, offset);
            return getProducts(findAllPreparedStatement);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public Product findById(long id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Product product = null;
        try (PreparedStatement findOnePreparedStatement =
                     connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            findOnePreparedStatement.setLong(INDEX_1, id);
            try (ResultSet productResultSet = findOnePreparedStatement.executeQuery()) {
                if (productResultSet.next()) {
                    product = getProduct(productResultSet);
                }
            }
            return product;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<Product> findBySearchQuery(String query) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_PRODUCTS_BY_SEARCH_QUERY)) {
            findAllPreparedStatement.setString(INDEX_1, '%' + query + '%');
            return getProducts(findAllPreparedStatement);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public int getCountOfRowsByCategory(short id) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_PRODUCTS_COUNT_BY_CATEGORY)) {
            findAllPreparedStatement.setShort(INDEX_1, id);
            int count = ZERO;
            try (ResultSet productsResultSet = findAllPreparedStatement.executeQuery()) {
                if (productsResultSet.next()) {
                    count = productsResultSet.getInt(INDEX_1);
                }
            }
            return count;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private void fillStatement(PreparedStatement preparedStatement, Product product) throws SQLException {
        preparedStatement.setString(INDEX_1, product.getName());
        preparedStatement.setString(INDEX_2, product.getShortDescription());
        preparedStatement.setString(INDEX_3, product.getDescription());
        preparedStatement.setBigDecimal(INDEX_4, product.getPrice());
        preparedStatement.setShort(INDEX_5, product.getCategoryId());
        if (product.getImageUrl() == null) {
            preparedStatement.setNull(INDEX_6, Types.NULL);
        } else {
            preparedStatement.setString(INDEX_6, product.getImageUrl());
        }
    }

    @Override
    protected void fillInsertStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        fillStatement(preparedStatement, entity);
    }

    @Override
    protected void fillUpdateStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setLong(INDEX_7, entity.getId());
    }

    @Override
    protected void fillRemoveStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        preparedStatement.setLong(INDEX_1, entity.getId());
    }

    @Override
    protected void setEntityPrimaryKeyField(PreparedStatement preparedStatement, Product entity) throws SQLException {
        entity.setId(getGeneratedPrimaryKey(preparedStatement));
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong(COLUMN_ID));
        product.setName(resultSet.getString(COLUMN_NAME));
        product.setShortDescription(resultSet.getString(COLUMN_SHORT_DESCRIPTION));
        product.setDescription(resultSet.getString(COLUMN_DESCRIPTION));
        product.setPrice(resultSet.getBigDecimal(COLUMN_PRICE));
        product.setCategoryId(resultSet.getShort(COLUMN_CATEGORY_ID));
        product.setImageUrl(resultSet.getString(COLUMN_IMAGE_URL));
        return product;
    }

    private List<Product> getProducts(PreparedStatement preparedStatement)
            throws SQLException {
        List<Product> products = new ArrayList<>();
        try (ResultSet productsResultSet = preparedStatement.executeQuery()) {
            while (productsResultSet.next()) {
                products.add(getProduct(productsResultSet));
            }
        }
        return products;
    }
}