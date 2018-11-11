package com.epam.computershop.action.order;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class SwapOrderStatusAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(SwapOrderStatusAction.class);

    public SwapOrderStatusAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);
        long orderIdFromRequest = tryParseLong(req.getParameter(ID));

        if (orderIdFromRequest != INVALID_NUMBER) {
            findAndSwap(req, orderIdFromRequest, messagesForJsp);
        } else {
            messagesForJsp.add(GENERAL_WARN_BAD_DATA);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }

    private void findAndSwap(HttpServletRequest req, long orderIdFromRequest, List<String> messagesForJsp){
        OrderStatus status = OrderStatus.valueOf(req.getParameter(ORDER_STATUS));
        if(status.equals(OrderStatus.BASKET)){
            return;
        }

        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        OrderDao orderDao = new OrderDao();
        try {
            Order order = orderDao.findById(orderIdFromRequest);
            if (orderIsBasket(order, currentUser) || currentUser.getRole().equals(UserRole.ADMIN)) {
                order.setStatus(status);
                order.setChangeDate(new Timestamp(System.currentTimeMillis()));
                orderDao.update(order);
                LOGGER.debug("Order id - " + orderIdFromRequest + " status was updated");
                messagesForJsp.add(GENERAL_SUCCESS);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update order status, order id - " + orderIdFromRequest, e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
    }

    private boolean orderIsBasket(Order order, User currentUser){
        return (order.getUserId() == currentUser.getId())
                && (order.getStatus().equals(OrderStatus.BASKET));
    }
}
