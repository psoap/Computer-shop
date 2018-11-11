package com.epam.computershop.action.user.admin;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.URLUtil;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class SwapUserRoleAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(SwapUserRoleAction.class);

    public SwapUserRoleAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        long userIdFromRequest = tryParseLong(req.getParameter(ID));
        UserRole role = UserRole.valueOf(req.getParameter(USER_ROLE));
        if ((userIdFromRequest != INVALID_NUMBER)) {
            UserDao userDao = new UserDao();
            try {
                User user = userDao.findUserById(userIdFromRequest);
                if (user != null) {
                    user.setRole(role);
                    userDao.update(user);
                    LOGGER.debug("User id - " + userIdFromRequest + "role was updated to role - "
                            + role);
                    messagesForJsp.add(GENERAL_SUCCESS);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to swap user role, user id - " + userIdFromRequest, e);
                messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
            }
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return URLUtil.getRefererURL(req);
    }
}
