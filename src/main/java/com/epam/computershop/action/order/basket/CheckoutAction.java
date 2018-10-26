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
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CheckoutAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(CheckoutAction.class);

    public CheckoutAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(ConstantStorage.CURRENT_USER);
        BigDecimal totalPrice = (BigDecimal) session.getAttribute(ConstantStorage.TOTAL_PRICE);
        long deliveryProfileIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.CURRENT_DELIVPROF));
        if (totalPrice != null && deliveryProfileIdFromRequest != NumberUtil.INVALID_NUMBER && !(totalPrice.compareTo(currentUser.getBalance()) == ConstantStorage.ONE)) {
            try {
                DeliveryProfileDao profileDao = new DeliveryProfileDao();
                DeliveryProfile profile = profileDao.findById(deliveryProfileIdFromRequest);
                OrderDao orderDao = new OrderDao();
                List<Order> buffOrders = orderDao.findUserOrdersByStatus(currentUser.getId(), ConstantStorage.ORDER_STATUS_BASKET);
                if (!buffOrders.isEmpty() && profile != null && profile.getUserId() == currentUser.getId()) {
                    Order basket = buffOrders.get(ConstantStorage.ZERO);
                    basket.setStatusId(ConstantStorage.ORDER_STATUS_PAID);
                    basket.setDeliveryProfileId(profile.getId());
                    basket.setTotalPrice(totalPrice);
                    orderDao.update(basket);
                    UserDao userDao = new UserDao();
                    currentUser.setBalance(currentUser.getBalance().subtract(totalPrice));
                    userDao.update(currentUser);
                    LOGGER.debug("Basket was ordered, by user -" + currentUser.getLogin());
                    messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                    resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to update basket status, by user - " + currentUser.getLogin());
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        } else {
            messagesForJsp.add(ConstantStorage.CHECKOUT_WARN_NOT_ENOUGH_BALANCE);
        }
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_BASKET_SHOW);
    }
}
