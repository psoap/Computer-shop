package com.epam.computershop.listener;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.dao.LangDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.CategoryUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.computershop.util.ConstantStorage.*;

public class MainServletContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(MainServletContextListener.class);
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        try {
            connectionPool = ConnectionPool.getInstance();
            List<Locale> locales = initLocales(servletContext);
            initCategories(servletContext, locales);
            LOGGER.info("Application started successfully.");
        } catch (SQLException | ConnectionPoolException ex) {
            LOGGER.fatal("Failed to initialize servlet context.", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            connectionPool.close();
        } catch (ConnectionPoolException ex) {
            LOGGER.error(ex);
        }
        LOGGER.info("Application stopped successfully.");
    }

    private List<Locale> initLocales(ServletContext context) throws SQLException, ConnectionPoolException {
        LangDao langDao = new LangDao();
        List<Locale> locales = langDao.findAll();
        context.setAttribute(ALL_LOCALES, locales);
        LOGGER.info("Lang have loaded.");
        return locales;
    }

    private void initCategories(ServletContext context, List<Locale> locales)
            throws SQLException, ConnectionPoolException {
        CategoryDao categoryDao = new CategoryDao();
        Map<Locale, CopyOnWriteArrayList<Category>> localesCategories = categoryDao.findAllByEachLocale(locales);
        CategoryUtil.fillCategoriesChildren(localesCategories);
        context.setAttribute(ALL_LOCALES_CATEGORIES, localesCategories);
        LOGGER.info("Categories have loaded.");
    }
}