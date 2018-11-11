package com.epam.computershop.action.lang;

import com.epam.computershop.action.Action;
import com.epam.computershop.entity.Category;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;

public class SwapLangAction extends Action {

    public SwapLangAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String langCode = req.getParameter(LANG_CODE);
        HttpSession session = req.getSession();
        List<Locale> locales = (List<Locale>) req.getServletContext().getAttribute(ALL_LOCALES);
        for (Locale locale : locales) {
            if (locale.getLanguage().equals(langCode)) {
                session.setAttribute(CURRENT_LOCALE, locale);
                Map<Locale, CopyOnWriteArrayList<Category>> allCategories =
                        (Map<Locale, CopyOnWriteArrayList<Category>>) req.getServletContext()
                                .getAttribute(ALL_LOCALES_CATEGORIES);

                session.setAttribute(CURRENT_LOCALE_CATEGORIES, allCategories.get(locale));
            }
        }
        resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return URLUtil.getRefererURL(req);
    }
}