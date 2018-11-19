package com.epam.computershop.action;

import com.epam.computershop.action.category.ChangeCategoryAction;
import com.epam.computershop.action.category.RemoveCategoryAction;
import com.epam.computershop.action.category.ShowManageCategoriesPageAction;
import com.epam.computershop.action.deliveryprofile.ChangeDeliveryProfileAction;
import com.epam.computershop.action.deliveryprofile.RemoveDeliveryProfileAction;
import com.epam.computershop.action.deliveryprofile.ShowChangePageDeliveryProfileAction;
import com.epam.computershop.action.deliveryprofile.ShowDeliveryProfilesCatalogAction;
import com.epam.computershop.action.lang.AddLangAction;
import com.epam.computershop.action.lang.ShowManageLangPageAction;
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
import com.epam.computershop.enums.UserRole;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
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

    public static final String ACTION_DELIVERY_PROFILE_CHANGE = "/change_delivery_profile";
    public static final String ACTION_DELIVERY_PROFILE_REMOVE = "/remove_delivery_profile";
    public static final String ACTION_DELIVERY_PROFILE_CATALOG = "/catalog_delivery_profile";
    public static final String ACTION_DELIVERY_PROFILE_SHOW_EDIT_PAGE = "/edit_page_delivery_profile";

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
    public static final String ACTION_LANG_SHOW_MANAGE_PAGE = "/manage_lang";
    public static final String ACTION_LANG_SWAP = "/swap_lang";

    private static final Map<String, Action> uriAction = new HashMap<>();
    private volatile static ActionFactory currentActionFactory;

    private ActionFactory() {
        //User account actions
        uriAction.put(ACTION_USER_LOGIN, new LoginAction(UserRole.GUEST_ONLY));
        uriAction.put(ACTION_USER_REGISTRATION, new RegistrationAction(UserRole.GUEST_ONLY));
        uriAction.put(ACTION_USER_LOGOUT, new LogoutAction(UserRole.USER));
        uriAction.put(ACTION_USER_EDIT_PERSONAL, new EditPersonalAction(UserRole.USER));
        uriAction.put(ACTION_USER_EDIT_BALANCE, new EditBalanceAction(UserRole.USER));
        uriAction.put(ACTION_USER_SWAP_ROLE, new SwapUserRoleAction(UserRole.ADMIN));
        uriAction.put(ACTION_USER_CATALOG, new UsersCatalogAction(UserRole.ADMIN));
        uriAction.put(ACTION_USER_SHOW_LOGIN_PAGE, new ShowLoginPageAction(UserRole.GUEST_ONLY));
        uriAction.put(ACTION_USER_SHOW_REGISTRATION_PAGE, new ShowRegistrationPageAction(UserRole.GUEST_ONLY));
        uriAction.put(ACTION_USER_SHOW_PERSONAL_PAGE, new ShowPersonalPageAction(UserRole.USER));
        uriAction.put(ACTION_USER_SHOW_EDIT_BALANCE_PAGE, new ShowEditBalancePageAction(UserRole.USER));

        //Delivery profile actions
        uriAction.put(ACTION_DELIVERY_PROFILE_CATALOG, new ShowDeliveryProfilesCatalogAction(UserRole.USER));
        uriAction.put(ACTION_DELIVERY_PROFILE_CHANGE, new ChangeDeliveryProfileAction(UserRole.USER));
        uriAction.put(ACTION_DELIVERY_PROFILE_REMOVE, new RemoveDeliveryProfileAction(UserRole.USER));
        uriAction.put(ACTION_DELIVERY_PROFILE_SHOW_EDIT_PAGE, new ShowChangePageDeliveryProfileAction(UserRole.USER));

        //Category actions
        uriAction.put(ACTION_CATEGORY_CHANGE, new ChangeCategoryAction(UserRole.ADMIN));
        uriAction.put(ACTION_CATEGORY_REMOVE, new RemoveCategoryAction(UserRole.ADMIN));
        uriAction.put(ACTION_CATEGORY_SHOW_MANAGE_PAGE, new ShowManageCategoriesPageAction(UserRole.ADMIN));

        //Product actions
        uriAction.put(ACTION_PRODUCT_CATALOG, new ShowProductsCatalogAction(UserRole.GUEST));
        uriAction.put(ACTION_PRODUCT_SEARCH, new SearchProductAction(UserRole.GUEST));
        uriAction.put(ACTION_PRODUCT_CHANGE, new ChangeProductAction(UserRole.ADMIN));
        uriAction.put(ACTION_PRODUCT_SHOW_EDIT_PAGE, new ShowChangePageProductAction(UserRole.ADMIN));
        uriAction.put(ACTION_PRODUCT_REMOVE, new RemoveProductAction(UserRole.ADMIN));
        uriAction.put(ACTION_PRODUCT_SHOW, new ShowProductAction(UserRole.GUEST));

        //Order actions
        uriAction.put(ACTION_ORDER_CATALOG, new ShowOrdersCatalogAction(UserRole.USER));
        uriAction.put(ACTION_ORDERS_USERS_CATALOG, new UsersOrdersCatalogAction(UserRole.ADMIN));
        uriAction.put(ACTION_ORDER_REMOVE, new RemoveOrderActon(UserRole.USER));
        uriAction.put(ACTION_ORDER_SWAP_STATUS, new SwapOrderStatusAction(UserRole.ADMIN));
        uriAction.put(ACTION_ORDER_SHOW, new ShowOrderAction(UserRole.USER));
        //Basket actions
        uriAction.put(ACTION_BASKET_SHOW, new ShowBasketAction(UserRole.GUEST));
        uriAction.put(ACTION_BASKET_ADD_PRODUCT, new AddToBasketAction(UserRole.GUEST));
        uriAction.put(ACTION_BASKET_EDIT_QUANTITY, new UpdateQuantityAction(UserRole.GUEST));
        uriAction.put(ACTION_BASKET_REMOVE_PRODUCT, new RemoveFromBasketAction(UserRole.GUEST));
        uriAction.put(ACTION_BASKET_CHECKOUT, new CheckoutAction(UserRole.USER));
        uriAction.put(ACTION_BASKET_SHOW_CHECKOUT_PAGE, new ShowCheckoutPageAction(UserRole.GUEST));

        //Lang actions
        uriAction.put(ACTION_LANG_SWAP, new SwapLangAction(UserRole.GUEST));
        uriAction.put(ACTION_LANG_ADD, new AddLangAction(UserRole.ADMIN));
        uriAction.put(ACTION_LANG_SHOW_MANAGE_PAGE, new ShowManageLangPageAction(UserRole.ADMIN));
    }

    public static ActionFactory getInstance() {
        ActionFactory actionFactory = currentActionFactory;
        if (actionFactory == null) {
            synchronized (ActionFactory.class){
                actionFactory = currentActionFactory;
                if (actionFactory == null) {
                    actionFactory = currentActionFactory = new ActionFactory();
                }
            }
        }
        return actionFactory;
    }

    public Action getAction(String actionUri) {
        Action action = uriAction.get(actionUri);
        if (action == null) {
            action = new NotFoundAction(UserRole.GUEST);
        }
        return action;
    }

    public UserRole getActionAccessRole(String actionUri) {
        return getAction(actionUri).getAccessRole();
    }
}
