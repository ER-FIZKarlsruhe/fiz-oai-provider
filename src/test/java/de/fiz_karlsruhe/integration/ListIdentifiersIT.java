package de.fiz_karlsruhe.integration;

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

public class ListIdentifiersIT extends BaseIT {

  final static Logger logger = LogManager.getLogger(ListIdentifiersIT.class);

  @Test
  public void testListIdentifiers() throws Exception {
    logger.info("testListIdentifiers");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListIdentifiersWithDateRange() throws Exception {
    logger.info("testListIdentifiersWithDateRange");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    params.add(new BasicNameValuePair("from", "1970-01-01"));
    params.add(new BasicNameValuePair("until", "9999-12-31"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListIdentifiersWithDatetimeRange() throws Exception {
    logger.info("testListIdentifiersWithDatetimeRange");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    params.add(new BasicNameValuePair("from", "1970-01-01T00:00:01Z"));
    params.add(new BasicNameValuePair("until", "9999-12-31T23:59:59Z"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
  
  @Test
  public void testListIdentifiersWithInvalidDateRange() throws Exception {
    logger.info("testListIdentifiersWithInvalidDateRange");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    params.add(new BasicNameValuePair("until", "1970-01-01"));//until is smaller than from
    params.add(new BasicNameValuePair("from", "9999-12-31"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("testListIdentifiersWithInvalidRange response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
  @Test
  public void testListIdentifiersWithInvalidDatetimeRange() throws Exception {
    logger.info("testListIdentifiersWithInvalidDatetimeRange");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    params.add(new BasicNameValuePair("until", "1970-01-01T11:12:12Z"));//until is 1s smaller than from
    params.add(new BasicNameValuePair("from", "1970-01-01T11:12:13Z"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("testListIdentifiersWithInvalidRange response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
  @Test
  public void testListIdentifiersMissingMetadataArgument() throws Exception {
    logger.info("testListIdentifiersMissingMetadataArgument");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
  @Test
  public void testListIdentifiersBadFromArgument() throws Exception {
    logger.info("testListIdentifiersBadFromArgument");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("from", "abc"));//Must be like '2019-12-12'
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
  @Test
  public void testListIdentifiersCannotDisseminateFormat() throws Exception {
    logger.info("testListIdentifiersCannotDisseminateFormat");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oo"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("cannotDisseminateFormat"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListIdentifiersEmptySet() throws Exception {
    logger.info("testListIdentifiersEmptySet");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListIdentifiers"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "empty"));//Returns an empty resultSet from the backend
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("noRecordsMatch"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
}