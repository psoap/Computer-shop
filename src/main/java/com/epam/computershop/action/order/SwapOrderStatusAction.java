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
import java.sql.Timestamp;
import java.util.List;

public class SwapOrderStatusAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(SwapOrderStatusAction.class);

    public SwapOrderStatusAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);
        long orderIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        short orderStatusIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.ORDER_STATUS_ID));

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);

        if (orderIdFromRequest != NumberUtil.INVALID_NUMBER) {
            OrderDao orderDao = new OrderDao();
            try {
                Order order = orderDao.findById(orderIdFromRequest);
                if (order.getUserId() == currentUser.getId() && order.getStatusId() == ConstantStorage.ORDER_STATUS_BASKET
                        || currentUser.getRoleId() == ConstantStorage.ROLE_ID_ADMIN) {
                    order.setStatusId(orderStatusIdFromRequest);
                    order.setChangeDate(new Timestamp(System.currentTimeMillis()));
                    orderDao.update(order);
                    LOGGER.debug("Order id - " + orderIdFromRequest + " status was updated");
                    messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to update order status, order id - " + orderIdFromRequest);
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        } else {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }
}
