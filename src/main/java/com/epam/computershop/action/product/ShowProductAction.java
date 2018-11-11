package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class ShowProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowProductAction.class);

    public ShowProductAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_PRODUCT;

        long productIdFromRequest = tryParseLong(req.getParameter(ID));
        if (productIdFromRequest != INVALID_NUMBER) {
            find(productIdFromRequest, req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }

    private void find(long productIdFromRequest, HttpServletRequest req, HttpServletResponse resp ) {
        ProductDao productDao = new ProductDao();
        try {
            Product product = productDao.findById(productIdFromRequest);
            if (product != null) {
                req.setAttribute(PRODUCT, product);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to select product id - " + productIdFromRequest, e);
        }
    }
}
