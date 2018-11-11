package com.epam.computershop.action.category;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class ChangeCategoryAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ChangeCategoryAction.class);

    public ChangeCategoryAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        short categoryIdFromRequest = tryParseShort(req.getParameter(ID));
        if (categoryIdFromRequest == INVALID_NUMBER) {
            add(req, messagesForJsp);
        } else {
            edit(req, messagesForJsp);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_CATEGORY_SHOW_MANAGE_PAGE);
    }

    private void add(HttpServletRequest req, List<String> messagesForJsp) {
        List<Locale> locales = (List<Locale>) req.getServletContext().getAttribute(ALL_LOCALES);

        List<Category> categoryTranslations = new ArrayList<>(locales.size());
        boolean hasValidationError = fillCategoryTranslations(req, locales, categoryTranslations, messagesForJsp);
        if(hasValidationError){
            return;
        }

        Map<Locale, CopyOnWriteArrayList<Category>> localesCategories =
                (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext()
                        .getAttribute(ALL_LOCALES_CATEGORIES);
        CategoryDao categoryDao = new CategoryDao();
        try {
            categoryDao.insertCategoryWithTranslations(categoryTranslations);
            CategoryUtil.refreshLocalesCategories(localesCategories, categoryDao.findAllByEachLocale(locales));
            CategoryUtil.fillCategoriesChildren(localesCategories);
            LOGGER.debug("Category id - " + categoryTranslations.get(ZERO).getId() + " was created");
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to insert category.", e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
    }

    private void edit(HttpServletRequest req, List<String> messagesForJsp) {
        Map<Locale, CopyOnWriteArrayList<Category>> localesCategories =
                (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext()
                        .getAttribute(ALL_LOCALES_CATEGORIES);

        short categoryIdFromRequest = tryParseShort(req.getParameter(ID));
        String categoryLangCodeFromRequest = req.getParameter(LANG_CODE).trim();
        String categoryNameFromRequest = req.getParameter(NAME).trim();

        if (validateName(categoryNameFromRequest, messagesForJsp)) {
            return;
        }

        CopyOnWriteArrayList<Category> localeCategories = localesCategories
                .get(Locale.forLanguageTag(categoryLangCodeFromRequest));

        localeCategories.stream().filter(category -> category.getId() == categoryIdFromRequest)
                .findFirst()
                .ifPresent(category -> updateCategoryName(categoryNameFromRequest, category, messagesForJsp));
    }

    private boolean fillCategoryTranslations(HttpServletRequest req, List<Locale> locales,
                                           List<Category> categoryTranslations, List<String> messagesForJsp){
        short parentId = tryParseShort(req.getParameter(PARENT_ID));
        for (Locale locale : locales) {
            String name = req.getParameter(locale.getLanguage()).trim();

            if (validateName(name, messagesForJsp)) {
                return true;
            }

            Category category = new Category();
            category.setParentId((parentId == INVALID_NUMBER) ? null : parentId);
            category.setName(name);
            category.setLangCode(locale.getLanguage());
            categoryTranslations.add(category);
        }
        return false;
    }

    private void updateCategoryName(String newCategoryName, Category category, List<String> messagesForJsp) {
        String oldName = category.getName();
        category.setName(newCategoryName);
        try {
            CategoryDao categoryDao = new CategoryDao();
            categoryDao.update(category);
            LOGGER.debug("Category - " + newCategoryName + " was updated");
            messagesForJsp.add(GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            category.setName(oldName);
            LOGGER.error("Failed to update category, id - " + category.getId(), e);
            messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
        }
    }

    private boolean validateName(String categoryName, List<String> messagesForJsp) {
        boolean result = false;
        if (!Validator.validate(CATEGORY_NAME_PATTERN, categoryName)) {
            messagesForJsp.add(CHANGE_CATEGORY_WARN_BAD_NAME);
            result = true;
        }
        return result;
    }
}