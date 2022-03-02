<%@ page isELIgnored="false" %> 
<%@ page import="de.fiz_karlsruhe.service.ConfigurationService" %>

<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html>
<head>
<title>${ConfigurationService.getInstance().getBrandingServiceName()} - OAI Provider</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="20" cellpadding="0">

<jsp:include page="header.jsp"></jsp:include>

<tr valign="top">
<td>
<h2>${ConfigurationService.getInstance().getBrandingServiceName()} - OAI Provider Startpage</h2>

${ConfigurationService.getInstance().getBrandingWelcomeText()}
</td>
</tr>

<jsp:include page="footer.jsp"></jsp:include>

</table>
</body>
</html>