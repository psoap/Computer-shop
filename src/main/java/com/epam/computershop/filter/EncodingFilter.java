package com.epam.computershop.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final String ENCODING_PARAM = "encoding";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) {
        String configEncoding = filterConfig.getInitParameter(ENCODING_PARAM);
        encoding = (configEncoding != null) ? configEncoding : "utf-8";
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //Unsupported method
    }
}
