<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>

<%@ page isELIgnored="false" %> 
<%@ page import="de.fiz_karlsruhe.service.ConfigurationService" %>

<!-- New Namespace -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:oai="http://www.openarchives.org/OAI/2.0/"
                              xmlns:oai_id="http://www.openarchives.org/OAI/2.0/oai-identifier"
                              xmlns:oai_branding="http://www.openarchives.org/OAI/2.0/branding/"
                              xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
                              xmlns:oai_etdms="http://www.ndltd.org/standards/metadata/etdms/1.0/"
                              xmlns:dc="http://purl.org/dc/elements/1.1/"
                              xmlns:toolkit="http://oai.dlib.vt.edu/OAI/metadata/toolkit">
  <xsl:output method="html" version="4.0"/>
  <xsl:strip-space elements="*"/>

  <xsl:template match="/oai:OAI-PMH">
    <html>
      <head>
        <title><xsl:value-of select="oai:request/@verb"/> Response OAI-Provider</title>
        <style type="text/css">
          <xsl:comment>
            body {
                color:${ConfigurationService.getInstance().getBrandingFontColor()};
                font-family:${ConfigurationService.getInstance().getBrandingFontFamily()};
            }
            a { text-decoration: none; }
            a:visited {
              color: ${ConfigurationService.getInstance().getBrandingFontColor()};
            }
            .divider {
                COLOR: ${ConfigurationService.getInstance().getBrandingFontColor()};
            }
            .nospace {
                MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px
            }
            .valign-top { vertical-align: top; }
            .layout-outer { width: 100%; border-spacing: 20px; }
            .layout-outer td { padding: 0; }
            .layout-inner { width: 100%; border-spacing: 0; }
            .layout-inner td { padding: 4px; }
            .layout-sp2 { width: 100%; border-spacing: 2px; }
            .layout-sp2 td { padding: 0; }
            .layout-sp4 { width: 100%; border-spacing: 4px; }
            .layout-sp4 td { padding: 0; }
            .layout-plain { width: 100%; border-spacing: 0; }
            .layout-plain td { padding: 0; }
            .label-cell { width: 150px; }
            .label-cell-200 { width: 200px; }
            .bg-light { background-color: #eeeeee; }
          </xsl:comment>
        </style>
          <script src="js/vendor/jquery-3.7.1/jquery-3.7.1.min.js"></script>
          <script src="js/vendor/autosize/autosize.min.js"></script>
          <script src="js/fiz-oai-provider.js"></script>
      </head>
      <body leftmargin="0" topmargin="0">
        <table border="0" class="layout-outer">

          <jsp:include page="header.jsp"></jsp:include>

          <tr class="valign-top">
            <td style="background-color: ${ConfigurationService.getInstance().getBrandingColor()};">
              <table border="0" class="layout-inner">
                <xsl:apply-templates select="oai:responseDate|oai:request"/>
              </table>
            </td>
          </tr>
          <tr class="valign-top">
            <td><xsl:apply-templates select="oai:Identify|oai:GetRecord|oai:ListIdentifiers|oai:ListMetadataFormats|oai:ListRecords|oai:ListSets|oai:error"/></td>
          </tr>
          
          <jsp:include page="footer.jsp"></jsp:include>
          
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="oai:Identify|oai:GetRecord">
    <h2><xsl:value-of select="name()"/></h2>
    <table border="0" class="layout-sp2">
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="oai:ListMetadataFormats">
    <h2><xsl:value-of select="name()"/></h2>
      <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="oai:ListSets">
    <h2><xsl:value-of select="name()"/></h2>
    <table border="0" class="layout-inner">
      <tr class="valign-top">
        <td class="label-cell"><strong>setSpec</strong></td>
        <td><strong>setName</strong></td>
      </tr>
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="oai:ListRecords">
    <h2><xsl:value-of select="name()"/> (<xsl:value-of select="/oai:OAI-PMH/oai:request/@metadataPrefix"/>)</h2>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="oai:ListIdentifiers">
    <h2><xsl:value-of select="name()"/></h2>
    <table border="0" class="layout-sp2">
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="oai:error">
    <h2><font color="red"><xsl:value-of select="name()"/></font></h2>
    <table border="0" class="layout-sp2">
      <tr class="valign-top">
        <td class="label-cell-200"><strong><xsl:value-of select="@code"/></strong></td>
        <td><xsl:value-of select="."/></td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template match="oai:record">
    <tr class="valign-top">
      <td>
        <table border="0" class="layout-sp2">
          <xsl:apply-templates/>
        </table>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:header">
    <tr class="valign-top">
      <td class="bg-light">
        <table border="0" class="layout-sp4">
		  <xsl:if test="@status">
			<tr class="valign-top">
			  <td class="label-cell"><strong>status</strong></td>
			  <td><xsl:value-of select="@status"/></td>
			</tr>
		  </xsl:if>
          <xsl:apply-templates/>
        </table>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:metadata">
    <tr class="valign-top">
      <td>
      <textarea class="xml-content-area" style="width:100%; height:200px; font-family:monospace; margin-bottom:10px;"><xsl:copy-of select="node()" /></textarea>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="*" mode="serialize">
    <xsl:text disable-output-escaping="yes">&lt;</xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:apply-templates select="@*" mode="serialize" />
    <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
    <xsl:apply-templates select="node()" mode="serialize" />
    <xsl:text disable-output-escaping="yes">&lt;/</xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
  </xsl:template>

  <xsl:template match="@*" mode="serialize">
    <xsl:text> </xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:text>="</xsl:text>
    <xsl:value-of select="."/>
    <xsl:text>"</xsl:text>
  </xsl:template>

  <xsl:template match="oai:set" >
    <tr class="valign-top">
      <xsl:apply-templates/>
    </tr>
  </xsl:template>

<!--   <xsl:template match="oai:setSpec"> -->
<!--     <tr class="valign-top"> -->
<!--       <td><strong><xsl:value-of select="name()"/></strong></td> -->
<!--       <td><a><xsl:attribute name="href">/fiz-oai-provider/OAIHandler?verb=ListRecords&amp;metadataPrefix=oai_dc&amp;set=<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></td> -->
<!--     </tr> -->
<!--   </xsl:template> -->

  <xsl:template match="oai:setSpec">
    <td class="label-cell"><a><xsl:attribute name="href"><%=request.getContextPath()%>/OAIHandler?verb=ListRecords&amp;renderHtml=true&amp;metadataPrefix=oai_dc&amp;set=<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></td>
  </xsl:template>

  <xsl:template match="oai:setName">
    <td><xsl:value-of select="."/></td>
  </xsl:template>

  <xsl:template match="oai:responseDate">
    <tr class="valign-top">
      <td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:value-of select="."/></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:request">
    <tr class="valign-top">
      <td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:value-of select="."/>?verb=<xsl:value-of select="@verb"/></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:*">
    <tr class="valign-top">
      <td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:value-of select="."/></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:adminEmail">
    <tr class="valign-top">
      <td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
      <td><cite><a><xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></cite></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:resumptionToken">
    <tr class="valign-top">
      <td>ResumptionToken: <a><xsl:attribute name="href"><%=request.getContextPath()%>/OAIHandler?verb=<xsl:value-of select="//oai:OAI-PMH/oai:request/@verb"/>&amp;renderHtml=true&amp;resumptionToken=<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:identifier">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td><a><xsl:attribute name="href"><%=request.getContextPath()%>/OAIHandler?verb=GetRecord&amp;renderHtml=true&amp;metadataPrefix=<xsl:choose><xsl:when test="/oai:OAI-PMH/oai:request/@metadataPrefix"><xsl:value-of select="/oai:OAI-PMH/oai:request/@metadataPrefix"/></xsl:when><xsl:otherwise>oai_dc</xsl:otherwise></xsl:choose>&amp;identifier=<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></td>
    </tr>
  </xsl:template>

<!--
  <xsl:template name="apply-templates-copy-all">
    <xsl:copy>
      <xsl:call-template name="apply-templates-copy-all"/>
    </xsl:copy>
  </xsl:template>
-->

  <xsl:template match="oai:description">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:apply-templates/></td>
    </tr>
  </xsl:template>

<!--
  <xsl:template match="oai_id:oai-identifier">
    <table border="0">
      <tr class="valign-top"><td>OAI Identifier</td></tr>
      <xsl:apply-templates/>
    </table>
  </xsl:template>
-->

  <xsl:template match="oai_id:oai-identifier">
    <table border="0" class="layout-plain">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/>:</strong></td>
    </tr>
    <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="oai_id:*">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:apply-templates/></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai_branding:branding">
    <table border="0" class="layout-plain">
    <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="oai_branding:metadataRendering">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td>
        <a>
          <xsl:attribute name="href">
            <xsl:value-of select="."/>
          </xsl:attribute>
          <xsl:attribute name="type">
            <xsl:value-of select="@mimeType"/>
          </xsl:attribute>
          <xsl:value-of select="@metadataNamespace"/>
        </a>
      </td>
    </tr>
  </xsl:template>

<!--
  <xsl:template match="oai_branding:metadataRendering>
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
    </tr>
  </xsl:template>
-->

  <xsl:template match="oai_branding:collectionIcon">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td>
        <a>
          <xsl:attribute name="href">
            <xsl:value-of select="oai_branding:link"/>
          </xsl:attribute>
          <img>
            <xsl:attribute name="width">
              <xsl:value-of select="oai_branding:width"/>
            </xsl:attribute>
            <xsl:attribute name="height">
              <xsl:value-of select="oai_branding:height"/>
            </xsl:attribute>
            <xsl:attribute name="src">
              <xsl:value-of select="oai_branding:url"/>
            </xsl:attribute>
            <xsl:attribute name="alt">
              <xsl:value-of select="oai_branding:title"/>
            </xsl:attribute>
          </img>
        </a>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="oai_branding:*">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:apply-templates/></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai:metadataFormat">
    <table border="0" class="layout-inner">
      <tr class="valign-top"><td class="label-cell"><strong>metadataPrefix</strong></td><td><a><xsl:attribute name="href"><%=request.getContextPath()%>/OAIHandler?verb=ListRecords&amp;renderHtml=true&amp;metadataPrefix=<xsl:value-of select="oai:metadataPrefix"/></xsl:attribute><xsl:value-of select="oai:metadataPrefix"/></a></td></tr>
        <tr class="valign-top"><td class="label-cell"><strong>schema</strong></td><td><a><xsl:attribute name="href"><xsl:value-of select="oai:schema"/></xsl:attribute><xsl:value-of select="oai:schema"/></a></td></tr>
        <tr class="valign-top"><td class="label-cell"><strong>metadataNamespace</strong></td><td><a><xsl:attribute name="href"><xsl:value-of select="oai:metadataNamespace"/></xsl:attribute><xsl:value-of select="oai:metadataNamespace"/></a></td></tr>
    </table>
    <hr/>
  </xsl:template>

  <xsl:template match="oai_dc:dc">
        <table border="0" class="layout-sp4">
          <xsl:apply-templates/>
        </table>
  </xsl:template>

  <xsl:template match="oai_etdms:thesis">
        <table border="0" class="layout-sp4">
          <xsl:apply-templates/>
        </table>
  </xsl:template>

  <xsl:template match="dc:identifier">
    <tr class="valign-top">
      <td><strong><xsl:value-of select="name()"/></strong></td>
      <td><a><xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></td>
    </tr>
  </xsl:template>

  <xsl:template match="dc:*">
    <tr class="valign-top">
      <td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
      <td><xsl:value-of select="."/></td>
    </tr>
  </xsl:template>

  <xsl:template match="oai_etdms:*">
    <tr class="valign-top">
      <td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
      <xsl:if test="@resource">
        <td><a><xsl:attribute name="href"><xsl:value-of select="@resource"/></xsl:attribute><xsl:value-of select="."/></a></td>
      </xsl:if>
      <xsl:if test="not(@resource)">
        <td><xsl:value-of select="."/></td>
      </xsl:if>
    </tr>
  </xsl:template>

  <xsl:template match="toolkit:toolkit">
    <table border="0" class="layout-plain">
      <tr class="valign-top"><td class="label-cell"><strong><xsl:value-of select="name()"/></strong></td>
        <td>
    <a>
      <xsl:attribute name="href">
        <xsl:value-of select="toolkit:URL"/>
      </xsl:attribute>
      <img border="0">
        <xsl:attribute name="alt">
          <xsl:value-of select="toolkit:title"/>
        </xsl:attribute>
        <xsl:attribute name="src">
          <xsl:value-of select="toolkit:toolkitIcon"/>
        </xsl:attribute>
      </img>
    </a>
    (version <xsl:value-of select="toolkit:version"/>)
        </td>
      </tr>
    </table>
  </xsl:template>

</xsl:stylesheet>