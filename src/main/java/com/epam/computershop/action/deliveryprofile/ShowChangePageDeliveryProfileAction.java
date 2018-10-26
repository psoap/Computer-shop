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

public class ShowChangePageDeliveryProfileAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowChangePageDeliveryProfileAction.class);

    public ShowChangePageDeliveryProfileAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_DELIVEPROF_CHANGE;

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        long profileIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));

        if (profileIdFromRequest != NumberUtil.INVALID_NUMBER) {
            User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
            DeliveryProfileDao profileDao = new DeliveryProfileDao();
            DeliveryProfile profile = null;
            try {
                profile = profileDao.findById(profileIdFromRequest);
                if (profile != null && profile.getUserId() == currentUser.getId()) {
                    req.setAttribute(ConstantStorage.CURRENT_DELIVPROF, profile);
                } else {
                    resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                    responseUrl = URLUtil.getRefererURL(req);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select delivery profile by user " + currentUser.getLogin());
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
                resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                responseUrl = URLUtil.getRefererURL(req);
            }
        }
        return responseUrl;
    }
}
