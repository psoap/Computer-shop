package com.epam.computershop.action.product;

import com.epam.computershop.action.RemoveAction;
import com.epam.computershop.dao.ProductDao;
import com.epam.computershop.entity.Product;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;

public class RemoveProductAction extends RemoveAction {
    private static final Logger LOGGER = Logger.getLogger(RemoveProductAction.class);

    public RemoveProductAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    protected String remove(HttpServletRequest req, long productIdFromRequest, List<String> messagesForJsp) {
        ProductDao productDao = new ProductDao();
        Product dummyProduct = new Product();
        dummyProduct.setId(productIdFromRequest);
        try {
            productDao.remove(dummyProduct);
            LOGGER.debug("Product id" + productIdFromRequest + " was removed");
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to remove product id - " + productIdFromRequest, e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return URLUtil.getRefererURL(req);
    }
}
