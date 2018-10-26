<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.delivprofs" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <c:choose>
                <c:when test="${not empty requestScope.user_delivprofiles}">
                    <table class="data-list">
                        <tr>
                            <td><fmt:message key="txt.id" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.first_name" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.last_name" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.patronymic" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.address_location" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.phone_number" bundle="${langBundle}"/></td>
                        </tr>
                        <c:forEach var="delivprof" items="${requestScope.user_delivprofiles}">
                            <tr>
                                <td><c:out value="#${delivprof.id}"/></td>
                                <td><c:out value="${delivprof.firstName}"/></td>
                                <td><c:out value="${delivprof.lastName}"/></td>
                                <td><c:out value="${delivprof.patronymic}"/></td>
                                <td><c:out value="${delivprof.addressLocation}"/></td>
                                <td><c:out value="${delivprof.phoneNumber}"/></td>
                                <td>
                                    <c:url var="editUrl" value="/do/edit_page_delivprof">
                                        <c:param name="id" value="${delivprof.id}"/>
                                    </c:url>
                                    <a href="${editUrl}">
                                        <button><fmt:message key="txt.edit" bundle="${langBundle}"/></button>
                                    </a>
                                </td>
                                <td>
                                    <c:url var="remUrl" value="/do/remove_delivprof">
                                        <c:param name="id" value="${delivprof.id}"/>
                                    </c:url>
                                    <a href="${remUrl}">
                                        <button><fmt:message key="txt.remove" bundle="${langBundle}"/></button>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <fmt:message key="txt.list_empty" bundle="${langBundle}"/>
                </c:otherwise>
            </c:choose>
            <a href="<c:url value="/do/edit_page_delivprof"/>">
                <button><fmt:message key="txt.add" bundle="${langBundle}"/></button>
            </a>
        </div>
    </jsp:body>
</t:wrapper>
