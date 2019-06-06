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
    
    
  }

  private static void initGetSpecificFormat(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/oaiDc"))
        .respond(response().withStatusCode(200)
            .withBody("{\n" + "  \"metadataPrefix\":\"oaiDc\",\n"
                + "  \"schemaLocation\":\"http://www.openarchives.org/OAI/2.0/oai_dc.xsd\",\n"
                + "  \"crosswalkStyleSheet\":\"\",\n" + "  \"identifierXpath\":\"/dc/identifier\"\n" + "}"));
  }
  
  
  private static void initGetAllFormats(MockServerClient serverClient) {
    serverClient.when(request().withMethod("GET").withPath("/format"))
        .respond(response().withStatusCode(200)
            .withBody("[{\n" + 
                "  \"metadataPrefix\":\"oaiDc\",\n" + 
                "  \"schemaLocation\":\"http://www.openarchives.org/OAI/2.0/oai_dc.xsd\",\n" + 
                "  \"schemaNamespace\":\"http://www.openarchives.org/OAI/2.0\",\n" + 
                "  \"crosswalkStyleSheet\":\"\", \n" + 
                "  \"identifierXpath\":\"/dc/identifier\"\n" + 
                "},\n" + 
                "{\n" + 
                "  \"metadataPrefix\":\"radar\",\n" + 
                "  \"schemaLocation\":\"https://www.radar-service.eu/en/radar-schema/radarDataset.xsd\",\n" + 
                "  \"schemaNamespace\":\"https://www.radar-service.eu/en/radar-schema\",\n" + 
                "  \"crosswalkStyleSheet\":\"<xsl:stylesheet ...>...</xsl>\", \n" + 
                "  \"identifierXpath\":\"/radarDataset/identifier\"\n" + 
                "}\n" + 
                "]"));
  }
}
