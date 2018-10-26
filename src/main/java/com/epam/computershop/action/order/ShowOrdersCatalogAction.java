package com.epam.computershop.action.order;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class ShowOrdersCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowOrdersCatalogAction.class);

    public ShowOrdersCatalogAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl;
        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        OrderDao orderDao = new OrderDao();
        try {
            List<Order> orders = orderDao.findAllByUserId(currentUser.getId());
            req.setAttribute(ConstantStorage.ORDERS, orders);
            responseUrl = JSP_ORDER_CATALOG;
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select all orders by user - " + currentUser.getLogin());
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
