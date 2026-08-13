<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html lang="en">
<head>
<title>OAI ListIdentifiers Request Form [OAICat]</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<table border="0" class="layout-outer" role="presentation" aria-label="Page layout">

<jsp:include page="header.jsp"></jsp:include>

<tr class="valign-top">
<td>
<h2>OAI ListIdentifiers Request Form</h2>
<form class="nospace" action="<%=request.getContextPath()%>/OAIHandler" method="POST">
<input type="hidden" name="verb" value="ListIdentifiers">
<input type="hidden" name="renderHtml" value="true">
<table border="0" class="layout-inner" role="presentation" aria-label="ListIdentifiers form fields">
<tr class="valign-top">
<td class="label-cell"><strong>from:</strong></td>
<td><input type="text" name="from"></td>
</tr>
<tr class="valign-top">
<td class="label-cell"><strong>until:</strong></td>
<td><input type="text" name="until"></td>
</tr>
<tr class="valign-top">
<td class="label-cell"><strong>set:</strong></td>
<td><input type="text" name="set"></td>
</tr>
<tr class="valign-top">
<td class="label-cell"><strong>metadataPrefix:</strong></td>
<td><input type="text" name="metadataPrefix"></td>
</tr>
<tr class="valign-top">
<td class="label-cell">&nbsp;</td>
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