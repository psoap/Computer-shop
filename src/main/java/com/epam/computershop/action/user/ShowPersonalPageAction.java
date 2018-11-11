package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPersonalPageAction extends Action {

    public ShowPersonalPageAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return JSP_USER_PERSONAL;
    }
}
