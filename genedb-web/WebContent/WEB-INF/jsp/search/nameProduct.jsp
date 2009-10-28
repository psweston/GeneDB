<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="misc" uri="misc" %>
<format:headerRound title="Name/Product Search">
    <st:init />
    <link rel="stylesheet" type="text/css" href="<misc:url value="/includes/style/genedb/genePage.css"/>" />
</format:headerRound>
<div id="geneDetails">
    <format:genePageSection id="nameSearch" className="whiteBox">
        <form:form commandName="query" action="Query" method="GET">
        <input type="hidden" name="q" value="nameProduct" />
            <table>

                <tr>
                  <td>Name/Product:</td>
                  <td>
                    <form:input id="nameProduct" path="search"/>
                    <font color="red">&nbsp;<form:errors path="search" /></font>
                  </td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td colspan="2">
                    <input type="submit" value="Submit" />
                  </td>
                  <td>&nbsp;</td>
                </tr>
            </table>

        </form:form>
    </format:genePageSection>
</div>
<format:footer />