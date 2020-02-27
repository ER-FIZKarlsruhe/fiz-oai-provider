<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html>
<head>
<title>OAI ListRecords Request Form [oai-provider]</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="20" cellpadding="0">

<jsp:include page="header.jsp"></jsp:include>

<tr valign="top">
<td>
<h2>OAI ListRecords Request Form</h2>
<form class="nospace" action="<%=request.getContextPath()%>/OAIHandler" method="POST">
<input type="hidden" name="verb" value="ListRecords">
<table width="100%" border="0" cellspacing="0" cellpadding="4">
<tr valign="top">
<td width="150"><strong>from:</strong></td>
<td><input type="text" name="from"></td>
</tr>
<tr valign="top">
<td width="150"><strong>until:</strong></td>
<td><input type="text" name="until"></td>
</tr>
<tr valign="top">
<td width="150"><strong>set:</strong></td>
<td><input type="text" name="set"></td>
</tr>
<tr valign="top">
<td width="150"><strong>metadataPrefix:</strong></td>
<td><input type="text" name="metadataPrefix"></td>
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