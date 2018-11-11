package com.epam.computershop.action.lang;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.dao.LangDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.enums.UserRole;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.Validator.*;
import static com.epam.computershop.util.ValidatorSettingsStorage.*;

public class AddLangAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(AddLangAction.class);

    public AddLangAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ServletContext servletContext = req.getServletContext();
        HttpSession session = req.getSession();
        Map<Locale, CopyOnWriteArrayList<Category>> localesCategories =
                (Map<Locale, CopyOnWriteArrayList<Category>>) servletContext.getAttribute(ALL_LOCALES_CATEGORIES);
        List<String> messagesForJsp = (List<String>) session.getAttribute(MESSAGES);
        Locale currentLocale = (Locale) session.getAttribute(CURRENT_LOCALE);
        CopyOnWriteArrayList<Category> currentLocaleCategories = localesCategories.get(currentLocale);

        String code = req.getParameter(LANG_CODE);
        String name = req.getParameter(NAME);
        String[] translationCategoriesNames = req.getParameterValues(TRANSLATE_CATEGORIES);

        if (validateData(code, name) || (translationCategoriesNames.length != currentLocaleCategories.size())) {
            messagesForJsp.add(GENERAL_WARN_BAD_DATA);
        } else {
            Locale locale = Locale.forLanguageTag(code);
            LangDao langDao = new LangDao();
            try {
                langDao.insert(code, name);
                addCategoriesTranslations(locale, currentLocaleCategories,
                        translationCategoriesNames, localesCategories);

                ((List<Locale>) servletContext.getAttribute(ALL_LOCALES)).add(locale);
                LOGGER.debug("Lang - " + code + " was added");
                messagesForJsp.add(GENERAL_SUCCESS);
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to insert language.", e);
                messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
            }
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(ActionFactory.ACTION_LANG_SHOW_MANAGE_PAGE);
    }

    private void addCategoriesTranslations(Locale locale, CopyOnWriteArrayList<Category> currentLocaleCategories,
                                           String[] translationCategoriesNames,
                                           Map<Locale, CopyOnWriteArrayList<Category>> localesCategories)
            throws SQLException, ConnectionPoolException {
        if (!currentLocaleCategories.isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            List<Category> translatedCategories =
                    fillCategoriesTranslation(locale.getLanguage(), translationCategoriesNames,
                            currentLocaleCategories);
            categoryDao.insertCategoriesTranslations(translatedCategories);
            Map<Locale, CopyOnWriteArrayList<Category>> newLocaleCategories = new HashMap<>(ONE);
            newLocaleCategories.put(locale, new CopyOnWriteArrayList<>(translatedCategories));
            CategoryUtil.fillCategoriesChildren(newLocaleCategories);
            localesCategories.put(locale, newLocaleCategories.get(locale));
        }
    }

    private boolean validateData(String code, String name){
        return !validate(LANG_CODE_NAME_PATTERN, code) || !validate(LANG_CODE_NAME_PATTERN, name);
    }

    private List<Category> fillCategoriesTranslation(String langCode, String[] translateCategoriesNames,
                                                     CopyOnWriteArrayList<Category> currentLangCategories) {
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
}