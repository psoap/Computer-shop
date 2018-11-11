package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class ShowProductsCatalogAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowProductsCatalogAction.class);

    public ShowProductsCatalogAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_PRODUCT_CATALOG;

        short categoryIdFromRequest = tryParseShort(req.getParameter(ID));
        if (categoryIdFromRequest != INVALID_NUMBER) {
            CopyOnWriteArrayList<Category> categories =
                    (CopyOnWriteArrayList<Category>) req.getSession().getAttribute(CURRENT_LOCALE_CATEGORIES);
            categories.stream()
                    .filter(foundedCategory -> foundedCategory.getId() == categoryIdFromRequest)
                    .findFirst()
                    .map(category -> ifCategoryFounded(category, req))
                    .orElseGet(() -> {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return null;
                    });
        } else {
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }

    private Category ifCategoryFounded(Category category, HttpServletRequest req) {
        short pageIdFromRequest = tryParseShort(req.getParameter(PAGE));
        ProductDao productDao = new ProductDao();
        try {
            int count = productDao.getCountOfRowsByCategory(category.getId());
            if (count > ZERO) {
                List<Product> products = productDao.findAllByCategory(
                        category.getId(),
                        COUNT_PRODUCTS_ON_PAGE,
                        COUNT_PRODUCTS_ON_PAGE * (Math.abs(pageIdFromRequest) - 1));
                req.setAttribute(PRODUCTS, products);
                req.setAttribute(ID, category.getId());
                req.setAttribute(QUANTITY, count);
            }
            req.setAttribute(NAME, category.getName());
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select category products.", e);
        }
        return category;
    }
}
