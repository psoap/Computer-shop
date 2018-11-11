<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.checkout" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <form action="<c:url value="/do/checkout"/>" method="post">
            <label><fmt:message key="txt.delivery_profile" bundle="${langBundle}"/>:</label>
            <select name="current_delivery_profile">
                <option selected="selected"></option>
                <c:forEach var="profile" items="${requestScope.user_delivery_profiles}">
                    <option value="${profile.id}"><c:out value="${profile.addressLocation} | ${profile.phoneNumber}"/></option>
                </c:forEach>
            </select><br/>
            <label><fmt:message key="txt.balance" bundle="${langBundle}"/>:</label>: ${sessionScope.current_user.balance}<br/>
            <label><fmt:message key="txt.order_cost" bundle="${langBundle}"/>:</label>: ${sessionScope.total_price}<br/>
            <input type="submit" value="<fmt:message key="txt.save" bundle="${langBundle}"/>">
        </form>
    </jsp:body>
</t:wrapper>
