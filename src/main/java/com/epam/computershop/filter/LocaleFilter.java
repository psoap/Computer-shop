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
import java.util.concurrent.CopyOnWriteArrayList;

public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        //Unsupported method
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();
        Locale currentLocale = (Locale) session.getAttribute(ConstantStorage.CURRENT_LOCALE);
        if (currentLocale == null) {
            List<Locale> supportedLocales =
                    (List<Locale>) req.getServletContext().getAttribute(ConstantStorage.ALL_LOCALES);
            currentLocale = supportedLocales.stream()
                    .filter(locale -> locale.getLanguage().equals(req.getLocale().getLanguage()))
                    .findAny()
                    .orElse(Locale.forLanguageTag(req.getServletContext().getInitParameter("defaultLang")));
            session.setAttribute(ConstantStorage.CURRENT_LOCALE, currentLocale);

            Map<Locale, CopyOnWriteArrayList<Category>> localeCategories =
                    (Map<Locale, CopyOnWriteArrayList<Category>>) session.getServletContext()
                            .getAttribute(ConstantStorage.ALL_LOCALES_CATEGORIES);
            CopyOnWriteArrayList<Category> categories = localeCategories.get(currentLocale);
            session.setAttribute(ConstantStorage.CURRENT_LOCALE_CATEGORIES, categories);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //Unsupported method
    }
}
