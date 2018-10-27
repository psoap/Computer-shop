package com.epam.computershop.action.lang;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.dao.LangDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.CategoryUtil;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.Validator;
import com.epam.computershop.util.ValidatorSettingsStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AddLangAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(AddLangAction.class);
    private static final int LANG_CODE_LENGTH = 2;

    public AddLangAction(short accessRoleId) {
        super(accessRoleId);
    }

    private List<Category> fillCategoriesTranslate(String langCode, String[] translateCategoriesNames, CopyOnWriteArrayList<Category> currentLangCategories) {
        List<Category> translatedCategories = new ArrayList<>(currentLangCategories.size());
        for (int i = 0; i < currentLangCategories.size(); i++) {
            Category cloneCategory = new Category(currentLangCategories.get(i));
            cloneCategory.setLangCode(langCode);
            cloneCategory.setName(translateCategoriesNames[i]);
            cloneCategory.setChildren(new ArrayList<>());
            translatedCategories.add(cloneCategory);
        }
        return translatedCategories;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ServletContext servletContext = req.getServletContext();
        HttpSession session = req.getSession();
        Map<Locale, CopyOnWriteArrayList<Category>> langsCategories = (Map<Locale, CopyOnWriteArrayList<Category>>) servletContext.getAttribute(ConstantStorage.ALL_CATEGORIES);
        List<String> messagesForJsp = (List<String>) session.getAttribute(ConstantStorage.MESSAGES);
        Locale currentLocale = (Locale) session.getAttribute(ConstantStorage.CURRENT_LANG);
        CopyOnWriteArrayList<Category> currentLangCategories = langsCategories.get(currentLocale);

        String code = req.getParameter(ConstantStorage.LANG_CODE);
        String name = req.getParameter(ConstantStorage.NAME);
        Locale locale = Locale.forLanguageTag(code);
        String[] translateCategoriesNames = req.getParameterValues(ConstantStorage.TRANSLATE_CATEGORIES);

        if (code.length() != LANG_CODE_LENGTH
                || !Validator.validate(ValidatorSettingsStorage.LANG_CODE_NAME_PATTERN, code)
                || !Validator.validate(ValidatorSettingsStorage.LANG_CODE_NAME_PATTERN, name)
                || translateCategoriesNames.length != currentLangCategories.size()) {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        } else {
            LangDao langDao = new LangDao();
            CategoryDao categoryDao = new CategoryDao();
            try {
                langDao.insert(code, name);
                if (!currentLangCategories.isEmpty()) {
                    List<Category> translatedCategories = fillCategoriesTranslate(code, translateCategoriesNames, currentLangCategories);
                    categoryDao.insertTranslateList(translatedCategories);
                    Map<Locale, CopyOnWriteArrayList<Category>> newLangCategories = new HashMap<>(ConstantStorage.ONE);
                    newLangCategories.put(locale, new CopyOnWriteArrayList<>(translatedCategories));
                    CategoryUtil.fillChildrenCategories(newLangCategories);
                    langsCategories.put(locale, newLangCategories.get(locale));
                }
                ((List<Locale>) servletContext.getAttribute(ConstantStorage.ALL_LANGS)).add(locale);
                LOGGER.debug("Lang - " + code + " was added");
                messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to insert language");
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        }
        return JSP_LANG_MANAGE;
    }
}