<%@ page isELIgnored="false" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="de.fiz_karlsruhe.service.ConfigurationService" %>

<tr valign="top">
<td bgcolor="#669933" height="1"></td>
</tr>
<tr valign="top">
<td><a target="_blank" href="${ConfigurationService.getInstance().getBrandingImpressum()}">Impressum</a> | <a  target="_blank" href="${ConfigurationService.getInstance().getBrandingPrivacy()}">Datenschutz</a></td>
</tr>
<tr valign="top">
<td><a href="http://www.oclc.org/research/software/oai/cat.shtm"><img border="0" src="oaicat_icon.gif" alt="OAICat - An OAI-PMH v2 Repository Framework" width="120" height="60"></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://www.openarchives.org"><img border="0" src="http://www.openarchives.org/images/OA100.gif"/></a></td>
</tr>