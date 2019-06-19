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

public class IdentifyIT extends BaseIT {

  
  final static Logger logger = LogManager.getLogger(IdentifyIT.class);
  
  @Test
  public void testGetIdentify() throws Exception {
    logger.info("testGetIdentifier");
    String url = TEST_OAI_URL + "?verb=Identify";
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpPost(url))) {

      String bodyAsString = EntityUtils.toString(response.getEntity());

      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
 
  
  @Test
  public void testGetIdentifyWrongVerbName() throws Exception {
    logger.info("testGetIdentifyWrongVerbName");
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "Identify"));
    params.add(new BasicNameValuePair("metadataPrefix", "oaiDc"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }  
  
}