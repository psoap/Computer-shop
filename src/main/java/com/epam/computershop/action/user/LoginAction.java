package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
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

public class LoginAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(LoginAction.class);

    public LoginAction(short accessRoleId) {
        super(accessRoleId);
    }

    private static boolean validateForm(String login, String password) {
        return Validator.validate(ValidatorSettingsStorage.REGISTRATION_LOGIN_USERNAME_PATTERN, login)
                && Validator.validate(ValidatorSettingsStorage.REGISTRATION_LOGIN_PASSWORD_PATTERN, password);
    }

    private void updateUserBasket(HttpSession session, User user) {
        Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(ConstantStorage.BASKET);
        OrderDao orderDao = new OrderDao();
        OrderProductDao orderProductDao = new OrderProductDao();
        try {
            List<Order> buffBasket = orderDao.findUserOrdersByStatus(user.getId(), ConstantStorage.ORDER_STATUS_BASKET);
            if (sessionBasket != null) {
                if (!buffBasket.isEmpty()) {
                    Order databaseCart = buffBasket.get(ConstantStorage.ZERO);
                    List<Long> basketProductsIds = orderProductDao.findAllProductsIdsByOrder(databaseCart.getId());
                    sessionBasket.keySet()
                            .removeAll(basketProductsIds);
                    if (!sessionBasket.isEmpty()) {
                        orderProductDao.insertLarge(user.getId(), ConstantStorage.ORDER_STATUS_BASKET, sessionBasket);
                    }
                } else {
                    Order newBasket = new Order();
                    newBasket.setUserId(user.getId());
                    newBasket.setStatusId(ConstantStorage.ORDER_STATUS_BASKET);
                    newBasket.setTotalPrice(ConstantStorage.ZERO_BALANCE);
                    newBasket.setChangeDate(new Timestamp(System.currentTimeMillis()));
                    orderDao.insert(newBasket);
                    orderProductDao.insertLarge(user.getId(), ConstantStorage.ORDER_STATUS_BASKET, sessionBasket);
                }
                session.removeAttribute(ConstantStorage.BASKET);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to load user's basket while authentication");
        }
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_USER_SHOW_LOGIN_PAGE);

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        String login = req.getParameter(ConstantStorage.LOGIN);
        String password = req.getParameter(ConstantStorage.PASSWORD);

        if (!validateForm(login, password)) {
            messagesForJsp.add(ConstantStorage.LOGIN_WARN_BAD_DATA);
        } else {
            UserDao userDao = new UserDao();
            try {
                User user = userDao.findUserByLogin(login);
                if (user != null && user.getPassword().equals(HashUtil.getSHA1(password))) {
                    HttpSession session = req.getSession();
                    session.setAttribute(ConstantStorage.CURRENT_USER, user);
                    updateUserBasket(session, user);
                    LOGGER.debug("User - " + user.getLogin() + " authenticated at session - " + session.getId());
                    responseUrl = ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL))
                            .concat(JSP_INDEX);
                } else {
                    messagesForJsp.add(ConstantStorage.LOGIN_WARN_BAD_DATA);
                }
            } catch (SQLException | ConnectionPoolException | NoSuchAlgorithmException e) {
                LOGGER.error("Failed to authenticate user - " + login);
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return responseUrl;
    }
}
