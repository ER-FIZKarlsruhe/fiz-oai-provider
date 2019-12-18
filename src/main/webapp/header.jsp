<%@ page isELIgnored="false" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="de.fiz_karlsruhe.service.ConfigurationService" %>


<tr valign="top">
<td bgcolor="${ConfigurationService.getInstance().getBrandingColor()}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr valign="top">
<td width="50%"><a href="${pageContext.request.contextPath}"><img src="banner01.gif" alt="${ConfigurationService.getInstance().getBrandingServiceName()}" width="388" height="120" border="0"></a></td>
<td width="50%" align="right"><a href="${ConfigurationService.getInstance().getBrandingServiceUrl()}"><img src="logo" alt="${ConfigurationService.getInstance().getBrandingServiceName()} Logo" width="202" height="120" border="0"></a></td>
</tr>
</table>
</td>
</tr>
<tr valign="top">
<td><a href="identify">Identify</a>&nbsp;<span class="divider">|</span>&nbsp;<a href="getRecord">GetRecord</a>&nbsp;<span class="divider">|</span>&nbsp;<a href="listIdentifiers">ListIdentifiers</a>&nbsp;(<a href="listIdentifiersResumption">Resumption</a>)&nbsp;<span class="divider">|</span>&nbsp;<a href="listMetadataFormats">ListMetadataFormats</a>&nbsp;<span class="divider">|</span>&nbsp;<a href="listRecords">ListRecords</a>&nbsp;(<a href="listRecordsResumption">Resumption</a>)&nbsp;<span class="divider">|</span>&nbsp;<a href="listSets">ListSets</a>&nbsp;(<a href="listSetsResumption">Resumption</a>)</td>
</tr>