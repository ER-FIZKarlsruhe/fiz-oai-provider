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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class ListRecordsIT extends BaseIT {

  final static Logger logger = LogManager.getLogger(ListRecordsIT.class);

  @Test
  public void testListRecordsMetadataPrefixOaiDc() throws Exception {
    logger.info("testListRecordsMetadataPrefixOaiDc");
    
    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "fiz"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("oai_dc"));
      Assert.assertTrue(bodyAsString.contains("status=\"deleted\""));
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
      Assert.assertTrue(bodyAsString.contains("oai_dc"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }
  
  @Test
  public void testListRecordsMetadataPrefixDatacite() throws Exception {
    logger.info("testListRecordsMetadataPrefixDatacite");
    
    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "datacite"));
    params.add(new BasicNameValuePair("set", "fiz"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("datacite"));
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
      Assert.assertTrue(bodyAsString.contains("datacite"));
    }

  }
  

  @Test
  public void testListRecordsMissingMetadataArgument() throws Exception {
    logger.info("testListRecordsMissingMetadataArgument");
    
    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
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
  
  @Test
  public void testListRecordsBadFromArgument() throws Exception {
    logger.info("testListRecordsBadFromArgument");
    
    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("from", "abc"));//Must be like '2019-12-12'
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
  
  @Test
  public void testListRecordsCannotDisseminateFormat() throws Exception {
    logger.info("testListRecordsCannotDisseminateFormat");
    
    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "oo"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("cannotDisseminateFormat"));
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
      Assert.assertTrue(bodyAsString.contains("cannotDisseminateFormat"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }
  }

  @Test
  public void testListRecordsEmptySet() throws Exception {
    logger.info("testListRecordsEmptySet");
    
    // POST
    HttpPost httpPost = new HttpPost(TEST_OAI_URL);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("verb", "ListRecords"));
    params.add(new BasicNameValuePair("metadataPrefix", "oai_dc"));
    params.add(new BasicNameValuePair("set", "empty"));//Returns an empty resultSet from the backend
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(httpPost)) {
      String bodyAsString = EntityUtils.toString(response.getEntity());
      logger.info("POST URL: " + httpPost.getURI().toString());
      logger.info("POST response: " + bodyAsString);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      Assert.assertNotNull(bodyAsString);
      Assert.assertTrue(bodyAsString.contains("noRecordsMatch"));
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
      Assert.assertTrue(bodyAsString.contains("noRecordsMatch"));
      Assert.assertTrue(validateAgainstOaiDcXsd(bodyAsString));
    }

  }
  
}