package com.epam.computershop.action.category;

import com.epam.computershop.action.Action;
import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowManageCategoriesPageAction extends Action {
    public ShowManageCategoriesPageAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return JSP_CATEGORY_MANAGE;
    }
}
