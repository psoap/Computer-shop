package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class CheckoutAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(CheckoutAction.class);

    public CheckoutAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        BigDecimal totalPrice = (BigDecimal) session.getAttribute(TOTAL_PRICE);
        long deliveryProfileIdFromRequest = tryParseLong(req.getParameter(CURRENT_DELIVERY_PROFILE));
        if ((totalPrice != null) && (deliveryProfileIdFromRequest != INVALID_NUMBER)
                && isEnoughBalance(currentUser, totalPrice)) {
            try {
                DeliveryProfile profile = findDeliveryProfile(deliveryProfileIdFromRequest);

                checkout(profile, currentUser, totalPrice, resp, messagesForJsp);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to update basket status, by user - " + currentUser.getLogin());
                messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
            }
        } else {
            messagesForJsp.add(CHECKOUT_WARN_NOT_ENOUGH_BALANCE);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_BASKET_SHOW);
    }

    private DeliveryProfile findDeliveryProfile(long deliveryProfileId)
            throws ConnectionPoolException, SQLException {
        DeliveryProfileDao profileDao = new DeliveryProfileDao();
        return profileDao.findById(deliveryProfileId);
    }

    private void checkout(DeliveryProfile profile, User currentUser, BigDecimal totalPrice,
                          HttpServletResponse resp, List<String> messagesForJsp)
            throws SQLException, ConnectionPoolException {
        OrderDao orderDao = new OrderDao();
        List<Order> buffOrders = orderDao.findUserOrdersByStatus(currentUser.getId(), OrderStatus.BASKET);
        if (!buffOrders.isEmpty() && (profile != null) && (profile.getUserId() == currentUser.getId())) {
            Order basket = buffOrders.get(ZERO);
            basket.setStatus(OrderStatus.PAID);
            basket.setDeliveryProfileId(profile.getId());
            basket.setTotalPrice(totalPrice);
            orderDao.update(basket);

            updateUserBalance(currentUser, totalPrice);

            LOGGER.debug("Basket was ordered, by user -" + currentUser.getLogin());
            messagesForJsp.add(GENERAL_SUCCESS);
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        }
    }

    private void updateUserBalance(User currentUser, BigDecimal totalPrice)
            throws SQLException, ConnectionPoolException {
        UserDao userDao = new UserDao();
        currentUser.setBalance(currentUser.getBalance().subtract(totalPrice));
        try {
            userDao.update(currentUser);
        } catch (SQLException | ConnectionPoolException e) {
            currentUser.setBalance(currentUser.getBalance().add(totalPrice));
            throw e;
        }
    }

    private boolean isEnoughBalance(User currentUser, BigDecimal totalPrice) {
        return totalPrice.compareTo(currentUser.getBalance()) != ONE;
    }
}
