package com.epam.computershop.util;

public final class SQLQueriesStorage {
    //Tables' columns names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_STATUS_ID = "status_id";
    public static final String COLUMN_DELIVERY_PROFILE_ID = "delivery_profile_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE_ID = "role_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_ADDRESS_LOCATION = "address_location";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PATRONYMIC = "patronymic";
    public static final String COLUMN_PARENT_ID = "parent_id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    public static final String COLUMN_CHANGE_DATE = "change_date";
    public static final String COLUMN_IMAGE_URL = "image_url";

    //User Table
    public static final String SELECT_ALL_USERS = "SELECT * FROM \"user\" ";
    public static final String SELECT_USER_BY_LOGIN = SELECT_ALL_USERS + "WHERE login=?;";
    public static final String SELECT_USER_BY_ID = SELECT_ALL_USERS + "WHERE id=?;";
    public static final String INSERT_USER = "INSERT INTO \"user\" (email, password, role_id, balance, login)\n" +
            "VALUES(?, ?, ?, ?, ?);";
    public static final String UPDATE_USER = "UPDATE \"user\"\n" +
            "SET email = ?, password = ?, role_id = ?, balance = ?\nWHERE id = ?;";
    public static final String DELETE_USER = "DELETE FROM \"user\" WHERE id = ?;";

    //Delivery Profile Table
    public static final String SELECT_DELIVERY_PROFILES_BY_USER_ID = "SELECT * FROM \"delivery_profile\" WHERE user_id=?";
    public static final String SELECT_USER_DELIVERY_PROFILE_BY_ID = "SELECT * FROM \"delivery_profile\" WHERE id=?;";
    public static final String INSERT_DELIVERY_PROFILE = "INSERT INTO \"delivery_profile\" (first_name, last_name, patronymic, address_location, phone_number, user_id)\n" +
            "VALUES(?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_DELIVERY_PROFILE = "UPDATE \"delivery_profile\"\n" +
            "SET first_name=?, last_name=?, patronymic=?, address_location=?, phone_number=?\n" +
            "WHERE id = ?;";
    public static final String DELETE_DELIVERY_PROFILE = "DELETE FROM \"delivery_profile\" WHERE id = ?;";

    //Lang Table
    public static final String SELECT_ALL_LOCALES = "SELECT code FROM \"lang\" ";
    public static final String INSERT_LOCALE = "INSERT INTO \"lang\" (code, name)\nVALUES(?, ?);";

    //Category Table
    public static final String INSERT_CATEGORY = "INSERT INTO \"category\" (parent_id)\nVALUES(?);";
    public static final String DELETE_CATEGORY = "DELETE FROM \"category_translate\" " +
            "WHERE \"category_translate\".category_id = ? OR \"category_translate\".category_id " +
            "IN (SELECT \"category\".id FROM \"category\" WHERE \"category\".parent_id = ?);\n" +
            "DELETE FROM \"category\" " +
            "WHERE \"category\".id = ? OR \"category\".parent_id = ?;";
    public static final String SELECT_CATEGORIES_BY_LOCALE =
            "SELECT * FROM \"category\", \"category_translate\" " +
                    "WHERE \"category_translate\".lang_code = ? " +
                    "AND \"category_translate\".category_id = \"category\".id;";

    //Category Translate Table
    public static final String INSERT_CATEGORY_TRANSLATION = "INSERT INTO \"category_translate\" (category_id, name, lang_code)\nVALUES(?, ?, ?);";
    public static final String UPDATE_CATEGORY_TRANSLATION = "UPDATE \"category_translate\"\n" +
            "SET name = ?\n" +
            "WHERE category_id = ? AND lang_code=?";

    //Product Table
    public static final String INSERT_PRODUCT = "INSERT INTO \"product\" (name, short_description, description, price, category_id, image_url)\nVALUES(?, ?, ?, ?, ?, ?);";
    public static final String DELETE_PRODUCT = "DELETE FROM \"product\" WHERE id = ?;";
    public static final String SELECT_PRODUCTS_BY_CATEGORY = "SELECT * FROM \"product\" WHERE \"product\".category_id = ? LIMIT ? OFFSET ?;";
    public static final String SELECT_PRODUCTS_BY_SEARCH_QUERY = "SELECT * FROM \"product\" WHERE LOWER(\"product\".name) LIKE LOWER(?);";
    public static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM \"product\" WHERE id = ?;";
    public static final String UPDATE_PRODUCT = "UPDATE \"product\"\n" +
            "SET name=?, short_description=?, description=?, price=?, category_id=?, image_url=?\nWHERE id=?;";
    public static final String SELECT_PRODUCTS_COUNT_BY_CATEGORY = "SELECT COUNT(id) FROM \"product\" WHERE category_id = ?;";

    //Order Table
    public static final String INSERT_ORDER = "INSERT INTO \"order\" (delivery_profile_id, status_id, total_price, change_date, user_id)\n VALUES(?, ?, ?, ?, ?);";
    public static final String DELETE_ORDER = "DELETE FROM \"order_product\" WHERE order_id = ?;DELETE FROM \"order\" WHERE id = ?;";
    public static final String SELECT_ORDERS = "SELECT * FROM \"order\" WHERE user_id=?;";
    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM \"order\" WHERE id=?;";
    public static final String SELECT_ORDERS_BY_STATUS_AND_USER_ID = "SELECT * FROM \"order\" WHERE user_id=? AND status_id=?;";
    public static final String SELECT_ORDERS_BY_STATUS = "SELECT * FROM \"order\" WHERE status_id=?;";
    public static final String UPDATE_ORDER = "UPDATE \"order\"\n" +
            "SET delivery_profile_id=?, status_id=?, total_price=?, change_date=?\nWHERE id=?;";

    //Order Product Table
    public static final String SELECT_ORDER_PRODUCT = "SELECT * FROM \"order_product\"\n" +
            "FULL OUTER JOIN \"product\" ON \"order_product\".product_id=\"product\".id\n" +
            "WHERE \"order_product\".order_id=?";
    public static final String INSERT_ORDER_PRODUCT = "INSERT INTO \"order_product\" (order_id, product_id, quantity)\n" +
            "SELECT \"order\".id, ?, ? FROM \"order\" WHERE user_id=? AND status_id=?" +
            "ON CONFLICT ON CONSTRAINT order_product_order_id_product_id_key DO UPDATE SET quantity=?;";
    public static final String UPDATE_ORDER_PRODUCT = "UPDATE \"order_product\"\n" +
            "SET quantity=?\n" +
            "WHERE order_id IN (SELECT id FROM \"order\" WHERE user_id=? AND status_id=?) AND product_id=?;";
    public static final String DELETE_ORDER_PRODUCT = "DELETE FROM \"order_product\" WHERE order_id IN (SELECT id FROM \"order\" WHERE user_id=? AND status_id=?) AND product_id=?;";

    private SQLQueriesStorage() {
    }
}
