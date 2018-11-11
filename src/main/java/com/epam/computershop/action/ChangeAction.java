package com.epam.computershop.action;

import com.epam.computershop.entity.Entity;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.INVALID_NUMBER;
import static com.epam.computershop.util.NumberUtil.tryParseLong;

public abstract class ChangeAction <T extends Entity> extends Action {

    protected ChangeAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);
        T entity = getEntityFromRequest(req);

        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        if(validateForm(entity, messagesForJsp)){
            return URLUtil.getRefererURL(req);
        }

        long entityIdFromRequest = tryParseLong(req.getParameter(ID));
        String responseUrl;
        if (entityIdFromRequest == INVALID_NUMBER) {
            responseUrl = add(req, entity, messagesForJsp);
        } else {
            responseUrl = edit(req, entityIdFromRequest, entity, messagesForJsp);
        }
        return responseUrl;
    }

    protected abstract String add(HttpServletRequest req, T entity, List<String> messagesForJsp);

    protected abstract String edit(HttpServletRequest req, long entityId, T entity, List<String> messagesForJsp);

    protected boolean validateForm(T entity, List<String> messagesForJsp){
        return false;
    }

    protected abstract T getEntityFromRequest(HttpServletRequest req);
}
