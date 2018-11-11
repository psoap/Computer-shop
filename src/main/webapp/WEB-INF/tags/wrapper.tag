<%@tag pageEncoding="UTF-8" %>
<%@attribute name="title" type="java.lang.String" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tg" uri="http://epam.com/myTld/locale" %>
<fmt:setLocale value="${sessionScope.current_locale}"/>
<fmt:setBundle basename="lang.lang" var="langBundle"/>

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="<c:url value="/resources/css/workarea_style.css"/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/resources/css/header_style.css"/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/resources/css/footer_style.css"/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/resources/css/style.css"/>" type="text/css" rel="stylesheet"/>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <script src="<c:url value="/resources/js/ui.js"/>"></script>
</head>
<body>
<div id="fnl-wrapper">
    <header>
        <div class="fnl-header">
            <div id="fnl-header-mini-bar">
                <div class="row">
                    <div class="fnl-header-mini-bar-right">
                        <a class="top-button-container" href="<c:url value="/do/basket"/>">
                            <span><img src="<c:url value="/resources/images/btn-mini-cart.png"/>"/></span>
                            <span><fmt:message key="txt.basket" bundle="${langBundle}"/></span>
                        </a>
                        <c:choose>
                            <c:when test="${not empty sessionScope.current_user}">
                                <a class="top-button-container" href="<c:url value="/do/personal"/>">
                                    <span><img src="<c:url value="/resources/images/btn-acc.png"/>"/></span>
                                    <span><fmt:message key="txt.my_account" bundle="${langBundle}"/>
                                        (${sessionScope.current_user.login})</span>
                                </a>
                                <c:if test="${sessionScope.current_user.role eq 'ADMIN'}">
                                    <a class="top-button-container" href="<c:url value="/do/manage_categories"/>">
                                        <span><img src="<c:url value="/resources/images/btn-set.png"/>"/></span>
                                        <span><fmt:message key="txt.admin_panel" bundle="${langBundle}"/></span>
                                    </a>
                                </c:if>
                                <a class="top-button-container" href="<c:url value="/do/logout"/>">
                                    <span><img src="<c:url value="/resources/images/next.png"/>"/></span>
                                    <span><fmt:message key="txt.logout" bundle="${langBundle}"/></span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a class="top-button-container" href="<c:url value="/do/login"/>">
                                    <span><img src="<c:url value="/resources/images/btn-login.png"/>"/></span>
                                    <span><fmt:message key="btn.login" bundle="${langBundle}"/></span>
                                </a>
                                <a class="top-button-container" href="<c:url value="/do/registration"/>">
                                    <span><img src="<c:url value="/resources/images/btn-reg.png"/>"/></span>
                                    <span><fmt:message key="txt.registration" bundle="${langBundle}"/></span>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="fnl-header-mini-bar-left">
                        <form id="lang-form" action="<c:url value="/do/swap_lang"/>">
                            <select name="lang_code" onchange="changeLang()">
                                <c:forEach var="lang" items="${applicationScope.all_locales}">
                                    <option value="${lang.language}"
                                            <c:if test="${sessionScope.current_locale eq lang}">selected="selected"</c:if>>
                                        <c:out value="${tg:getDisplayName(lang)}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </form>
                    </div>
                </div>
            </div>
            <div class="clear">
                <div class="row">
                    <div class="fnl-header-left">
                        <a href="<c:url value="/"/>">
                            <div class="top-logo">
                                <img src="<c:url value="/resources/images/logo.png"/>"/>
                            </div>
                        </a>
                    </div>
                    <div class="fnl-header-center">
                    </div>
                </div>
                <div class="fnl-header-right">
                </div>
            </div>
        </div>
        <div class="clear">
            <div id="fnl-header-bar">
                <div class="row">
                    <div class="fnl-header-bar-left">
                        <div class="top-nav-button">
                            <fmt:message key="txt.menu" bundle="${langBundle}"/>
                        </div>
                    </div>
                    <div class="fnl-header-bar-right">
                        <form class="top-search" action="<c:url value="/do/search"/>">
                            <input type="text" name="query">
                            <input type="submit" value="<fmt:message key="txt.search" bundle="${langBundle}"/>">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </header>
    <div class="row">
        <c:if test="${not empty sessionScope.messages}">
            <div id="messages">
                <c:forEach var="messageKey" items="${sessionScope.messages}">
                    <fmt:message key="${messageKey}" bundle="${langBundle}"/><br/>
                </c:forEach>
                    ${sessionScope.messages.clear()}
            </div>
        </c:if>
        <div class="workarea">
            <jsp:doBody/>
        </div>
    </div>
</div>
<footer>
    <div class="fnl-footer">
        <div class="row">
            Computer shop 2018
        </div>
    </div>
</footer>
</body>
</html>