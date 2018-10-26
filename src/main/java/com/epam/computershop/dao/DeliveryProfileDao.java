package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.SQLQueriesStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProfileDao implements Dao<DeliveryProfile> {
    @Override
    public DeliveryProfile insert(DeliveryProfile entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(SQLQueriesStorage.INSERT_DELIVERY_PROFILE, Statement.RETURN_GENERATED_KEYS)) {
            insertPreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getUserId());
            insertPreparedStatement.setString(ConstantStorage.INDEX_2, entity.getFirstName());
            insertPreparedStatement.setString(ConstantStorage.INDEX_3, entity.getLastName());
            insertPreparedStatement.setString(ConstantStorage.INDEX_4, entity.getPatronymic());
            insertPreparedStatement.setString(ConstantStorage.INDEX_5, entity.getAddressLocation());
            insertPreparedStatement.setString(ConstantStorage.INDEX_6, entity.getPhoneNumber());
            insertPreparedStatement.execute();
            ResultSet keysResultSet = insertPreparedStatement.getGeneratedKeys();
            if (keysResultSet.next()) {
                entity.setId(keysResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
            }
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public DeliveryProfile update(DeliveryProfile entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(SQLQueriesStorage.UPDATE_DELIVERY_PROFILE)) {
            updatePreparedStatement.setString(ConstantStorage.INDEX_1, entity.getFirstName());
            updatePreparedStatement.setString(ConstantStorage.INDEX_2, entity.getLastName());
            updatePreparedStatement.setString(ConstantStorage.INDEX_3, entity.getPatronymic());
            updatePreparedStatement.setString(ConstantStorage.INDEX_4, entity.getAddressLocation());
            updatePreparedStatement.setString(ConstantStorage.INDEX_5, entity.getPhoneNumber());
            updatePreparedStatement.setLong(ConstantStorage.INDEX_6, entity.getId());
            updatePreparedStatement.execute();
            return entity;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void remove(DeliveryProfile entity) throws SQLException, ConnectionPoolException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(SQLQueriesStorage.DELETE_DELIVERY_PROFILE)) {
            deletePreparedStatement.setLong(ConstantStorage.INDEX_1, entity.getId());
            deletePreparedStatement.execute();
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public List<DeliveryProfile> findAllByUserId(long userId) throws SQLException, ConnectionPoolException {
        List<DeliveryProfile> profiles = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_DELIVERY_PROFILES_BY_USER_ID)) {
            findAllPreparedStatement.setLong(ConstantStorage.INDEX_1, userId);
            try (ResultSet profilesResultSet = findAllPreparedStatement.executeQuery()) {
                fillListFromResultSet(profilesResultSet, profiles);
            }
            return profiles;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public DeliveryProfile findById(long id) throws ConnectionPoolException, SQLException {
        DeliveryProfile profile = null;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findOnePreparedStatement = connection.prepareStatement(SQLQueriesStorage.SELECT_USER_DELIVERY_PROFILE_BY_ID)) {
            findOnePreparedStatement.setLong(ConstantStorage.INDEX_1, id);
            try (ResultSet profileResultSet = findOnePreparedStatement.executeQuery()) {
                if (profileResultSet.next()) {
                    profile = new DeliveryProfile();
                    profile.setId(profileResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
                    profile.setUserId(profileResultSet.getLong(SQLQueriesStorage.COLUMN_USER_ID));
                    profile.setFirstName(profileResultSet.getString(SQLQueriesStorage.COLUMN_FIRST_NAME));
                    profile.setLastName(profileResultSet.getString(SQLQueriesStorage.COLUMN_LAST_NAME));
                    profile.setPatronymic(profileResultSet.getString(SQLQueriesStorage.COLUMN_PATRONYMIC));
                    profile.setAddressLocation(profileResultSet.getString(SQLQueriesStorage.COLUMN_ADDRESS_LOCATION));
                    profile.setPhoneNumber(profileResultSet.getString(SQLQueriesStorage.COLUMN_PHONE_NUMBER));
                }
            }
            return profile;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private static void fillListFromResultSet(ResultSet profilesResultSet, List<DeliveryProfile> profilesList) throws SQLException {
        while (profilesResultSet.next()) {
            DeliveryProfile profile = new DeliveryProfile();
            profile.setId(profilesResultSet.getLong(SQLQueriesStorage.COLUMN_ID));
            profile.setUserId(profilesResultSet.getLong(SQLQueriesStorage.COLUMN_USER_ID));
            profile.setFirstName(profilesResultSet.getString(SQLQueriesStorage.COLUMN_FIRST_NAME));
            profile.setLastName(profilesResultSet.getString(SQLQueriesStorage.COLUMN_LAST_NAME));
            profile.setPatronymic(profilesResultSet.getString(SQLQueriesStorage.COLUMN_PATRONYMIC));
            profile.setAddressLocation(profilesResultSet.getString(SQLQueriesStorage.COLUMN_ADDRESS_LOCATION));
            profile.setPhoneNumber(profilesResultSet.getString(SQLQueriesStorage.COLUMN_PHONE_NUMBER));
            profilesList.add(profile);
        }
    }
}
