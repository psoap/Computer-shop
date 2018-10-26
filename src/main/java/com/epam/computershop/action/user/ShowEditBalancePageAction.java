package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowEditBalancePageAction extends Action {

    public ShowEditBalancePageAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return JSP_USER_EDIT_BALANCE;
    }
}
