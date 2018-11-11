package com.epam.computershop.action.deliveryprofile;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;

public class ShowDeliveryProfilesCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowDeliveryProfilesCatalogAction.class);

    public ShowDeliveryProfilesCatalogAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_DELIVERY_PROFILE_CATALOG;
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        DeliveryProfileDao profilesDao = new DeliveryProfileDao();
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        try {
            List<DeliveryProfile> profiles = profilesDao.findAllByUserId(currentUser.getId());
            req.setAttribute(DELIVERY_PROFILES, profiles);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select all delivery profiles by user - " + currentUser.getLogin(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
