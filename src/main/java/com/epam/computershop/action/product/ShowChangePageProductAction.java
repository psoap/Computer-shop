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
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class ShowChangePageProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ShowChangePageProductAction.class);

    public ShowChangePageProductAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = JSP_PRODUCT_CHANGE;

        long productIdFromRequest = tryParseLong(req.getParameter(ID));
        if (productIdFromRequest != INVALID_NUMBER) {
            ProductDao productDao = new ProductDao();
            try {
                Product product = productDao.findById(productIdFromRequest);
                req.setAttribute(PRODUCT, product);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to select product id - " + productIdFromRequest, e);
                resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                responseUrl = URLUtil.getRefererURL(req);
                List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);
                messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
            }
        }
        return responseUrl;
    }
}
