package com.epam.computershop.action;

import com.epam.computershop.action.category.ChangeCategoryAction;
import com.epam.computershop.action.category.RemoveCategoryAction;
import com.epam.computershop.action.category.ShowManageCategoriesPageAction;
import com.epam.computershop.action.deliveryprofile.ChangeDeliveryProfileAction;
import com.epam.computershop.action.deliveryprofile.RemoveDeliveryProfileAction;
import com.epam.computershop.action.deliveryprofile.ShowChangePageDeliveryProfileAction;
import com.epam.computershop.action.deliveryprofile.ShowDeliveryProfilesCatalogAction;
import com.epam.computershop.action.lang.AddLangAction;
import com.epam.computershop.action.lang.ShowManageLangsPageAction;
import com.epam.computershop.action.lang.SwapLangAction;
import com.epam.computershop.action.order.RemoveOrderActon;
import com.epam.computershop.action.order.ShowOrderAction;
import com.epam.computershop.action.order.ShowOrdersCatalogAction;
import com.epam.computershop.action.order.SwapOrderStatusAction;
import com.epam.computershop.action.order.basket.*;
import com.epam.computershop.action.product.*;
import com.epam.computershop.action.user.*;
import com.epam.computershop.action.user.admin.SwapUserRoleAction;
import com.epam.computershop.action.user.admin.UsersCatalogAction;
import com.epam.computershop.action.user.admin.UsersOrdersCatalogAction;
import com.epam.computershop.util.ConstantStorage;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private volatile static ActionFactory currentActionFactory;
    private static final Map<String, Action> actionsUries = new HashMap<>();

    public static final String ACTION_USER_LOGIN = "/auth";
    public static final String ACTION_USER_REGISTRATION = "/reg";
    public static final String ACTION_USER_LOGOUT = "/logout";
    public static final String ACTION_USER_EDIT_PERSONAL = "/edit_personal";
    public static final String ACTION_USER_EDIT_BALANCE = "/edit_balance";
    public static final String ACTION_USER_SWAP_ROLE = "/swap_user_role";
    public static final String ACTION_USER_CATALOG = "/catalog_user";
    public static final String ACTION_USER_SHOW_PERSONAL_PAGE = "/personal";
    public static final String ACTION_USER_SHOW_LOGIN_PAGE = "/login";
    public static final String ACTION_USER_SHOW_REGISTRATION_PAGE = "/registration";
    public static final String ACTION_USER_SHOW_EDIT_BALANCE_PAGE = "/edit_page_balance";

    public static final String ACTION_DELIVPROF_CHANGE = "/change_delivprof";
    public static final String ACTION_DELIVPROF_REMOVE = "/remove_delivprof";
    public static final String ACTION_DELIVPROF_CATALOG = "/catalog_delivprof";
    public static final String ACTION_DELIVPROF_SHOW_EDIT_PAGE = "/edit_page_delivprof";

    public static final String ACTION_CATEGORY_CHANGE = "/change_category";
    public static final String ACTION_CATEGORY_REMOVE = "/remove_category";
    public static final String ACTION_CATEGORY_SHOW_MANAGE_PAGE = "/manage_categories";

    public static final String ACTION_PRODUCT_CHANGE = "/change_product";
    public static final String ACTION_PRODUCT_REMOVE = "/remove_product";
    public static final String ACTION_PRODUCT_CATALOG = "/catalog_product";
    public static final String ACTION_PRODUCT_SEARCH = "/search";
    public static final String ACTION_PRODUCT_SHOW = "/product";
    public static final String ACTION_PRODUCT_SHOW_EDIT_PAGE = "/edit_page_product";

    public static final String ACTION_ORDER_REMOVE = "/remove_order";
    public static final String ACTION_ORDER_CATALOG = "/catalog_order";
    public static final String ACTION_ORDERS_USERS_CATALOG = "/catalog_users_orders";
    public static final String ACTION_ORDER_SWAP_STATUS = "/swap_order_status";
    public static final String ACTION_ORDER_SHOW = "/order";

    public static final String ACTION_BASKET_ADD_PRODUCT = "/add_to_basket";
    public static final String ACTION_BASKET_REMOVE_PRODUCT = "/remove_from_basket";
    public static final String ACTION_BASKET_EDIT_QUANTITY = "/update_quantity";
    public static final String ACTION_BASKET_SHOW = "/basket";
    public static final String ACTION_BASKET_CHECKOUT = "/checkout";
    public static final String ACTION_BASKET_SHOW_CHECKOUT_PAGE = "/page_checkout";

    public static final String ACTION_LANG_ADD = "/add_lang";
    public static final String ACTION_LANG_SHOW_MANAGE_PAGE = "/manage_langs";
    public static final String ACTION_LANG_SWAP = "/swap_lang";

    private ActionFactory() {
        //User account actions
        actionsUries.put(ACTION_USER_LOGIN, new LoginAction(ConstantStorage.ROLE_ID_GUEST_ONLY));
        actionsUries.put(ACTION_USER_REGISTRATION, new RegistrationAction(ConstantStorage.ROLE_ID_GUEST_ONLY));
        actionsUries.put(ACTION_USER_LOGOUT, new LogoutAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_USER_EDIT_PERSONAL, new EditPersonalAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_USER_EDIT_BALANCE, new EditBalanceAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_USER_SWAP_ROLE, new SwapUserRoleAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_USER_CATALOG, new UsersCatalogAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_USER_SHOW_LOGIN_PAGE, new ShowLoginPageAction(ConstantStorage.ROLE_ID_GUEST_ONLY));
        actionsUries.put(ACTION_USER_SHOW_REGISTRATION_PAGE, new ShowRegistrationPageAction(ConstantStorage.ROLE_ID_GUEST_ONLY));
        actionsUries.put(ACTION_USER_SHOW_PERSONAL_PAGE, new ShowPersonalPageAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_USER_SHOW_EDIT_BALANCE_PAGE, new ShowEditBalancePageAction(ConstantStorage.ROLE_ID_USER));

        //Delivery profile actions
        actionsUries.put(ACTION_DELIVPROF_CATALOG, new ShowDeliveryProfilesCatalogAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_DELIVPROF_CHANGE, new ChangeDeliveryProfileAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_DELIVPROF_REMOVE, new RemoveDeliveryProfileAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_DELIVPROF_SHOW_EDIT_PAGE, new ShowChangePageDeliveryProfileAction(ConstantStorage.ROLE_ID_USER));

        //Category actions
        actionsUries.put(ACTION_CATEGORY_CHANGE, new ChangeCategoryAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_CATEGORY_REMOVE, new RemoveCategoryAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_CATEGORY_SHOW_MANAGE_PAGE, new ShowManageCategoriesPageAction(ConstantStorage.ROLE_ID_ADMIN));

        //Product actions
        actionsUries.put(ACTION_PRODUCT_CATALOG, new ShowProductsCatalogAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_PRODUCT_SEARCH, new SearchProductAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_PRODUCT_CHANGE, new ChangeProductAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_PRODUCT_SHOW_EDIT_PAGE, new ShowChangePageProductAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_PRODUCT_REMOVE, new RemoveProductAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_PRODUCT_SHOW, new ShowProductAction(ConstantStorage.ROLE_ID_GUEST));

        //Order actions
        actionsUries.put(ACTION_ORDER_CATALOG, new ShowOrdersCatalogAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_ORDERS_USERS_CATALOG, new UsersOrdersCatalogAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_ORDER_REMOVE, new RemoveOrderActon(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_ORDER_SWAP_STATUS, new SwapOrderStatusAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_ORDER_SHOW, new ShowOrderAction(ConstantStorage.ROLE_ID_USER));
        //Basket actions
        actionsUries.put(ACTION_BASKET_SHOW, new ShowBasketAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_BASKET_ADD_PRODUCT, new AddToBasketAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_BASKET_EDIT_QUANTITY, new UpdateQuantityAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_BASKET_REMOVE_PRODUCT, new RemoveFromBasketAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_BASKET_CHECKOUT, new CheckoutAction(ConstantStorage.ROLE_ID_USER));
        actionsUries.put(ACTION_BASKET_SHOW_CHECKOUT_PAGE, new ShowCheckoutPageAction(ConstantStorage.ROLE_ID_GUEST));

        //Lang actions
        actionsUries.put(ACTION_LANG_SWAP, new SwapLangAction(ConstantStorage.ROLE_ID_GUEST));
        actionsUries.put(ACTION_LANG_ADD, new AddLangAction(ConstantStorage.ROLE_ID_ADMIN));
        actionsUries.put(ACTION_LANG_SHOW_MANAGE_PAGE, new ShowManageLangsPageAction(ConstantStorage.ROLE_ID_ADMIN));
    }

    public static ActionFactory getInstance() {
        ActionFactory actionFactory = currentActionFactory;
        if (actionFactory == null) {
            synchronized (ActionFactory.class) {
                actionFactory = currentActionFactory;
                if (actionFactory == null) {
                    actionFactory = currentActionFactory = new ActionFactory();
                }
            }
        }
        return actionFactory;
    }

    public Action getAction(String actionUri) {
        Action action = actionsUries.get(actionUri);
        if (action == null) {
            action = new ShowIndexPageAction(ConstantStorage.ROLE_ID_GUEST);
        }
        return action;
    }

    public Short getActionAccessRoleId(String actionUri) {
        Action action = actionsUries.get(actionUri);
        short accessRoleId;
        if (action != null) {
            accessRoleId = actionsUries.get(actionUri).getAccessRoleId();
        } else {
            accessRoleId = ConstantStorage.ROLE_ID_ERROR;
        }
        return accessRoleId;
    }
}
