package com.epam.computershop.action.user.admin;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class UsersOrdersCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(UsersOrdersCatalogAction.class);

    public UsersOrdersCatalogAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_ORDER_ADMIN_CATALOG;
        short orderStatusIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.ID));
        if (orderStatusIdFromRequest != NumberUtil.INVALID_NUMBER) {
            OrderDao orderDao = new OrderDao();
            try {
                List<Order> orders = orderDao.findAllUsersOrdersByStatus(orderStatusIdFromRequest);
                req.setAttribute(ConstantStorage.ORDERS, orders);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select orders by stratus id - " + orderStatusIdFromRequest);
            }
        }
        return responseUrl;
    }
}
