package com.epam.computershop.action.lang;

import com.epam.computershop.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowManageLangsPageAction extends Action {
    public ShowManageLangsPageAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return JSP_LANG_MANAGE;
    }
}
