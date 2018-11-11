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

public class EditPersonalAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(EditPersonalAction.class);

    public EditPersonalAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        String password = getUpdatedPassword(currentUser, req, messagesForJsp);
        String email = req.getParameter(EMAIL);
        validateEmail(email, messagesForJsp);

        if (messagesForJsp.isEmpty()) {
            updateUser(currentUser, email, password, messagesForJsp);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_USER_SHOW_PERSONAL_PAGE);
    }

    private void updateUser(User currentUser, String email, String password, List<String> messagesForJsp) {
        User dummyUser = new User();
        dummyUser.setId(currentUser.getId());
        dummyUser.setEmail(email);
        dummyUser.setBalance(currentUser.getBalance());
        dummyUser.setRole(currentUser.getRole());
        dummyUser.setPassword(password);

        UserDao userDao = new UserDao();
        try {
            userDao.update(dummyUser);
            currentUser.setEmail(dummyUser.getEmail());
            currentUser.setPassword(dummyUser.getPassword());
            LOGGER.debug("Personal data was updated by user - " + currentUser.getLogin());
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update data by user - " + currentUser.getLogin());
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
    }

    private void validateEmail(String email, List<String> messagesForJsp){
        if (!validate(REGISTRATION_EMAIL_PATTERN, email)) {
            messagesForJsp.add(CHANGE_USER_WARN_BAD_EMAIL);
        }
    }

    private String getUpdatedPassword(User currentUser, HttpServletRequest req, List<String> messagesForJsp){
        String password = req.getParameter(PASSWORD);
        String confirmPassword = req.getParameter(CONFIRM_PASSWORD);
        if (isPasswordChanged(password, confirmPassword)) {
            if (!password.equals(confirmPassword)) {
                messagesForJsp.add(CHANGE_USER_WARN_NOT_EQUALS_PASS);
            } else if (!validate(REGISTRATION_LOGIN_PASSWORD_PATTERN, password)) {
                messagesForJsp.add(CHANGE_USER_WARN_BAD_PASSWORD);
            } else {
                try {
                    password = HashUtil.getSha1(password);
                } catch (NoSuchAlgorithmException e) {
                    messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
                }
            }
        } else {
            password = currentUser.getPassword();
        }
        return password;
    }

    private boolean isPasswordChanged(String password, String confirmPassword){
        return password != null && confirmPassword != null
                && !password.equals(EMPTY_STRING)
                && !confirmPassword.equals(EMPTY_STRING);
    }
}