package com.epam.computershop.util;

import java.math.BigDecimal;

public final class ConstantStorage {
    //Constants, that must be synchronized with database values
    public static final short ORDER_STATUS_BASKET = 1;
    public static final short ORDER_STATUS_PAID = 2;
    public static final short ORDER_STATUS_SHIPPING = 3;
    public static final short ORDER_STATUS_DELIVERED = 4;
    public static final short ROLE_ID_ADMIN = 2;
    public static final short ROLE_ID_USER = 1;

    //----------------------------------------
    public static final short ROLE_ID_GUEST = 0;
    public static final short ROLE_ID_GUEST_ONLY = -1;
    public static final short ROLE_ID_ERROR = -2;
    public static final short ZERO = 0;
    public static final short ONE = 1;
    public static final String EMPTY_STRING = "";
    public static final BigDecimal ZERO_BALANCE = new BigDecimal(ZERO);
    public static final short COUNT_PRODUCTS_ON_PAGE = 5;

    //Statement indexes
    public static final int INDEX_1 = 1;
    public static final int INDEX_2 = 2;
    public static final int INDEX_3 = 3;
    public static final int INDEX_4 = 4;
    public static final int INDEX_5 = 5;
    public static final int INDEX_6 = 6;
    public static final int INDEX_7 = 7;

    //Scopes attributes names
    public static final String APPLICATION_URL = "application_url";
    public static final String APPLICATION_URL_WITH_SERVLET_PATH = "application_servlet_url";
    public static final String CURRENT_USER = "current_user";
    public static final String CURRENT_DELIVPROF = "current_delivprof";
    public static final String CURRENT_LANG = "current_lang";
    public static final String BASKET = "current_cart";
    public static final String CURRENT_LANG_CATEGORIES = "lang_categories";
    public static final String MESSAGES = "messages";
    public static final String ALL_CATEGORIES = "all_categories";
    public static final String ALL_LANGS = "all_langs";
    public static final String USERS = "users";
    public static final String DELIVERY_PROFILES = "user_delivprofiles";
    public static final String PRODUCTS = "products";
    public static final String PRODUCT = "product";
    public static final String ORDERS = "orders";
    public static final String ORDER_STATUS_ID = "status_id";
    public static final String USER_ROLE_ID = "role_id";
    public static final String TOTAL_PRICE = "total_price";
    public static final String QUANTITY = "quantity";
    public static final String QUERY = "query";
    public static final String PAGE = "page";
    public static final String TRANSLATE_CATEGORIES = "translate_categories";
    public static final String BALANCE = "balance";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PATRONYMIC = "patronymic";
    public static final String ADDRESS_LOCATION = "address_location";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PARENT_ID = "parent_id";
    public static final String LANG_CODE = "lang_code";
    public static final String DESCRIPTION = "description";
    public static final String SHORT_DESCRIPTION = "short_description";
    public static final String PRICE = "price";
    public static final String CATEGORY_ID = "category_id";
    public static final String FILE_URL = "file_url";

    //General actions messages
    public static final String GENERAL_ERROR_ACTION_FAILED = "general.error.action.failed";
    public static final String GENERAL_WARN_BAD_DATA = "general.warn.bad_data";
    public static final String GENERAL_SUCCESS = "general.success";
    //Registration, edit account actions messages
    public static final String REGISTRATION_WARN_BUSY_LOGIN = "registration.warn.busy_login";
    public static final String REGISTRATION_WARN_BAD_LOGIN = "registration.warn.bad_login";
    public static final String CHANGE_USER_WARN_NOT_EQUALS_PASS = "change_user.warn.not_equals";
    public static final String CHANGE_USER_WARN_BAD_PASSWORD = "change_user.warn.bad_password";
    public static final String CHANGE_USER_WARN_BAD_EMAIL = "change_user.warn.bad_email";
    public static final String CHANGE_USER_SUCCESS_CREATE_USER = "change_user.success.create_user";
    public static final String CHECKOUT_WARN_NOT_ENOUGH_BALANCE = "checkout.warn.not_enough";
    //Login action messages
    public static final String LOGIN_WARN_BAD_DATA = "login.warn.bad_data";
    //Change delivery profile messages
    public static final String CHANGE_DELIVPROF_WARN_BAD_FULL_NAME = "change_delivprof.warn.full_name";
    public static final String CHANGE_DELIVPROF_WARN_BAD_ADDRESS = "change_delivprof.warn.address";
    public static final String CHANGE_DELIVPROF_WARN_BAD_PHONE = "change_delivprof.warn.phone";
    //Change category messages
    public static final String CHANGE_CATEGORY_WARN_BAD_NAME = "change_category.warn.name";

    private ConstantStorage() {
    }
}
