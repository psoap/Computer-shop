package com.epam.computershop.action.product;

import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.action.ChangeAction;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Product;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class ChangeProductAction extends ChangeAction<Product> {
    private static final Logger LOGGER = Logger.getLogger(ChangeProductAction.class);

    private static final String REDIRECT_URI = ActionFactory.ACTION_PRODUCT_SHOW + "?id=";

    public ChangeProductAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    protected String add(HttpServletRequest req, Product entity, List<String> messagesForJsp) {
        String responseUrl = ActionFactory.ACTION_PRODUCT_SHOW_EDIT_PAGE;
        ProductDao productDao = new ProductDao();
        try {
            productDao.insert(entity);
            req.setAttribute(PRODUCT, entity);
            LOGGER.debug("Product id - " + entity.getId() + " was created");
            messagesForJsp.add(GENERAL_SUCCESS);
            responseUrl = REDIRECT_URI + entity.getId();
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to insert product.", e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(responseUrl);
    }

    @Override
    protected String edit(HttpServletRequest req, long entityId, Product entity, List<String> messagesForJsp) {
        String responseUrl = ActionFactory.ACTION_PRODUCT_SHOW_EDIT_PAGE;
        ProductDao productDao = new ProductDao();
        try {
            Product existedProduct = productDao.findById(entityId);
            if (existedProduct != null) {
                existedProduct.setName(entity.getName());
                existedProduct.setShortDescription(entity.getShortDescription());
                existedProduct.setDescription(entity.getDescription());
                existedProduct.setPrice(entity.getPrice());
                existedProduct.setCategoryId(entity.getCategoryId());
                if (entity.getImageUrl() != null) {
                    existedProduct.setImageUrl(entity.getImageUrl());
                }

                productDao.update(existedProduct);
                req.setAttribute(PRODUCT, existedProduct);
                LOGGER.debug("Product id - " + entityId + " data was updated");
                messagesForJsp.add(GENERAL_SUCCESS);
                responseUrl = REDIRECT_URI + entityId;
            } else {
                messagesForJsp.add(GENERAL_WARN_BAD_DATA);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update product id - " + entityId, e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(responseUrl);
    }

    @Override
    protected Product getEntityFromRequest(HttpServletRequest req) {
        Product product = new Product();
        product.setName(req.getParameter(NAME));
        product.setShortDescription(req.getParameter(SHORT_DESCRIPTION));
        product.setDescription(req.getParameter(DESCRIPTION));
        product.setPrice(tryParseBigDecimal(req.getParameter(PRICE)));
        product.setCategoryId(tryParseShort(req.getParameter(CATEGORY_ID)));
        product.setImageUrl((String) req.getSession().getAttribute(FILE_URL));
        return product;
    }

    @Override
    protected boolean validateForm(Product product, List<String> messagesForJsp) {
        boolean result = false;
        if (!Validator.validate(PRODUCT_NAME_PATTERN, product.getName())) {
            messagesForJsp.add(GENERAL_WARN_BAD_DATA);
            result = true;
        }
        if (product.getPrice() == null) {
            messagesForJsp.add(GENERAL_WARN_BAD_DATA);
            result = true;
        }
        if (product.getCategoryId() == INVALID_NUMBER) {
            messagesForJsp.add(GENERAL_WARN_BAD_DATA);
            result = true;
        }
        return result;
    }
}
