package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ChangeProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ChangeProductAction.class);

    private static final String REDIRECT_URI = ActionFactory.ACTION_PRODUCT_SHOW + "?id=";

    public ChangeProductAction(short accessRoleId) {
        super(accessRoleId);
    }

    private static void validateForm(String name, BigDecimal price,
                                     short categoryId, List<String> messagesForJsp) {
        if (!Validator.validate(ValidatorSettingsStorage.PRODUCT_NAME_PATTERN, name)) {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        }
        if (price == null) {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        }
        if (categoryId == NumberUtil.INVALID_NUMBER) {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        }
    }

    private String add(String name, String shortDescription, String description, BigDecimal price,
                       short categoryId, String imageUrl, HttpServletRequest req, HttpServletResponse resp,
                       List<String> messagesForJsp) {
        String responseUrl = ActionFactory.ACTION_PRODUCT_SHOW_EDIT_PAGE;
        ProductDao productDao = new ProductDao();
        Product product = new Product();
        product.setName(name);
        product.setShortDescription(shortDescription);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategoryId(categoryId);
        product.setImageUrl(imageUrl);
        try {
            productDao.insert(product);
            req.setAttribute(ConstantStorage.PRODUCT, product);
            LOGGER.debug("Product id - " + product.getId() + " was created");
            messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
            responseUrl = REDIRECT_URI + product.getId();
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to insert product");
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
        return responseUrl;
    }

    private String edit(long id, String name, String shortDescription, String description, BigDecimal price,
                        short categoryId, String imageUrl, HttpServletRequest req, HttpServletResponse resp,
                        List<String> messagesForJsp) {
        String responseUrl = ActionFactory.ACTION_PRODUCT_SHOW_EDIT_PAGE;
        ProductDao productDao = new ProductDao();
        try {
            Product product = productDao.findById(id);
            if (product != null) {
                product.setName(name);
                product.setShortDescription(shortDescription);
                product.setDescription(description);
                product.setPrice(price);
                product.setCategoryId(categoryId);
                if(imageUrl!=null){
                    product.setImageUrl(imageUrl);
                }
                productDao.update(product);
                req.setAttribute(ConstantStorage.PRODUCT, product);
                LOGGER.debug("Product id - " + id + " data was updated");
                messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                responseUrl = REDIRECT_URI + id;
            } else {
                messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to update product id - " + id);
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
        return responseUrl;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl;
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        String name = req.getParameter(ConstantStorage.NAME);
        String imageUrl = (String) req.getSession().getAttribute(ConstantStorage.FILE_URL);
        String description = req.getParameter(ConstantStorage.DESCRIPTION);
        String shortDescription = req.getParameter(ConstantStorage.SHORT_DESCRIPTION);
        BigDecimal price = NumberUtil.tryParseBigDecimal(req.getParameter(ConstantStorage.PRICE));
        short categoryId = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.CATEGORY_ID));

        long productIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        validateForm(name, price, categoryId, messagesForJsp);

        if (messagesForJsp.isEmpty()) {
            if (productIdFromRequest == NumberUtil.INVALID_NUMBER) {
                responseUrl = add(name, shortDescription, description, price, categoryId, imageUrl, req, resp, messagesForJsp);
            } else {
                responseUrl = edit(productIdFromRequest, name, shortDescription, description, price, categoryId, imageUrl, req, resp, messagesForJsp);
            }
            responseUrl = ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                    .concat(responseUrl);
        } else {
            responseUrl = URLUtil.getRefererURL(req);
        }
        req.getSession().removeAttribute(ConstantStorage.FILE_URL);
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return responseUrl;
    }
}
