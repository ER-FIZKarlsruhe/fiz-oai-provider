<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html>
<head>
<title>OAI ListMetadataFormats Request Form [oai-provider]</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="oaicat.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<table border="0" class="layout-outer">

<jsp:include page="header.jsp"></jsp:include>

<tr class="valign-top">
<td>
<h2>OAI ListMetadataFormats Request Form</h2>
<form class="nospace" action="<%=request.getContextPath()%>/OAIHandler" method="POST">
<input type="hidden" name="verb" value="ListMetadataFormats">
<input type="hidden" name="renderHtml" value="true">
<table border="0" class="layout-inner">
<tr class="valign-top">
<td class="label-cell"><strong>identifier:</strong></td>
<td><input type="text" name="identifier"></td>
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