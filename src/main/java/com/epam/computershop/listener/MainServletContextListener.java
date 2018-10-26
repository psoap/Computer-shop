package com.epam.computershop.listener;

import com.epam.computershop.connectionpool.ConnectionPool;
import com.epam.computershop.dao.CategoryDao;
import com.epam.computershop.dao.LangDao;
import com.epam.computershop.entity.Category;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.CategoryUtil;
import com.epam.computershop.util.ConstantStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainServletContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(MainServletContextListener.class);
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            connectionPool = ConnectionPool.getInstance();

            ServletContext servletContext = servletContextEvent.getServletContext();

            CategoryDao categoryDao = new CategoryDao();
            LangDao langDao = new LangDao();
            List<Locale> langs = langDao.findAll();
            servletContext.setAttribute(ConstantStorage.ALL_LANGS, langs);
            LOGGER.info("Lang have loaded");
            Map<Locale, CopyOnWriteArrayList<Category>> langsCategories = categoryDao.findAllByLangs(langs);
            CategoryUtil.fillChildrenCategories(langsCategories);
            servletContext.setAttribute(ConstantStorage.ALL_CATEGORIES, langsCategories);
            LOGGER.info("Categories have loaded");
            LOGGER.info("Application started successfully");
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.fatal("Failed to initialize servlet context");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        connectionPool.close();
        LOGGER.info("Application stopped successfully");
    }
}
