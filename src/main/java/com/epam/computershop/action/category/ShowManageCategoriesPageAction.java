package com.epam.computershop.action.category;

import com.epam.computershop.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowManageCategoriesPageAction extends Action {

    public ShowManageCategoriesPageAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return JSP_CATEGORY_MANAGE;
    }
}
