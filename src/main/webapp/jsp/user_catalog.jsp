<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.users" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/admin_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <table class="data-list">
                <tr>
                    <td>id</td>
                    <td><fmt:message key="txt.login" bundle="${langBundle}"/></td>
                    <td><fmt:message key="txt.email" bundle="${langBundle}"/></td>
                    <td></td>
                    <td><fmt:message key="txt.balance" bundle="${langBundle}"/></td>
                </tr>
                <c:forEach var="user" items="${requestScope.users}">
                    <tr>
                        <td><c:out value="${user.id}"/></td>
                        <td><c:out value="${user.login}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${user.role eq 'ADMIN'}">
                                    <fmt:message key="txt.admin" bundle="${langBundle}"/>
                                    <c:url var="swapRole" value="/do/swap_user_role">
                                        <c:param name="id" value="${user.id}"/>
                                        <c:param name="role" value="USER"/>
                                    </c:url>
                                    <a href="${swapRole}">
                                        -> <fmt:message key="txt.user" bundle="${langBundle}"/>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="txt.user" bundle="${langBundle}"/>
                                    <c:url var="swapRole" value="/do/swap_user_role">
                                        <c:param name="id" value="${user.id}"/>
                                        <c:param name="role" value="ADMIN"/>
                                    </c:url>
                                    <a href="${swapRole}">
                                        -> <fmt:message key="txt.admin" bundle="${langBundle}"/>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${user.balance}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </jsp:body>
</t:wrapper>
