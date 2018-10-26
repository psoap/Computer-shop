package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.util.ConstantStorage;
import com.epam.computershop.util.NumberUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class EditBalanceAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(EditBalanceAction.class);

    public EditBalanceAction(short accessRoleId) {
        super(accessRoleId);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = ActionFactory.ACTION_USER_SHOW_EDIT_BALANCE_PAGE;

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(ConstantStorage.MESSAGES);

        User currentUser = (User) req.getSession().getAttribute(ConstantStorage.CURRENT_USER);
        BigDecimal deltaBalance = NumberUtil.tryParseBigDecimal(req.getParameter(ConstantStorage.BALANCE));

        if (deltaBalance != null && deltaBalance.intValue() > ConstantStorage.ZERO) {
            currentUser.setBalance(currentUser.getBalance().add(deltaBalance));
            try {
                UserDao userDao = new UserDao();
                userDao.update(currentUser);
                messagesForJsp.add(ConstantStorage.GENERAL_SUCCESS);
                responseUrl = ActionFactory.ACTION_USER_SHOW_PERSONAL_PAGE;
                LOGGER.debug("Balance was updated on " + deltaBalance + ", by user - " + currentUser.getLogin());
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to update balance by user - " + currentUser.getLogin());
                messagesForJsp.add(ConstantStorage.GENERAL_ERROR_ACTION_FAILED);
                currentUser.setBalance(currentUser.getBalance().subtract(deltaBalance));
            }
        } else {
            messagesForJsp.add(ConstantStorage.GENERAL_WARN_BAD_DATA);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(ConstantStorage.APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(responseUrl);
    }
}
