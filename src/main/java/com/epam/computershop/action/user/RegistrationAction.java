package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.HashUtil;
import com.epam.computershop.util.Validator;
import com.epam.computershop.util.ValidatorSettingsStorage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class RegistrationAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RegistrationAction.class);

    public RegistrationAction(short accessRoleId) {
        super(accessRoleId);
    }

    private static void validateForm(String login, String email, String password, List<String> messagesForJsp) {
        if (!Validator.validate(ValidatorSettingsStorage.REGISTRATION_LOGIN_USERNAME_PATTERN, login)) {
            messagesForJsp.add(ConstantStorage.REGISTRATION_WARN_BAD_LOGIN);
        }
        if (!Validator.validate(ValidatorSettingsStorage.REGISTRATION_EMAIL_PATTERN, email)) {
            messagesForJsp.add(ConstantStorage.CHANGE_USER_WARN_BAD_EMAIL);
        }
        if (!Validator.validate(ValidatorSettingsStorage.REGISTRATION_LOGIN_PASSWORD_PATTERN, password)) {
            messagesForJsp.add(ConstantStorage.CHANGE_USER_WARN_BAD_PASSWORD);
        }
    }

    private String createUser(String login, String email, String password, List<String> messagesForJsp) {
        String responseUrl = ActionFactory.ACTION_USER_SHOW_REGISTRATION_PAGE;
        try {
            UserDao userDao = new UserDao();
            User existedUser = userDao.findUserByLogin(login);
            if (existedUser == null) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setLogin(login);
                String passwordHash = HashUtil.getSHA1(password);
                newUser.setPassword(passwordHash);
                newUser.setRoleId(ConstantStorage.ONE);
                newUser.setBalance(ConstantStorage.ZERO_BALANCE);
                userDao.insert(newUser);
                LOGGER.debug("User - " + login + " was created");
                messagesForJsp.add(ConstantStorage.CHANGE_USER_SUCCESS_CREATE_USER);
                responseUrl = ActionFactory.ACTION_USER_SHOW_LOGIN_PAGE;
            } else {
                messagesForJsp.add(ConstantStorage.REGISTRATION_WARN_BUSY_LOGIN);
            }
        } catch (SQLException | ConnectionPoolException | NoSuchAlgorithmException e) {
            LOGGER.error("Failed to insert new user");
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
        return responseUrl;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = ActionFactory.ACTION_USER_SHOW_REGISTRATION_PAGE;

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        String password = req.getParameter(ConstantStorage.PASSWORD);
        String confirmPassword = req.getParameter(ConstantStorage.CONFIRM_PASSWORD);

        if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
            messagesForJsp.add(ConstantStorage.CHANGE_USER_WARN_NOT_EQUALS_PASS);
        }
        String login = req.getParameter(ConstantStorage.LOGIN);
        String email = req.getParameter(ConstantStorage.EMAIL);
        validateForm(login, email, password, messagesForJsp);

        if (messagesForJsp.isEmpty()) {
            responseUrl = createUser(login, email, password, messagesForJsp);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH)).concat(responseUrl);
    }
}
