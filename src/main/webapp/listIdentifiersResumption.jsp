<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html>
<head>
<title>OAI ListIdentifiers (Resumption) Request Form [OAICat]</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="20" cellpadding="0">

<jsp:include page="header.jsp"></jsp:include>

<tr valign="top">
<td>
<h2>OAI ListIdentifiers (Resumption) Request Form</h2>
<form class="nospace" action="<%=request.getContextPath()%>/OAIHandler" method="POST">
<input type="hidden" name="verb" value="ListIdentifiers">
<table width="100%" border="0" cellspacing="0" cellpadding="4">
<tr valign="top">
<td width="150"><strong>ResumptionToken:</strong></td>
<td><input type="text" name="resumptionToken"></td>
</tr>
<tr valign="top">
<td width="150">&nbsp;</td>
<td><input type="submit"></td>
</tr>
</table>
</form>
</td>
</tr>

<jsp:include page="footer.jsp"></jsp:include>

</table>
</body>
</html>