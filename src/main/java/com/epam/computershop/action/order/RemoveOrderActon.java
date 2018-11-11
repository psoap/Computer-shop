package com.epam.computershop.action.order;

import com.epam.computershop.action.RemoveAction;
import com.epam.computershop.dao.OrderDao;
import com.epam.computershop.entity.Order;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;

public class RemoveOrderActon extends RemoveAction {
    private static final Logger LOGGER = Logger.getLogger(RemoveOrderActon.class);

    public RemoveOrderActon(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    protected String remove(HttpServletRequest req, long orderIdFromRequest, List<String> messagesForJsp){
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        OrderDao orderDao = new OrderDao();
        try {
            Order order = orderDao.findById(orderIdFromRequest);
            if ((order != null) && (order.getUserId() == currentUser.getId())) {
                orderDao.remove(order);
                LOGGER.debug("Order id - " + orderIdFromRequest + "was removed");
                messagesForJsp.add(GENERAL_SUCCESS);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to remove order - " + orderIdFromRequest
                    + ", user - " + currentUser.getLogin(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return URLUtil.getRefererURL(req);
    }
}
