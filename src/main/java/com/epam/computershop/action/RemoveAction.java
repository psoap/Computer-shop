package com.epam.computershop.action;

import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.GENERAL_ERROR_ACTION_FAILED;
import static com.epam.computershop.util.ConstantStorage.ID;
import static com.epam.computershop.util.ConstantStorage.MESSAGES;
import static com.epam.computershop.util.NumberUtil.INVALID_NUMBER;
import static com.epam.computershop.util.NumberUtil.tryParseLong;

public abstract class RemoveAction extends Action {

    protected RemoveAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);
        long profileIdFromRequest = tryParseLong(req.getParameter(ID));

        String responseUrl;
        if (profileIdFromRequest != INVALID_NUMBER) {
            responseUrl = remove(req, profileIdFromRequest, messagesForJsp);
        } else {
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
            responseUrl = URLUtil.getRefererURL(req);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return responseUrl;
    }

    protected abstract String remove(HttpServletRequest req, long id, List<String> messagesForJsp);
}
