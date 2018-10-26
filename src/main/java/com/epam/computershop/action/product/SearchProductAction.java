package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class SearchProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(SearchProductAction.class);

    public SearchProductAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl;

        String queryFromRequest = req.getParameter(ConstantStorage.QUERY);
        if (queryFromRequest != null && Validator.validate(ValidatorSettingsStorage.PRODUCT_NAME_PATTERN, queryFromRequest)) {
            ProductDao productDao = new ProductDao();
            try {
                req.setAttribute(ConstantStorage.PRODUCTS, productDao.findBySearchQuery(queryFromRequest));
                req.setAttribute(ConstantStorage.NAME, queryFromRequest);
                responseUrl = JSP_PRODUCT_CATALOG;
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select products by query - " + queryFromRequest);
                responseUrl = URLUtil.getRefererURL(req);
            }
        } else {
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
