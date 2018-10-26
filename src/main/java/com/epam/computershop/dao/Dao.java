package com.epam.computershop.dao;

import com.epam.computershop.entity.Entity;
import com.epam.computershop.exception.ConnectionPoolException;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T extends Entity> {
    T insert(T entity) throws SQLException, ConnectionPoolException;

    T update(T entity) throws SQLException, ConnectionPoolException;

    void remove(T entity) throws SQLException, ConnectionPoolException;
}