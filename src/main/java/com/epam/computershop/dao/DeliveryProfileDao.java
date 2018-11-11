package com.epam.computershop.dao;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.exception.ConnectionPoolException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.SQLQueriesStorage.*;

public class DeliveryProfileDao extends Dao<DeliveryProfile> {
    @Override
    public void insert(DeliveryProfile entity) throws SQLException, ConnectionPoolException {
        defaultInsert(entity,INSERT_DELIVERY_PROFILE);
    }

    @Override
    public void update(DeliveryProfile entity) throws SQLException, ConnectionPoolException {
        defaultUpdate(entity, UPDATE_DELIVERY_PROFILE);
    }

    @Override
    public void remove(DeliveryProfile entity) throws SQLException, ConnectionPoolException {
        defaultRemove(entity, DELETE_DELIVERY_PROFILE);
    }

    public List<DeliveryProfile> findAllByUserId(long userId) throws SQLException, ConnectionPoolException {
        List<DeliveryProfile> profiles = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findAllPreparedStatement =
                     connection.prepareStatement(SELECT_DELIVERY_PROFILES_BY_USER_ID)) {
            findAllPreparedStatement.setLong(INDEX_1, userId);
            try (ResultSet profilesResultSet = findAllPreparedStatement.executeQuery()) {
                while (profilesResultSet.next()) {
                    profiles.add(getFilledProfile(profilesResultSet));
                }
            }
            return profiles;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public DeliveryProfile findById(long id) throws ConnectionPoolException, SQLException {
        DeliveryProfile profile = null;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement findOnePreparedStatement =
                     connection.prepareStatement(SELECT_USER_DELIVERY_PROFILE_BY_ID)) {
            findOnePreparedStatement.setLong(INDEX_1, id);
            try (ResultSet profileResultSet = findOnePreparedStatement.executeQuery()) {
                if (profileResultSet.next()) {
                    profile = getFilledProfile(profileResultSet);
                }
            }
            return profile;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    private void fillStatement(PreparedStatement preparedStatement, DeliveryProfile deliveryProfile)
            throws SQLException {
        preparedStatement.setString(INDEX_1, deliveryProfile.getFirstName());
        preparedStatement.setString(INDEX_2, deliveryProfile.getLastName());
        preparedStatement.setString(INDEX_3, deliveryProfile.getPatronymic());
        preparedStatement.setString(INDEX_4, deliveryProfile.getAddressLocation());
        preparedStatement.setString(INDEX_5, deliveryProfile.getPhoneNumber());
    }

    @Override
    protected void fillInsertStatement(PreparedStatement preparedStatement, DeliveryProfile entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setLong(INDEX_6, entity.getUserId());
    }

    @Override
    protected void fillUpdateStatement(PreparedStatement preparedStatement, DeliveryProfile entity) throws SQLException {
        fillStatement(preparedStatement, entity);
        preparedStatement.setLong(INDEX_6, entity.getId());
    }

    @Override
    protected void fillRemoveStatement(PreparedStatement preparedStatement, DeliveryProfile entity) throws SQLException {
        preparedStatement.setLong(INDEX_1, entity.getId());
    }

    @Override
    protected void setEntityPrimaryKeyField(PreparedStatement preparedStatement, DeliveryProfile entity) throws SQLException {
        entity.setId(getGeneratedPrimaryKey(preparedStatement));
    }

    private DeliveryProfile getFilledProfile(ResultSet resultSet) throws SQLException {
        DeliveryProfile profile = new DeliveryProfile();
        profile.setId(resultSet.getLong(COLUMN_ID));
        profile.setUserId(resultSet.getLong(COLUMN_USER_ID));
        profile.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
        profile.setLastName(resultSet.getString(COLUMN_LAST_NAME));
        profile.setPatronymic(resultSet.getString(COLUMN_PATRONYMIC));
        profile.setAddressLocation(resultSet.getString(COLUMN_ADDRESS_LOCATION));
        profile.setPhoneNumber(resultSet.getString(COLUMN_PHONE_NUMBER));
        return profile;
    }
}
