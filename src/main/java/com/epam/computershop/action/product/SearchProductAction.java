package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class SearchProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(SearchProductAction.class);

    public SearchProductAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String queryFromRequest = req.getParameter(QUERY);
        String responseUrl;
        if ((queryFromRequest != null)
                && Validator.validate(PRODUCT_NAME_PATTERN, queryFromRequest)) {
            ProductDao productDao = new ProductDao();
            try {
                req.setAttribute(PRODUCTS, productDao.findBySearchQuery(queryFromRequest));
                req.setAttribute(NAME, queryFromRequest);
                responseUrl = JSP_PRODUCT_CATALOG;
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select products by query - " + queryFromRequest, e);
                responseUrl = URLUtil.getRefererURL(req);
            }
        } else {
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
