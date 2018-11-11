package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.Product;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static com.epam.computershop.util.ConstantStorage.*;

public class ShowBasketAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowBasketAction.class);

    public ShowBasketAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_ORDER;

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        try {
            if (currentUser != null) {
                showUserBasket(currentUser, req);
            } else {
                showGuestBasket(req);
            }
            session.setAttribute(TOTAL_PRICE, new BigDecimal(ZERO));
            req.setAttribute(ORDER_STATUS, OrderStatus.BASKET);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select products from basket.", e);
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }

    private void showUserBasket(User currentUser, HttpServletRequest req)
            throws SQLException, ConnectionPoolException {
        OrderDao orderDao = new OrderDao();
        List<Order> buffOrders = orderDao.findUserOrdersByStatus(currentUser.getId(), OrderStatus.BASKET);
        if (!buffOrders.isEmpty()) {
            Order basket = buffOrders.get(ZERO);
            OrderProductDao orderProductDao = new OrderProductDao();
            req.setAttribute(PRODUCTS, orderProductDao.findAllByOrderId(basket.getId()));
        }
    }

    private void showGuestBasket(HttpServletRequest req)
            throws SQLException, ConnectionPoolException {
        HttpSession session = req.getSession();
        Map<Long, Short> basket = (Map<Long, Short>) session.getAttribute(BASKET);
        if (basket != null) {
            Map<Product, Short> basketProducts = new HashMap<>(basket.size());
            ProductDao productDao = new ProductDao();
            for (Map.Entry<Long, Short> entry : basket.entrySet()) {
                basketProducts.put(productDao.findById(entry.getKey()), entry.getValue());
            }
            req.setAttribute(PRODUCTS, basketProducts);
        }
    }
}