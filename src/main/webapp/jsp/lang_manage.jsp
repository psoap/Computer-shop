<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.manage_langs" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <form action="<c:url value="/do/add_lang"/>" method="post">
                <table>
                    <tr>
                        <td><label><fmt:message key="txt.code" bundle="${langBundle}"/>:</label></td>
                        <td><input type="text" name="lang_code"></td>
                    </tr>
                    <tr>
                        <td><label><fmt:message key="txt.name" bundle="${langBundle}"/>:</label></td>
                        <td><input type="text" name="name"></td>
                    </tr>
                    <c:if test="${not empty sessionScope.lang_categories}">
                        <c:forEach var="category" items="${sessionScope.lang_categories}">
                            <tr>
                                <td>${category.name}</td>
                                <td><input type="text" name="translate_categories"></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr>
                        <td><input type="submit" value="<fmt:message key="txt.save" bundle="${langBundle}"/>"></td>
                    </tr>
                </table>
            </form>
        </div>
    </jsp:body>
</t:wrapper>
