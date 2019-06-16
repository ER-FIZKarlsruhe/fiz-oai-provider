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

public class ListRecordsIT extends BaseIT {

  final static Logger logger = LogManager.getLogger(ListRecordsIT.class);

  public void testListRecordsMetadataPrefixOaiDc() throws Exception {
    logger.info("testListRecordsMetadataPrefixOaiDc");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("oai_dc"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  
  @Test
  public void testListRecordsMetadataPrefixDatacite() throws Exception {
    logger.info("testListRecordsMetadataPrefixDatacite");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "datacite"));
    params.add(new BasicNameValuePair("set", "fiz"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("datacite"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  
//
//  @Test
//  public void testListIdentifiersMissingMetadataArgument() throws Exception {
//    logger.info("testListIdentifiers");
//    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("verb", "ListRecords"));
//    httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//    try (CloseableHttpClient client = HttpClientBuilder.create().build();
//        CloseableHttpResponse response = client.execute(httpPost)) {
//      String bodyAsString = EntityUtils.toString(response.getEntity());
//      System.out.println("response: " + bodyAsString);
//      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
//      Assert.assertNotNull(bodyAsString);
//      Assert.assertTrue(bodyAsString.contains("badArgument"));
//      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
//    }
//  }
//  
//  @Test
//  public void testListIdentifiersBadFromArgument() throws Exception {
//    logger.info("testListIdentifiers");
//    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("verb", "ListRecords"));
//    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
//    params.add(new BasicNameValuePair("from", "abc"));//Must be like '2019-12-12'
//    httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//    try (CloseableHttpClient client = HttpClientBuilder.create().build();
//        CloseableHttpResponse response = client.execute(httpPost)) {
//      String bodyAsString = EntityUtils.toString(response.getEntity());
//      System.out.println("response: " + bodyAsString);
//      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
//      Assert.assertNotNull(bodyAsString);
//      Assert.assertTrue(bodyAsString.contains("badArgument"));
//      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
//    }
//  }
//  
//  @Test
//  public void testListIdentifiersCannotDisseminateFormat() throws Exception {
//    logger.info("testListIdentifiers");
//    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("verb", "ListRecords"));
//    params.add(new BasicNameValuePair("metadataPrefix", "oo"));
//    httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//    try (CloseableHttpClient client = HttpClientBuilder.create().build();
//        CloseableHttpResponse response = client.execute(httpPost)) {
//      String bodyAsString = EntityUtils.toString(response.getEntity());
//      System.out.println("response: " + bodyAsString);
//      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
//      Assert.assertNotNull(bodyAsString);
//      Assert.assertTrue(bodyAsString.contains("cannotDisseminateFormat"));
//      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
//    }
//  }
//
//  @Test
//  public void testListIdentifiersEmptySet() throws Exception {
//    logger.info("testListIdentifiers");
//    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("verb", "ListRecords"));
//    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
//    params.add(new BasicNameValuePair("set", "empty"));//Returns an empty resultSet from the backend
//    httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//    try (CloseableHttpClient client = HttpClientBuilder.create().build();
//        CloseableHttpResponse response = client.execute(httpPost)) {
//      String bodyAsString = EntityUtils.toString(response.getEntity());
//      System.out.println("response: " + bodyAsString);
//      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
//      Assert.assertNotNull(bodyAsString);
//      Assert.assertTrue(bodyAsString.contains("noRecordsMatch"));
//      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
//    }
//  }
  
}