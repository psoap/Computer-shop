package com.epam.computershop.action.order;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class RemoveOrderActon extends Action {
    private static final Logger LOGGER = Logger.getLogger(RemoveOrderActon.class);

    public RemoveOrderActon(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        long orderIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));

        if (orderIdFromRequest != NumberUtil.INVALID_NUMBER) {
            OrderDao orderDao = new OrderDao();
            try {
                Order order = orderDao.findById(orderIdFromRequest);
                if (order != null && order.getUserId() == currentUser.getId()) {
                    orderDao.remove(order);
                    LOGGER.debug("Order id - " + orderIdFromRequest + "was removed");
                    messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to remove order - " + orderIdFromRequest
                        + ", user - " + currentUser.getLogin());
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        } else {
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }
}
