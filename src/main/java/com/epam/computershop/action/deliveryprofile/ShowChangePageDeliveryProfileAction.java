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
import static com.epam.computershop.util.NumberUtil.*;

public class ShowChangePageDeliveryProfileAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowChangePageDeliveryProfileAction.class);

    public ShowChangePageDeliveryProfileAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_DELIVERY_PROFILE_CHANGE;
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);
        long profileIdFromRequest = tryParseLong(req.getParameter(ID));

        if (profileIdFromRequest != INVALID_NUMBER) {
            User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
            try {
                find(currentUser, profileIdFromRequest, req);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select delivery profile by user " + currentUser.getLogin(), e);
                messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
                resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                responseUrl = URLUtil.getRefererURL(req);
            }
        }
        return responseUrl;
    }

    private void find(User currentUser, long profileId, HttpServletRequest req)
            throws ConnectionPoolException, SQLException {
        DeliveryProfileDao profileDao = new DeliveryProfileDao();
        DeliveryProfile profile = profileDao.findById(profileId);
        if ((profile != null) && (profile.getUserId() == currentUser.getId())) {
            req.setAttribute(CURRENT_DELIVERY_PROFILE, profile);
        }
    }
}
