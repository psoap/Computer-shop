package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class CategoryDao extends Dao<Category> {

    public void insertCategoryWithTranslations(List<Category> categories)
            throws SQLException, ConnectionPoolException {
        Category category = categories.get(ZERO);
        insert(category);
        insertCategoryTranslations(category.getId(), categories);
    }

    public void insertCategoriesTranslations(List<Category> categories)
            throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        connection.setAutoCommit(false);
        try (PreparedStatement insertTranslationPreparedStatement =
                     connection.prepareStatement(INSERT_CATEGORY_TRANSLATION)) {
            for (Category category : categories) {
                fillTranslationStatement(insertTranslationPreparedStatement, category);
                insertTranslationPreparedStatement.execute();
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

    @Override
    public void insert(Category entity) throws SQLException, ConnectionPoolException {
        insertDefault(entity, INSERT_CATEGORY);
    }

    @Override
    public void update(Category entity) throws SQLException, ConnectionPoolException {
        updateDefault(entity, UPDATE_CATEGORY_TRANSLATION);
    }

    @Override
    public void remove(Category entity) throws SQLException, ConnectionPoolException {
        removeDefault(entity, DELETE_CATEGORY);
    }

    public Map<Locale, CopyOnWriteArrayList<Category>> findAllByEachLocale(List<Locale> locales)
            throws SQLException, ConnectionPoolException {
        Map<Locale, CopyOnWriteArrayList<Category>> allCategories = new HashMap<>(locales.size());
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_CATEGORIES_BY_LOCALE)) {
            for (Locale locale : locales) {
                findAllPreparedStatement.setString(INDEX_1, locale.getLanguage());
                allCategories.put(locale, getLocaleCategories(findAllPreparedStatement, locale));
            }
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
        return allCategories;
    }

    private void insertCategoryTranslations(short categoryId, List<Category> categories)
            throws ConnectionPoolException, SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertTranslatePreparedStatement =
                     connection.prepareStatement(INSERT_CATEGORY_TRANSLATION)) {
            for (Category categoryTranslation : categories) {
                categoryTranslation.setId(categoryId);
                fillTranslationStatement(insertTranslatePreparedStatement, categoryTranslation);
                insertTranslatePreparedStatement.execute();
            }
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    protected void fillInsertStatement(PreparedStatement preparedStatement, Category entity) throws SQLException {
        if (entity.getParentId() == null) {
            preparedStatement.setNull(INDEX_1, Types.NULL);
        } else {
            preparedStatement.setShort(INDEX_1, entity.getParentId());
        }
    }

    @Override
    protected void fillUpdateStatement(PreparedStatement preparedStatement, Category entity) throws SQLException {
        preparedStatement.setString(INDEX_1, entity.getName());
        preparedStatement.setShort(INDEX_2, entity.getId());
        preparedStatement.setString(INDEX_3, entity.getLangCode());
    }

    @Override
    protected void fillRemoveStatement(PreparedStatement preparedStatement, Category entity) throws SQLException {
        preparedStatement.setShort(INDEX_1, entity.getId());
        preparedStatement.setShort(INDEX_2, entity.getId());
        preparedStatement.setShort(INDEX_3, entity.getId());
        preparedStatement.setShort(INDEX_4, entity.getId());
    }

    @Override
    protected void setEntityPrimaryKeyField(PreparedStatement preparedStatement, Category entity) throws SQLException {
        entity.setId((short) getGeneratedPrimaryKey(preparedStatement));
    }

    private void fillTranslationStatement(PreparedStatement preparedStatement, Category category)
            throws SQLException {
        preparedStatement.setShort(INDEX_1, category.getId());
        preparedStatement.setString(INDEX_2, category.getName());
        preparedStatement.setString(INDEX_3, category.getLangCode());
    }

    private CopyOnWriteArrayList<Category> getLocaleCategories(PreparedStatement preparedStatement,
                                                               Locale currentLocale) throws SQLException {
        CopyOnWriteArrayList<Category> localeCategories = new CopyOnWriteArrayList<>();
        try (ResultSet categoriesResultSet = preparedStatement.executeQuery()) {
            while (categoriesResultSet.next()) {
                Category category = new Category();
                category.setId(categoriesResultSet.getShort(COLUMN_ID));
                category.setName(categoriesResultSet.getString(COLUMN_NAME));
                category.setParentId(categoriesResultSet.getShort(COLUMN_PARENT_ID));
                category.setLangCode(currentLocale.getLanguage());
                localeCategories.add(category);
            }
        }
        return localeCategories;
    }
}