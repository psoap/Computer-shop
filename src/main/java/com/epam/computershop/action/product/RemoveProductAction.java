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

public class RemoveProductAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RemoveProductAction.class);

    public RemoveProductAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        long productIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        if (productIdFromRequest != NumberUtil.INVALID_NUMBER) {
            ProductDao productDao = new ProductDao();
            Product dummyProduct = new Product();
            dummyProduct.setId(productIdFromRequest);
            try {
                productDao.remove(dummyProduct);
                LOGGER.debug("Product id" + productIdFromRequest + " was removed");
                messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to remove product id - " + productIdFromRequest);
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        } else {
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }
}
