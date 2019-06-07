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

import junit.framework.TestCase;

public class OaiHandlerIT extends TestCase {

  public void testGetIdentifier() throws Exception {
    System.out.println("testGetIdentifier");
    String url = "http://localhost:8999/fiz-oai-provider/OAIHandler?verb=Identify";
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpPost(url))) {

      String bodyAsString = EntityUtils.toString(response.getEntity());
      System.out.println(bodyAsString);
      assertNotNull(bodyAsString);
      assertEquals(200, response.getStatusLine().getStatusCode());
    }
  }

  public void testGetRecord() throws Exception {
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
      System.out.println(bodyAsString);
      assertEquals(200, response.getStatusLine().getStatusCode());
      assertNotNull(bodyAsString);
    }
  }

}