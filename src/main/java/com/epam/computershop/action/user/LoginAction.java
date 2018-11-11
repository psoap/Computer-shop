package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.Validator.*;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class LoginAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(LoginAction.class);

    public LoginAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_USER_SHOW_LOGIN_PAGE);

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        String login = req.getParameter(LOGIN);
        String password = req.getParameter(PASSWORD);

        if (validateForm(login, password)) {
            if (authentication(login, password, req.getSession(), messagesForJsp)) {
                responseUrl = ((String) req.getServletContext().getAttribute(APPLICATION_URL))
                        .concat(JSP_INDEX);
            }
        } else {
            messagesForJsp.add(LOGIN_WARN_BAD_DATA);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return responseUrl;
    }

    private boolean authentication(String login, String password,
                                   HttpSession session, List<String> messagesForJsp) {
        boolean result = false;
        UserDao userDao = new UserDao();
        try {
            User user = userDao.findUserByLogin(login);
            if ((user != null) && user.getPassword().equals(HashUtil.getSha1(password))) {
                session.setAttribute(CURRENT_USER, user);
                updateUserBasket(session, user);
                LOGGER.debug("User - " + user.getLogin() + " authenticated at session - " + session.getId());
                result = true;
            } else {
                messagesForJsp.add(LOGIN_WARN_BAD_DATA);
            }
        } catch (SQLException | ConnectionPoolException | NoSuchAlgorithmException e) {
            LOGGER.error("Failed to authenticate user - " + login, e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return result;
    }

    private static boolean validateForm(String login, String password) {
        return validate(REGISTRATION_LOGIN_USERNAME_PATTERN, login)
                && validate(REGISTRATION_LOGIN_PASSWORD_PATTERN, password);
    }

    private void updateUserBasket(HttpSession session, User user) {
        Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(BASKET);
        OrderDao orderDao = new OrderDao();
        OrderProductDao orderProductDao = new OrderProductDao();
        try {
            List<Order> buffBasket = orderDao.findUserOrdersByStatus(user.getId(), OrderStatus.BASKET);
            if (sessionBasket != null) {
                if (!buffBasket.isEmpty()) {
                    updateExistedBasket(sessionBasket, orderProductDao, user);
                } else {
                    createAndFillBasket(sessionBasket, orderDao, orderProductDao, user);
                }
                session.removeAttribute(BASKET);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to load user's basket while authentication.", e);
        }
    }

    private void updateExistedBasket(Map<Long, Short> sessionBasket, OrderProductDao orderProductDao,
                                     User user) throws SQLException, ConnectionPoolException {
        orderProductDao.insertLarge(user.getId(), OrderStatus.BASKET, sessionBasket);
    }

    private void createAndFillBasket(Map<Long, Short> sessionBasket, OrderDao orderDao,
                                     OrderProductDao orderProductDao, User user)
            throws SQLException, ConnectionPoolException {
        Order newBasket = new Order();
        newBasket.setUserId(user.getId());
        newBasket.setStatus(OrderStatus.BASKET);
        newBasket.setTotalPrice(ZERO_BALANCE);
        newBasket.setChangeDate(new Timestamp(System.currentTimeMillis()));
        orderDao.insert(newBasket);
        orderProductDao.insertLarge(user.getId(), OrderStatus.BASKET, sessionBasket);
    }
}
