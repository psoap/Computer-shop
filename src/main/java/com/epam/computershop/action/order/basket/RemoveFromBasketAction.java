package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Map;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class RemoveFromBasketAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RemoveFromBasketAction.class);

    public RemoveFromBasketAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        long productIdFromRequest = tryParseLong(req.getParameter(ID));
        if (productIdFromRequest != INVALID_NUMBER) {
            removeProduct(currentUser, productIdFromRequest, session);
        } else {
            clearBasket(currentUser, session);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }

    private void removeProduct(User currentUser, long productIdFromRequest, HttpSession session){
        if (currentUser != null) {
            OrderProductDao orderProductDao = new OrderProductDao();
            try {
                orderProductDao.remove(currentUser.getId(), OrderStatus.BASKET, productIdFromRequest);
                LOGGER.debug("Product id - " + productIdFromRequest
                        + " was removed from basket, by user -" + currentUser.getLogin());
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to remove product id - " + productIdFromRequest
                        + ", from basket, by user - " + currentUser.getLogin(), e);
            }
        } else {
            Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(BASKET);
            sessionBasket.remove(productIdFromRequest);
        }
    }

    private void clearBasket(User currentUser, HttpSession session){
        if (currentUser != null) {
            OrderDao orderDao = new OrderDao();
            try {
                Order order = orderDao.findUserOrdersByStatus(currentUser.getId(), OrderStatus.BASKET).get(ZERO);
                if (order != null) {
                    orderDao.remove(order);
                    LOGGER.debug("Basket was cleared, by user -" + currentUser.getLogin());
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to remove basket, by user - " + currentUser.getLogin(), e);
            }
        } else {
            Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(BASKET);
            sessionBasket.clear();
        }
    }
}
