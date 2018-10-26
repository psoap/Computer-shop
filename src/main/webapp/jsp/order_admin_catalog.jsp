<%@page contentType="text/html" pageEncoding="UTF-8" %>
<fmt:message key="txt.manage_orders" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <form action="<c:url value="/do/catalog_users_orders"/>">
                <select name="id">
                    <option value="2">
                        <fmt:message key="txt.order_status.pending" bundle="${langBundle}"/>
                    </option>
                    <option value="3">
                        <fmt:message key="txt.order_status.shipping" bundle="${langBundle}"/>
                    </option>
                    <option value="4">
                        <fmt:message key="txt.order_status.delivered" bundle="${langBundle}"/>
                    </option>
                </select>
                <input type="submit" value="<fmt:message key="txt.search" bundle="${langBundle}"/>">
            </form>
            <c:choose>
                <c:when test="${not empty requestScope.orders}">
                    <table class="data-list">
                        <c:forEach var="order" items="${requestScope.orders}">
                            <c:if test="${order.statusId ne constants.ORDER_STATUS_BASKET}">
                                <tr>
                                    <c:url var="url" value="/do/order">
                                        <c:param name="id" value="${order.id}"/>
                                    </c:url>
                                    <td><a href="${url}"><c:out value="#${order.id}"/></a></td>
                                    <td><c:out value="${order.deliveryProfileId}"/></td>
                                    <td><c:out value="${order.totalPrice}"/></td>
                                    <td>
                                        <form action="<c:url value="/do/swap_order_status"/>">
                                            <input type="hidden" name="id" value="${order.id}">
                                            <select name="status_id">
                                                <option value="${constants.ORDER_STATUS_PAID}">
                                                    <fmt:message key="txt.order_status.pending" bundle="${langBundle}"/>
                                                </option>
                                                <option value="${constants.ORDER_STATUS_SHIPPING}">
                                                    <fmt:message key="txt.order_status.shipping"
                                                                 bundle="${langBundle}"/>
                                                </option>
                                                <option value="${constants.ORDER_STATUS_DELIVERED}">
                                                    <fmt:message key="txt.order_status.delivered"
                                                                 bundle="${langBundle}"/>
                                                </option>
                                            </select>
                                            <input type="submit"
                                                   value="<fmt:message key="txt.edit" bundle="${langBundle}"/>">
                                        </form>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <fmt:message key="txt.list_empty" bundle="${langBundle}"/>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:wrapper>
