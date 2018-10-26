package com.epam.computershop.listener;

import com.epam.computershop.entity.User;
import com.epam.computershop.util.ConstantStorage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

public class MainHttpSessionListener implements HttpSessionListener {
    private static final Logger LOGGER = Logger.getLogger(MainHttpSessionListener.class);
    private static final int MAX_COUNT_OF_MESSAGES = 5;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        List<String> messagesForJsp = new ArrayList<>(MAX_COUNT_OF_MESSAGES);
        session.setAttribute(ConstantStorage.MESSAGES, messagesForJsp);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        User user = (User) httpSessionEvent.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        if (user != null) {
            LOGGER.debug("User - " + user.getLogin() + " went out");
        }
    }
}