package com.epam.computershop.action.user;

import com.epam.computershop.action.Action;
import com.epam.computershop.action.ActionFactory;
import com.epam.computershop.dao.UserDao;
import com.epam.computershop.entity.User;
import com.epam.computershop.exception.ConnectionPoolException;
import com.epam.computershop.enums.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static com.epam.computershop.util.ConstantStorage.*;
import static com.epam.computershop.util.NumberUtil.*;

public class EditBalanceAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(EditBalanceAction.class);

    public EditBalanceAction(UserRole accessRole) {
        super(accessRole);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String responseUrl = ActionFactory.ACTION_USER_SHOW_EDIT_BALANCE_PAGE;

        List<String> messagesForJsp = (List<String>) req.getSession().getAttribute(MESSAGES);

        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        BigDecimal deltaBalance = tryParseBigDecimal(req.getParameter(BALANCE));

        if ((deltaBalance != null) && (deltaBalance.intValue() > ZERO)) {
            currentUser.setBalance(currentUser.getBalance().add(deltaBalance));
            try {
                UserDao userDao = new UserDao();
                userDao.update(currentUser);
                messagesForJsp.add(GENERAL_SUCCESS);
                responseUrl = ActionFactory.ACTION_USER_SHOW_PERSONAL_PAGE;
                LOGGER.debug("Balance was updated on " + deltaBalance + ", by user - " + currentUser.getLogin());
            } catch (SQLException | ConnectionPoolException e) {
                LOGGER.error("Failed to update balance by user - " + currentUser.getLogin());
                messagesForJsp.add(GENERAL_ERROR_ACTION_FAILED);
                currentUser.setBalance(currentUser.getBalance().subtract(deltaBalance));
            }
        } else {
            messagesForJsp.add(GENERAL_WARN_BAD_DATA);
        }
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        return ((String) req.getServletContext().getAttribute(APPLICATION_URL_WITH_SERVLET_PATH))
                .concat(responseUrl);
    }
}
