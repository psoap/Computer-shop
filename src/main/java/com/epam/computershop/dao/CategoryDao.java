package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.SQLQueriesStorage;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CategoryDao implements Dao<Category> {

    @Override
    public Category insert(Category entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            insertPreparedStatement.setShort(ConstantStorage.INDEX_1, entity.getParentId());
            insertPreparedStatement.setString(ConstantStorage.INDEX_1, entity.getName());
            insertPreparedStatement.setString(ConstantStorage.INDEX_3, entity.getLangCode());
            insertPreparedStatement.execute();
            ResultSet keysResultSet = insertPreparedStatement.getGeneratedKeys();
            if (keysResultSet.next()) {
                short insertedCategoryId = keysResultSet.getShort(ConstantStorage.ID);
                entity.setId(insertedCategoryId);
            }
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public Category update(Category entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.UPDATE_CATEGORY)) {
            updatePreparedStatement.setString(ConstantStorage.INDEX_1, entity.getName());
            updatePreparedStatement.setShort(ConstantStorage.INDEX_2, entity.getId());
            updatePreparedStatement.setString(ConstantStorage.INDEX_3, entity.getLangCode());
            updatePreparedStatement.execute();
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void remove(Category entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(SQLQueriesStorage.DELETE_CATEGORY)) {
            deletePreparedStatement.setShort(ConstantStorage.INDEX_1, entity.getId());
            deletePreparedStatement.setShort(ConstantStorage.INDEX_2, entity.getId());
            deletePreparedStatement.setShort(ConstantStorage.INDEX_3, entity.getId());
            deletePreparedStatement.setShort(ConstantStorage.INDEX_4, entity.getId());
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public Map<Locale, CopyOnWriteArrayList<Category>> findAllByLangs(List<Locale> langs) throws SQLException, ConnectionPoolException {
        Map<Locale, CopyOnWriteArrayList<Category>> all_categories = new HashMap<>(langs.size());
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_CATEGORIES_BY_LANG)) {
            for (Locale lang : langs) {
                findAllPreparedStatement.setString(ConstantStorage.INDEX_1, lang.getLanguage());
                CopyOnWriteArrayList<Category> langCategories = new CopyOnWriteArrayList<>();
                try (ResultSet categoriesRS = findAllPreparedStatement.executeQuery()) {
                    while (categoriesRS.next()) {
                        Category category = new Category();
                        category.setId(categoriesRS.getShort(SQLQueriesStorage.COLUMN_ID));
                        category.setName(categoriesRS.getString(SQLQueriesStorage.COLUMN_NAME));
                        category.setParentId(categoriesRS.getShort(SQLQueriesStorage.COLUMN_PARENT_ID));
                        category.setLangCode(lang.getLanguage());
                        langCategories.add(category);
                    }
                }
                all_categories.put(lang, langCategories);
            }
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
        return all_categories;
    }

    public void insertList(List<Category> entities) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        if (!entities.isEmpty()) {
            Category category = entities.get(ConstantStorage.ZERO);
            try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
                if (category.getParentId() != null) {
                    insertPreparedStatement.setShort(ConstantStorage.INDEX_1, category.getParentId());
                } else {
                    insertPreparedStatement.setNull(ConstantStorage.INDEX_1, Types.NULL);
                }
                insertPreparedStatement.execute();
                try (ResultSet keysResultSet = insertPreparedStatement.getGeneratedKeys()) {
                    if (keysResultSet.next()) {
                        short id = keysResultSet.getShort(ConstantStorage.ID);
                        try (PreparedStatement insertTranslatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_CATEGORY_TRANSLATE)) {
                            for (Category entity : entities) {
                                entity.setId(id);
                                insertTranslatePreparedStatement.setShort(ConstantStorage.INDEX_1, id);
                                insertTranslatePreparedStatement.setString(ConstantStorage.INDEX_2, entity.getName());
                                insertTranslatePreparedStatement.setString(ConstantStorage.INDEX_3, entity.getLangCode());
                                insertTranslatePreparedStatement.execute();
                            }
                        }
                    } else {
                        throw new SQLException("Can't get generated keys");
                    }
                }
            } finally {
                ConnectionPool.getInstance().returnConnection(connection);
            }
        }
    }

    public void insertTranslateList(List<Category> entities) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        if (!entities.isEmpty()) {
            try (PreparedStatement insertTranslatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_CATEGORY_TRANSLATE)) {
                for (Category entity : entities) {
                    insertTranslatePreparedStatement.setShort(ConstantStorage.INDEX_1, entity.getId());
                    insertTranslatePreparedStatement.setString(ConstantStorage.INDEX_2, entity.getName());
                    insertTranslatePreparedStatement.setString(ConstantStorage.INDEX_3, entity.getLangCode());
                    insertTranslatePreparedStatement.execute();
                }
            } finally {
                ConnectionPool.getInstance().returnConnection(connection);
            }
        }
    }
}
