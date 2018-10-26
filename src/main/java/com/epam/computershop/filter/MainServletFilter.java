package com.epam.computershop.filter;

import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.entity.User;
import com.epam.computershop.util.ConstantStorage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

public class MainServletFilter implements Filter {
    private static boolean applicationUrlIsDefined = false;
    private ActionFactory actionFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        initApplicationURLS(req);

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        short accessRoleId = actionFactory.getActionAccessRoleId(req.getPathInfo());
        if (accessRoleId == ConstantStorage.ROLE_ID_ERROR) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            if (currentUser != null) {
                if (accessRoleId > currentUser.getRoleId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                } else if (accessRoleId == ConstantStorage.ROLE_ID_GUEST_ONLY) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } else if (accessRoleId != ConstantStorage.ROLE_ID_GUEST_ONLY && accessRoleId != ConstantStorage.ROLE_ID_GUEST) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void initApplicationURLS(HttpServletRequest req) {
        if (!applicationUrlIsDefined) {
            synchronized (MainServletFilter.class) {
                String appUrl = req.getScheme() + "://" + req.getHeader(HttpHeaders.HOST) + req.getContextPath();
                String appServletUrl = appUrl.concat(req.getServletPath());
                ServletContext servletContext = req.getServletContext();
                servletContext.setAttribute(ConstantStorage.APPLICATION_URL, appUrl);
                servletContext.setAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH, appServletUrl);
                applicationUrlIsDefined = true;
            }
        }
    }
}