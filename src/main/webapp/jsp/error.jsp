<%@page contentType="text/html" pageEncoding="UTF-8" %>
<t:wrapper title="${pageContext.errorData.statusCode}">
    <jsp:body>
        <%@ page isErrorPage="true" %>
        <div style="flex-wrap: wrap;margin: auto;text-align: center;">
            <h2>${pageContext.errorData.statusCode} -
            <c:choose>
            <c:when test="${pageContext.errorData.statusCode eq 404}">
                Not found</h2>
                <img src="<c:url value="/resources/images/404.gif" />">
            </c:when>
            <c:when test="${pageContext.errorData.statusCode eq 403}">
                Forbidden</h2>
                <img src="<c:url value="/resources/images/403.jpg" />">
            </c:when>
            <c:when test="${pageContext.errorData.statusCode eq 500}">
                Internal Server Error</h2>
                <img src="<c:url value="/resources/images/500.jpg" />">
            </c:when>
            </c:choose>
        </div>
    </jsp:body>
</t:wrapper>

