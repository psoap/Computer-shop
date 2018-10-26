package com.epam.computershop.action.lang;

import com.epam.computershop.action.Action;
import com.epam.computershop.entity.Category;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.URLUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SwapLangAction extends Action {

    public SwapLangAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String langCode = req.getParameter(ConstantStorage.LANG_CODE);
        HttpSession session = req.getSession();
        List<Locale> langs = (List<Locale>) req.getServletContext().getAttribute(ConstantStorage.ALL_LANGS);
        for (Locale lang : langs) {
            if (lang.getLanguage().equals(langCode)) {
                session.setAttribute(ConstantStorage.CURRENT_LANG, lang);
                Map<Locale, CopyOnWriteArrayList<Category>> allCategories = (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext().getAttribute(ConstantStorage.ALL_CATEGORIES);
                session.setAttribute(ConstantStorage.CURRENT_LANG_CATEGORIES, allCategories.get(lang));
            }
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }
}