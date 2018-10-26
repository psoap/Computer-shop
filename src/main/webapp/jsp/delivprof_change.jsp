<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.delivprof" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <c:set var="currentDelivprof" value="${requestScope.current_delivprof}"/>
            <form action="<c:url value="/do/change_delivprof"/>" method="post">
                <c:choose>
                    <c:when test="${not empty currentDelivprof}">
                        <input type="hidden" name="id" value="${currentDelivprof.id}"/><br/>
                        <label><fmt:message key="txt.first_name" bundle="${langBundle}"/>:</label><input type="text" name="first_name" value="${current_delivprof.firstName}"/><br/>
                        <label><fmt:message key="txt.last_name" bundle="${langBundle}"/>:</label><input type="text" name="last_name" value="${current_delivprof.lastName}"/> <br/>
                        <label><fmt:message key="txt.patronymic" bundle="${langBundle}"/>:</label><input type="text" name="patronymic" value="${current_delivprof.patronymic}"/><br/>
                        <label><fmt:message key="txt.address_location" bundle="${langBundle}"/>:</label><input type="text" name="address_location"
                                                                                                               value="${current_delivprof.addressLocation}"/><br/>
                        <label><fmt:message key="txt.phone_number" bundle="${langBundle}"/>:</label><input type="text" name="phone_number" value="${current_delivprof.phoneNumber}"/><br/>
                    </c:when>
                    <c:otherwise>
                        <label><fmt:message key="txt.first_name" bundle="${langBundle}"/>:</label><input type="text" name="first_name"/><br/>
                        <label><fmt:message key="txt.last_name" bundle="${langBundle}"/>:</label><input type="text" name="last_name"/> <br/>
                        <label><fmt:message key="txt.patronymic" bundle="${langBundle}"/>:</label><input type="text" name="patronymic"/><br/>
                        <label><fmt:message key="txt.address_location" bundle="${langBundle}"/>:</label><input type="text" name="address_location"/><br/>
                        <label><fmt:message key="txt.phone_number" bundle="${langBundle}"/>:</label><input type="text" name="phone_number"/> <br/>
                    </c:otherwise>
                </c:choose>
                <input type="submit" value="<fmt:message key="txt.save" bundle="${langBundle}"/>"/>
            </form>
        </div>
    </jsp:body>
</t:wrapper>
