<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html lang="en">
<head>
<title>OAI Identify Request Form [oai-provider]</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<table border="0" class="layout-outer" role="presentation" aria-label="Page layout">

<jsp:include page="header.jsp"></jsp:include>

<tr class="valign-top">
<td>
<h2>OAI Identify Request Form</h2>
<form class="nospace" action="<%=request.getContextPath()%>/OAIHandler" method="POST">
<input type="hidden" name="verb" value="Identify">
<input type="hidden" name="renderHtml" value="true">
<table border="0" class="layout-inner" role="presentation" aria-label="Identify form fields">
<tr class="valign-top">
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