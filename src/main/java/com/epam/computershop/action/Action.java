package com.epam.computershop.action;

import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public abstract class Action {
    protected static final String JSP_INDEX = "/";

    protected static final String JSP_USER_LOGIN = "/jsp/user_login.jsp";
    protected static final String JSP_USER_REGISTRATION = "/jsp/user_registration.jsp";
    protected static final String JSP_USER_PERSONAL = "/jsp/user_personal.jsp";
    protected static final String JSP_USER_EDIT_BALANCE = "/jsp/user_edit_balance.jsp";

    protected static final String JSP_DELIVERY_PROFILE_CATALOG = "/jsp/delivery_profile_catalog.jsp";
    protected static final String JSP_DELIVERY_PROFILE_CHANGE = "/jsp/delivery_profile_change.jsp";

    protected static final String JSP_CATEGORY_MANAGE = "/jsp/category_manage.jsp";

    protected static final String JSP_PRODUCT_CATALOG = "/jsp/product_catalog.jsp";
    protected static final String JSP_PRODUCT = "/jsp/product.jsp";
    protected static final String JSP_PRODUCT_CHANGE = "/jsp/product_change.jsp";

    protected static final String JSP_ORDER = "/jsp/order.jsp";
    protected static final String JSP_ORDER_CHECKOUT = "/jsp/order_checkout.jsp";
    protected static final String JSP_ORDER_CATALOG = "/jsp/order_catalog.jsp";
    protected static final String JSP_ORDER_ADMIN_CATALOG = "/jsp/order_admin_catalog.jsp";

    protected static final String JSP_USER_CATALOG = "/jsp/user_catalog.jsp";

    protected static final String JSP_LANG_MANAGE = "/jsp/lang_manage.jsp";

    private final UserRole ACCESS_ROLE;

    protected Action(UserRole accessRole) {
        this.ACCESS_ROLE = accessRole;
    }

    public abstract String execute(HttpServletRequest req, HttpServletResponse resp);

    public UserRole getAccessRole() {
        return ACCESS_ROLE;
    }
}
