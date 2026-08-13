<%@ page isELIgnored="false" %> 
<%@ page import="de.fiz_karlsruhe.service.ConfigurationService" %>

<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html lang="en">
<head>
<title>${ConfigurationService.getInstance().getBrandingServiceName()} - OAI Provider</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<table border="0" class="layout-outer" role="presentation" aria-label="Page layout">

<jsp:include page="header.jsp"></jsp:include>

<tr class="valign-top">
<td>
<h2>${ConfigurationService.getInstance().getBrandingServiceName()} - OAI Provider Startpage</h2>

${ConfigurationService.getInstance().getBrandingWelcomeText()}
</td>
</tr>

<jsp:include page="footer.jsp"></jsp:include>

</table>
</body>
</html>