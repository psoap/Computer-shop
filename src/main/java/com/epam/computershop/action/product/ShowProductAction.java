package com.epam.computershop.action.product;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.ProductDao;
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

public class ShowProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowProductAction.class);

    public ShowProductAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_PRODUCT;

        long productIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        if (productIdFromRequest != NumberUtil.INVALID_NUMBER) {
            ProductDao productDao = new ProductDao();
            try {
                Product product = productDao.findById(productIdFromRequest);
                if (product != null) {
                    req.setAttribute(ConstantStorage.PRODUCT, product);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (SQLException | ConnectionPoolException | IOException e) {
                LOGGER.error("Failed to select product id - " + productIdFromRequest);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
            responseUrl = URLUtil.getRefererURL(req);
        }
        return responseUrl;
    }
}
