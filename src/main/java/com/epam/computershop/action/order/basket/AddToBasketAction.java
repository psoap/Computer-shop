package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AddToBasketAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(AddToBasketAction.class);

    public AddToBasketAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(ConstantStorage.CURRENT_USER);
        long productIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        OrderProductDao orderProductDao = new OrderProductDao();
        if (productIdFromRequest != NumberUtil.INVALID_NUMBER) {
            if (currentUser != null) {
                try {
                    //Insert return true if basket not exist in Order table, then it creates new basket and insert
                    if (orderProductDao.insert(currentUser.getId(), ConstantStorage.ORDER_STATUS_BASKET, productIdFromRequest, ConstantStorage.ONE)) {
                        OrderDao orderDao = new OrderDao();
                        Order newBasket = new Order();
                        newBasket.setUserId(currentUser.getId());
                        newBasket.setStatusId(ConstantStorage.ORDER_STATUS_BASKET);
                        newBasket.setTotalPrice(ConstantStorage.ZERO_BALANCE);
                        newBasket.setChangeDate(new Timestamp(System.currentTimeMillis()));
                        orderDao.insert(newBasket);
                        orderProductDao.insert(currentUser.getId(), ConstantStorage.ORDER_STATUS_BASKET, productIdFromRequest, ConstantStorage.ONE);
                    }
                    LOGGER.debug("Product id - " + productIdFromRequest + " was added to basket, by user -" + currentUser.getLogin());
                } catch (SQLException | ConnectionPoolException e) {
                    LOGGER.error("Failed to insert product to basket, by user - " + currentUser.getLogin());
                }
            } else {
                Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(ConstantStorage.BASKET);
                if (sessionBasket == null) {
                    sessionBasket = new HashMap<>();
                    session.setAttribute(ConstantStorage.BASKET, sessionBasket);
                }
                sessionBasket.put(productIdFromRequest, ConstantStorage.ONE);
            }
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }
}
