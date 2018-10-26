<%@page contentType="text/html" pageEncoding="UTF-8" %>
<fmt:message key="txt.registration" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <form class="data-form-center" action="<c:url value="/do/reg"/>" method="post">
            <label><fmt:message key="txt.email" bundle="${langBundle}"/>:</label><input type="email" name="email"/>
            <label><fmt:message key="txt.username" bundle="${langBundle}"/>:</label><input type="text" name="login"/>
            <label><fmt:message key="txt.password" bundle="${langBundle}"/>:</label><input type="password"
                                                                                           name="password"/>
            <label><fmt:message key="txt.confirm.password" bundle="${langBundle}"/>:</label><input type="password"
                                                                                                   name="confirm_password"/>
            <br/><input type="submit" value="<fmt:message key="txt.registration" bundle="${langBundle}"/>"/>
        </form>
    </jsp:body>
</t:wrapper>