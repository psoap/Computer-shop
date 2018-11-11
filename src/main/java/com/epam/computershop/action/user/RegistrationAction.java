package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.Validator.*;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class RegistrationAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RegistrationAction.class);

    public RegistrationAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = ActionFactory.ACTION_USER_SHOW_REGISTRATION_PAGE;

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        String password = req.getParameter(PASSWORD);
        String confirmPassword = req.getParameter(CONFIRM_PASSWORD);

        if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
            messagesForJsp.add(CHANGE_USER_WARN_NOT_EQUALS_PASS);
        }
        String login = req.getParameter(LOGIN);
        String email = req.getParameter(EMAIL);
        validateForm(login, email, password, messagesForJsp);

        if (messagesForJsp.isEmpty()) {
            responseUrl = registration(login, email, password, messagesForJsp);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH)).concat(responseUrl);
    }

    private String registration(String login, String email, String password, List<String> messagesForJsp) {
        String responseUrl = ActionFactory.ACTION_USER_SHOW_REGISTRATION_PAGE;
        try {
            UserDao userDao = new UserDao();
            User existedUser = userDao.findUserByLogin(login);
            if (existedUser == null) {
                createUser(userDao, login, email, password);
                LOGGER.debug("User - " + login + " was created");
                messagesForJsp.add(CHANGE_USER_SUCCESS_CREATE_USER);
                responseUrl = ActionFactory.ACTION_USER_SHOW_LOGIN_PAGE;
            } else {
                messagesForJsp.add(REGISTRATION_WARN_BUSY_LOGIN);
            }
        } catch (SQLException | ConnectionPoolException | NoSuchAlgorithmException e) {
            LOGGER.error("Failed to insert new user");
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return responseUrl;
    }

    private void createUser(UserDao userDao, String login, String email, String password)
            throws NoSuchAlgorithmException, SQLException, ConnectionPoolException {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setLogin(login);
        String passwordHash = HashUtil.getSha1(password);
        newUser.setPassword(passwordHash);
        newUser.setRole(UserRole.USER);
        newUser.setBalance(ZERO_BALANCE);
        userDao.insert(newUser);
    }

    private static void validateForm(String login, String email, String password, List<String> messagesForJsp) {
        if (!validate(REGISTRATION_LOGIN_USERNAME_PATTERN, login)) {
            messagesForJsp.add(REGISTRATION_WARN_BAD_LOGIN);
        }
        if (!validate(REGISTRATION_EMAIL_PATTERN, email)) {
            messagesForJsp.add(CHANGE_USER_WARN_BAD_EMAIL);
        }
        if (!validate(REGISTRATION_LOGIN_PASSWORD_PATTERN, password)) {
            messagesForJsp.add(CHANGE_USER_WARN_BAD_PASSWORD);
        }
    }
}
