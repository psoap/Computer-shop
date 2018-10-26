package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.Product;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class ShowBasketAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowBasketAction.class);

    public ShowBasketAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_ORDER;

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(ConstantStorage.CURRENT_USER);
        try {
            Map<Product, Short> basketProducts = null;
            if (currentUser != null) {
                OrderDao orderDao = new OrderDao();
                List<Order> buffOrders = orderDao.findUserOrdersByStatus(currentUser.getId(), ConstantStorage.ORDER_STATUS_BASKET);
                if (!buffOrders.isEmpty()) {
                    Order basket = buffOrders.get(ConstantStorage.ZERO);
                    OrderProductDao orderProductDao = new OrderProductDao();
                    basketProducts = orderProductDao.findAllByOrderId(basket.getId());
                    req.setAttribute(ConstantStorage.PRODUCTS, basketProducts);
                }
            } else {
                Map<Long, Short> basket = (Map<Long, Short>) session.getAttribute(ConstantStorage.BASKET);
                if (basket != null) {
                    basketProducts = new HashMap<>(basket.size());
                    ProductDao productDao = new ProductDao();
                    for (Map.Entry<Long, Short> entry : basket.entrySet()) {
                        basketProducts.put(productDao.findById(entry.getKey()), entry.getValue());
                    }
                }
            }
            session.setAttribute(ConstantStorage.TOTAL_PRICE, new BigDecimal(ConstantStorage.ZERO));
            req.setAttribute(ConstantStorage.PRODUCTS, basketProducts);
            req.setAttribute(ConstantStorage.ORDER_STATUS_ID, ConstantStorage.ORDER_STATUS_BASKET);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select products from basket, by user - " + currentUser.getLogin());
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}