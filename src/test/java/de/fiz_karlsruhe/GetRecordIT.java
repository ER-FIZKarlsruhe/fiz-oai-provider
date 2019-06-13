package de.fiz_karlsruhe;

import java.util.ArrayList;
import java.util.List;

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
import org.junit.Assert;
import org.junit.Test;


public class GetRecordIT extends BaseIT {

  final static Logger logger = LogManager.getLogger(GetRecordIT.class);

  @Test
  public void testGetSingleRecord() throws Exception {
    logger.info("testGetRecord");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "GetRecord"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:10.0133/10000386"));
    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  
  @Test
  public void testGetSingleRecordInvalidIdentifier() throws Exception {
    logger.info("testGetRecord");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "GetRecord"));
    params.add(new BasicNameValuePair("identifier", "oai:frlsruhe.de:1133/1003"));//Invalid ID
    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("testGetSingleRecordInvalidIdentifier response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("idDoesNotExist"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }

  @Test
  public void testGetSingleRecordInvalidFormat() throws Exception {
    logger.info("testGetRecord");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "GetRecord"));
    params.add(new BasicNameValuePair("identifier", "oai:frlsruhe.de:1133/1003"));//Invalid ID
    params.add(new BasicNameValuePair("metadataPrefix", "oc"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("testGetSingleRecordInvalidIdentifier response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("cannotDisseminateFormat"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }

  @Test
  public void testGetSingleRecordMissingIdentifier() throws Exception {
    logger.info("testGetRecord");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "GetRecord"));
    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  
  @Test
  public void testGetSingleRecordMissingMetadataPrefix() throws Exception {
    logger.info("testGetRecord");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "GetRecord"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:10.0133/10000386"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }  
  
}