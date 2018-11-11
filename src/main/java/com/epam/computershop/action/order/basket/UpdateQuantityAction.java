package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.OrderProductDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.OrderStatus;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class UpdateQuantityAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(UpdateQuantityAction.class);

    public UpdateQuantityAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        long productIdFromRequest = tryParseLong(req.getParameter(ID));
        short quantityFromRequest = tryParseShort(req.getParameter(QUANTITY));
        if ((productIdFromRequest != INVALID_NUMBER) && (quantityFromRequest > ZERO)) {
            if (currentUser != null) {
                updateByUser(currentUser, productIdFromRequest, quantityFromRequest, messagesForJsp);
            } else {
                updateByGuest(session, productIdFromRequest, quantityFromRequest, messagesForJsp);
            }
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }

    private void updateByUser(User currentUser, long productIdFromRequest,
                              short quantityFromRequest, List<String> messagesForJsp) {
        OrderProductDao orderProductDao = new OrderProductDao();
        try {
            orderProductDao.update(currentUser.getId(), OrderStatus.BASKET,
                    productIdFromRequest, quantityFromRequest);
            LOGGER.debug("Quantity on product id -" + productIdFromRequest
                    + "was update to " + quantityFromRequest + ", by user - " + currentUser.getLogin());
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update quantity at basket, by user - "
                    + currentUser.getLogin(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
    }

    private void updateByGuest(HttpSession session, long productIdFromRequest,
                               short quantityFromRequest, List<String> messagesForJsp) {
        Map<Long, Short> sessionBasket = (Map<Long, Short>) session.getAttribute(BASKET);
        sessionBasket.replace(productIdFromRequest, quantityFromRequest);
        messagesForJsp.add(GENERAL_SUCCESS);
    }
}
