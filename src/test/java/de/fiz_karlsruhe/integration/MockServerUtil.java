package de.fiz_karlsruhe.integration;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mockserver.client.MockServerClient;
import org.mockserver.client.initialize.ExpectationInitializer;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.model.Parameter;

public class MockServerUtil  implements ExpectationInitializer {

  final static Logger logger = LogManager.getLogger(MockServerUtil.class);
  
  @Override
  public void initializeExpectations(MockServerClient mockServerClient) {
    logger.info("MockServer initializeExpectations");
    ConfigurationProperties.httpProxy("proxy.fiz-karlsruhe.de:8888");
    
    initGetSpecificFormat(mockServerClient);
    initGetAllFormats(mockServerClient);
    initGetAllCrosswalks(mockServerClient);
    initGetItem(mockServerClient);
    initGetXslt(mockServerClient);
    initGetSpecificSet(mockServerClient);
    initGetAllSets(mockServerClient);
    initGetItemsNoContent(mockServerClient);
    initGetItemsWithContent(mockServerClient);
    initGetItemsNoResultData(mockServerClient);
  }

  private void initGetItem(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/item/10\\.0133.*").withQueryStringParameters(new Parameter("format", Arrays.asList("radar"))))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "  \"identifier\": \"10.0133/48320\",\n" + 
                "  \"datestamp\": \"2019-07-12T12:39:37Z\",\n" + 
                "  \"deleteFlag\": false,\n" + 
                "  \"tags\": null,\n" + 
                "  \"sets\": null,\n" + 
                "  \"formats\": null,\n" + 
                "  \"ingestFormat\": \"radar\",\n" + 
                "  \"content\": {\n" + 
                "    \"identifier\": \"10.0133/48320\",\n" + 
                "    \"format\": \"radar\",\n" + 
                "    \"content\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>\\n<ns2:radarDataset xmlns=\\\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\\\" xmlns:ns2=\\\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\\\">\\n    <identifier identifierType=\\\"DOI\\\">10.0133/48320</identifier>\\n    <creators>\\n        <creator>\\n            <creatorName>Superuser, Generic1</creatorName>\\n            <givenName>Generic1</givenName>\\n            <familyName>Superuser</familyName>\\n        </creator>\\n    </creators>\\n    <title>Chart.js-2.7.1.zip</title>\\n    <publishers>\\n        <publisher>Radar4Kit</publisher>\\n    </publishers>\\n    <productionYear>2019</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Computer Science</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\\\"Dataset\\\"></resource>\\n    <rights>\\n        <controlledRights>CC BY-ND 4.0 Attribution-NoDerivs</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Radar4Kit</rightsHolder>\\n    </rightsHolders>\\n</ns2:radarDataset>\\n\"\n" + 
                "  }\n" + 
                "}"));

    serverClient.when(request().withMethod("GET").withPath("/item/10\\.0133.*").withQueryStringParameters(new Parameter("format", Arrays.asList("oai_dc"))))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "  \"identifier\": \"10.0133/48320\",\n" + 
                "  \"datestamp\": \"2019-07-12T12:39:37Z\",\n" + 
                "  \"deleteFlag\": false,\n" + 
                "  \"tags\": null,\n" + 
                "  \"sets\": null,\n" + 
                "  \"formats\": null,\n" + 
                "  \"ingestFormat\": \"radar\",\n" + 
                "  \"content\": {\n" + 
                "    \"identifier\": \"10.0133/48320\",\n" + 
                "    \"format\": \"oai_dc\",\n" + 
                "    \"content\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><oai_dc:dc xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai_dc/ oai_dc.xsd\\\" xmlns:dc=\\\"http://purl.org/dc/elements/1.1/\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns:oai_dc=\\\"http://www.openarchives.org/OAI/2.0/oai_dc/\\\">\\n  <dc:identifier>10.0133/48320</dc:identifier>\\n  <dc:identifier>DOI</dc:identifier>\\n  <dc:creator>Superuser, Generic1</dc:creator>\\n  <dc:title>Chart.js-2.7.1.zip</dc:title>\\n  <dc:publisher>Radar4Kit</dc:publisher>\\n  <dc:date>2019</dc:date>\\n  <dc:subject>Computer Science</dc:subject>\\n  <dc:type/>\\n  <dc:type>Dataset</dc:type>\\n  <dc:rights>CC BY-ND 4.0 Attribution-NoDerivs</dc:rights>\\n  <dc:rights>Radar4Kit</dc:rights>\\n  <dc:language/>\\n  <dc:format>application/zip</dc:format>\\n</oai_dc:dc>\\n\"\n" + 
                "  }\n" + 
                "}"));
  }
  
  private void initGetItemsNoContent(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/item").withQueryStringParameters(new Parameter("content", Arrays.asList("false")), new Parameter("set", Arrays.asList("fiz"))))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "  \"total\": 11,\n" + 
                "  \"offset\": 0,\n" + 
                "  \"size\": 11,\n" + 
                "  \"data\": [{\n" + 
                "    \"identifier\": \"10.0133/48320\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50699\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/47867\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50695\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50296\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50235\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/48163\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:21Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50693\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/49593\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/48162\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/47868\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:21Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": null\n" + 
                "  }]\n" + 
                "}"));
  }
  
  private void initGetItemsWithContent(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/item").withQueryStringParameters(new Parameter("content", Arrays.asList("true")), new Parameter("set", Arrays.asList("fiz"))))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "  \"total\": 11,\n" + 
                "  \"offset\": 0,\n" + 
                "  \"size\": 11,\n" + 
                "  \"data\": [{\n" + 
                "    \"identifier\": \"10.0133/48320\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/48320\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/48320</identifier>\\n    <creators>\\n        <creator>\\n            <creatorName>Superuser, Generic1</creatorName>\\n            <givenName>Generic1</givenName>\\n            <familyName>Superuser</familyName>\\n        </creator>\\n    </creators>\\n    <title>Chart.js-2.7.1.zip</title>\\n    <publishers>\\n        <publisher>Radar4Kit</publisher>\\n    </publishers>\\n    <productionYear>2019</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Computer Science</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <rights>\\n        <controlledRights>CC BY-ND 4.0 Attribution-NoDerivs</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Radar4Kit</rightsHolder>\\n    </rightsHolders>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50699\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/50699\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/50699</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Name, Vorname</creatorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Name</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>Test 3</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>Other</controlledRights>\\n        <additionalRights></additionalRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Max Mustermann</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/47867\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/47867\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/47867</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Name, Vorname</creatorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Name</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>chromedriver_win32.zip</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>CC BY-NC 4.0 Attribution-NonCommercial</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Nachname, Vorname oder Name einer Institution</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50695\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/50695\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/50695</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Name, Vorname</creatorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Name</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>Test 2</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>Other</controlledRights>\\n        <additionalRights></additionalRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Max Mustermann</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50296\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/50296\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/50296</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Name, Vorname</creatorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Name</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>4</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>CC BY-NC 4.0 Attribution-NonCommercial</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Nachname, Vorname oder Name einer Institution</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50235\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/50235\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/50235</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Mustermann, Max</creatorName>\\n            <givenName>Max</givenName>\\n            <familyName>Mustermann</familyName>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>Test</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>CC BY-NC 4.0 Attribution-NonCommercial</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Nachname, Vorname oder Name einer Institution</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/48163\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:21Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/48163\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/48163</identifier>\\n    <creators>\\n        <creator>\\n            <creatorName>Superuser, Generic1</creatorName>\\n            <givenName>Generic1</givenName>\\n            <familyName>Superuser</familyName>\\n        </creator>\\n    </creators>\\n    <title>apache-tomcat-8.5.37.zip</title>\\n    <publishers>\\n        <publisher>FIZ</publisher>\\n    </publishers>\\n    <productionYear>2019</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Computer Science</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <rights>\\n        <controlledRights>CC BY-SA 4.0 Attribution-ShareAlike</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>FIZ</rightsHolder>\\n    </rightsHolders>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/50693\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/50693\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/50693</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Name, Vorname</creatorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Name</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>RAD-1780</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>Other</controlledRights>\\n        <additionalRights></additionalRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Max Mustermann</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/49593\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/49593\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/49593</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Name, Vorname</creatorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Name</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Nachname, Vorname</contributorName>\\n            <givenName>Vorname</givenName>\\n            <familyName>Nachname</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>AEIOU</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2015-2016</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>CC BY-NC 4.0 Attribution-NonCommercial</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Nachname, Vorname oder Name einer Institution</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/48162\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:20Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/48162\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/48162</identifier>\\n    <creators>\\n        <creator>\\n            <creatorName>Superuser, Generic1</creatorName>\\n            <givenName>Generic1</givenName>\\n            <familyName>Superuser</familyName>\\n        </creator>\\n    </creators>\\n    <title>chromedriver_win32.zip</title>\\n    <publishers>\\n        <publisher>FIZ</publisher>\\n    </publishers>\\n    <productionYear>2019</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Computer Science</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <rights>\\n        <controlledRights>CC BY-SA 4.0 Attribution-ShareAlike</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>FIZ</rightsHolder>\\n    </rightsHolders>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }, {\n" + 
                "    \"identifier\": \"10.0133/47868\",\n" + 
                "    \"datestamp\": \"2019-07-19T08:28:21Z\",\n" + 
                "    \"deleteFlag\": false,\n" + 
                "    \"tags\": null,\n" + 
                "    \"ingestFormat\": \"radar\",\n" + 
                "    \"content\": {\n" + 
                "      \"identifier\": \"10.0133/47868\",\n" + 
                "      \"format\": \"radar\",\n" + 
                "      \"content\": \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\\n<ns2:radarDataset xmlns=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\" xmlns:ns2=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\">\\n    <identifier identifierType=\"DOI\">10.0133/47868</identifier>\\n    <alternateIdentifiers>\\n        <alternateIdentifier alternateIdentifierType=\"Freitext, z.B. interne Identifikationsnummer\">Bereits bestehende und nicht von RADAR vergebene Identifier (z.B. institutsinterne Identifier) fÃ¼r das Objekt bzw. dessen Inhalt</alternateIdentifier>\\n    </alternateIdentifiers>\\n    <relatedIdentifiers>\\n        <relatedIdentifier relatedIdentifierType=\"bibcode\" relationType=\"IsCitedBy\">Identifier, der auf ergÃ¤nzende Quellen/Materialien zu diesem Objekt verweisen, z.B. einen wissenschaftlichen Artikel</relatedIdentifier>\\n    </relatedIdentifiers>\\n    <creators>\\n        <creator>\\n            <creatorName>Superuser, Generic1</creatorName>\\n            <givenName>Generic1</givenName>\\n            <familyName>Superuser</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <creatorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</creatorAffiliation>\\n        </creator>\\n    </creators>\\n    <contributors>\\n        <contributor contributorType=\"ContactPerson\">\\n            <contributorName>Superuser, Generic1</contributorName>\\n            <givenName>Generic1</givenName>\\n            <familyName>Superuser</familyName>\\n            <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">2220-0021-5000-0004</nameIdentifier>\\n            <contributorAffiliation>Institutionelle ZugehÃ¶rigkeit (nur fÃ¼r Personen)</contributorAffiliation>\\n        </contributor>\\n    </contributors>\\n    <title>apache-tomcat-8.0.53-windows-x64.zip</title>\\n    <additionalTitles>\\n        <additionalTitle additionalTitleType=\"TranslatedTitle\">ErgÃ¤nzungen zum Titel des Objektes, z.B. dessen Ã?bersetzung</additionalTitle>\\n    </additionalTitles>\\n    <descriptions>\\n        <description descriptionType=\"Abstract\">Inhaltliche Beschreibung des Objekts, z.B. in Form eines Abstracts. RADAR empfiehlt, diese Beschreibung auf Englisch vorzunehmen.</description>\\n    </descriptions>\\n    <keywords>\\n        <keyword>Schlagworte zur weiteren Charakerisierung des Objekts. FÃ¼r eine optimale Such- und Auffindbarkeit der Daten empfiehlt RADAR, eindeutige Begriffe auf Englisch als Schlagworte zu verwenden - diese sollten sich von den Beschreibungen im Titel bzw. Untertitel unterscheiden.</keyword>\\n    </keywords>\\n    <publishers>\\n        <publisher>Personen oder Organisationen, die dafÃ¼r verantwortlich ist, dass Daten in der gegebenen Form bei RADAR archiviert oder publiziert werden</publisher>\\n    </publishers>\\n    <productionYear>2019</productionYear>\\n    <publicationYear>2019</publicationYear>\\n    <language>deu</language>\\n    <subjectAreas>\\n        <subjectArea>\\n            <controlledSubjectAreaName>Agriculture</controlledSubjectAreaName>\\n        </subjectArea>\\n    </subjectAreas>\\n    <resource resourceType=\"Dataset\"></resource>\\n    <geoLocations>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationBox>\\n                <southWestPoint>\\n                    <latitude>29.612</latitude>\\n                    <longitude>54.668</longitude>\\n                </southWestPoint>\\n                <northEastPoint>\\n                    <latitude>38.29</latitude>\\n                    <longitude>80.728</longitude>\\n                </northEastPoint>\\n            </geoLocationBox>\\n        </geoLocation>\\n        <geoLocation>\\n            <geoLocationCountry>GERMANY</geoLocationCountry>\\n            <geoLocationRegion>Geographische Region, Land oder Ort, an dem Daten erhoben wurden oder auf welchen sich die Daten beziehen</geoLocationRegion>\\n            <geoLocationPoint>\\n                <latitude>50.1136</latitude>\\n                <longitude>9.25087</longitude>\\n            </geoLocationPoint>\\n        </geoLocation>\\n    </geoLocations>\\n    <dataSources>\\n        <dataSource dataSourceDetail=\"Other\">Angabe zu Prozeduren oder Quellen fÃ¼r die Datenerhebung</dataSource>\\n    </dataSources>\\n    <software>\\n        <softwareType type=\"Other\">\\n            <softwareName softwareVersion=\"Version der Software\">Name der Software</softwareName>\\n            <alternativeSoftwareName alternativeSoftwareVersion=\"Version der alternativen Software\">Name der alternativen Software</alternativeSoftwareName>\\n        </softwareType>\\n    </software>\\n    <processing>\\n        <dataProcessing>Optionale Angabe von weiteren, ggf. sekundÃ¤ren Modifikationen an Forschungsdaten, etwa wenn Rohdaten weiter bearbeitet wurden (z.B. Statistik)</dataProcessing>\\n    </processing>\\n    <rights>\\n        <controlledRights>CC BY-NC 4.0 Attribution-NonCommercial</controlledRights>\\n    </rights>\\n    <rightsHolders>\\n        <rightsHolder>Nachname, Vorname oder Name einer Institution</rightsHolder>\\n    </rightsHolders>\\n    <relatedInformations>\\n        <relatedInformation>Optionale Angabe von bestimmten, wichtigen Komponenten, die den Datensatz auszeichnen, z.B.: Database ID, Registrierungsnummer, GenBank, IntEnz, PubChem, MedGen, PMID, PDB, Molecular Formula</relatedInformation>\\n    </relatedInformations>\\n    <fundingReferences>\\n        <fundingReference>\\n            <funderName>DFG</funderName>\\n            <funderIdentifier type=\"CrossRefFunder\">http://dx.doi.org/10.13039/501100001659</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n        <fundingReference>\\n            <funderName>Test</funderName>\\n            <funderIdentifier type=\"ISNI\">033000012150090X</funderIdentifier>\\n            <awardNumber>BE 1042/7-1</awardNumber>\\n            <awardURI>http://gepris.dfg.de/gepris/projekt/237143194</awardURI>\\n            <awardTitle>RADAR Research Data Repositorium</awardTitle>\\n        </fundingReference>\\n    </fundingReferences>\\n</ns2:radarDataset>\\n\"\n" + 
                "    }\n" + 
                "  }]\n" + 
                "}"));
  }
  
  
  private void initGetItemsNoResultData(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/item").withQueryStringParameters(new Parameter("content", Arrays.asList("false")), new Parameter("set", Arrays.asList("empty")) ))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "    \"total\": \"0\",\n" + 
                "    \"offset\": \"0\",\n" + 
                "    \"size\": \"100\",\n" + 
                "    \"data\": []\n" + 
                "}"));
  }
  
  
  private void initGetSpecificFormat(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/format/datacite"))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "  \"metadataPrefix\": \"datacite\",\n" + 
                "  \"schemaLocation\": \"https://schema.datacite.org/meta/kernel-4.0/metadata.xsd\",\n" + 
                "  \"schemaNamespace\": \"http://datacite.org/schema/kernel-4\",\n" + 
                "  \"identifierXpath\": \"/identifier\"\n" + 
                "}"));
    
    serverClient.when(request().withMethod("GET").withPath("/format/oai_dc"))
    .respond(response().withStatusCode(200)
        .withBody("{\n" + 
            "  \"metadataPrefix\": \"oai_dc\",\n" + 
            "  \"schemaLocation\": \"http://www.openarchives.org/OAI/2.0/oai_dc.xsd\",\n" + 
            "  \"schemaNamespace\": \"http://www.openarchives.org/OAI/2.0/oai_dc/\",\n" + 
            "  \"identifierXpath\": \"/identifier\"\n" + 
            "}"));
    
    serverClient.when(request().withMethod("GET").withPath("/format/radar"))
    .respond(response().withStatusCode(200)
        .withBody("{\n" + 
            "  \"metadataPrefix\": \"radar\",\n" + 
            "  \"schemaLocation\": \"https://www.radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\",\n" + 
            "  \"schemaNamespace\": \"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset/\",\n" + 
            "  \"identifierXpath\": \"/identifier\"\n" + 
            "}"));
  }
  
  
  private void initGetAllFormats(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/format"))
        .respond(response().withStatusCode(200)
            .withBody("[{\n" + 
                "  \"metadataPrefix\":\"oaiDc\",\n" + 
                "  \"schemaLocation\":\"http://www.openarchives.org/OAI/2.0/oai_dc.xsd\",\n" + 
                "  \"schemaNamespace\":\"http://www.openarchives.org/OAI/2.0\",\n" + 
                "  \"crosswalkStyleSheet\":\"http://localhost:1080/xslt/radar2oai\", \n" + 
                "  \"identifierXpath\":\"/dc/identifier\"\n" + 
                "},\n" +
                "{\n" + 
                "  \"metadataPrefix\":\"datacite\",\n" + 
                "  \"schemaLocation\":\"http://schema.datacite.org/meta/kernel-4/metadata.xsd\",\n" + 
                "  \"schemaNamespace\":\"http://datacite.org/schema/kernel-4\",\n" + 
                "  \"crosswalkStyleSheet\":\"http://localhost:1080/xslt/radar2datacite\", \n" + 
                "  \"identifierXpath\":\"/resource/identifier\"\n" + 
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
  
  private void initGetAllCrosswalks(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/crosswalk"))
        .respond(response().withStatusCode(200)
            .withBody("[{\n" + 
                "  \"name\": \"Radar2datacite\",\n" + 
                "  \"formatFrom\": \"radar\",\n" + 
                "  \"formatTo\": \"datacite\",\n" + 
                "  \"xsltStylesheet\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n<!--\\nCopyright 2018 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH\\n\\nLicensed under the Apache License, Version 2.0 (the \\\"License\\\");\\nyou may not use this file except in compliance with the License.\\nYou may obtain a copy of the License at\\n\\n    http://www.apache.org/licenses/LICENSE-2.0\\n\\nUnless required by applicable law or agreed to in writing, software\\ndistributed under the License is distributed on an \\\"AS IS\\\" BASIS,\\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\\nSee the License for the specific language governing permissions and\\nlimitations under the License.\\n-->\\n<xsl:stylesheet xmlns:xsl=\\\"http://www.w3.org/1999/XSL/Transform\\\" xmlns=\\\"http://datacite.org/schema/kernel-4\\\" xmlns:rtype=\\\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-types\\\"\\n  xmlns:rad=\\\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\\\" xmlns:re=\\\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\\\"\\n  xmlns:dct=\\\"http://purl.org/dc/terms/\\\" xmlns:dck=\\\"http://datacite.org/schema/kernel-4\\\" xmlns:xd=\\\"http://www.oxygenxml.com/ns/doc/xsl\\\"\\n  exclude-result-prefixes=\\\"#default rtype rad re dct dck xd\\\" version=\\\"1.0\\\">\\n\\n  <xsl:output method=\\\"xml\\\" indent=\\\"yes\\\" />\\n\\n  <xsl:param name=\\\"datasetSize\\\" />\\n  <xsl:param name=\\\"currentYear\\\" />\\n\\n  <xsl:template match=\\\"radarDataset\\\">\\n    <resource xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns=\\\"http://datacite.org/schema/kernel-4\\\"\\n            xsi:schemaLocation=\\\"http://datacite.org/schema/kernel-4 http://schema.datacite.org/meta/kernel-4/metadata.xsd\\\">\\n      <xsl:apply-templates select=\\\"identifier\\\" />\\n      <xsl:apply-templates select=\\\"creators\\\" />\\n      <xsl:call-template name=\\\"titles\\\" />\\n      <xsl:apply-templates select=\\\"publishers\\\" />\\n      <xsl:apply-templates select=\\\"productionYear\\\" />\\n      <xsl:call-template name=\\\"publicationYear\\\" />\\n      <xsl:call-template name=\\\"subjects\\\" />\\n      <xsl:apply-templates select=\\\"resource\\\" />\\n      <xsl:call-template name=\\\"contributors\\\" />\\n      <xsl:call-template name=\\\"descriptions\\\" />\\n      <xsl:apply-templates select=\\\"language\\\" />\\n      <xsl:apply-templates select=\\\"alternateIdentifiers\\\" />\\n      <xsl:apply-templates select=\\\"relatedIdentifiers\\\" />\\n      <xsl:apply-templates select=\\\"geoLocations\\\" />\\n      <xsl:apply-templates select=\\\"fundingReferences\\\" />\\n      <xsl:call-template name=\\\"sizes\\\" />\\n      <xsl:call-template name=\\\"formats\\\" />\\n    </resource>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"identifier\\\">\\n    <xsl:if test=\\\"@identifierType = 'DOI'\\\">\\n      <identifier>\\n        <xsl:attribute name=\\\"identifierType\\\"><xsl:value-of select=\\\"@identifierType\\\" /></xsl:attribute>\\n        <xsl:value-of select=\\\".\\\" />\\n      </identifier>\\n    </xsl:if>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"nameIdentifier\\\">\\n    <xsl:for-each select=\\\"nameIdentifier\\\">\\n      <nameIdentifier schemeURI=\\\"http://orcid.org/\\\" nameIdentifierScheme=\\\"ORCID\\\">\\n        <xsl:value-of select=\\\".\\\" />\\n      </nameIdentifier>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"creators\\\">\\n    <creators>\\n      <xsl:for-each select=\\\"creator\\\">\\n        <creator>\\n          <creatorName>\\n            <xsl:value-of select=\\\"creatorName\\\" />\\n          </creatorName>\\n          <xsl:for-each select=\\\"givenName\\\">\\n            <givenName>\\n              <xsl:value-of select=\\\".\\\" />\\n            </givenName>\\n          </xsl:for-each>\\n          <xsl:for-each select=\\\"familyName\\\">\\n            <familyName>\\n              <xsl:value-of select=\\\".\\\" />\\n            </familyName>\\n          </xsl:for-each>\\n          <xsl:call-template name=\\\"nameIdentifier\\\" />\\n          <affiliation>\\n            <xsl:value-of select=\\\"creatorAffiliation\\\" />\\n          </affiliation>\\n        </creator>\\n      </xsl:for-each>\\n    </creators>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"titles\\\">\\n    <titles>\\n      <xsl:call-template name=\\\"title\\\" />\\n      <xsl:call-template name=\\\"additionalTitle\\\" />\\n    </titles>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"title\\\">\\n    <title>\\n      <xsl:value-of select=\\\"/radarDataset/title\\\" />\\n    </title>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"additionalTitle\\\">\\n    <xsl:for-each select=\\\"/radarDataset/additionalTitles/additionalTitle\\\">\\n      <title>\\n        <xsl:attribute name=\\\"titleType\\\"><xsl:value-of select=\\\"@additionalTitleType\\\" /></xsl:attribute>\\n        <xsl:value-of select=\\\".\\\" />\\n      </title>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n\\n  <xsl:template match=\\\"publishers\\\">\\n    <publisher>\\n      <xsl:for-each select=\\\"publisher\\\">\\n        <xsl:value-of select=\\\"concat(., substring(',', 2 - (position() != last())))\\\" />\\n      </xsl:for-each>\\n    </publisher>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"productionYear\\\">\\n    <dates>\\n      <date>\\n        <xsl:attribute name=\\\"dateType\\\">Created</xsl:attribute>\\n        <xsl:value-of select=\\\".\\\" />\\n      </date>\\n    </dates>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"publicationYear\\\">\\n    <publicationYear>\\n      <xsl:choose>\\n        <xsl:when test=\\\"/radarDataset/publicationYear\\\">\\n          <xsl:value-of select=\\\"/radarDataset/publicationYear\\\" />\\n        </xsl:when>\\n        <xsl:otherwise>\\n          <xsl:value-of select=\\\"$currentYear\\\" />\\n        </xsl:otherwise>\\n      </xsl:choose>\\n    </publicationYear>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"subjects\\\">\\n    <subjects>\\n      <xsl:apply-templates select=\\\"subjectAreas\\\" />\\n      <xsl:apply-templates select=\\\"keywords\\\" />\\n    </subjects>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"subjectAreas\\\">\\n    <xsl:for-each select=\\\"subjectArea\\\">\\n      <subject>\\n        <xsl:choose>\\n          <xsl:when test=\\\"controlledSubjectAreaName != 'Other'\\\">\\n            <xsl:value-of select=\\\"controlledSubjectAreaName\\\" />\\n          </xsl:when>\\n          <xsl:otherwise>\\n            <xsl:value-of select=\\\"additionalSubjectAreaName\\\" />\\n          </xsl:otherwise>\\n        </xsl:choose>\\n      </subject>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"keywords\\\">\\n    <xsl:for-each select=\\\"keyword\\\">\\n      <subject>\\n        <xsl:value-of select=\\\".\\\" />\\n      </subject>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"resource\\\">\\n    <resourceType>\\n      <xsl:attribute name=\\\"resourceTypeGeneral\\\">\\n                <xsl:value-of select=\\\"@resourceType\\\" />\\n            </xsl:attribute>\\n      <xsl:value-of select=\\\".\\\" />\\n    </resourceType>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"contributors\\\">\\n    <contributors>\\n      <xsl:call-template name=\\\"rightsHolder\\\" />\\n      <xsl:call-template name=\\\"contributor\\\" />\\n    </contributors>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"contributor\\\">\\n    <xsl:for-each select=\\\"/radarDataset/contributors/contributor\\\">\\n      <contributor>\\n        <xsl:attribute name=\\\"contributorType\\\"><xsl:value-of select=\\\"@contributorType\\\" /></xsl:attribute>\\n        <contributorName>\\n          <xsl:value-of select=\\\"./contributorName\\\" />\\n        </contributorName>\\n        <xsl:call-template name=\\\"nameIdentifier\\\" />\\n        <affiliation>\\n          <xsl:value-of select=\\\"contributorAffiliation\\\" />\\n        </affiliation>\\n      </contributor>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"rightsHolder\\\">\\n    <xsl:for-each select=\\\"/radarDataset/rightsHolders/rightsHolder\\\">\\n      <contributor>\\n        <xsl:attribute name=\\\"contributorType\\\">RightsHolder</xsl:attribute>\\n        <contributorName>\\n          <xsl:value-of select=\\\".\\\" />\\n        </contributorName>\\n      </contributor>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n\\n  <xsl:template name=\\\"descriptions\\\">\\n    <descriptions>\\n      <xsl:call-template name=\\\"description\\\" />\\n      <xsl:call-template name=\\\"dataSource\\\" />\\n      <xsl:call-template name=\\\"processing\\\" />\\n      <xsl:call-template name=\\\"relatedInformation\\\" />\\n    </descriptions>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"description\\\">\\n    <xsl:for-each select=\\\"/radarDataset/descriptions/description\\\">\\n      <description>\\n        <xsl:choose>\\n          <xsl:when test=\\\"@descriptionType = 'TechnicalRemarks'\\\">\\n            <xsl:attribute name=\\\"descriptionType\\\">Other</xsl:attribute>\\n          </xsl:when>\\n          <xsl:when test=\\\"@descriptionType = 'Object'\\\">\\n            <xsl:attribute name=\\\"descriptionType\\\">Other</xsl:attribute>\\n          </xsl:when>\\n          <xsl:when test=\\\"@descriptionType = 'Method'\\\">\\n            <xsl:attribute name=\\\"descriptionType\\\">Methods</xsl:attribute>\\n          </xsl:when>\\n          <xsl:otherwise>\\n            <xsl:attribute name=\\\"descriptionType\\\"><xsl:value-of select=\\\"@descriptionType\\\" /></xsl:attribute>\\n          </xsl:otherwise>\\n        </xsl:choose>\\n        <xsl:value-of select=\\\".\\\" />\\n      </description>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"dataSource\\\">\\n    <xsl:for-each select=\\\"/radarDataset/dataSources/dataSource\\\">\\n      <description>\\n        <xsl:attribute name=\\\"descriptionType\\\">Methods</xsl:attribute>\\n        <xsl:value-of select=\\\".\\\" />\\n      </description>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"processing\\\">\\n    <xsl:for-each select=\\\"/radarDataset/processing/dataProcessing\\\">\\n      <description>\\n        <xsl:attribute name=\\\"descriptionType\\\">Other</xsl:attribute>\\n        <xsl:value-of select=\\\".\\\" />\\n      </description>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"relatedInformation\\\">\\n    <xsl:for-each select=\\\"/radarDataset/relatedInformations/relatedInformation\\\">\\n      <description>\\n        <xsl:attribute name=\\\"descriptionType\\\">Other</xsl:attribute>\\n        <xsl:value-of select=\\\".\\\" />\\n      </description>\\n    </xsl:for-each>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"alternateIdentifiers\\\">\\n    <alternateIdentifiers>\\n      <xsl:for-each select=\\\"alternateIdentifier\\\">\\n        <alternateIdentifier>\\n          <xsl:attribute name=\\\"alternateIdentifierType\\\">\\n            <xsl:value-of select=\\\"@alternateIdentifierType\\\" />\\n          </xsl:attribute>\\n          <xsl:value-of select=\\\".\\\" />\\n        </alternateIdentifier>\\n      </xsl:for-each>\\n    </alternateIdentifiers>\\n  </xsl:template>\\n\\n\\n  <xsl:template match=\\\"relatedIdentifiers\\\">\\n    <relatedIdentifiers>\\n      <xsl:for-each select=\\\"relatedIdentifier\\\">\\n        <relatedIdentifier>\\n          <xsl:attribute name=\\\"relatedIdentifierType\\\">\\n              <xsl:value-of select=\\\"@relatedIdentifierType\\\" />\\n          </xsl:attribute>\\n          <xsl:attribute name=\\\"relationType\\\">\\n              <xsl:value-of select=\\\"@relationType\\\" />\\n          </xsl:attribute>\\n          <xsl:value-of select=\\\".\\\" />\\n        </relatedIdentifier>\\n      </xsl:for-each>\\n    </relatedIdentifiers>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"geoLocations\\\">\\n    <geoLocations>\\n      <xsl:for-each select=\\\"geoLocation\\\">\\n        <geoLocation>\\n          <xsl:apply-templates select=\\\"geoLocationCountry\\\" />\\n          <xsl:apply-templates select=\\\"geoLocationPoint\\\" />\\n          <xsl:apply-templates select=\\\"geoLocationBox\\\" />\\n        </geoLocation>\\n      </xsl:for-each>\\n    </geoLocations>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"geoLocationCountry\\\">\\n    <geoLocationPlace>\\n      <xsl:value-of select=\\\".\\\" />\\n    </geoLocationPlace>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"geoLocationPoint\\\">\\n    <geoLocationPoint>\\n      <pointLongitude>\\n        <xsl:value-of select=\\\"longitude\\\" />\\n      </pointLongitude>\\n      <pointLatitude>\\n        <xsl:value-of select=\\\"latitude\\\" />\\n      </pointLatitude>\\n    </geoLocationPoint>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"geoLocationBox\\\">\\n    <geoLocationBox>\\n      <westBoundLongitude>\\n        <xsl:value-of select=\\\"southWestPoint/longitude\\\" />\\n      </westBoundLongitude>\\n      <eastBoundLongitude>\\n        <xsl:value-of select=\\\"northEastPoint/longitude\\\" />\\n      </eastBoundLongitude>\\n      <southBoundLatitude>\\n        <xsl:value-of select=\\\"southWestPoint/latitude\\\" />\\n      </southBoundLatitude>\\n      <northBoundLatitude>\\n        <xsl:value-of select=\\\"northEastPoint/latitude\\\" />\\n      </northBoundLatitude>\\n    </geoLocationBox>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"fundingReferences\\\">\\n    <fundingReferences>\\n      <xsl:for-each select=\\\"fundingReference\\\">\\n        <fundingReference>\\n          <funderName>\\n            <xsl:value-of select=\\\"funderName\\\" />\\n          </funderName>\\n\\n          <xsl:if test=\\\"funderIdentifier != ''\\\">\\n            <funderIdentifier>\\n              <xsl:choose>\\n                <xsl:when test=\\\"funderIdentifier/@type = 'CrossRefFunder'\\\">\\n                  <xsl:attribute name=\\\"funderIdentifierType\\\">Crossref Funder ID</xsl:attribute>\\n                </xsl:when>\\n                <xsl:otherwise>\\n                  <xsl:attribute name=\\\"funderIdentifierType\\\"><xsl:value-of select=\\\"funderIdentifier/@type\\\" /></xsl:attribute>\\n                </xsl:otherwise>\\n              </xsl:choose>\\n              <xsl:value-of select=\\\"funderIdentifier\\\" />\\n            </funderIdentifier>\\n          </xsl:if>\\n\\n          <xsl:if test=\\\"awardNumber != ''\\\">\\n            <awardNumber>\\n              <xsl:attribute name=\\\"awardURI\\\">\\n                  <xsl:value-of select=\\\"awardURI\\\" />\\n              </xsl:attribute>\\n              <xsl:value-of select=\\\"awardNumber\\\" />\\n            </awardNumber>\\n          </xsl:if>\\n\\n          <xsl:if test=\\\"awardTitle != ''\\\">\\n            <awardTitle>\\n              <xsl:value-of select=\\\"awardTitle\\\" />\\n            </awardTitle>\\n          </xsl:if>\\n        </fundingReference>\\n      </xsl:for-each>\\n    </fundingReferences>\\n  </xsl:template>\\n\\n  <xsl:template match=\\\"language\\\">\\n    <language>\\n      <xsl:choose>\\n        <xsl:when test=\\\". = 'aar'\\\">\\n          aa\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'abk'\\\">\\n          ab\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ave'\\\">\\n          ae\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'afr'\\\">\\n          af\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'aka'\\\">\\n          ak\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'amh'\\\">\\n          am\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'arg'\\\">\\n          an\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ara'\\\">\\n          ar\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'asm'\\\">\\n          as\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ava'\\\">\\n          av\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'aym'\\\">\\n          ay\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'aze'\\\">\\n          az\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bak'\\\">\\n          ba\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bel'\\\">\\n          be\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bul'\\\">\\n          bg\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bis'\\\">\\n          bi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bam'\\\">\\n          bm\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ben'\\\">\\n          bn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bod'\\\">\\n          bo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bre'\\\">\\n          br\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'bos'\\\">\\n          bs\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'cat'\\\">\\n          ca\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'che'\\\">\\n          ce\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'cha'\\\">\\n          ch\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'cos'\\\">\\n          co\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'cre'\\\">\\n          cr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ces'\\\">\\n          cs\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'chu'\\\">\\n          cu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'chv'\\\">\\n          cv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'cym'\\\">\\n          cy\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'dan'\\\">\\n          da\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'deu'\\\">\\n          de\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'div'\\\">\\n          dv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'dzo'\\\">\\n          dz\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ewe'\\\">\\n          ee\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ell'\\\">\\n          el\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'eng'\\\">\\n          en\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'epo'\\\">\\n          eo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'spa'\\\">\\n          es\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'est'\\\">\\n          et\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'eus'\\\">\\n          eu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'fas'\\\">\\n          fa\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ful'\\\">\\n          ff\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'fin'\\\">\\n          fi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'fij'\\\">\\n          fj\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'fao'\\\">\\n          fo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'fra'\\\">\\n          fr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'fry'\\\">\\n          fy\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'gle'\\\">\\n          ga\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'gla'\\\">\\n          gd\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'glg'\\\">\\n          gl\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'grn'\\\">\\n          gn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'guj'\\\">\\n          gu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'glv'\\\">\\n          gv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hau'\\\">\\n          ha\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'heb'\\\">\\n          he\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hin'\\\">\\n          hi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hmo'\\\">\\n          ho\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hrv'\\\">\\n          hr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hat'\\\">\\n          ht\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hun'\\\">\\n          hu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'hye'\\\">\\n          hy\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'her'\\\">\\n          hz\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ina'\\\">\\n          ia\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ind'\\\">\\n          id\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ile'\\\">\\n          ie\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ibo'\\\">\\n          ig\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'iii'\\\">\\n          ii\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ipk'\\\">\\n          ik\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ido'\\\">\\n          io\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'isl'\\\">\\n          is\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ita'\\\">\\n          it\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'iku'\\\">\\n          iu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'jpn'\\\">\\n          ja\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'jav'\\\">\\n          jv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kat'\\\">\\n          ka\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kon'\\\">\\n          kg\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kik'\\\">\\n          ki\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kua'\\\">\\n          kj\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kaz'\\\">\\n          kk\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kal'\\\">\\n          kl\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'khm'\\\">\\n          km\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kan'\\\">\\n          kn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kor'\\\">\\n          ko\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kau'\\\">\\n          kr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kas'\\\">\\n          ks\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kur'\\\">\\n          ku\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kom'\\\">\\n          kv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'cor'\\\">\\n          kw\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kir'\\\">\\n          ky\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lat'\\\">\\n          la\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ltz'\\\">\\n          lb\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lug'\\\">\\n          lg\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lim'\\\">\\n          li\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lin'\\\">\\n          ln\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lao'\\\">\\n          lo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lit'\\\">\\n          lt\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lub'\\\">\\n          lu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'lav'\\\">\\n          lv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mlg'\\\">\\n          mg\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mah'\\\">\\n          mh\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mri'\\\">\\n          mi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mkd'\\\">\\n          mk\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mal'\\\">\\n          ml\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mon'\\\">\\n          mn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mar'\\\">\\n          mr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'msa'\\\">\\n          ms\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mlt'\\\">\\n          mt\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'mya'\\\">\\n          my\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nau'\\\">\\n          na\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nob'\\\">\\n          nb\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nde'\\\">\\n          nd\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nep'\\\">\\n          ne\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ndo'\\\">\\n          ng\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nld'\\\">\\n          nl\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nno'\\\">\\n          nn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nor'\\\">\\n          no\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nbl'\\\">\\n          nr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nav'\\\">\\n          nv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'nya'\\\">\\n          ny\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'oci'\\\">\\n          oc\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'oji'\\\">\\n          oj\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'orm'\\\">\\n          om\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ori'\\\">\\n          or\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'oss'\\\">\\n          os\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'pan'\\\">\\n          pa\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'pli'\\\">\\n          pi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'pol'\\\">\\n          pl\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'pus'\\\">\\n          ps\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'por'\\\">\\n          pt\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'que'\\\">\\n          qu\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'roh'\\\">\\n          rm\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'run'\\\">\\n          rn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ron'\\\">\\n          ro\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'rus'\\\">\\n          ru\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'kin'\\\">\\n          rw\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'san'\\\">\\n          sa\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'srd'\\\">\\n          sc\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'snd'\\\">\\n          sd\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sme'\\\">\\n          se\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sag'\\\">\\n          sg\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sin'\\\">\\n          si\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'slk'\\\">\\n          sk\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'slv'\\\">\\n          sl\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'smo'\\\">\\n          sm\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sna'\\\">\\n          sn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'som'\\\">\\n          so\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sqi'\\\">\\n          sq\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'srp'\\\">\\n          sr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ssw'\\\">\\n          ss\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sot'\\\">\\n          st\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'sun'\\\">\\n          su\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'swe'\\\">\\n          sv\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'swa'\\\">\\n          sw\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tam'\\\">\\n          ta\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tel'\\\">\\n          te\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tgk'\\\">\\n          tg\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tha'\\\">\\n          th\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tir'\\\">\\n          ti\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tuk'\\\">\\n          tk\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tgl'\\\">\\n          tl\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tsn'\\\">\\n          tn\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ton'\\\">\\n          to\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tur'\\\">\\n          tr\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tso'\\\">\\n          ts\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tat'\\\">\\n          tt\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'twi'\\\">\\n          tw\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'tah'\\\">\\n          ty\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'uig'\\\">\\n          ug\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ukr'\\\">\\n          uk\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'uzb'\\\">\\n          uz\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'ven'\\\">\\n          ve\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'vie'\\\">\\n          vi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'vol'\\\">\\n          vo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'wln'\\\">\\n          wa\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'wol'\\\">\\n          wo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'xho'\\\">\\n          xh\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'yid'\\\">\\n          yi\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'yor'\\\">\\n          yo\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'zha'\\\">\\n          za\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'zho'\\\">\\n          zh\\n        </xsl:when>\\n        <xsl:when test=\\\". = 'zul'\\\">\\n          zu\\n        </xsl:when>\\n      </xsl:choose>\\n    </language>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"sizes\\\">\\n    <sizes>\\n      <size>\\n        <xsl:value-of select=\\\"$datasetSize\\\" />\\n      </size>\\n    </sizes>\\n  </xsl:template>\\n\\n  <xsl:template name=\\\"formats\\\">\\n    <formats>\\n      <format>application/zip</format>\\n    </formats>\\n  </xsl:template>\\n\\n</xsl:stylesheet>\\n\"\n" + 
                "}, {\n" + 
                "  \"name\": \"Radar2OAI_DC_v09\",\n" + 
                "  \"formatFrom\": \"radar\",\n" + 
                "  \"formatTo\": \"oai_dc\",\n" + 
                "  \"xsltStylesheet\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n<!--\\nCopyright 2018 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH\\n\\nLicensed under the Apache License, Version 2.0 (the \\\"License\\\");\\nyou may not use this file except in compliance with the License.\\nYou may obtain a copy of the License at\\n\\n    http://www.apache.org/licenses/LICENSE-2.0\\n\\nUnless required by applicable law or agreed to in writing, software\\ndistributed under the License is distributed on an \\\"AS IS\\\" BASIS,\\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\\nSee the License for the specific language governing permissions and\\nlimitations under the License.\\n-->\\n<xsl:stylesheet xmlns:xsl=\\\"http://www.w3.org/1999/XSL/Transform\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns:oai_dc=\\\"http://www.openarchives.org/OAI/2.0/oai_dc/\\\" xmlns:dc=\\\"http://purl.org/dc/elements/1.1/\\\" version=\\\"1.0\\\">\\n\\n  <xsl:output method=\\\"xml\\\" indent=\\\"yes\\\"/>\\n  \\n  <!-- Used in xsl files for later versions to apply the corresponding layout per condition -->\\n  <xsl:template match=\\\"*\\\">\\n    <xsl:for-each select=\\\"namespace::*\\\">\\n      <namespace><xsl:value-of select=\\\"local-name()\\\"/>:<xsl:value-of select=\\\".\\\"/></namespace>\\n      <xsl:if test=\\\"local-name() = ''\\\">\\n        <xsl:value-of select=\\\".\\\"/>\\n      </xsl:if>\\n    </xsl:for-each>  \\n  </xsl:template>\\n  \\n  <xsl:template match=\\\"/\\\">\\n    <!-- Check for radar version and apply the corresponding template -->\\n    <xsl:variable name=\\\"namespace\\\">\\n      <xsl:apply-templates select=\\\"*\\\"/>\\n    </xsl:variable>\\n    <xsl:choose>\\n      <xsl:when test=\\\"contains($namespace, '/v09/')\\\">\\n        <xsl:call-template name=\\\"radar-v09\\\"/>\\n      </xsl:when>\\n      <xsl:when test=\\\"contains($namespace, '/v08/')\\\">\\n        <xsl:call-template name=\\\"radar-v08\\\"/>\\n      </xsl:when>\\n      <xsl:otherwise>\\n        <xsl:call-template name=\\\"radar\\\"/>\\n      </xsl:otherwise>\\n    </xsl:choose>\\n  </xsl:template>\\n  \\n  <xsl:template name=\\\"radar-v09\\\">\\n    <oai_dc:dc xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai_dc/ oai_dc.xsd\\\">\\n      <dc:identifier>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='identifier']\\\"/>\\n      </dc:identifier>\\n      <dc:identifier>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='identifier']/@identifierType\\\"/>\\n      </dc:identifier>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='creators']/*[local-name()='creator']\\\">\\n        <dc:creator>\\n          <xsl:value-of select=\\\"*[local-name()='creatorName']\\\"/>\\n        </dc:creator>\\n        <xsl:if test=\\\"*[local-name()='nameIdentifier']\\\">\\n          <dc:identifier>\\n            <xsl:value-of select=\\\"*[local-name()='nameIdentifier']\\\"/>\\n          </dc:identifier>\\n        </xsl:if>\\n      </xsl:for-each>\\n      <dc:title>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='title']\\\"/>\\n      </dc:title>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='publishers']\\\">\\n        <dc:publisher>\\n          <xsl:value-of select=\\\"*[local-name()='publisher']\\\"/>\\n        </dc:publisher>\\n      </xsl:for-each>\\n      <dc:date>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='publicationYear']\\\"/>\\n      </dc:date>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='subjectAreas']/*[local-name()='subjectArea']/*\\\">\\n        <dc:subject>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:subject>\\n      </xsl:for-each>\\n      <dc:type>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='resource']\\\"/>\\n      </dc:type>\\n      <dc:type>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='resource']/@resourceType\\\"/>\\n      </dc:type>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='rights']/*\\\">\\n        <dc:rights>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:rights>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='rightsHolders']/*[local-name()='rightsHolder']\\\">\\n        <dc:rights>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:rights>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='contributors']/*[local-name()='contributor']\\\">\\n        <dc:contributor>\\n          <xsl:value-of select=\\\"*[local-name()='contributorName']\\\"/>\\n        </dc:contributor>\\n        <xsl:if test=\\\"*[local-name()='nameIdentifier']\\\">\\n          <dc:identifier>\\n            <xsl:value-of select=\\\"*[local-name()='nameIdentifier']\\\"/>\\n          </dc:identifier>\\n        </xsl:if>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='descriptions']/*[local-name()='description']\\\">\\n        <dc:description>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:description>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='keywords']/*[local-name()='keyword']\\\">\\n        <dc:description>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:description>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='contributors']/*[local-name()='contributor']\\\">\\n        <dc:contributor>\\n          <xsl:value-of select=\\\"*[local-name()='contributorName']\\\"/>\\n        </dc:contributor>\\n      </xsl:for-each>\\n      <dc:language>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='language']\\\"/>\\n      </dc:language>\\n      <xsl:if test=\\\"*[local-name()='radarDataset']/*[local-name()='alternateIdentifiers']/*[local-name()='alternateIdentifier']\\\">\\n        <dc:identifier>\\n          <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='alternateIdentifiers']/*[local-name()='alternateIdentifier']\\\"/>\\n        </dc:identifier>\\n        <dc:identifier>\\n          <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='alternateIdentifiers']/*[local-name()='alternateIdentifier']/@alternateIdentifierType\\\"/>\\n        </dc:identifier>\\n      </xsl:if>\\n      <xsl:if test=\\\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']\\\">\\n        <dc:relation>\\n          <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']\\\"/>\\n        </dc:relation>\\n        <dc:identifier>\\n          <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']\\\"/>\\n        </dc:identifier>\\n        <dc:relation>\\n          <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']/@relatedIdentifierType\\\"/>\\n        </dc:relation>\\n        <dc:identifier>\\n          <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='relatedIdentifiers']/*[local-name()='relatedIdentifier']/@relatedIdentifierType\\\"/>\\n        </dc:identifier>\\n      </xsl:if>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='geoLocations']/*[local-name()='geoLocation']\\\">\\n        <dc:coverage>\\n          <xsl:choose>\\n            <xsl:when test=\\\"*[local-name()='geoLocationCountry'] and *[local-name()='geoLocationRegion']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationCountry']\\\"/>\\n              <xsl:text> (</xsl:text>\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationRegion']\\\"/>\\n              <xsl:text>)</xsl:text>\\n            </xsl:when>\\n            <xsl:when test=\\\"*[local-name()='geoLocationCountry']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationCountry']\\\"/>\\n            </xsl:when>\\n            <xsl:when test=\\\"*[local-name()='geoLocationRegion']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationRegion']\\\"/>\\n            </xsl:when>\\n          </xsl:choose>\\n          <xsl:if test=\\\"*[local-name()='geoLocationPoint'] or *[local-name()='geoLocationBox']\\\">\\n            <xsl:text>: </xsl:text>\\n            <xsl:choose>\\n              <xsl:when test=\\\"*[local-name()='geoLocationBox']\\\">\\n                <xsl:text>Lat/Long/Datum </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='longitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='longitude']\\\"/>\\n              </xsl:when>\\n              <xsl:when test=\\\"*[local-name()='geoLocationPoint']\\\">\\n                <xsl:text>Lat/Long/Datum </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationPoint']/*[local-name()='longitude']\\\"/>\\n              </xsl:when>\\n            </xsl:choose>\\n            <xsl:text> (WGS 84)</xsl:text>\\n          </xsl:if>\\n        </dc:coverage>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='fundingReferences']/*[local-name()='fundingReference']\\\">\\n        <xsl:if test=\\\"*[local-name()='funderIdentifier']\\\">\\n          <dc:identifier>\\n            <xsl:value-of select=\\\"*[local-name()='funderIdentifier']\\\"/>\\n          </dc:identifier>\\n        </xsl:if>\\n      </xsl:for-each>\\n      <dc:format>application/zip</dc:format>\\n    </oai_dc:dc>\\n  </xsl:template>\\n  \\n  <xsl:template name=\\\"radar-v08\\\">\\n    <oai_dc:dc xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\\\">\\n      <dc:identifier>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='identifier']\\\"/>\\n      </dc:identifier>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='creators']/*[local-name()='creator']\\\">\\n        <dc:creator>\\n          <xsl:value-of select=\\\"*[local-name()='creatorName']\\\"/>\\n        </dc:creator>\\n      </xsl:for-each>\\n      <dc:title>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='title']\\\"/>\\n      </dc:title>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='publishers']\\\">\\n        <dc:publisher>\\n          <xsl:value-of select=\\\"*[local-name()='publisher']\\\"/>\\n        </dc:publisher>\\n      </xsl:for-each>\\n      <dc:date>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='publicationYear']\\\"/>\\n      </dc:date>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='subjectAreas']/*[local-name()='subjectArea']/*\\\">\\n        <dc:subject>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:subject>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='rights']/*\\\">\\n        <dc:rights>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:rights>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='descriptions']/*[local-name()='description']\\\">\\n        <dc:description>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:description>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='keywords']/*[local-name()='keyword']\\\">\\n        <dc:description>\\n          <xsl:value-of select=\\\".\\\"/>\\n        </dc:description>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='contributors']\\\">\\n        <dc:contributor>\\n          <xsl:value-of select=\\\"*[local-name()='contributor']\\\"/>\\n        </dc:contributor>\\n      </xsl:for-each>\\n      <dc:language>\\n        <xsl:value-of select=\\\"*[local-name()='radarDataset']/*[local-name()='language']\\\"/>\\n      </dc:language>\\n      <xsl:for-each select=\\\"*[local-name()='radarDataset']/*[local-name()='geoLocations']/*[local-name()='geoLocation']\\\">\\n        <dc:coverage>\\n          <xsl:choose>\\n            <xsl:when test=\\\"*[local-name()='geoLocationCountry'] and *[local-name()='geoLocationRegion']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationCountry']\\\"/>\\n              <xsl:text> (</xsl:text>\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationRegion']\\\"/>\\n              <xsl:text>)</xsl:text>\\n            </xsl:when>\\n            <xsl:when test=\\\"*[local-name()='geoLocationCountry']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationCountry']\\\"/>\\n            </xsl:when>\\n            <xsl:when test=\\\"*[local-name()='geoLocationRegion']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationRegion']\\\"/>\\n            </xsl:when>\\n          </xsl:choose>\\n          <xsl:if test=\\\"*[local-name()='geoLocationPoint'] or *[local-name()='geoLocationBox']\\\">\\n            <xsl:text>: </xsl:text>\\n            <xsl:choose>\\n              <xsl:when test=\\\"*[local-name()='geoLocationBox']\\\">\\n                <xsl:text>Lat/Long/Datum </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='longitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='longitude']\\\"/>\\n              </xsl:when>\\n              <xsl:when test=\\\"*[local-name()='geoLocationPoint']\\\">\\n                <xsl:text>Lat/Long/Datum </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationPoint']/*[local-name()='longitude']\\\"/>\\n              </xsl:when>\\n            </xsl:choose>\\n            <xsl:text> (WGS 84)</xsl:text>\\n          </xsl:if>\\n        </dc:coverage>\\n      </xsl:for-each>\\n    </oai_dc:dc>\\n  </xsl:template>\\n  \\n  <xsl:template name=\\\"radar\\\">\\n    <oai_dc:dc xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\\\">\\n      <dc:identifier>\\n        <xsl:value-of select=\\\"*[local-name()='radar']/*[local-name()='identifier']\\\"/>\\n      </dc:identifier>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='creators']/*[local-name()='creator']\\\">\\n        <dc:creator>\\n          <xsl:value-of select=\\\"*[local-name()='creatorName']\\\"/>\\n        </dc:creator>\\n      </xsl:for-each>\\n      <dc:title>\\n        <xsl:value-of select=\\\"*[local-name()='radar']/*[local-name()='title']\\\"/>\\n      </dc:title>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='publishers']\\\">\\n        <dc:publisher>\\n          <xsl:value-of select=\\\"*[local-name()='publisher']\\\"/>\\n        </dc:publisher>\\n      </xsl:for-each>\\n      <dc:date>\\n        <xsl:value-of select=\\\"*[local-name()='radar']/*[local-name()='publicationYear']\\\"/>\\n      </dc:date>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='subjectAreas']\\\">\\n        <xsl:for-each select=\\\"*[local-name()='subjectArea']/*\\\">\\n          <dc:subject>\\n            <xsl:value-of select=\\\".\\\"/>\\n          </dc:subject>\\n        </xsl:for-each>\\n      </xsl:for-each>\\n      <dc:rights>\\n        <xsl:value-of select=\\\"*[local-name()='radar']/*[local-name()='rights']\\\"/>\\n      </dc:rights>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='descriptions']\\\">\\n        <dc:description>\\n          <xsl:value-of select=\\\"*[local-name()='description']\\\"/>\\n        </dc:description>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='keywords']\\\">\\n        <dc:description>\\n          <xsl:value-of select=\\\"*[local-name()='keyword']\\\"/>\\n        </dc:description>\\n      </xsl:for-each>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='contributors']\\\">\\n        <dc:contributor>\\n          <xsl:value-of select=\\\"*[local-name()='contributor']\\\"/>\\n        </dc:contributor>\\n      </xsl:for-each>\\n      <dc:language>\\n        <xsl:value-of select=\\\"*[local-name()='radar']/*[local-name()='language']\\\"/>\\n      </dc:language>\\n      <xsl:for-each select=\\\"*[local-name()='radar']/*[local-name()='geoLocations']/*[local-name()='geoLocation']\\\">\\n        <dc:coverage>\\n          <xsl:choose>\\n            <xsl:when test=\\\"*[local-name()='geoLocationCountry'] and *[local-name()='geoLocationRegion']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationCountry']\\\"/>\\n              <xsl:text> (</xsl:text>\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationRegion']\\\"/>\\n              <xsl:text>)</xsl:text>\\n            </xsl:when>\\n            <xsl:when test=\\\"*[local-name()='geoLocationCountry']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationCountry']\\\"/>\\n            </xsl:when>\\n            <xsl:when test=\\\"*[local-name()='geoLocationRegion']\\\">\\n              <xsl:value-of select=\\\"*[local-name()='geoLocationRegion']\\\"/>\\n            </xsl:when>\\n          </xsl:choose>\\n          <xsl:if test=\\\"*[local-name()='geoLocationPoint'] or *[local-name()='geoLocationBox']\\\">\\n            <xsl:text>: </xsl:text>\\n            <xsl:choose>\\n              <xsl:when test=\\\"*[local-name()='geoLocationBox']\\\">\\n                <xsl:text>Lat/Long/Datum </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='southWestPoint']/*[local-name()='longitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationBox']/*[local-name()='northEastPoint']/*[local-name()='longitude']\\\"/>\\n              </xsl:when>\\n              <xsl:when test=\\\"*[local-name()='geoLocationPoint']\\\">\\n                <xsl:text>Lat/Long/Datum </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationPoint']/*[local-name()='latitude']\\\"/>\\n                <xsl:text> </xsl:text>\\n                <xsl:value-of select=\\\"*[local-name()='geoLocationPoint']/*[local-name()='longitude']\\\"/>\\n              </xsl:when>\\n            </xsl:choose>\\n            <xsl:text> (WGS 84)</xsl:text>\\n          </xsl:if>\\n        </dc:coverage>\\n      </xsl:for-each>\\n    </oai_dc:dc>\\n  </xsl:template>\\n  \\n</xsl:stylesheet>\\n\"\n" + 
                "}]"));
  }
  
  private void initGetSpecificSet(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/set/Deutsche%20Fotothek"))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + 
                "  \"name\":\"Deutsche Fotothek\",\n" + 
                "  \"searchUrl\":\"https://www.deutsche-digitale-bibliothek.de/searchresults?isThumbnailFiltered=true&query=&facetValues%5B%5D=provider_fct%3DDeutsche+Fotothek&offset=0\",\n" + 
                "  \"identifierCssSelector\":\"\"\n" + 
                "}"));
  }
  
  private void initGetAllSets(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/set"))
        .respond(response().withStatusCode(200)
            .withBody("[\n" + 
                "{\n" + 
                "  \"name\":\"Deutsche Fotothek\",\n" +
                "  \"spec\":\"institution\",\n" +
                "  \"description\":\"Description for Deutsche Fotothek\"\n" +
                "}\n" + 
                ",\n" + 
                "{\n" + 
                "  \"name\":\"TIB\",\n" +
                "  \"spec\":\"institution:hannover\",\n" +
                "  \"description\":\"Description for TIB\"\n" +
                "}\n" + 
                "]"));
  }
  
  
  private void initGetXslt(MockServerClient serverClient) {
    
    serverClient.when(request().withMethod("GET").withPath("/xslt/radar2datacite"))
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
            "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns=\"http://datacite.org/schema/kernel-4\" xmlns:rtype=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-types\"\n" + 
            "  xmlns:rad=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-dataset\" xmlns:re=\"http://radar-service.eu/schemas/descriptive/radar/v09/radar-elements\"\n" + 
            "  xmlns:dct=\"http://purl.org/dc/terms/\" xmlns:dck=\"http://datacite.org/schema/kernel-4\" xmlns:xd=\"http://www.oxygenxml.com/ns/doc/xsl\"\n" + 
            "  exclude-result-prefixes=\"#default rtype rad re dct dck xd\" version=\"1.0\">\n" + 
            "\n" + 
            "  <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
            "\n" + 
            "  <xsl:param name=\"datasetSize\" />\n" + 
            "  <xsl:param name=\"currentYear\" />\n" + 
            "\n" + 
            "  <xsl:template match=\"radarDataset\">\n" + 
            "    <resource xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://datacite.org/schema/kernel-4\"\n" + 
            "            xsi:schemaLocation=\"http://datacite.org/schema/kernel-4 http://schema.datacite.org/meta/kernel-4/metadata.xsd\">\n" + 
            "      <xsl:apply-templates select=\"identifier\" />\n" + 
            "      <xsl:apply-templates select=\"creators\" />\n" + 
            "      <xsl:call-template name=\"titles\" />\n" + 
            "      <xsl:apply-templates select=\"publishers\" />\n" + 
            "      <xsl:apply-templates select=\"productionYear\" />\n" + 
            "      <xsl:call-template name=\"publicationYear\" />\n" + 
            "      <xsl:call-template name=\"subjects\" />\n" + 
            "      <xsl:apply-templates select=\"resource\" />\n" + 
            "      <xsl:call-template name=\"contributors\" />\n" + 
            "      <xsl:call-template name=\"descriptions\" />\n" + 
            "      <xsl:apply-templates select=\"language\" />\n" + 
            "      <xsl:apply-templates select=\"alternateIdentifiers\" />\n" + 
            "      <xsl:apply-templates select=\"relatedIdentifiers\" />\n" + 
            "      <xsl:apply-templates select=\"geoLocations\" />\n" + 
            "      <xsl:apply-templates select=\"fundingReferences\" />\n" + 
            "      <xsl:call-template name=\"sizes\" />\n" + 
            "      <xsl:call-template name=\"formats\" />\n" + 
            "    </resource>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"identifier\">\n" + 
            "    <xsl:if test=\"@identifierType = 'DOI'\">\n" + 
            "      <identifier>\n" + 
            "        <xsl:attribute name=\"identifierType\"><xsl:value-of select=\"@identifierType\" /></xsl:attribute>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </identifier>\n" + 
            "    </xsl:if>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"nameIdentifier\">\n" + 
            "    <xsl:for-each select=\"nameIdentifier\">\n" + 
            "      <nameIdentifier schemeURI=\"http://orcid.org/\" nameIdentifierScheme=\"ORCID\">\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </nameIdentifier>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"creators\">\n" + 
            "    <creators>\n" + 
            "      <xsl:for-each select=\"creator\">\n" + 
            "        <creator>\n" + 
            "          <creatorName>\n" + 
            "            <xsl:value-of select=\"creatorName\" />\n" + 
            "          </creatorName>\n" + 
            "          <xsl:for-each select=\"givenName\">\n" + 
            "            <givenName>\n" + 
            "              <xsl:value-of select=\".\" />\n" + 
            "            </givenName>\n" + 
            "          </xsl:for-each>\n" + 
            "          <xsl:for-each select=\"familyName\">\n" + 
            "            <familyName>\n" + 
            "              <xsl:value-of select=\".\" />\n" + 
            "            </familyName>\n" + 
            "          </xsl:for-each>\n" + 
            "          <xsl:call-template name=\"nameIdentifier\" />\n" + 
            "          <affiliation>\n" + 
            "            <xsl:value-of select=\"creatorAffiliation\" />\n" + 
            "          </affiliation>\n" + 
            "        </creator>\n" + 
            "      </xsl:for-each>\n" + 
            "    </creators>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"titles\">\n" + 
            "    <titles>\n" + 
            "      <xsl:call-template name=\"title\" />\n" + 
            "      <xsl:call-template name=\"additionalTitle\" />\n" + 
            "    </titles>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"title\">\n" + 
            "    <title>\n" + 
            "      <xsl:value-of select=\"/radarDataset/title\" />\n" + 
            "    </title>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"additionalTitle\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/additionalTitles/additionalTitle\">\n" + 
            "      <title>\n" + 
            "        <xsl:attribute name=\"titleType\"><xsl:value-of select=\"@additionalTitleType\" /></xsl:attribute>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </title>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "\n" + 
            "  <xsl:template match=\"publishers\">\n" + 
            "    <publisher>\n" + 
            "      <xsl:for-each select=\"publisher\">\n" + 
            "        <xsl:value-of select=\"concat(., substring(',', 2 - (position() != last())))\" />\n" + 
            "      </xsl:for-each>\n" + 
            "    </publisher>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"productionYear\">\n" + 
            "    <dates>\n" + 
            "      <date>\n" + 
            "        <xsl:attribute name=\"dateType\">Created</xsl:attribute>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </date>\n" + 
            "    </dates>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"publicationYear\">\n" + 
            "    <publicationYear>\n" + 
            "      <xsl:choose>\n" + 
            "        <xsl:when test=\"/radarDataset/publicationYear\">\n" + 
            "          <xsl:value-of select=\"/radarDataset/publicationYear\" />\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:otherwise>\n" + 
            "          <xsl:value-of select=\"$currentYear\" />\n" + 
            "        </xsl:otherwise>\n" + 
            "      </xsl:choose>\n" + 
            "    </publicationYear>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"subjects\">\n" + 
            "    <subjects>\n" + 
            "      <xsl:apply-templates select=\"subjectAreas\" />\n" + 
            "      <xsl:apply-templates select=\"keywords\" />\n" + 
            "    </subjects>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"subjectAreas\">\n" + 
            "    <xsl:for-each select=\"subjectArea\">\n" + 
            "      <subject>\n" + 
            "        <xsl:choose>\n" + 
            "          <xsl:when test=\"controlledSubjectAreaName != 'Other'\">\n" + 
            "            <xsl:value-of select=\"controlledSubjectAreaName\" />\n" + 
            "          </xsl:when>\n" + 
            "          <xsl:otherwise>\n" + 
            "            <xsl:value-of select=\"additionalSubjectAreaName\" />\n" + 
            "          </xsl:otherwise>\n" + 
            "        </xsl:choose>\n" + 
            "      </subject>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"keywords\">\n" + 
            "    <xsl:for-each select=\"keyword\">\n" + 
            "      <subject>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </subject>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"resource\">\n" + 
            "    <resourceType>\n" + 
            "      <xsl:attribute name=\"resourceTypeGeneral\">\n" + 
            "                <xsl:value-of select=\"@resourceType\" />\n" + 
            "            </xsl:attribute>\n" + 
            "      <xsl:value-of select=\".\" />\n" + 
            "    </resourceType>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"contributors\">\n" + 
            "    <contributors>\n" + 
            "      <xsl:call-template name=\"rightsHolder\" />\n" + 
            "      <xsl:call-template name=\"contributor\" />\n" + 
            "    </contributors>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"contributor\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/contributors/contributor\">\n" + 
            "      <contributor>\n" + 
            "        <xsl:attribute name=\"contributorType\"><xsl:value-of select=\"@contributorType\" /></xsl:attribute>\n" + 
            "        <contributorName>\n" + 
            "          <xsl:value-of select=\"./contributorName\" />\n" + 
            "        </contributorName>\n" + 
            "        <xsl:call-template name=\"nameIdentifier\" />\n" + 
            "        <affiliation>\n" + 
            "          <xsl:value-of select=\"contributorAffiliation\" />\n" + 
            "        </affiliation>\n" + 
            "      </contributor>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"rightsHolder\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/rightsHolders/rightsHolder\">\n" + 
            "      <contributor>\n" + 
            "        <xsl:attribute name=\"contributorType\">RightsHolder</xsl:attribute>\n" + 
            "        <contributorName>\n" + 
            "          <xsl:value-of select=\".\" />\n" + 
            "        </contributorName>\n" + 
            "      </contributor>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "\n" + 
            "  <xsl:template name=\"descriptions\">\n" + 
            "    <descriptions>\n" + 
            "      <xsl:call-template name=\"description\" />\n" + 
            "      <xsl:call-template name=\"dataSource\" />\n" + 
            "      <xsl:call-template name=\"processing\" />\n" + 
            "      <xsl:call-template name=\"relatedInformation\" />\n" + 
            "    </descriptions>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"description\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/descriptions/description\">\n" + 
            "      <description>\n" + 
            "        <xsl:choose>\n" + 
            "          <xsl:when test=\"@descriptionType = 'TechnicalRemarks'\">\n" + 
            "            <xsl:attribute name=\"descriptionType\">Other</xsl:attribute>\n" + 
            "          </xsl:when>\n" + 
            "          <xsl:when test=\"@descriptionType = 'Object'\">\n" + 
            "            <xsl:attribute name=\"descriptionType\">Other</xsl:attribute>\n" + 
            "          </xsl:when>\n" + 
            "          <xsl:when test=\"@descriptionType = 'Method'\">\n" + 
            "            <xsl:attribute name=\"descriptionType\">Methods</xsl:attribute>\n" + 
            "          </xsl:when>\n" + 
            "          <xsl:otherwise>\n" + 
            "            <xsl:attribute name=\"descriptionType\"><xsl:value-of select=\"@descriptionType\" /></xsl:attribute>\n" + 
            "          </xsl:otherwise>\n" + 
            "        </xsl:choose>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </description>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"dataSource\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/dataSources/dataSource\">\n" + 
            "      <description>\n" + 
            "        <xsl:attribute name=\"descriptionType\">Methods</xsl:attribute>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </description>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"processing\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/processing/dataProcessing\">\n" + 
            "      <description>\n" + 
            "        <xsl:attribute name=\"descriptionType\">Other</xsl:attribute>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </description>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"relatedInformation\">\n" + 
            "    <xsl:for-each select=\"/radarDataset/relatedInformations/relatedInformation\">\n" + 
            "      <description>\n" + 
            "        <xsl:attribute name=\"descriptionType\">Other</xsl:attribute>\n" + 
            "        <xsl:value-of select=\".\" />\n" + 
            "      </description>\n" + 
            "    </xsl:for-each>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"alternateIdentifiers\">\n" + 
            "    <alternateIdentifiers>\n" + 
            "      <xsl:for-each select=\"alternateIdentifier\">\n" + 
            "        <alternateIdentifier>\n" + 
            "          <xsl:attribute name=\"alternateIdentifierType\">\n" + 
            "            <xsl:value-of select=\"@alternateIdentifierType\" />\n" + 
            "          </xsl:attribute>\n" + 
            "          <xsl:value-of select=\".\" />\n" + 
            "        </alternateIdentifier>\n" + 
            "      </xsl:for-each>\n" + 
            "    </alternateIdentifiers>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "\n" + 
            "  <xsl:template match=\"relatedIdentifiers\">\n" + 
            "    <relatedIdentifiers>\n" + 
            "      <xsl:for-each select=\"relatedIdentifier\">\n" + 
            "        <relatedIdentifier>\n" + 
            "          <xsl:attribute name=\"relatedIdentifierType\">\n" + 
            "              <xsl:value-of select=\"@relatedIdentifierType\" />\n" + 
            "          </xsl:attribute>\n" + 
            "          <xsl:attribute name=\"relationType\">\n" + 
            "              <xsl:value-of select=\"@relationType\" />\n" + 
            "          </xsl:attribute>\n" + 
            "          <xsl:value-of select=\".\" />\n" + 
            "        </relatedIdentifier>\n" + 
            "      </xsl:for-each>\n" + 
            "    </relatedIdentifiers>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"geoLocations\">\n" + 
            "    <geoLocations>\n" + 
            "      <xsl:for-each select=\"geoLocation\">\n" + 
            "        <geoLocation>\n" + 
            "          <xsl:apply-templates select=\"geoLocationCountry\" />\n" + 
            "          <xsl:apply-templates select=\"geoLocationPoint\" />\n" + 
            "          <xsl:apply-templates select=\"geoLocationBox\" />\n" + 
            "        </geoLocation>\n" + 
            "      </xsl:for-each>\n" + 
            "    </geoLocations>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"geoLocationCountry\">\n" + 
            "    <geoLocationPlace>\n" + 
            "      <xsl:value-of select=\".\" />\n" + 
            "    </geoLocationPlace>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"geoLocationPoint\">\n" + 
            "    <geoLocationPoint>\n" + 
            "      <pointLongitude>\n" + 
            "        <xsl:value-of select=\"longitude\" />\n" + 
            "      </pointLongitude>\n" + 
            "      <pointLatitude>\n" + 
            "        <xsl:value-of select=\"latitude\" />\n" + 
            "      </pointLatitude>\n" + 
            "    </geoLocationPoint>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"geoLocationBox\">\n" + 
            "    <geoLocationBox>\n" + 
            "      <westBoundLongitude>\n" + 
            "        <xsl:value-of select=\"southWestPoint/longitude\" />\n" + 
            "      </westBoundLongitude>\n" + 
            "      <eastBoundLongitude>\n" + 
            "        <xsl:value-of select=\"northEastPoint/longitude\" />\n" + 
            "      </eastBoundLongitude>\n" + 
            "      <southBoundLatitude>\n" + 
            "        <xsl:value-of select=\"southWestPoint/latitude\" />\n" + 
            "      </southBoundLatitude>\n" + 
            "      <northBoundLatitude>\n" + 
            "        <xsl:value-of select=\"northEastPoint/latitude\" />\n" + 
            "      </northBoundLatitude>\n" + 
            "    </geoLocationBox>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"fundingReferences\">\n" + 
            "    <fundingReferences>\n" + 
            "      <xsl:for-each select=\"fundingReference\">\n" + 
            "        <fundingReference>\n" + 
            "          <funderName>\n" + 
            "            <xsl:value-of select=\"funderName\" />\n" + 
            "          </funderName>\n" + 
            "\n" + 
            "          <xsl:if test=\"funderIdentifier != ''\">\n" + 
            "            <funderIdentifier>\n" + 
            "              <xsl:choose>\n" + 
            "                <xsl:when test=\"funderIdentifier/@type = 'CrossRefFunder'\">\n" + 
            "                  <xsl:attribute name=\"funderIdentifierType\">Crossref Funder ID</xsl:attribute>\n" + 
            "                </xsl:when>\n" + 
            "                <xsl:otherwise>\n" + 
            "                  <xsl:attribute name=\"funderIdentifierType\"><xsl:value-of select=\"funderIdentifier/@type\" /></xsl:attribute>\n" + 
            "                </xsl:otherwise>\n" + 
            "              </xsl:choose>\n" + 
            "              <xsl:value-of select=\"funderIdentifier\" />\n" + 
            "            </funderIdentifier>\n" + 
            "          </xsl:if>\n" + 
            "\n" + 
            "          <xsl:if test=\"awardNumber != ''\">\n" + 
            "            <awardNumber>\n" + 
            "              <xsl:attribute name=\"awardURI\">\n" + 
            "                  <xsl:value-of select=\"awardURI\" />\n" + 
            "              </xsl:attribute>\n" + 
            "              <xsl:value-of select=\"awardNumber\" />\n" + 
            "            </awardNumber>\n" + 
            "          </xsl:if>\n" + 
            "\n" + 
            "          <xsl:if test=\"awardTitle != ''\">\n" + 
            "            <awardTitle>\n" + 
            "              <xsl:value-of select=\"awardTitle\" />\n" + 
            "            </awardTitle>\n" + 
            "          </xsl:if>\n" + 
            "        </fundingReference>\n" + 
            "      </xsl:for-each>\n" + 
            "    </fundingReferences>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template match=\"language\">\n" + 
            "    <language>\n" + 
            "      <xsl:choose>\n" + 
            "        <xsl:when test=\". = 'aar'\">\n" + 
            "          aa\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'abk'\">\n" + 
            "          ab\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ave'\">\n" + 
            "          ae\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'afr'\">\n" + 
            "          af\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'aka'\">\n" + 
            "          ak\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'amh'\">\n" + 
            "          am\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'arg'\">\n" + 
            "          an\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ara'\">\n" + 
            "          ar\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'asm'\">\n" + 
            "          as\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ava'\">\n" + 
            "          av\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'aym'\">\n" + 
            "          ay\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'aze'\">\n" + 
            "          az\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bak'\">\n" + 
            "          ba\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bel'\">\n" + 
            "          be\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bul'\">\n" + 
            "          bg\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bis'\">\n" + 
            "          bi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bam'\">\n" + 
            "          bm\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ben'\">\n" + 
            "          bn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bod'\">\n" + 
            "          bo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bre'\">\n" + 
            "          br\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'bos'\">\n" + 
            "          bs\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'cat'\">\n" + 
            "          ca\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'che'\">\n" + 
            "          ce\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'cha'\">\n" + 
            "          ch\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'cos'\">\n" + 
            "          co\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'cre'\">\n" + 
            "          cr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ces'\">\n" + 
            "          cs\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'chu'\">\n" + 
            "          cu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'chv'\">\n" + 
            "          cv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'cym'\">\n" + 
            "          cy\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'dan'\">\n" + 
            "          da\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'deu'\">\n" + 
            "          de\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'div'\">\n" + 
            "          dv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'dzo'\">\n" + 
            "          dz\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ewe'\">\n" + 
            "          ee\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ell'\">\n" + 
            "          el\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'eng'\">\n" + 
            "          en\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'epo'\">\n" + 
            "          eo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'spa'\">\n" + 
            "          es\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'est'\">\n" + 
            "          et\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'eus'\">\n" + 
            "          eu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'fas'\">\n" + 
            "          fa\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ful'\">\n" + 
            "          ff\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'fin'\">\n" + 
            "          fi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'fij'\">\n" + 
            "          fj\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'fao'\">\n" + 
            "          fo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'fra'\">\n" + 
            "          fr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'fry'\">\n" + 
            "          fy\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'gle'\">\n" + 
            "          ga\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'gla'\">\n" + 
            "          gd\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'glg'\">\n" + 
            "          gl\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'grn'\">\n" + 
            "          gn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'guj'\">\n" + 
            "          gu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'glv'\">\n" + 
            "          gv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hau'\">\n" + 
            "          ha\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'heb'\">\n" + 
            "          he\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hin'\">\n" + 
            "          hi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hmo'\">\n" + 
            "          ho\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hrv'\">\n" + 
            "          hr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hat'\">\n" + 
            "          ht\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hun'\">\n" + 
            "          hu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'hye'\">\n" + 
            "          hy\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'her'\">\n" + 
            "          hz\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ina'\">\n" + 
            "          ia\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ind'\">\n" + 
            "          id\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ile'\">\n" + 
            "          ie\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ibo'\">\n" + 
            "          ig\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'iii'\">\n" + 
            "          ii\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ipk'\">\n" + 
            "          ik\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ido'\">\n" + 
            "          io\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'isl'\">\n" + 
            "          is\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ita'\">\n" + 
            "          it\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'iku'\">\n" + 
            "          iu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'jpn'\">\n" + 
            "          ja\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'jav'\">\n" + 
            "          jv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kat'\">\n" + 
            "          ka\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kon'\">\n" + 
            "          kg\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kik'\">\n" + 
            "          ki\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kua'\">\n" + 
            "          kj\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kaz'\">\n" + 
            "          kk\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kal'\">\n" + 
            "          kl\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'khm'\">\n" + 
            "          km\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kan'\">\n" + 
            "          kn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kor'\">\n" + 
            "          ko\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kau'\">\n" + 
            "          kr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kas'\">\n" + 
            "          ks\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kur'\">\n" + 
            "          ku\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kom'\">\n" + 
            "          kv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'cor'\">\n" + 
            "          kw\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kir'\">\n" + 
            "          ky\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lat'\">\n" + 
            "          la\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ltz'\">\n" + 
            "          lb\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lug'\">\n" + 
            "          lg\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lim'\">\n" + 
            "          li\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lin'\">\n" + 
            "          ln\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lao'\">\n" + 
            "          lo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lit'\">\n" + 
            "          lt\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lub'\">\n" + 
            "          lu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'lav'\">\n" + 
            "          lv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mlg'\">\n" + 
            "          mg\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mah'\">\n" + 
            "          mh\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mri'\">\n" + 
            "          mi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mkd'\">\n" + 
            "          mk\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mal'\">\n" + 
            "          ml\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mon'\">\n" + 
            "          mn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mar'\">\n" + 
            "          mr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'msa'\">\n" + 
            "          ms\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mlt'\">\n" + 
            "          mt\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'mya'\">\n" + 
            "          my\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nau'\">\n" + 
            "          na\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nob'\">\n" + 
            "          nb\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nde'\">\n" + 
            "          nd\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nep'\">\n" + 
            "          ne\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ndo'\">\n" + 
            "          ng\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nld'\">\n" + 
            "          nl\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nno'\">\n" + 
            "          nn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nor'\">\n" + 
            "          no\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nbl'\">\n" + 
            "          nr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nav'\">\n" + 
            "          nv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'nya'\">\n" + 
            "          ny\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'oci'\">\n" + 
            "          oc\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'oji'\">\n" + 
            "          oj\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'orm'\">\n" + 
            "          om\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ori'\">\n" + 
            "          or\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'oss'\">\n" + 
            "          os\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'pan'\">\n" + 
            "          pa\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'pli'\">\n" + 
            "          pi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'pol'\">\n" + 
            "          pl\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'pus'\">\n" + 
            "          ps\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'por'\">\n" + 
            "          pt\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'que'\">\n" + 
            "          qu\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'roh'\">\n" + 
            "          rm\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'run'\">\n" + 
            "          rn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ron'\">\n" + 
            "          ro\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'rus'\">\n" + 
            "          ru\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'kin'\">\n" + 
            "          rw\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'san'\">\n" + 
            "          sa\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'srd'\">\n" + 
            "          sc\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'snd'\">\n" + 
            "          sd\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sme'\">\n" + 
            "          se\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sag'\">\n" + 
            "          sg\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sin'\">\n" + 
            "          si\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'slk'\">\n" + 
            "          sk\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'slv'\">\n" + 
            "          sl\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'smo'\">\n" + 
            "          sm\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sna'\">\n" + 
            "          sn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'som'\">\n" + 
            "          so\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sqi'\">\n" + 
            "          sq\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'srp'\">\n" + 
            "          sr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ssw'\">\n" + 
            "          ss\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sot'\">\n" + 
            "          st\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'sun'\">\n" + 
            "          su\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'swe'\">\n" + 
            "          sv\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'swa'\">\n" + 
            "          sw\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tam'\">\n" + 
            "          ta\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tel'\">\n" + 
            "          te\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tgk'\">\n" + 
            "          tg\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tha'\">\n" + 
            "          th\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tir'\">\n" + 
            "          ti\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tuk'\">\n" + 
            "          tk\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tgl'\">\n" + 
            "          tl\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tsn'\">\n" + 
            "          tn\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ton'\">\n" + 
            "          to\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tur'\">\n" + 
            "          tr\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tso'\">\n" + 
            "          ts\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tat'\">\n" + 
            "          tt\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'twi'\">\n" + 
            "          tw\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'tah'\">\n" + 
            "          ty\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'uig'\">\n" + 
            "          ug\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ukr'\">\n" + 
            "          uk\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'uzb'\">\n" + 
            "          uz\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'ven'\">\n" + 
            "          ve\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'vie'\">\n" + 
            "          vi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'vol'\">\n" + 
            "          vo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'wln'\">\n" + 
            "          wa\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'wol'\">\n" + 
            "          wo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'xho'\">\n" + 
            "          xh\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'yid'\">\n" + 
            "          yi\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'yor'\">\n" + 
            "          yo\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'zha'\">\n" + 
            "          za\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'zho'\">\n" + 
            "          zh\n" + 
            "        </xsl:when>\n" + 
            "        <xsl:when test=\". = 'zul'\">\n" + 
            "          zu\n" + 
            "        </xsl:when>\n" + 
            "      </xsl:choose>\n" + 
            "    </language>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"sizes\">\n" + 
            "    <sizes>\n" + 
            "      <size>\n" + 
            "        <xsl:value-of select=\"$datasetSize\" />\n" + 
            "      </size>\n" + 
            "    </sizes>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "  <xsl:template name=\"formats\">\n" + 
            "    <formats>\n" + 
            "      <format>application/zip</format>\n" + 
            "    </formats>\n" + 
            "  </xsl:template>\n" + 
            "\n" + 
            "</xsl:stylesheet>\n" + 
            ""));
    
    
    
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
