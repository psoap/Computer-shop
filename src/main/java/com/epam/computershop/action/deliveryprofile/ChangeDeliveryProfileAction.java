package com.epam.computershop.action.deliveryprofile;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.DeliveryProfileDao;
import com.epam.computershop.entity.DeliveryProfile;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class ChangeDeliveryProfileAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ChangeDeliveryProfileAction.class);

    public ChangeDeliveryProfileAction(short accessRoleId) {
        super(accessRoleId);
    }

    private static void validateForm(String firstName, String lastName, String patronymic,
                                     String addressLocation, String phoneNumber, List<String> messagesForJsp) {
        if (!(Validator.validate(ValidatorSettingsStorage.DELIVPROF_FULL_NAME_PATTERN, firstName)
                && Validator.validate(ValidatorSettingsStorage.DELIVPROF_FULL_NAME_PATTERN, lastName)
                && Validator.validate(ValidatorSettingsStorage.DELIVPROF_FULL_NAME_PATTERN, patronymic))) {
            messagesForJsp.add(ConstantStorage.CHANGE_DELIVPROF_WARN_BAD_FULL_NAME);
        }
        if (!Validator.validate(ValidatorSettingsStorage.DELIVPROF_ADDRESS_PATTERN, addressLocation)) {
            messagesForJsp.add(ConstantStorage.CHANGE_DELIVPROF_WARN_BAD_ADDRESS);
        }
        if (!Validator.validate(ValidatorSettingsStorage.DELIVPROF_PHONE_PATTERN, phoneNumber)) {
            messagesForJsp.add(ConstantStorage.CHANGE_DELIVPROF_WARN_BAD_PHONE);
        }
    }

    private DeliveryProfile add(User currentUser, DeliveryProfile profile, List<String> messagesForJsp) throws SQLException, ConnectionPoolException {
        DeliveryProfileDao profileDao = new DeliveryProfileDao();
        DeliveryProfile newProfile = new DeliveryProfile();
        profileDao.insert(profile);
        LOGGER.debug("Delivery profile id - " + newProfile.getId() + " was created, by user - " + currentUser.getLogin());
        messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
        return newProfile;
    }

    private DeliveryProfile edit(User currentUser, DeliveryProfile profile, List<String> messagesForJsp) throws SQLException, ConnectionPoolException {
        DeliveryProfileDao profilesDao = new DeliveryProfileDao();
        DeliveryProfile existedProfile = profilesDao.findById(profile.getId());
        if (existedProfile != null && existedProfile.getUserId() == currentUser.getId()) {
            existedProfile.setFirstName(profile.getFirstName());
            existedProfile.setLastName(profile.getLastName());
            existedProfile.setPatronymic(profile.getPatronymic());
            existedProfile.setAddressLocation(profile.getAddressLocation());
            existedProfile.setPhoneNumber(profile.getPhoneNumber());
            profilesDao.update(existedProfile);
            LOGGER.debug("Delivery profile id - " + profile.getId() + " was updated, by user - " + currentUser.getLogin());
            messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
        } else {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        }
        return profile;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_DELIVEPROF_CHANGE;
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        String firstName = req.getParameter(ConstantStorage.FIRST_NAME).trim();
        String lastName = req.getParameter(ConstantStorage.LAST_NAME).trim();
        String patronymic = req.getParameter(ConstantStorage.PATRONYMIC).trim();
        String address = req.getParameter(ConstantStorage.ADDRESS_LOCATION).trim();
        String phone = req.getParameter(ConstantStorage.PHONE_NUMBER).trim();

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        long profileIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        validateForm(firstName, lastName, patronymic, address, phone, messagesForJsp);

        if (messagesForJsp.isEmpty()) {
            DeliveryProfile buffDeliveryProfile = new DeliveryProfile();
            buffDeliveryProfile.setFirstName(firstName);
            buffDeliveryProfile.setLastName(lastName);
            buffDeliveryProfile.setPatronymic(patronymic);
            buffDeliveryProfile.setAddressLocation(address);
            buffDeliveryProfile.setPhoneNumber(phone);

            if (profileIdFromRequest == NumberUtil.INVALID_NUMBER) {
                try {
                    buffDeliveryProfile.setUserId(currentUser.getId());
                    add(currentUser, buffDeliveryProfile, messagesForJsp);
                    resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                    responseUrl = ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                            .concat(ActionFactory.ACTION_DELIVPROF_CATALOG);
                } catch (SQLException | ConnectionPoolException e) {
                    LOGGER.error("Failed to insert delivery profile by user - " + currentUser.getLogin());
                    messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
                }
            } else {
                try {
                    edit(currentUser, buffDeliveryProfile, messagesForJsp);
                    resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                    responseUrl = ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                            .concat(ActionFactory.ACTION_DELIVPROF_CATALOG);
                } catch (SQLException | ConnectionPoolException e) {
                    LOGGER.error("Failed to update delivery profile by user - " + currentUser.getLogin());
                    messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
                }
            }
        }
        return responseUrl;
    }
}
