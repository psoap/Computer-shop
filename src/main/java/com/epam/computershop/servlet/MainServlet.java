package com.epam.computershop.servlet;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final ActionFactory ACTION_FACTORY = ActionFactory.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = ACTION_FACTORY.getAction(req.getPathInfo());
        String url = action.execute(req, resp);
        if (resp.getStatus() == HttpServletResponse.SC_OK) {
            req.getRequestDispatcher(url).forward(req, resp);
        } else if (resp.getStatus() == HttpServletResponse.SC_SEE_OTHER
                || resp.getStatus() == HttpServletResponse.SC_TEMPORARY_REDIRECT) {
            resp.setHeader(HttpHeaders.LOCATION, url);
        } else if(resp.getStatus() == HttpServletResponse.SC_NOT_FOUND){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}