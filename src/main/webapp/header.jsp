<%@ page isELIgnored="false" %> 
<%@ page import="de.fiz_karlsruhe.service.ConfigurationService" %>

<style>
  body {
      color:${ConfigurationService.getInstance().getBrandingFontColor()};
      font-family:${ConfigurationService.getInstance().getBrandingFontFamily()};
  }
  a { 
    color: ${ConfigurationService.getInstance().getBrandingFontColor()};
    text-decoration: none; 
  }
  a:visited {
    color: ${ConfigurationService.getInstance().getBrandingFontColor()};
  }
  .divider {
      COLOR: ${ConfigurationService.getInstance().getBrandingFontColor()};
  }
  .nospace {
      MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px
  }
</style>

<tr valign="top">
<td bgcolor="${ConfigurationService.getInstance().getBrandingColor()}">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="top">
<td colspan="2"><a href="${ConfigurationService.getInstance().getBrandingServiceUrl()}"><img src="logo" alt="${ConfigurationService.getInstance().getBrandingServiceName()} Logo" height="100"></img></a></td>
</tr>
</table>
</td>
</tr>
<tr valign="top">
<td><a href="identify">Identify</a>&#160;<span class="divider">|</span>&#160;<a href="getRecord">GetRecord</a>&#160;<span class="divider">|</span>&#160;<a href="listIdentifiers">ListIdentifiers</a>&#160;(<a href="listIdentifiersResumption">Resumption</a>)&#160;<span class="divider">|</span>&#160;<a href="listMetadataFormats">ListMetadataFormats</a>&#160;<span class="divider">|</span>&#160;<a href="listRecords">ListRecords</a>&#160;(<a href="listRecordsResumption">Resumption</a>)&#160;<span class="divider">|</span>&#160;<a href="listSets">ListSets</a>&#160;(<a href="listSetsResumption">Resumption</a>)</td>
</tr>