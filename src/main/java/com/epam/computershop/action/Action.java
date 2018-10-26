package com.epam.computershop.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Action {
    protected short accessRoleId;

    protected String JSP_INDEX = "/";

    protected String JSP_USER_LOGIN = "/jsp/user_login.jsp";
    protected String JSP_USER_REGISTRATION = "/jsp/user_registration.jsp";
    protected String JSP_USER_PERSONAL = "/jsp/user_personal.jsp";
    protected String JSP_USER_EDIT_BALANCE = "/jsp/user_edit_balance.jsp";

    protected String JSP_DELIVEPROF_CATALOG = "/jsp/delivprof_catalog.jsp";
    protected String JSP_DELIVEPROF_CHANGE = "/jsp/delivprof_change.jsp";

    protected String JSP_CATEGORY_MANAGE = "/jsp/category_manage.jsp";

    protected String JSP_PRODUCT_CATALOG = "/jsp/product_catalog.jsp";
    protected String JSP_PRODUCT = "/jsp/product.jsp";
    protected String JSP_PRODUCT_CHANGE = "/jsp/product_change.jsp";

    protected String JSP_ORDER = "/jsp/order.jsp";
    protected String JSP_ORDER_CHECKOUT = "/jsp/order_checkout.jsp";
    protected String JSP_ORDER_CATALOG = "/jsp/order_catalog.jsp";
    protected String JSP_ORDER_ADMIN_CATALOG = "/jsp/order_admin_catalog.jsp";

    protected String JSP_USER_CATALOG = "/jsp/user_catalog.jsp";

    protected String JSP_LANG_MANAGE = "/jsp/lang_manage.jsp";

    public Action(short accessRoleId) {
        this.accessRoleId = accessRoleId;
    }

    public abstract String execute(HttpServletRequest req, HttpServletResponse resp);

    public short getAccessRoleId() {
        return accessRoleId;
    }
}
