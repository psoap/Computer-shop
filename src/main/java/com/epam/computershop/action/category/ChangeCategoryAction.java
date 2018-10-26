package com.epam.computershop.action.category;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChangeCategoryAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(ChangeCategoryAction.class);

    public ChangeCategoryAction(short accessRoleId) {
        super(accessRoleId);
    }

    private void add(HttpServletRequest req, List<String> messagesForJsp) {
        List<Locale> langs = (List<Locale>) req.getServletContext().getAttribute(ConstantStorage.ALL_LANGS);
        Map<Locale, CopyOnWriteArrayList<Category>> langsCategories = (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext().getAttribute(ConstantStorage.ALL_CATEGORIES);

        List<Category> categoryTranslate = new ArrayList<>(langs.size());
        short parentId = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.PARENT_ID));
        for (Locale lang : langs) {
            String name = req.getParameter(lang.getLanguage());
            if (!Validator.validate(ValidatorSettingsStorage.CATEGORY_NAME_PATTERN, name)) {
                messagesForJsp.add(ConstantStorage.CHANGE_CATEGORY_WARN_BAD_NAME);
                return;
            }
            Category category = new Category();
            category.setParentId(parentId == NumberUtil.INVALID_NUMBER ? null : parentId);
            category.setName(name);
            category.setLangCode(lang.getLanguage());
            categoryTranslate.add(category);
        }

        CategoryDao categoryDao = new CategoryDao();
        try {
            categoryDao.insertList(categoryTranslate);
            CategoryUtil.refreshLangsCategories(langsCategories, categoryDao.findAllByLangs(langs));
            CategoryUtil.fillChildrenCategories(langsCategories);
            LOGGER.debug("Category id - " + categoryTranslate.get(ConstantStorage.ZERO).getId() + " was created");
            messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Failed to insert category");
            messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
        }
    }

    private void edit(HttpServletRequest req, List<String> messagesForJsp) {
        Map<Locale, CopyOnWriteArrayList<Category>> langsCategories = (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext().getAttribute(ConstantStorage.ALL_CATEGORIES);

        short categoryIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.ID));
        String categoryLangCodeFromRequest = req.getParameter(ConstantStorage.LANG_CODE);
        String categoryNameFromRequest = req.getParameter(ConstantStorage.NAME);

        if (!Validator.validate(ValidatorSettingsStorage.CATEGORY_NAME_PATTERN, categoryNameFromRequest)) {
            messagesForJsp.add(ConstantStorage.CHANGE_CATEGORY_WARN_BAD_NAME);
            return;
        }
        CategoryDao categoryDao = new CategoryDao();
        CopyOnWriteArrayList<Category> langCategories = langsCategories.get(Locale.forLanguageTag(categoryLangCodeFromRequest));
        for (Category category : langCategories) {
            if (category.getId() == categoryIdFromRequest) {
                String oldName = category.getName();
                category.setName(categoryNameFromRequest);
                try {
                    categoryDao.update(category);
                    LOGGER.debug("Category id - " + categoryIdFromRequest + " was updated");
                    messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                } catch (SQLException | ConnectionPoolException e) {
                    category.setName(oldName);
                    LOGGER.error("Failed to update category, id - " + category.getId());
                    messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
                }
                break;
            }
        }
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        short categoryIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.ID));
        if (categoryIdFromRequest == NumberUtil.INVALID_NUMBER) {
            add(req, messagesForJsp);
        } else {
            edit(req, messagesForJsp);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_CATEGORY_SHOW_MANAGE_PAGE);
    }
}