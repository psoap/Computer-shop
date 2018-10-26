<%@page contentType="text/html" pageEncoding="UTF-8" %>
<fmt:message key="txt.manage_categories" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/admin_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <h4><fmt:message key="txt.add" bundle="${langBundle}"/></h4>
            <form action="<c:url value="/do/change_category"/>" method="post">
                <table>
                    <tr>
                        <td><fmt:message key="txt.parent_category" bundle="${langBundle}"/></td>
                        <td>
                            <select name="parent_id">
                                <option selected="selected"></option>
                                <c:if test="${not empty sessionScope.lang_categories}">
                                    <c:forEach var="category" items="${sessionScope.lang_categories}">
                                        <c:if test="${category.parentId eq 0}">
                                            <option value="${category.id}">${category.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </td>
                    </tr>
                    <c:forEach var="lang" items="${applicationScope.all_langs}">
                        <tr>
                            <td>${tg:getTranslatedName(lang, sessionScope.current_lang)} <fmt:message
                                    key="txt.translate"
                                    bundle="${langBundle}"/></td>
                            <td><input type="text" name="${lang.language}" value=""/></td>
                        </tr>
                    </c:forEach>
                </table>
                <input type="submit" value="<fmt:message key="txt.add" bundle="${langBundle}"/>">
            </form>
            <hr/>
            <h4><fmt:message key="txt.edit" bundle="${langBundle}"/></h4>
            <form action="<c:url value="/do/change_category"/>" method="post">
                <table>
                    <tr>
                        <td><fmt:message key="txt.category" bundle="${langBundle}"/></td>
                        <td>
                            <input type="hidden" name="lang_code" id="lang_code">
                            <select name="id" id="select_id" onchange="editCategoryLang()">
                                <c:forEach var="langCategoriesEntry" items="${applicationScope.all_categories}">
                                    <c:forEach var="category" items="${langCategoriesEntry.value}">
                                        <option value="${category.id}" class="${category.langCode}">
                                            <c:out value="${category.name}"/>
                                        </option>
                                    </c:forEach>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><label><fmt:message key="txt.name" bundle="${langBundle}"/></label></td>
                        <td><input type="text" name="name"/></td>
                    </tr>
                </table>
                <input type="submit" value="<fmt:message key="txt.edit" bundle="${langBundle}"/>">
            </form>
            <hr/>
            <c:if test="${not empty sessionScope.lang_categories}">
                <h4><fmt:message key="txt.remove" bundle="${langBundle}"/></h4>
                <form action="<c:url value="/do/remove_category"/>">
                    <table>
                        <tr>
                            <td><fmt:message key="txt.category" bundle="${langBundle}"/></td>
                            <td>
                                <select name="id">
                                    <c:forEach var="category" items="${sessionScope.lang_categories}">
                                        <option value="${category.id}">${category.name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </table>
                    <input type="submit" value="<fmt:message key="txt.remove" bundle="${langBundle}"/>">
                </form>
            </c:if>
        </div>
    </jsp:body>
</t:wrapper>
