package com.epam.computershop.action.category;

import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.action.RemoveAction;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.CategoryUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;

public class RemoveCategoryAction extends RemoveAction {
    private static final Logger LOGGER = Logger.getLogger(RemoveCategoryAction.class);

    public RemoveCategoryAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    protected String remove(HttpServletRequest req, long categoryIdFromRequest, List<String> messagesForJsp) {
        Category dummyCategory = new Category();
        dummyCategory.setId((short) categoryIdFromRequest);

        CategoryDao categoryDao = new CategoryDao();
        try {
            categoryDao.remove(dummyCategory);

            Map<Locale, CopyOnWriteArrayList<Category>> localesCategories = (
                    (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext()
                            .getAttribute(ALL_LOCALES_CATEGORIES)
            );
            List<Locale> locales = ((List) req.getServletContext().getAttribute(ALL_LOCALES));
            CategoryUtil.refreshLocalesCategories(localesCategories, categoryDao.findAllByEachLocale(locales));
            CategoryUtil.fillCategoriesChildren(localesCategories);
            LOGGER.debug("Category id - " + categoryIdFromRequest + " was removed");
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to remove category, id - " + categoryIdFromRequest, e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_CATEGORY_SHOW_MANAGE_PAGE);
    }
}
