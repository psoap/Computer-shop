package com.epam.computershop.servlet;

import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.HashUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UploadServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UploadServlet.class);
    private static final String URI_SEPARATOR = "/";
    private static final int FILENAME_LENGTH = 7;
    private static String uploadFilePath;
    private static String uploadPath;

    @Override
    public void init() throws ServletException {
        String applicationPath = getServletContext().getRealPath(ConstantStorage.EMPTY_STRING);
        uploadPath = getServletContext().getInitParameter("uploadsFolder");
        if(uploadPath==null){
            uploadPath = "uploads";
        }
        uploadFilePath = applicationPath + File.separator + uploadPath;
        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (Part part : req.getParts()) {
            if (part != null && part.getSize() > ConstantStorage.ZERO) {
                HttpSession session = req.getSession();
                session.removeAttribute(ConstantStorage.FILE_URL);
                String filenameExtension = part.getSubmittedFileName().substring(part.getSubmittedFileName().lastIndexOf('.'));
                try {
                    String fileNameWithoutPath = HashUtil.getRandomMd5().substring(ConstantStorage.ZERO, FILENAME_LENGTH) + filenameExtension;
                    part.write(uploadFilePath + File.separator + fileNameWithoutPath);
                    String fileUrl = URI_SEPARATOR + uploadPath + URI_SEPARATOR + fileNameWithoutPath;
                    req.getSession().setAttribute(ConstantStorage.FILE_URL, fileUrl);
                } catch (NoSuchAlgorithmException e) {
                    LOGGER.error("Failed to get random md5 while upload file");
                }
            }
        }
    }
}
