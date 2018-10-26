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
import java.sql.SQLException;
import java.util.List;

public class ShowChangePageProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowChangePageProductAction.class);

    public ShowChangePageProductAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_PRODUCT_CHANGE;

        long productIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        if (productIdFromRequest != NumberUtil.INVALID_NUMBER) {
            ProductDao productDao = new ProductDao();
            try {
                Product product = productDao.findById(productIdFromRequest);
                req.setAttribute(ConstantStorage.PRODUCT, product);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select product id - " + productIdFromRequest);
                resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                responseUrl = URLUtil.getRefererURL(req);
                List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        }
        return responseUrl;
    }
}
