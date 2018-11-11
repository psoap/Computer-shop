<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.orders" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <c:if test="${not empty requestScope.orders}">
                <table class="data-list">
                    <tr>
                        <td><fmt:message key="txt.id" bundle="${langBundle}"/></td>
                        <td><fmt:message key="txt.order_status" bundle="${langBundle}"/></td>
                        <td><fmt:message key="txt.delivery_profile" bundle="${langBundle}"/></td>
                        <td><fmt:message key="txt.order_cost" bundle="${langBundle}"/></td>
                    </tr>
                    <c:forEach var="order" items="${requestScope.orders}">
                        <c:if test="${order.status ne 'BASKET'}">
                            <tr>
                                <c:url var="url" value="/do/order">
                                    <c:param name="id" value="${order.id}"/>
                                </c:url>
                                <td><a href="${url}"><c:out value="#${order.id}"/></a></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.status eq 'PAID'}">
                                            <fmt:message key="txt.order_status.pending" bundle="${langBundle}"/>
                                        </c:when>
                                        <c:when test="${order.status eq 'SHIPPING'}">
                                            <fmt:message key="txt.order_status.shipping" bundle="${langBundle}"/>
                                        </c:when>
                                        <c:when test="${order.status eq 'DELIVERED'}">
                                            <fmt:message key="txt.order_status.delivered" bundle="${langBundle}"/>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td><c:out value="#${order.deliveryProfileId}"/></td>
                                <td><c:out value="${order.totalPrice}"/></td>
                                <td>
                                    <c:url value="/do/remove_order" var="remUrl">
                                        <c:param name="id" value="${order.id}"/>
                                    </c:url>
                                    <a href="${remUrl}">
                                        <button><fmt:message key="txt.remove" bundle="${langBundle}"/></button>
                                    </a>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </jsp:body>
</t:wrapper>
