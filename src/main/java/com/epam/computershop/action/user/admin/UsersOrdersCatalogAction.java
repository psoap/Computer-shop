package com.epam.computershop.action.user.admin;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;

public class UsersOrdersCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(UsersOrdersCatalogAction.class);

    public UsersOrdersCatalogAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        OrderStatus status = OrderStatus.valueOf(req.getParameter(ORDER_STATUS));
        OrderDao orderDao = new OrderDao();
        try {
            List<Order> orders = orderDao.findAllUsersOrdersByStatus(status);
            req.setAttribute(ORDERS, orders);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select orders by stratus - " + status);
        }
        return JSP_ORDER_ADMIN_CATALOG;
    }
}
