<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="btn.login" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <form class="data-form-center" action="<c:url value="/do/auth"/>" method="post">
            <label><fmt:message key="txt.username" bundle="${langBundle}"/>:</label>
            <input type="text" name="login"/>
            <label><fmt:message key="txt.password" bundle="${langBundle}"/>:</label>
            <input type="password" name="password"/>
            <input type="submit" value="<fmt:message key="btn.login" bundle="${langBundle}"/>"/>
        </form>
    </jsp:body>
</t:wrapper>