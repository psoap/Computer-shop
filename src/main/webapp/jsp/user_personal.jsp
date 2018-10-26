<%@page contentType="text/html" pageEncoding="UTF-8" %>
<fmt:message key="txt.personal_data" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
        <form action="<c:url value="/do/edit_personal"/>" method="post">
            <table>
                <tr>
                    <td><label><fmt:message key="txt.balance" bundle="${langBundle}"/>:</label></td>
                    <td><c:out value="${sessionScope.current_user.balance}"/></td>
                </tr>
                <tr>
                    <td><label><fmt:message key="txt.username" bundle="${langBundle}"/>:</label></td>
                    <td><c:out value="${sessionScope.current_user.login}"/></td>
                </tr>
                <tr>
                    <td><label><fmt:message key="txt.email" bundle="${langBundle}"/>:</label></td>
                    <td><input type="email" name="email"
                               value="${sessionScope.current_user.email}"/></td>
                </tr>
                <tr>
                    <td><label><fmt:message key="txt.password" bundle="${langBundle}"/>:</label></td>
                    <td><input type="password"
                               name="password"/></td>
                </tr>
                <tr>
                    <td><label><fmt:message key="txt.confirm.password" bundle="${langBundle}"/>:</label></td>
                    <td><input type="password"
                               name="confirm_password"/></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" class="form-button"
                                           value="<fmt:message key="txt.save" bundle="${langBundle}"/>"/></td>
                </tr>
            </table>
        </form>
    </jsp:body>
</t:wrapper></div>