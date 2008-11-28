<%@ tag display-name="moveToBasket"
        body-content="empty" %>
<%@ attribute name="uniqueName" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
<c:when test="${inBasket eq false}">
<img id="basketbutton" src="<c:url value="/" />includes/images/addToBasket.gif" onclick="addToBasket('${dto.uniqueName}')" style="cursor: pointer; cursor: hand;">
</c:when>
<c:otherwise>
<img src="<c:url value="/includes/images/alreadyInBasket.gif" />">
</c:otherwise>
</c:choose>
