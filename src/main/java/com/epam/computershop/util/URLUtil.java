package com.epam.computershop.util;

import javax.servlet.http.HttpServletRequest;

public class URLUtil {
    private static final String REFERER = "referer";

    private URLUtil() {
    }

    public static String getRefererURL(HttpServletRequest req){
       String applicationUrl = (String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL);
       String url = req.getHeader(REFERER);
       if(!url.startsWith(applicationUrl)){
           url = applicationUrl;
       }
       return url;
   }
}