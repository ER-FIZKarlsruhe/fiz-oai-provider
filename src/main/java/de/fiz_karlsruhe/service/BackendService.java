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

package de.fiz_karlsruhe.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ORG.oclc.oai.server.verb.OAIInternalServerError;
import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.SearchResult;
import de.fiz_karlsruhe.model.Set;
import de.fiz_karlsruhe.model.Transformation;

public class BackendService {

  private static String backendBaseUrl;

  private static BackendService INSTANCE;

  final static Logger logger = LogManager.getLogger(BackendService.class);



  private BackendService(String backendBaseUrl) {
    BackendService.backendBaseUrl = backendBaseUrl;
  }

  public static BackendService getInstance(String backendBaseUrl) {
    if (INSTANCE == null) {
      INSTANCE = new BackendService(backendBaseUrl);
    }

    return INSTANCE;
  }

  public static BackendService getInstance() {
	    if (INSTANCE == null) {
	      throw new RuntimeException("Service must be initialized with backendUrl first!"); 
	    }

	    return INSTANCE;
	  }
  
  public Item getItem(String localIdentifier, String metadataPrefix) throws IOException {
    if (localIdentifier == null || localIdentifier.isEmpty()) {
      throw new IllegalArgumentException("localIdentifier must not be null");
    }
    
    if (metadataPrefix == null || metadataPrefix.isEmpty()) {
      throw new IllegalArgumentException("metadataPrefix must not be null");
    }
    
    ObjectMapper objectMapper = new ObjectMapper();

    Item item = null;
    String url = backendBaseUrl + "/item/" + URLEncoder.encode(localIdentifier, StandardCharsets.UTF_8)
            + "?format=" + URLEncoder.encode(metadataPrefix, StandardCharsets.UTF_8) + "&content=true";

    logger.info("getItem localIdentifier + metadataPrefix  url: {}", url);
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(getHttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());

        item = objectMapper.readValue(json, Item.class);
      }
    } catch (Exception e) {
      logger.error("Error on getItem", e);
    }

    return item;
  }

  public Item getItem(String localIdentifier) throws IOException {
    if (localIdentifier == null || localIdentifier.isEmpty()) {
      throw new IllegalArgumentException("localIdentifier must not be null");
    }
    
    ObjectMapper objectMapper = new ObjectMapper();

    Item item = null;
    String url = backendBaseUrl + "/item/" + URLEncoder.encode(localIdentifier, StandardCharsets.UTF_8);

    logger.debug("getItem localIdentifier url: {}", url);
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(getHttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());

        item = objectMapper.readValue(json, Item.class);
      }
    } catch (Exception e) {
      logger.error("Error on getItem", e);
    }

    return item;
  }
  
  
  public SearchResult<Item> getItems(boolean withContent, String searchMark, long rows, String set, String from, String until, String metadataPrefix)
      throws IOException {
    if (metadataPrefix == null || metadataPrefix.isEmpty()) {
      throw new IllegalArgumentException("metadataPrefix must not be null");
    }
    
    StringBuilder url = new StringBuilder();
    url.append(backendBaseUrl).append("/item?content=").append(withContent);
    url.append("&format=").append(URLEncoder.encode(metadataPrefix, StandardCharsets.UTF_8));
    if (StringUtils.isNotEmpty(searchMark)) {
      url.append("&searchMark=").append(searchMark);
    }
    url.append("&rows=").append(rows);
    if (StringUtils.isNotEmpty(set)) {
      url.append("&set=").append(set);
    }
    
    if (StringUtils.isNotEmpty(from)) {
      url.append("&from=").append(from);
    }

    if (StringUtils.isNotEmpty(until)) {
      url.append("&until=").append(until);
    }

    logger.info("getItems url: {}", url.toString());
    SearchResult<Item> result = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(getHttpGet(url.toString()))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        logger.debug("json {}", json);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(SearchResult.class, Item.class);
        result = objectMapper.readValue(json, type);
      }
    } catch (Exception e) {
      logger.error("Error on getIdentifiers", e);
    }

    return result;
  }

  public List<Format> getFormats() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    String url = backendBaseUrl + "/format";

    logger.info("getFormats url: {}", url);
    List<Format> formatList = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
      CloseableHttpResponse response = client.execute(getHttpGet(url))) {
        
      logger.info("getFormats response code: {}", response.getStatusLine().getStatusCode());
        
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        formatList = new ArrayList<Format>(Arrays.asList(mapper.readValue(json, Format[].class)));
      } 
    } catch (Exception e) {
      logger.error("Error on getFormats", e);
    }
    
    return formatList;
  }

  public Format getFormat(String metadataPrefix) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    String url = backendBaseUrl + "/format/" + metadataPrefix;

    logger.info("getFormat url: {}", url);
    Format format = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(getHttpGet(url))) {
        
      logger.info("getFormat response code: {}", response.getStatusLine().getStatusCode());
        
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        format = mapper.readValue(json, Format.class);
      }
    } catch (Exception e) {
      logger.error("Error on getFormats", e);
    }
    
    return format;
  }
  
  
  public List<Transformation> getTransformations() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    String url = backendBaseUrl + "/crosswalk";

    logger.info("getTransformations url: {}", url);
    List<Transformation> transformationList = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(getHttpGet(url))) {
        
      logger.info("getTransformations response code: {}", response.getStatusLine().getStatusCode());
        
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        transformationList = new ArrayList<Transformation>(Arrays.asList(mapper.readValue(json, Transformation[].class)));
      }
    } catch (Exception e) {
      logger.error("Error on getTransformations", e);
    }
    
    return transformationList;
  }
  
  
  public List<Set> getSets() throws OAIInternalServerError, IOException {
    ObjectMapper mapper = new ObjectMapper();
    String url = backendBaseUrl + "/set";
    logger.info("getSets url {}", url);

    List<Set> setObjects = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(getHttpGet(url))) {
        
      logger.info("getSets response code: {}", response.getStatusLine().getStatusCode());
        
        
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        setObjects = new ArrayList<Set>(Arrays.asList(mapper.readValue(json, Set[].class)));
      }
    } catch (Exception e) {
      logger.error("Error on getIdentifiers", e);
    }

    return setObjects;
  }

  private HttpGet getHttpGet(String url) {
    ConfigurationService configurationService = ConfigurationService.getInstance();
      
    int socketTimeout = configurationService.getHttpSocketTimeout();
    int connectionTimeout = configurationService.getHttpConnectionTimeout();
    logger.info("Init Http cient");
    logger.info("Set socket timout " + socketTimeout);
    logger.info("Set connection timout " + connectionTimeout);
    
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(RequestConfig.custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectionTimeout)
            .build());
    return httpGet;
  }

}
