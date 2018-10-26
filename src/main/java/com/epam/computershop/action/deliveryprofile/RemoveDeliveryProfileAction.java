package com.epam.computershop.action.deliveryprofile;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.entity.DeliveryProfile;
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

public class RemoveDeliveryProfileAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RemoveDeliveryProfileAction.class);

    public RemoveDeliveryProfileAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        long profileIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));

        if (profileIdFromRequest != NumberUtil.INVALID_NUMBER) {
            DeliveryProfileDao profilesDao = new DeliveryProfileDao();
            try {
                DeliveryProfile profile = profilesDao.findById(profileIdFromRequest);
                if (profile != null && profile.getUserId() == currentUser.getId()) {
                    profilesDao.remove(profile);
                    LOGGER.debug("Delivery profile id - " + profileIdFromRequest + "was removed, by user - " + currentUser.getLogin());
                    messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to remove delivery profile - " + profileIdFromRequest
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