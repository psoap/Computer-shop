package com.epam.computershop.servlet;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = System.currentTimeMillis();
    private static ActionFactory actionFactory;

    @Override
    public void init() throws ServletException {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = actionFactory.getAction(req.getPathInfo());
        String url = action.execute(req, resp);
        if (resp.getStatus() == HttpServletResponse.SC_OK) {
            req.getRequestDispatcher(url).forward(req, resp);
        } else if (resp.getStatus() == HttpServletResponse.SC_SEE_OTHER
                || resp.getStatus() == HttpServletResponse.SC_TEMPORARY_REDIRECT) {
            resp.setHeader(HttpHeaders.LOCATION, url);
        }
    }
}