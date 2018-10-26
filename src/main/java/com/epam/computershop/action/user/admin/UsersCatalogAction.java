package com.epam.computershop.action.user.admin;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class UsersCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(UsersCatalogAction.class);

    public UsersCatalogAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        UserDao userDao = new UserDao();
        try {
            req.setAttribute(ConstantStorage.USERS, userDao.findAll());
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select all users");
        }
        return JSP_USER_CATALOG;
    }
}
