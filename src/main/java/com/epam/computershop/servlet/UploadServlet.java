package com.epam.computershop.servlet;

import com.epam.computershop.util.HashUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.epam.computershop.util.ConstantStorage.*;

public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UploadServlet.class);
    private static final String URI_SEPARATOR = "/";
    private static final int FILENAME_LENGTH = 7;
    private String uploadsFolderPath;
    private String uploadsFolderName;

    @Override
    public void init() {
        String applicationPath = getServletContext().getRealPath(EMPTY_STRING);
        uploadsFolderName = getServletContext().getInitParameter("uploadsFolder");
        if (uploadsFolderName == null) {
            uploadsFolderName = "uploads";
        }
        uploadsFolderPath = applicationPath + File.separator + uploadsFolderName;
        File uploadFolder = new File(uploadsFolderPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (Part part : req.getParts()) {
            if ((part != null) && (part.getSize() > ZERO)) {
                HttpSession session = req.getSession();
                session.removeAttribute(FILE_URL);
                String fileNameExtension = part.getSubmittedFileName()
                        .substring(part.getSubmittedFileName().lastIndexOf('.'));
                try {
                    String fileNameWithoutPath = HashUtil.getRandomMd5()
                            .substring(ZERO, FILENAME_LENGTH) + fileNameExtension;
                    part.write(uploadsFolderPath + File.separator + fileNameWithoutPath);
                    String fileUrl = URI_SEPARATOR + uploadsFolderName + URI_SEPARATOR + fileNameWithoutPath;
                    req.getSession().setAttribute(FILE_URL, fileUrl);
                } catch (NoSuchAlgorithmException e) {
                    LOGGER.error("Failed to get random md5 while upload file.", e);
                }
            }
        }
    }
}
