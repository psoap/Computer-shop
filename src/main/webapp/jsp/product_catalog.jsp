<%@page contentType="text/html" pageEncoding="UTF-8" %>
<t:wrapper title="${requestScope.name}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/category_list.jspf" %>
        </div>
        <div class="workarea-right">
            <c:if test="${sessionScope.current_user.roleId eq constants.ROLE_ID_ADMIN}">
                <a href="<c:url value="/do/edit_page_product"/>">
                    <button><fmt:message key="txt.add" bundle="${langBundle}"/></button>
                </a>
            </c:if>
            <div class="products-container">
                <c:choose>
                    <c:when test="${not empty requestScope.products}">
                        <c:forEach var="product" items="${requestScope.products}">
                            <div class="product">
                                <c:url var="url" value="/do/product">
                                    <c:param name="id" value="${product.id}"/>
                                </c:url>
                                <a href="${url}">
                                    <c:choose>
                                        <c:when test="${not empty product.imageUrl}">
                                            <img class="product-image" src="<c:url value="${product.imageUrl}"/>"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="product-image"
                                                 src="<c:url value="/resources/images/no_image.jpg"/>"/>
                                        </c:otherwise>
                                    </c:choose></a>
                                <div class="product-info">
                                    <a href="${url}">
                                        <h3><c:out value="${product.name}"/></h3>
                                    </a>
                                    <c:out value="${product.shortDescription}"/>
                                </div>
                                <div class="product-buttons">
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
                        </c:forEach>
                        <t:page_navigation currentCount="${requestScope.quantity}" maxCount="${constants.COUNT_PRODUCTS_ON_PAGE}"
                                           parameterName="id" parameterValue="${requestScope.id}"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="txt.list_empty" bundle="${langBundle}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:wrapper>

