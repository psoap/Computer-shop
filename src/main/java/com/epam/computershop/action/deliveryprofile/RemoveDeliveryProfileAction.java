package com.epam.computershop.action.deliveryprofile;

import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.action.RemoveAction;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;

public class RemoveDeliveryProfileAction extends RemoveAction {
    private static final Logger LOGGER = Logger.getLogger(RemoveDeliveryProfileAction.class);

    public RemoveDeliveryProfileAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    protected String remove(HttpServletRequest req, long profileIdFromRequest, List<String> messagesForJsp){
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        DeliveryProfileDao profilesDao = new DeliveryProfileDao();
        try {
            DeliveryProfile profile = profilesDao.findById(profileIdFromRequest);
            if (profile != null && profile.getUserId() == currentUser.getId()) {
                profilesDao.remove(profile);
                LOGGER.debug("Delivery profile id - " + profileIdFromRequest
                        + "was removed, by user - " + currentUser.getLogin());
                messagesForJsp.add(GENERAL_SUCCESS);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to remove delivery profile - " + profileIdFromRequest
                    + ", user - " + currentUser.getLogin(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_DELIVERY_PROFILE_CATALOG);
    }
}