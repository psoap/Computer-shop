package com.epam.computershop.action.category;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.CategoryUtil;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoveCategoryAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RemoveCategoryAction.class);

    public RemoveCategoryAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        short categoryIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.ID));
        if (categoryIdFromRequest != NumberUtil.INVALID_NUMBER) {
            CategoryDao categoryDao = new CategoryDao();
            Category dummyCategory = new Category();
            dummyCategory.setId(categoryIdFromRequest);
            try {
                categoryDao.remove(dummyCategory);
                Map<Locale, CopyOnWriteArrayList<Category>> allCategories = ((Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext().getAttribute(ConstantStorage.ALL_CATEGORIES));
                List<Locale> allLangs = ((List) req.getServletContext().getAttribute(ConstantStorage.ALL_LANGS));
                CategoryUtil.refreshLangsCategories(allCategories, categoryDao.findAllByLangs(allLangs));
                CategoryUtil.fillChildrenCategories(allCategories);
                LOGGER.debug("Category id - " + categoryIdFromRequest + " was removed");
                messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to remove category, id - " + categoryIdFromRequest);
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        } else {
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_CATEGORY_SHOW_MANAGE_PAGE);
    }
}
