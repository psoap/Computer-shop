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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class AddToBasketAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(AddToBasketAction.class);

    public AddToBasketAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        long productIdFromRequest = tryParseLong(req.getParameter(ID));
        if (productIdFromRequest != INVALID_NUMBER) {
            if (currentUser != null) {
                addToUserBasket(currentUser, productIdFromRequest);
            } else {
                addToGuestBasket(session, productIdFromRequest);
            }
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }

    private void addToUserBasket(User currentUser, long productIdFromRequest){
        OrderProductDao orderProductDao = new OrderProductDao();
        try {
            //Insert return true if basket not exist in Order table, then it creates new basket and insert
            if (orderProductDao.insert(currentUser.getId(), OrderStatus.BASKET, productIdFromRequest, ONE)) {
                OrderDao orderDao = new OrderDao();
                Order newBasket = new Order();
                newBasket.setUserId(currentUser.getId());
                newBasket.setStatus(OrderStatus.BASKET);
                newBasket.setTotalPrice(ZERO_BALANCE);
                newBasket.setChangeDate(new Timestamp(System.currentTimeMillis()));
                orderDao.insert(newBasket);
                orderProductDao.insert(currentUser.getId(), OrderStatus.BASKET, productIdFromRequest, ONE);
            }
            LOGGER.debug("Product id - " + productIdFromRequest + " was added to basket, by user -"
                    + currentUser.getLogin());
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to insert product to basket, by user - " + currentUser.getLogin(), e);
        }
    }

    private void addToGuestBasket(HttpSession session, long productIdFromRequest){
        Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(BASKET);
        if (sessionBasket == null) {
            sessionBasket = new HashMap<>();
            session.setAttribute(BASKET, sessionBasket);
        }
        sessionBasket.put(productIdFromRequest, ONE);
    }
}
