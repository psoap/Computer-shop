package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ShowProductsCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowProductsCatalogAction.class);

    public ShowProductsCatalogAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_PRODUCT_CATALOG;

        short categoryIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.ID));
        short pageIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.PAGE));
        if (categoryIdFromRequest != NumberUtil.INVALID_NUMBER) {
            CopyOnWriteArrayList<Category> categories = (CopyOnWriteArrayList<Category>) req.getSession().getAttribute(ConstantStorage.CURRENT_LANG_CATEGORIES);
            Optional<Category> category = categories.stream().filter(categ -> categ.getId() == categoryIdFromRequest).findFirst();
            if (category.isPresent()) {
                ProductDao productDao = new ProductDao();
                try {
                    int count = productDao.getCountOfRowsByCategory(categoryIdFromRequest);
                    if (count > ConstantStorage.ZERO) {
                        List<Product> products = productDao.findAllByCategory(
                                categoryIdFromRequest,
                                ConstantStorage.COUNT_PRODUCTS_ON_PAGE,
                                ConstantStorage.COUNT_PRODUCTS_ON_PAGE * (Math.abs(pageIdFromRequest) - 1));
                        req.setAttribute(ConstantStorage.PRODUCTS, products);
                        req.setAttribute(ConstantStorage.ID, categoryIdFromRequest);
                        req.setAttribute(ConstantStorage.QUANTITY, count);
                    }
                    req.setAttribute(ConstantStorage.NAME, category.get().getName());
                } catch (SQLException | ConnectionPoolException e) {
                    LOGGER.error("Failed to select category products");
                }
            } else {
                try {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.error("Failed to send not found response");
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
