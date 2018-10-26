<%@page contentType="text/html" pageEncoding="UTF-8" %>
<fmt:message key="txt.order" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <c:if test="${not empty sessionScope.current_user}">
            <div class="workarea-left">
                <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
            </div>
        </c:if>
        <div class="workarea-right">
            <c:choose>
                <c:when test="${not empty requestScope.products}">
                    <div class="products-container">
                        <c:forEach var="entry" items="${requestScope.products}">
                            <c:set var="product" value="${entry.key}"/>
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
                                    </c:choose>
                                </a>
                                <div class="product-info">
                                    <a href="${url}">
                                        <h3><c:out value="${product.name}"/></h3>
                                    </a>
                                    <c:out value="${product.shortDescription}"/>
                                </div>
                                    <div class="product-buttons">
                                        <c:set var="total_price" value="${total_price+product.price*entry.value}"
                                               scope="session"/>
                                        <c:out value="${product.price*entry.value}"/><br/>
                                        <c:choose>
                                            <c:when test="${requestScope.status_id eq constants.ORDER_STATUS_BASKET}">
                                                <c:url var="productQuery" value="/do/update_quantity">
                                                    <c:param name="id" value="${product.id}"/>
                                                </c:url>
                                                <form action="${productQuery}" method="post">
                                                    <input type="number" name="quantity" value="${entry.value}"
                                                           style="width: 35px;">
                                                    <input type="submit"
                                                           value="<fmt:message key="txt.edit" bundle="${langBundle}"/>">
                                                </form>
                                                <c:url var="remUrl" value="/do/remove_from_basket">
                                                    <c:param name="id" value="${product.id}"/>
                                                </c:url>
                                                <a href="${remUrl}">
                                                    <button><fmt:message key="txt.remove" bundle="${langBundle}"/></button>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${entry.value}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                            </div>
                        </c:forEach>
                    </div>
                    <fmt:message key="txt.order_cost" bundle="${langBundle}"/>
                    <c:out value="${sessionScope.total_price}"/>
                    <c:if test="${requestScope.status_id eq constants.ORDER_STATUS_BASKET}">
                        <a href="<c:url value="/do/page_checkout"/>">
                            <button><fmt:message key="txt.checkout" bundle="${langBundle}"/></button>
                        </a>
                        <a href="<c:url value="/do/remove_from_basket"/>">
                            <button><fmt:message key="txt.clear_basket" bundle="${langBundle}"/></button>
                        </a>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <fmt:message key="txt.list_empty" bundle="${langBundle}"/>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:wrapper>
