<%@page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="product" value="${requestScope.product}"/>
<t:wrapper title="${product.name}">
    <jsp:body>
        <div class="workarea-left" style="text-align: center;">
            <c:choose>
                <c:when test="${not empty product.imageUrl}">
                    <img src="<c:url value="${product.imageUrl}"/>" width="250" height="250" align="center"/>
                </c:when>
                <c:otherwise>
                    <img src="<c:url value="/resources/images/no_image.jpg"/>" width="250" height="250" align="center"/>
                </c:otherwise>
            </c:choose>
            <div style="display: inline-block;">
                <c:out value="${product.price}"/>
                <c:url var="basketUrl" value="/do/add_to_basket">
                    <c:param name="id" value="${product.id}"/>
                </c:url>
                <a href="${basketUrl}">
                    <button><fmt:message key="txt.to_basket" bundle="${langBundle}"/></button>
                </a><br/>
                <c:if test="${sessionScope.current_user.roleId eq constants.ROLE_ID_ADMIN}">
                    <c:url var="editUrl" value="/do/edit_page_product">
                        <c:param name="id" value="${product.id}"/>
                    </c:url>
                    <a href="${editUrl}">
                        <button><fmt:message key="txt.edit" bundle="${langBundle}"/></button>
                    </a>
                    <c:url var="remUrl" value="/do/remove_product">
                        <c:param name="id" value="${product.id}"/>
                    </c:url>
                    <a href="${remUrl}">
                        <button><fmt:message key="txt.remove" bundle="${langBundle}"/></button>
                    </a>
                </c:if>
            </div>
        </div>
        <div class="workarea-right">
            <h1><c:out value="${product.name}"/></h1>
            <c:out value="${product.description}" escapeXml="false"/>
        </div>
    </jsp:body>
</t:wrapper>