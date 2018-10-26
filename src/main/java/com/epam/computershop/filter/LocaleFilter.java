package com.epam.computershop.filter;

import com.epam.computershop.entity.Category;
import com.epam.computershop.util.ConstantStorage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();
        Locale currentLocale = (Locale) session.getAttribute(ConstantStorage.CURRENT_LANG);
        if (currentLocale == null) {
            List<Locale> supportedLangs = (List<Locale>) req.getServletContext().getAttribute(ConstantStorage.ALL_LANGS);
            currentLocale = supportedLangs.stream()
                    .filter(locale -> locale.getLanguage().equals(req.getLocale().getLanguage()))
                    .findAny().orElse(Locale.forLanguageTag(req.getServletContext().getInitParameter("defaultLang")));
            session.setAttribute(ConstantStorage.CURRENT_LANG, currentLocale);
            Map<Locale, List<Category>> langCategories = (Map<Locale, List<Category>>) (session.getServletContext().getAttribute(ConstantStorage.ALL_CATEGORIES));
            List<Category> categories = langCategories.get(currentLocale);
            session.setAttribute(ConstantStorage.CURRENT_LANG_CATEGORIES, categories);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
