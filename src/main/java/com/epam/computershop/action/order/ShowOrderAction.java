package com.epam.computershop.action.order;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.entity.Product;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class ShowOrderAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowOrderAction.class);

    public ShowOrderAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_ORDER;

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(ConstantStorage.CURRENT_USER);
        long orderIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        if (orderIdFromRequest != NumberUtil.INVALID_NUMBER) {
            try {
                OrderProductDao orderProductDao = new OrderProductDao();
                Map<Product, Short> orderProducts = orderProductDao.findAllByOrderId(orderIdFromRequest);
                session.setAttribute(ConstantStorage.TOTAL_PRICE, new BigDecimal(ConstantStorage.ZERO));
                req.setAttribute(ConstantStorage.PRODUCTS, orderProducts);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select products from basket, by user - " + currentUser.getLogin());
                resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                responseUrl = URLUtil.getRefererURL(req);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
