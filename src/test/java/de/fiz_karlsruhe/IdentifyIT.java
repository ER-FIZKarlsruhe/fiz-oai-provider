package de.fiz_karlsruhe;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
    String url = "http://localhost:8999/fiz-oai-provider/OAIHandler?verb=Identify";
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpPost(url))) {

      String bodyAsString = EntityUtils.toString(response.getEntity());

      logger.debug("response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertTrue(validateAgainstOaiXsd(bodyAsString));
    }
  }
 
}