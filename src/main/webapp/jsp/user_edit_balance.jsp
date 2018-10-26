<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:message key="txt.replenish" bundle="${langBundle}" var="title"/>
<t:wrapper title="${title}">
    <jsp:body>
        <div class="workarea-left">
            <%@include file="/WEB-INF/jspf/profile_menu.jspf" %>
        </div>
        <div class="workarea-right">
            <form action="<c:url value="/do/edit_balance"/>">
                <table>
                    <tr>
                        <td><label><fmt:message key="txt.amount" bundle="${langBundle}"/>:</label></td>
                        <td><input type="number" name="balance"></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="submit" class="form-button" value="<fmt:message key="txt.save" bundle="${langBundle}"/>"></td>
                    </tr>
                </table>
            </form>
        </div>
    </jsp:body>
</t:wrapper>