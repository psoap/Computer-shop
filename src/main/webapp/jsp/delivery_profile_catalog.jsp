<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.delivery_profiles" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <c:choose>
                <c:when test="${not empty requestScope.user_delivery_profiles}">
                    <table class="data-list">
                        <tr>
                            <td><fmt:message key="txt.id" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.first_name" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.last_name" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.patronymic" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.address_location" bundle="${langBundle}"/></td>
                            <td><fmt:message key="txt.phone_number" bundle="${langBundle}"/></td>
                        </tr>
                        <c:forEach var="deliveryProfile" items="${requestScope.user_delivery_profiles}">
                            <tr>
                                <td><c:out value="#${deliveryProfile.id}"/></td>
                                <td><c:out value="${deliveryProfile.firstName}"/></td>
                                <td><c:out value="${deliveryProfile.lastName}"/></td>
                                <td><c:out value="${deliveryProfile.patronymic}"/></td>
                                <td><c:out value="${deliveryProfile.addressLocation}"/></td>
                                <td><c:out value="${deliveryProfile.phoneNumber}"/></td>
                                <td>
                                    <c:url var="editUrl" value="/do/edit_page_delivery_profile">
                                        <c:param name="id" value="${deliveryProfile.id}"/>
                                    </c:url>
                                    <a href="${editUrl}">
                                        <button><fmt:message key="txt.edit" bundle="${langBundle}"/></button>
                                    </a>
                                </td>
                                <td>
                                    <c:url var="remUrl" value="/do/remove_delivery_profile">
                                        <c:param name="id" value="${deliveryProfile.id}"/>
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
            <a href="<c:url value="/do/edit_page_delivery_profile"/>">
                <button><fmt:message key="txt.add" bundle="${langBundle}"/></button>
            </a>
        </div>
    </jsp:body>
</t:wrapper>
