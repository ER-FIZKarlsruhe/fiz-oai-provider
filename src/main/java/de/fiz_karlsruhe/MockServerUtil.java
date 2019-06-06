package de.fiz_karlsruhe;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;

public class MockServerUtil {

  public static void initMockServer() {
    System.out.println("initMockServer");
    ConfigurationProperties.httpProxy("proxy.fiz-karlsruhe.de:8888");
    
    MockServerClient serverClient = new MockServerClient("localhost", 8080, "mockserver");
    initGetSpecificFormat(serverClient);
    initGetAllFormats(serverClient);
    initGetItem(serverClient);
    initGetXslt(serverClient);
    
  }


  
  private static void initGetItem(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/item/10\\.0133.*"))
        .respond(response().withStatusCode(200)
            .withBody("<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\n" + 
                "    <identifier identifierType=\"DOI\">10.0133/10000386</identifier>\n" + 
                "    <creators>\n" + 
                "        <creator>\n" + 
                "            <creatorName>Mustermann, Max</creatorName>\n" + 
                "            <givenName>Max</givenName>\n" + 
                "            <familyName>Mustermann</familyName>\n" + 
                "        </creator>\n" + 
                "    </creators>\n" + 
                "    <title>123</title>\n" + 
                "    <publishers>\n" + 
                "        <publisher>FIZ Karlsruhe</publisher>\n" + 
                "    </publishers>\n" + 
                "    <productionYear>2019</productionYear>\n" + 
                "    <subjectAreas>\n" + 
                "        <subjectArea>\n" + 
                "            <controlledSubjectAreaName>Astrophysics and Astronomy</controlledSubjectAreaName>\n" + 
                "        </subjectArea>\n" + 
                "    </subjectAreas>\n" + 
                "    <resource resourceType=\"Collection\"></resource>\n" + 
                "    <rights>\n" + 
                "        <controlledRights>CC BY-SA 4.0 Attribution-ShareAlike</controlledRights>\n" + 
                "    </rights>\n" + 
                "    <rightsHolders>\n" + 
                "        <rightsHolder>FIZ Karlsruhe</rightsHolder>\n" + 
                "    </rightsHolders>\n" + 
                "</ns2:radarDataset>"));
  }
  
  
  private static void initGetSpecificFormat(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/oaiDc"))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + "  \"metadataPrefix\":\"oaiDc\",\n"
                + "  \"schemaLocation\":\"http://www.openarchives.org/OAI/2.0/oai_dc.xsd\",\n"
                + "  \"crosswalkStyleSheet\":\"http://localhost:8080/mockserver/xslt/radar2oai\",\n" + "  \"identifierXpath\":\"/dc/identifier\"\n" + "}"));
  }
  
  
  private static void initGetAllFormats(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/format"))
        .respond(response().withStatusCode(200)
            .withBody("[{\n" + 
                "  \"metadataPrefix\":\"oaiDc\",\n" + 
                "  \"schemaLocation\":\"http://www.openarchives.org/OAI/2.0/oai_dc.xsd\",\n" + 
                "  \"schemaNamespace\":\"http://www.openarchives.org/OAI/2.0\",\n" + 
                "  \"crosswalkStyleSheet\":\"http://localhost:8080/mockserver/xslt/radar2oai\", \n" + 
                "  \"identifierXpath\":\"/dc/identifier\"\n" + 
                "},\n" + 
                "{\n" + 
                "  \"metadataPrefix\":\"radar\",\n" + 
                "  \"schemaLocation\":\"https://www.radar-service.eu/en/radar-schema/radarDataset.xsd\",\n" + 
                "  \"schemaNamespace\":\"https://www.radar-service.eu/en/radar-schema\",\n" + 
                "  \"crosswalkStyleSheet\":\"\", \n" + 
                "  \"identifierXpath\":\"/radarDataset/identifier\"\n" + 
                "}\n" + 
                "]"));
  }
  
  
  private static void initGetXslt(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/xslt/radar2oai"))
        .respond(response().withStatusCode(200)
            .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                "<!--\n" + 
                "Copyright 2018 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH\n" + 
                "\n" + 
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" + 
                "you may not use this file except in compliance with the License.\n" + 
                "You may obtain a copy of the License at\n" + 
                "\n" + 
                "    http://www.apache.org/licenses/LICENSE-2.0\n" + 
                "\n" + 
                "Unless required by applicable law or agreed to in writing, software\n" + 
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" + 
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" + 
                "See the License for the specific language governing permissions and\n" + 
                "limitations under the License.\n" + 
                "-->\n" + 
                "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" version=\"1.0\">\n" + 
                "\n" + 
                "  <xsl:output method=\"xml\" indent=\"yes\"/>\n" + 
                "  \n" + 
                "  <!-- Used in xsl files for later versions to apply the corresponding layout per condition -->\n" + 
                "  <xsl:template match=\"*\">\n" + 
                "    <xsl:for-each select=\"namespace::*\">\n" + 
                "      <namespace><xsl:value-of select=\"local-name()\"/>:<xsl:value-of select=\".\"/></namespace>\n" + 
                "      <xsl:if test=\"local-name() = ''\">\n" + 
                "        <xsl:value-of select=\".\"/>\n" + 
                "      </xsl:if>\n" + 
                "    </xsl:for-each>  \n" + 
                "  </xsl:template>\n" + 
                "  \n" + 
                "  <xsl:template match=\"/\">\n" + 
                "    <!-- Check for radar version and apply the corresponding template -->\n" + 
                "    <xsl:variable name=\"namespace\">\n" + 
                "      <xsl:apply-templates select=\"*\"/>\n" + 
                "    </xsl:variable>\n" + 
                "    <xsl:choose>\n" + 
                "      <xsl:when test=\"contains($namespace, '/v09/')\">\n" + 
                "        <xsl:call-template name=\"radar-v09\"/>\n" + 
                "      </xsl:when>\n" + 
                "      <xsl:when test=\"contains($namespace, '/v08/')\">\n" + 
                "        <xsl:call-template name=\"radar-v08\"/>\n" + 
                "      </xsl:when>\n" + 
                "      <xsl:otherwise>\n" + 
                "        <xsl:call-template name=\"radar\"/>\n" + 
                "      </xsl:otherwise>\n" + 
                "    </xsl:choose>\n" + 
                "  </xsl:template>\n" + 
                "  \n" + 
                "  <xsl:template name=\"radar-v09\">\n" + 
                "    <oai_dc:dc xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ oai_dc.xsd\">\n" + 
                "      <dc:identifier>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='identifier']\"/>\n" + 
                "      </dc:identifier>\n" + 
                "      <dc:identifier>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='identifier']/@identifierType\"/>\n" + 
                "      </dc:identifier>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='creators']/*[local-name()='creator']\">\n" + 
                "        <dc:creator>\n" + 
                "          <xsl:value-of select=\"*[local-name()='creatorName']\"/>\n" + 
                "        </dc:creator>\n" + 
                "        <xsl:if test=\"*[local-name()='nameIdentifier']\">\n" + 
                "          <dc:identifier>\n" + 
                "            <xsl:value-of select=\"*[local-name()='nameIdentifier']\"/>\n" + 
                "          </dc:identifier>\n" + 
                "        </xsl:if>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:title>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='title']\"/>\n" + 
                "      </dc:title>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='publishers']\">\n" + 
                "        <dc:publisher>\n" + 
                "          <xsl:value-of select=\"*[local-name()='publisher']\"/>\n" + 
                "        </dc:publisher>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:date>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='publicationYear']\"/>\n" + 
                "      </dc:date>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='subjectAreas']/*[local-name()='subjectArea']/*\">\n" + 
                "        <dc:subject>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:subject>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:type>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='resource']\"/>\n" + 
                "      </dc:type>\n" + 
                "      <dc:type>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='resource']/@resourceType\"/>\n" + 
                "      </dc:type>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='rights']/*\">\n" + 
                "        <dc:rights>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:rights>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='rightsHolders']/*[local-name()='rightsHolder']\">\n" + 
                "        <dc:rights>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:rights>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='contributors']/*[local-name()='contributor']\">\n" + 
                "        <dc:contributor>\n" + 
                "          <xsl:value-of select=\"*[local-name()='contributorName']\"/>\n" + 
                "        </dc:contributor>\n" + 
                "        <xsl:if test=\"*[local-name()='nameIdentifier']\">\n" + 
                "          <dc:identifier>\n" + 
                "            <xsl:value-of select=\"*[local-name()='nameIdentifier']\"/>\n" + 
                "          </dc:identifier>\n" + 
                "        </xsl:if>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='descriptions']/*[local-name()='description']\">\n" + 
                "        <dc:description>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:description>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='keywords']/*[local-name()='keyword']\">\n" + 
                "        <dc:description>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:description>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='contributors']/*[local-name()='contributor']\">\n" + 
                "        <dc:contributor>\n" + 
                "          <xsl:value-of select=\"*[local-name()='contributorName']\"/>\n" + 
                "        </dc:contributor>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:language>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='language']\"/>\n" + 
                "      </dc:language>\n" + 
                "      <xsl:if test=\"*[local-name()='radarDataset']/*[local-name()='alternateIdentifiers']/*[local-name()='alternateIdentifier']\">\n" + 
                "        <dc:identifier>\n" + 
                "          <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='alternateIdentifiers']/*[local-name()='alternateIdentifier']\"/>\n" + 
                "        </dc:identifier>\n" + 
                "        <dc:identifier>\n" + 
                "          <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='alternateIdentifiers']/*[local-name()='alternateIdentifier']/@alternateIdentifierType\"/>\n" + 
                "        </dc:identifier>\n" + 
                "      </xsl:if>\n" + 
                "      <xsl:if test=\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']\">\n" + 
                "        <dc:relation>\n" + 
                "          <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']\"/>\n" + 
                "        </dc:relation>\n" + 
                "        <dc:identifier>\n" + 
                "          <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']\"/>\n" + 
                "        </dc:identifier>\n" + 
                "        <dc:relation>\n" + 
                "          <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']/@relatedIdentifierType\"/>\n" + 
                "        </dc:relation>\n" + 
                "        <dc:identifier>\n" + 
                "          <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']/@relatedIdentifierType\"/>\n" + 
                "        </dc:identifier>\n" + 
                "      </xsl:if>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='geoLocations']/*[local-name()='geoLocation']\">\n" + 
                "        <dc:coverage>\n" + 
                "          <xsl:choose>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationCountry'] and *[local-name()='geoLocationRegion']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationCountry']\"/>\n" + 
                "              <xsl:text> (</xsl:text>\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationRegion']\"/>\n" + 
                "              <xsl:text>)</xsl:text>\n" + 
                "            </xsl:when>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationCountry']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationCountry']\"/>\n" + 
                "            </xsl:when>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationRegion']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationRegion']\"/>\n" + 
                "            </xsl:when>\n" + 
                "          </xsl:choose>\n" + 
                "          <xsl:if test=\"*[local-name()='geoLocationPoint'] or *[local-name()='geoLocationBox']\">\n" + 
                "            <xsl:text>: </xsl:text>\n" + 
                "            <xsl:choose>\n" + 
                "              <xsl:when test=\"*[local-name()='geoLocationBox']\">\n" + 
                "                <xsl:text>Lat/Long/Datum </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='longitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='longitude']\"/>\n" + 
                "              </xsl:when>\n" + 
                "              <xsl:when test=\"*[local-name()='geoLocationPoint']\">\n" + 
                "                <xsl:text>Lat/Long/Datum </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationPoint']/*[local-name()='longitude']\"/>\n" + 
                "              </xsl:when>\n" + 
                "            </xsl:choose>\n" + 
                "            <xsl:text> (WGS 84)</xsl:text>\n" + 
                "          </xsl:if>\n" + 
                "        </dc:coverage>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='fundingReferences']/*[local-name()='fundingReference']\">\n" + 
                "        <xsl:if test=\"*[local-name()='funderIdentifier']\">\n" + 
                "          <dc:identifier>\n" + 
                "            <xsl:value-of select=\"*[local-name()='funderIdentifier']\"/>\n" + 
                "          </dc:identifier>\n" + 
                "        </xsl:if>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:format>application/zip</dc:format>\n" + 
                "    </oai_dc:dc>\n" + 
                "  </xsl:template>\n" + 
                "  \n" + 
                "  <xsl:template name=\"radar-v08\">\n" + 
                "    <oai_dc:dc xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n" + 
                "      <dc:identifier>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='identifier']\"/>\n" + 
                "      </dc:identifier>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='creators']/*[local-name()='creator']\">\n" + 
                "        <dc:creator>\n" + 
                "          <xsl:value-of select=\"*[local-name()='creatorName']\"/>\n" + 
                "        </dc:creator>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:title>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='title']\"/>\n" + 
                "      </dc:title>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='publishers']\">\n" + 
                "        <dc:publisher>\n" + 
                "          <xsl:value-of select=\"*[local-name()='publisher']\"/>\n" + 
                "        </dc:publisher>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:date>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='publicationYear']\"/>\n" + 
                "      </dc:date>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='subjectAreas']/*[local-name()='subjectArea']/*\">\n" + 
                "        <dc:subject>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:subject>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='rights']/*\">\n" + 
                "        <dc:rights>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:rights>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='descriptions']/*[local-name()='description']\">\n" + 
                "        <dc:description>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:description>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='keywords']/*[local-name()='keyword']\">\n" + 
                "        <dc:description>\n" + 
                "          <xsl:value-of select=\".\"/>\n" + 
                "        </dc:description>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='contributors']\">\n" + 
                "        <dc:contributor>\n" + 
                "          <xsl:value-of select=\"*[local-name()='contributor']\"/>\n" + 
                "        </dc:contributor>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:language>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radarDataset']/*[local-name()='language']\"/>\n" + 
                "      </dc:language>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radarDataset']/*[local-name()='geoLocations']/*[local-name()='geoLocation']\">\n" + 
                "        <dc:coverage>\n" + 
                "          <xsl:choose>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationCountry'] and *[local-name()='geoLocationRegion']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationCountry']\"/>\n" + 
                "              <xsl:text> (</xsl:text>\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationRegion']\"/>\n" + 
                "              <xsl:text>)</xsl:text>\n" + 
                "            </xsl:when>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationCountry']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationCountry']\"/>\n" + 
                "            </xsl:when>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationRegion']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationRegion']\"/>\n" + 
                "            </xsl:when>\n" + 
                "          </xsl:choose>\n" + 
                "          <xsl:if test=\"*[local-name()='geoLocationPoint'] or *[local-name()='geoLocationBox']\">\n" + 
                "            <xsl:text>: </xsl:text>\n" + 
                "            <xsl:choose>\n" + 
                "              <xsl:when test=\"*[local-name()='geoLocationBox']\">\n" + 
                "                <xsl:text>Lat/Long/Datum </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='longitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='longitude']\"/>\n" + 
                "              </xsl:when>\n" + 
                "              <xsl:when test=\"*[local-name()='geoLocationPoint']\">\n" + 
                "                <xsl:text>Lat/Long/Datum </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationPoint']/*[local-name()='longitude']\"/>\n" + 
                "              </xsl:when>\n" + 
                "            </xsl:choose>\n" + 
                "            <xsl:text> (WGS 84)</xsl:text>\n" + 
                "          </xsl:if>\n" + 
                "        </dc:coverage>\n" + 
                "      </xsl:for-each>\n" + 
                "    </oai_dc:dc>\n" + 
                "  </xsl:template>\n" + 
                "  \n" + 
                "  <xsl:template name=\"radar\">\n" + 
                "    <oai_dc:dc xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n" + 
                "      <dc:identifier>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radar']/*[local-name()='identifier']\"/>\n" + 
                "      </dc:identifier>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='creators']/*[local-name()='creator']\">\n" + 
                "        <dc:creator>\n" + 
                "          <xsl:value-of select=\"*[local-name()='creatorName']\"/>\n" + 
                "        </dc:creator>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:title>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radar']/*[local-name()='title']\"/>\n" + 
                "      </dc:title>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='publishers']\">\n" + 
                "        <dc:publisher>\n" + 
                "          <xsl:value-of select=\"*[local-name()='publisher']\"/>\n" + 
                "        </dc:publisher>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:date>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radar']/*[local-name()='publicationYear']\"/>\n" + 
                "      </dc:date>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='subjectAreas']\">\n" + 
                "        <xsl:for-each select=\"*[local-name()='subjectArea']/*\">\n" + 
                "          <dc:subject>\n" + 
                "            <xsl:value-of select=\".\"/>\n" + 
                "          </dc:subject>\n" + 
                "        </xsl:for-each>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:rights>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radar']/*[local-name()='rights']\"/>\n" + 
                "      </dc:rights>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='descriptions']\">\n" + 
                "        <dc:description>\n" + 
                "          <xsl:value-of select=\"*[local-name()='description']\"/>\n" + 
                "        </dc:description>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='keywords']\">\n" + 
                "        <dc:description>\n" + 
                "          <xsl:value-of select=\"*[local-name()='keyword']\"/>\n" + 
                "        </dc:description>\n" + 
                "      </xsl:for-each>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='contributors']\">\n" + 
                "        <dc:contributor>\n" + 
                "          <xsl:value-of select=\"*[local-name()='contributor']\"/>\n" + 
                "        </dc:contributor>\n" + 
                "      </xsl:for-each>\n" + 
                "      <dc:language>\n" + 
                "        <xsl:value-of select=\"*[local-name()='radar']/*[local-name()='language']\"/>\n" + 
                "      </dc:language>\n" + 
                "      <xsl:for-each select=\"*[local-name()='radar']/*[local-name()='geoLocations']/*[local-name()='geoLocation']\">\n" + 
                "        <dc:coverage>\n" + 
                "          <xsl:choose>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationCountry'] and *[local-name()='geoLocationRegion']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationCountry']\"/>\n" + 
                "              <xsl:text> (</xsl:text>\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationRegion']\"/>\n" + 
                "              <xsl:text>)</xsl:text>\n" + 
                "            </xsl:when>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationCountry']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationCountry']\"/>\n" + 
                "            </xsl:when>\n" + 
                "            <xsl:when test=\"*[local-name()='geoLocationRegion']\">\n" + 
                "              <xsl:value-of select=\"*[local-name()='geoLocationRegion']\"/>\n" + 
                "            </xsl:when>\n" + 
                "          </xsl:choose>\n" + 
                "          <xsl:if test=\"*[local-name()='geoLocationPoint'] or *[local-name()='geoLocationBox']\">\n" + 
                "            <xsl:text>: </xsl:text>\n" + 
                "            <xsl:choose>\n" + 
                "              <xsl:when test=\"*[local-name()='geoLocationBox']\">\n" + 
                "                <xsl:text>Lat/Long/Datum </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='longitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='longitude']\"/>\n" + 
                "              </xsl:when>\n" + 
                "              <xsl:when test=\"*[local-name()='geoLocationPoint']\">\n" + 
                "                <xsl:text>Lat/Long/Datum </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationPoint']/*[local-name()='latitude']\"/>\n" + 
                "                <xsl:text> </xsl:text>\n" + 
                "                <xsl:value-of select=\"*[local-name()='geoLocationPoint']/*[local-name()='longitude']\"/>\n" + 
                "              </xsl:when>\n" + 
                "            </xsl:choose>\n" + 
                "            <xsl:text> (WGS 84)</xsl:text>\n" + 
                "          </xsl:if>\n" + 
                "        </dc:coverage>\n" + 
                "      </xsl:for-each>\n" + 
                "    </oai_dc:dc>\n" + 
                "  </xsl:template>\n" + 
                "  \n" + 
                "</xsl:stylesheet>\n" + 
                ""));
  }
  
}
