package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowRegistrationPageAction extends Action {

    public ShowRegistrationPageAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return JSP_USER_REGISTRATION;
    }
}
