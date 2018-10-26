package com.epam.computershop.action.order.basket;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public class ShowCheckoutPageAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowCheckoutPageAction.class);

    public ShowCheckoutPageAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_ORDER_CHECKOUT;

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(ConstantStorage.CURRENT_USER);
        if (currentUser != null) {
            try {
                DeliveryProfileDao profileDao = new DeliveryProfileDao();
                List<DeliveryProfile> profiles = profileDao.findAllByUserId(currentUser.getId());
                req.setAttribute(ConstantStorage.DELIVERY_PROFILES, profiles);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select delivery profiles, by user - " + currentUser.getLogin());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                    .concat(ActionFactory.ACTION_USER_SHOW_LOGIN_PAGE);
        }
        return responseUrl;
    }
}
