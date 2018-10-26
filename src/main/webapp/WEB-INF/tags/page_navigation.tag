<%@tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="currentCount" type="java.lang.Integer" required="true" %>
<%@attribute name="maxCount" type="java.lang.Integer" required="true" %>
<%@attribute name="parameterName" type="java.lang.String" required="true" %>
<%@attribute name="parameterValue" type="java.lang.Object" required="true" %>
<c:if test="${currentCount>maxCount}">
    <div style="clear: both;text-align: center;">
        <fmt:formatNumber var="numbersOfPages"
                          value="${currentCount/maxCount + (currentCount/maxCount % 1 == 0 ? 0 : 0.5)}"
                          type="number" pattern="#"/>
        <c:forEach varStatus="loop" begin="1" end="${numbersOfPages}">
            <a href="<c:url value="/do/catalog_product?${parameterName}=${parameterValue}&page=${loop.count}"/>"><c:out
                    value="<${loop.count}>"/></a>
        </c:forEach>
    </div>
</c:if>