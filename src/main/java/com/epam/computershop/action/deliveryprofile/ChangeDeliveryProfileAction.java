package com.epam.computershop.action.deliveryprofile;

import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.action.ChangeAction;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.entity.User;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.Validator.validate;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class ChangeDeliveryProfileAction extends ChangeAction<DeliveryProfile> {
    private static final Logger LOGGER = Logger.getLogger(ChangeDeliveryProfileAction.class);

    public ChangeDeliveryProfileAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    protected String add(HttpServletRequest req, DeliveryProfile entity, List<String> messagesForJsp) {
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        entity.setUserId(currentUser.getId());

        DeliveryProfileDao profileDao = new DeliveryProfileDao();
        try {
            profileDao.insert(entity);
            LOGGER.debug("Delivery profile id - " + entity.getId()
                    + " was created, by user - " + currentUser.getLogin());
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to insert delivery profile by user - " + currentUser.getLogin(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_DELIVERY_PROFILE_CATALOG);
    }

    @Override
    protected String edit(HttpServletRequest req, long entityId,
                          DeliveryProfile entity, List<String> messagesForJsp) {
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        DeliveryProfileDao profilesDao = new DeliveryProfileDao();
        try {
            DeliveryProfile existedProfile = profilesDao.findById(entityId);
            if ((existedProfile != null) && (existedProfile.getUserId() == currentUser.getId())) {
                existedProfile.setFirstName(entity.getFirstName());
                existedProfile.setLastName(entity.getLastName());
                existedProfile.setPatronymic(entity.getPatronymic());
                existedProfile.setAddressLocation(entity.getAddressLocation());
                existedProfile.setPhoneNumber(entity.getPhoneNumber());
                profilesDao.update(existedProfile);
                LOGGER.debug("Delivery profile id - " + entityId
                        + " was updated, by user - " + currentUser.getLogin());
                messagesForJsp.add(GENERAL_SUCCESS);
            } else {
                messagesForJsp.add(GENERAL_WARN_BAD_DATA);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update delivery profile by user - " + currentUser.getLogin(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_DELIVERY_PROFILE_CATALOG);
    }

    @Override
    protected boolean validateForm(DeliveryProfile profile, List<String> messagesForJsp) {
        boolean result = false;
        if (!(validate(DELIVERY_PROFILE_FULL_NAME_PATTERN, profile.getFirstName())
                && validate(DELIVERY_PROFILE_FULL_NAME_PATTERN, profile.getLastName())
                && validate(DELIVERY_PROFILE_FULL_NAME_PATTERN, profile.getPatronymic()))) {
            messagesForJsp.add(CHANGE_DELIVERY_PROFILE_WARN_BAD_FULL_NAME);
            result = true;
        }
        if (!validate(DELIVERY_PROFILE_ADDRESS_PATTERN, profile.getAddressLocation())) {
            messagesForJsp.add(CHANGE_DELIVERY_PROFILE_WARN_BAD_ADDRESS);
            result = true;
        }
        if (!validate(DELIVERY_PROFILE_PHONE_PATTERN, profile.getPhoneNumber())) {
            messagesForJsp.add(CHANGE_DELIVERY_PROFILE_WARN_BAD_PHONE);
            result = true;
        }
        return result;
    }

    @Override
    protected DeliveryProfile getEntityFromRequest(HttpServletRequest req) {
        DeliveryProfile profile = new DeliveryProfile();
        profile.setFirstName(req.getParameter(FIRST_NAME).trim());
        profile.setLastName(req.getParameter(LAST_NAME).trim());
        profile.setPatronymic(req.getParameter(PATRONYMIC).trim());
        profile.setAddressLocation(req.getParameter(ADDRESS_LOCATION).trim());
        profile.setPhoneNumber(req.getParameter(PHONE_NUMBER).trim());
        return profile;
    }
}
