package de.fiz_karlsruhe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class OaiHandlerIT extends TestCase {

  
  final static Logger logger = LogManager.getLogger(OaiHandlerIT.class);
  
  public void testGetIdentify() throws Exception {
    System.out.println("testGetIdentifier");
    String url = "http://localhost:8999/fiz-oai-provider/OAIHandler?verb=Identify";
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpPost(url))) {

      String bodyAsString = EntityUtils.toString(response.getEntity());

      logger.info("response: " + bodyAsString);
      assertEquals(200, response.getStatusLine().getStatusCode());
      assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }

  public void testGetSingleRecord() throws Exception {
    System.out.println("testGetRecord");
    HttpPost httpPost = new HttpPost("http://localhost:8999/fiz-oai-provider/OAIHandler");
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "GetRecord"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:10.0133/10000386"));
    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("response: " + bodyAsString);
      assertEquals(200, response.getStatusLine().getStatusCode());
      assertNotNull(bodyAsString);
      assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }

  
  public void testListSets() throws Exception {
    System.out.println("testGetRecord");
    HttpPost httpPost = new HttpPost("http://localhost:8999/fiz-oai-provider/OAIHandler");
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListSets"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("response: " + bodyAsString);
      assertEquals(200, response.getStatusLine().getStatusCode());
      assertNotNull(bodyAsString);
      assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  
  
  
  
  static boolean validateAgainstOaiXsd(String xml) {
    try {
      ClassLoader classLoader = new OaiHandlerIT().getClass().getClassLoader();

      InputStream oaiPmhXsd = classLoader.getResourceAsStream("OAI-PMH.xsd");
      InputStream oaiIdentifierXsd = classLoader.getResourceAsStream("oai-identifier.xsd");
      InputStream oaiDcXsd = classLoader.getResourceAsStream("oai_dc.xsd");
      InputStream simpleDcXsd = classLoader.getResourceAsStream("simpledc20021212.xsd");
      
      InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
      
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      
      StreamSource[] schemas = {new StreamSource(oaiPmhXsd), new StreamSource(oaiIdentifierXsd), new StreamSource(oaiDcXsd), new StreamSource(simpleDcXsd)};
      Schema schema = factory.newSchema(schemas);
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xmlStream));
      return true;
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
      return false;
    }
  }

}