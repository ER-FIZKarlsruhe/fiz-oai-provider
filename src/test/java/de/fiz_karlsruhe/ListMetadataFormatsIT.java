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

public class ListMetadataFormatsIT extends BaseIT {

  final static Logger logger = LogManager.getLogger(ListMetadataFormatsIT.class);

 
  @Test
  public void testListMetadataFormats() throws Exception {
    logger.info("testListMetadataFormats");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  
  public void testListMetadataFormatsValidIdentifier() throws Exception {
    logger.info("testListMetadataFormatsValidIdentifier");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:10.0133/10000386"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
  

  public void testListMetadataFormatsInvalidIdentifier() throws Exception {
    logger.info("testListMetadataFormatsInvalidIdentifier");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:1010000386"));//invalid id
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("idDoesNotExist"));
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }

  public void testListMetadataFormatsBadArguments() throws Exception {
    logger.info("testListMetadataFormatsBadArguments");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    params.add(new BasicNameValuePair("identifiereeeee", "oai:fiz-karlsruhe.de:1010000386"));//Bad argument
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