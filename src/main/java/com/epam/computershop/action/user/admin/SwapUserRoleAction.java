package com.epam.computershop.action.user.admin;

import com.epam.computershop.action.Action;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import com.epam.computershop.util.URLUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class SwapUserRoleAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(SwapUserRoleAction.class);

    public SwapUserRoleAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        long userIdFromRequest = NumberUtil.tryParseLong(req.getParameter(ConstantStorage.ID));
        short roleIdFromRequest = NumberUtil.tryParseShort(req.getParameter(ConstantStorage.USER_ROLE_ID));
        if (userIdFromRequest != NumberUtil.INVALID_NUMBER && roleIdFromRequest != NumberUtil.INVALID_NUMBER) {
            UserDao userDao = new UserDao();
            try {
                User user = userDao.findUserById(userIdFromRequest);
                if (user != null) {
                    user.setRoleId(roleIdFromRequest);
                    userDao.update(user);
                    LOGGER.debug("User id - " + userIdFromRequest + "role was updated to role id - " + roleIdFromRequest);
                    messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                }
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to swap user role, user id - " + userIdFromRequest);
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
            }
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return URLUtil.getRefererURL(req);
    }
}
