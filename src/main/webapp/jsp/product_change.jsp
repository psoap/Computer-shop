<%@page contentType="text/html" pageEncoding="UTF-8" %>
<fmt:message key="txt.change_product" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/admin_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <form action="<c:url value="/upload"/>" method="post" enctype="multipart/form-data" target="imageFrame">
                <input type="file" name="file" size="0" accept="image/jpeg"/>
                <input type="submit" value="<fmt:message key="txt.upload" bundle="${langBundle}"/>"/>
            </form>
            <iframe name="imageFrame" hidden></iframe>
            <c:choose>
                <c:when test="${empty requestScope.product}">
                    <form action="<c:url value="/do/change_product"/>" method="post">
                        <label><fmt:message key="txt.name" bundle="${langBundle}"/>:</label> <input type="text"
                                                                                                    name="name"/><br/>
                        <label><fmt:message key="txt.category" bundle="${langBundle}"/>:</label> <select
                            name="category_id">
                        <c:forEach var="category" items="${sessionScope.current_locale_categories}">
                            <c:if test="${empty category.children}">
                                <option value="${category.id}">
                                    <c:out value="${category.name}"/>
                                </option>
                            </c:if>
                        </c:forEach>
                    </select><br/>
                        <label><fmt:message key="txt.short_description" bundle="${langBundle}"/>:</label>
                        <textarea name="short_description" maxlength="250"></textarea><br/>
                        <label><fmt:message key="txt.description" bundle="${langBundle}"/>:</label>
                        <textarea name="description"></textarea><br/>
                        <label><fmt:message key="txt.price" bundle="${langBundle}"/>:</label> <input type="text"
                                                                                                     name="price"
                                                                                                     value="${requestScope.product.price}"/><br/>

                        <input type="submit" value="<fmt:message key="txt.save" bundle="${langBundle}"/>"/>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="<c:url value="/do/change_product"/>" method="post">
                        <input type="hidden" name="id" value="${requestScope.product.id}"/><br/>
                        <label><fmt:message key="txt.name" bundle="${langBundle}"/>:</label> <input type="text"
                                                                                                    name="name"
                                                                                                    value="${requestScope.product.name}"/><br/>
                        <label><fmt:message key="txt.category" bundle="${langBundle}"/>:</label> <select
                            name="category_id">
                        <c:forEach var="category" items="${sessionScope.current_locale_categories}">
                            <c:if test="${empty category.children}">
                                <option value="${category.id}" <c:if
                                        test="${category.id eq requestScope.product.categoryId}">
                                    selected="selected"
                                </c:if>>
                                    <c:out value="${category.name}"/>
                                </option>
                            </c:if>
                        </c:forEach>
                    </select><br/>
                        <label><fmt:message key="txt.short_description" bundle="${langBundle}"/>:</label>
                        <textarea name="short_description" maxlength="250"><c:out value="${requestScope.product.shortDescription}"/></textarea><br/>
                        <label><fmt:message key="txt.description" bundle="${langBundle}"/>:</label>
                        <textarea name="description"><c:out value="${requestScope.product.description}"/></textarea><br/>
                        <label><fmt:message key="txt.price" bundle="${langBundle}"/>:</label> <input type="text"
                                                                                                     name="price"
                                                                                                     value="${requestScope.product.price}"/><br/>

                        <input type="submit" value="<fmt:message key="txt.save" bundle="${langBundle}"/>"/>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:wrapper>
