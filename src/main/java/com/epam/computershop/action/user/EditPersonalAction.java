package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class EditPersonalAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(EditPersonalAction.class);

    public EditPersonalAction(short accessRoleId) {
        super(accessRoleId);
    }

    private void updateUser(User currentUser, String email, String password, List<String> messagesForJsp) {
        UserDao userDao = new UserDao();
        User dummyUser = new User();
        dummyUser.setId(currentUser.getId());
        dummyUser.setEmail(email);
        dummyUser.setBalance(currentUser.getBalance());
        dummyUser.setRoleId(currentUser.getRoleId());
        dummyUser.setPassword(password);
        try {
            userDao.update(dummyUser);
            currentUser.setEmail(dummyUser.getEmail());
            currentUser.setPassword(dummyUser.getPassword());
            LOGGER.debug("Personal data was updated by user - " + currentUser.getLogin());
            messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update data by user - " + currentUser.getLogin());
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);

        String password = req.getParameter(ConstantStorage.PASSWORD);
        String confirmPassword = req.getParameter(ConstantStorage.CONFIRM_PASSWORD);

        if (password != null && confirmPassword != null
                && !password.equals(ConstantStorage.EMPTY_STRING)
                && !confirmPassword.equals(ConstantStorage.EMPTY_STRING)) {
            if (!password.equals(confirmPassword)) {
                messagesForJsp.add(ConstantStorage.CHANGE_USER_WARN_NOT_EQUALS_PASS);
            } else if (!Validator.validate(ValidatorSettingsStorage.REGISTRATION_LOGIN_PASSWORD_PATTERN, password)) {
                messagesForJsp.add(ConstantStorage.CHANGE_USER_WARN_BAD_PASSWORD);
            } else {
                try {
                    password = HashUtil.getSHA1(password);
                } catch (NoSuchAlgorithmException e) {
                    messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
                }
            }
        } else {
            password = currentUser.getPassword();
        }

        String email = req.getParameter(ConstantStorage.EMAIL);
        if (!Validator.validate(ValidatorSettingsStorage.REGISTRATION_EMAIL_PATTERN, email)) {
            messagesForJsp.add(ConstantStorage.CHANGE_USER_WARN_BAD_EMAIL);
        }
        if (messagesForJsp.isEmpty()) {
            updateUser(currentUser, email, password, messagesForJsp);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_USER_SHOW_PERSONAL_PAGE);
    }
}