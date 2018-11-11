package com.epam.computershop.action.order;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.Product;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.NumberUtil;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static com.epam.computershop.util.ConstantStorage.*;

public class ShowOrderAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowOrderAction.class);

    public ShowOrderAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_ORDER;

        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        long orderIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ID));
        if (orderIdFromRequest != NumberUtil.INVALID_NUMBER) {
            try {
                find(currentUser, orderIdFromRequest, req);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select products from basket, by user - "
                        + currentUser.getLogin(), e);
                resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                responseUrl = URLUtil.getRefererURL(req);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }

    private void find(User currentUser, long orderIdFromRequest, HttpServletRequest req)
            throws SQLException, ConnectionPoolException {
        OrderProductDao orderProductDao = new OrderProductDao();
        OrderDao orderDao = new OrderDao();
        Order order = orderDao.findById(orderIdFromRequest);

        if((order != null) && (order.getUserId() == currentUser.getId())
                || currentUser.getRole().equals(UserRole.ADMIN)){
            Map<Product, Short> orderProducts = orderProductDao.findAllByOrderId(orderIdFromRequest);
            req.getSession().setAttribute(TOTAL_PRICE, new BigDecimal(ZERO));
            req.setAttribute(PRODUCTS, orderProducts);
        }
    }
}
