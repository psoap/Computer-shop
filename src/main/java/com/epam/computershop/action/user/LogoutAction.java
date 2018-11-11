package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction extends Action {

    public LogoutAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL))
                .concat(JSP_INDEX);
    }
}