/*
 * Copyright 2019 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fiz_karlsruhe.integration;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class ListMetadataFormatsIT extends BaseIT {

  final static Logger logger = LogManager.getLogger(ListMetadataFormatsIT.class);

  @Test
  public void testListMetadataFormats() throws Exception {
    logger.info("testListMetadataFormats");

    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }

    // GET
    URI getUri = new URIBuilder(TEST_OAI_URL).addParameters(params).build();
    HttpGet httpGet = new HttpGet(getUri.toString());

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpGet)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("GET URL: " + httpGet.getURI().toString());
      logger.info("GET response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListMetadataFormatsValidIdentifier() throws Exception {
    logger.info("testListMetadataFormatsValidIdentifier");

    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:10.0133/10000386"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }

    // GET
    URI getUri = new URIBuilder(TEST_OAI_URL).addParameters(params).build();
    HttpGet httpGet = new HttpGet(getUri.toString());

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpGet)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("GET URL: " + httpGet.getURI().toString());
      logger.info("GET response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListMetadataFormatsInvalidIdentifier() throws Exception {
    logger.info("testListMetadataFormatsInvalidIdentifier");

    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    params.add(new BasicNameValuePair("identifier", "oai:fiz-karlsruhe.de:1010000386"));// invalid id
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("idDoesNotExist"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }

    // GET
    URI getUri = new URIBuilder(TEST_OAI_URL).addParameters(params).build();
    HttpGet httpGet = new HttpGet(getUri.toString());

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpGet)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("GET URL: " + httpGet.getURI().toString());
      logger.info("GET response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("idDoesNotExist"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListMetadataFormatsBadArguments() throws Exception {
    logger.info("testListMetadataFormatsBadArguments");

    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListMetadataFormats"));
    params.add(new BasicNameValuePair("identifiereeeee", "oai:fiz-karlsruhe.de:1010000386"));// Bad argument
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }

    // GET
    URI getUri = new URIBuilder(TEST_OAI_URL).addParameters(params).build();
    HttpGet httpGet = new HttpGet(getUri.toString());

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpGet)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("GET URL: " + httpGet.getURI().toString());
      logger.info("GET response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("badArgument"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

}