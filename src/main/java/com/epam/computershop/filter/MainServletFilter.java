package com.epam.computershop.filter;

import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.entity.User;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.enums.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainServletFilter implements Filter {
    private boolean applicationUrlIsDefined = false;
    private ActionFactory actionFactory;

    @Override
    public void init(FilterConfig filterConfig) {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (!applicationUrlIsDefined) {
            initApplicationUrls(req);
        }
        if (!actionIsAccessed(req, (HttpServletResponse) servletResponse)) {
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //Unsupported method
    }

    private boolean actionIsAccessed(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        UserRole accessRole = actionFactory.getActionAccessRole(req.getPathInfo());
        boolean result = true;
        if (currentUser != null) {
            if (accessRole.getId() > currentUser.getRole().getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                result = false;
            }
            if (accessRole.equals(UserRole.GUEST_ONLY)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                result = false;
            }
        } else if (accessRole != UserRole.GUEST_ONLY && accessRole != UserRole.GUEST) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            result = false;
        }
        return result;
    }

    private void initApplicationUrls(HttpServletRequest req) {
        Lock lock = new ReentrantLock();
        lock.lock();
        String appUrl = req.getScheme() + "://" + req.getHeader(HttpHeaders.HOST) + req.getContextPath();
        String appServletUrl = appUrl.concat(req.getServletPath());
        ServletContext servletContext = req.getServletContext();
        servletContext.setAttribute(ConstantStorage.APPLICATION_URL, appUrl);
        servletContext.setAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH, appServletUrl);
        applicationUrlIsDefined = true;
        lock.unlock();
    }
}