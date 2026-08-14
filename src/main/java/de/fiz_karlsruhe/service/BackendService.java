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

import de.fiz_karlsruhe.OaiRuntimeException;
import de.fiz_karlsruhe.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

public class BackendService {

  private static String backendBaseUrl;

  private static BackendService INSTANCE;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final CloseableHttpClient HTTP_CLIENT = buildHttpClient();

  final static Logger logger = LogManager.getLogger(BackendService.class);

  private static CloseableHttpClient buildHttpClient() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(100);
    connectionManager.setDefaultMaxPerRoute(20);

    return HttpClientBuilder.create()
        .setConnectionManager(connectionManager)
        .build();
  }

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
	      throw new OaiRuntimeException("Service must be initialized with backendUrl first!");
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
    
    Item item = null;
    String url = backendBaseUrl + "/item/" + URLEncoder.encode(localIdentifier, StandardCharsets.UTF_8)
            + "?format=" + URLEncoder.encode(metadataPrefix, StandardCharsets.UTF_8) + "&content=true";

    logger.info("getItem localIdentifier + metadataPrefix  url: {}", url);
    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());

        item = OBJECT_MAPPER.readValue(json, Item.class);
      }
    } catch (IOException e) {
      logger.error("Error on getItem", e);
      throw e;
    }

    return item;
  }

  public Item getItem(String localIdentifier) throws IOException {
    if (localIdentifier == null || localIdentifier.isEmpty()) {
      throw new IllegalArgumentException("localIdentifier must not be null");
    }

    Item item = null;
    String url = backendBaseUrl + "/item/" + URLEncoder.encode(localIdentifier, StandardCharsets.UTF_8);

    logger.debug("getItem localIdentifier url: {}", url);

    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());

        item = OBJECT_MAPPER.readValue(json, Item.class);
      }
    } catch (IOException e) {
      logger.error("Error on getItem", e);
      throw e;
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
      url.append("&searchMark=").append(URLEncoder.encode(searchMark, StandardCharsets.UTF_8));
    }
    url.append("&rows=").append(rows);
    if (StringUtils.isNotEmpty(set)) {
      url.append("&set=").append(URLEncoder.encode(set, StandardCharsets.UTF_8));
    }

    if (StringUtils.isNotEmpty(from)) {
      url.append("&from=").append(URLEncoder.encode(from, StandardCharsets.UTF_8));
    }

    if (StringUtils.isNotEmpty(until)) {
      url.append("&until=").append(URLEncoder.encode(until, StandardCharsets.UTF_8));
    }

    logger.info("getItems url: {}", url.toString());
    SearchResult<Item> result = null;

    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url.toString()))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        logger.debug("json {}", json);
        JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(SearchResult.class, Item.class);
        result = OBJECT_MAPPER.readValue(json, type);
      }
    } catch (IOException e) {
      logger.error("Error on getIdentifiers", e);
      throw e;
    }

    return result;
  }

  public List<Format> getFormats() throws IOException {
    String url = backendBaseUrl + "/format";

    logger.info("getFormats url: {}", url);
    List<Format> formatList = null;

    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {

      logger.info("getFormats response code: {}", response.getStatusLine().getStatusCode());

      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        formatList = new ArrayList<Format>(Arrays.asList(OBJECT_MAPPER.readValue(json, Format[].class)));
      }
    } catch (IOException e) {
      logger.error("Error on getFormats", e);
      throw e;
    }

    return formatList;
  }

  public Format getFormat(String metadataPrefix) throws IOException {
    String url = backendBaseUrl + "/format/" + URLEncoder.encode(metadataPrefix, StandardCharsets.UTF_8);

    logger.info("getFormat url: {}", url);
    Format format = null;

    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {

      logger.info("getFormat response code: {}", response.getStatusLine().getStatusCode());

      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        format = OBJECT_MAPPER.readValue(json, Format.class);
      }
    } catch (IOException e) {
      logger.error("Error on getFormats", e);
      throw e;
    }

    return format;
  }


  public List<Transformation> getTransformations() throws IOException {
    String url = backendBaseUrl + "/crosswalk";

    logger.info("getTransformations url: {}", url);
    List<Transformation> transformationList = null;

    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {

      logger.info("getTransformations response code: {}", response.getStatusLine().getStatusCode());

      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        transformationList = new ArrayList<Transformation>(Arrays.asList(OBJECT_MAPPER.readValue(json, Transformation[].class)));
      }
    } catch (IOException e) {
      logger.error("Error on getTransformations", e);
      throw e;
    }

    return transformationList;
  }


  public List<Set> getSets() throws IOException {
    String url = backendBaseUrl + "/set";
    logger.info("getSets url {}", url);

    List<Set> setObjects = null;

    try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {

      logger.info("getSets response code: {}", response.getStatusLine().getStatusCode());


      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        setObjects = new ArrayList<Set>(Arrays.asList(OBJECT_MAPPER.readValue(json, Set[].class)));
      }
    } catch (IOException e) {
      logger.error("Error on getIdentifiers", e);
      throw e;
    }

    return setObjects;
  }

    public ListSetsResult searchSets(String resumptionToken) throws IOException {
        String url = backendBaseUrl + "/set/search";

        if (StringUtils.isNotEmpty(resumptionToken)) {
            url = url + "?resumptionToken=" + URLEncoder.encode(resumptionToken, StandardCharsets.UTF_8);
        }

        logger.info("searchSets url {}", url);

        ListSetsResult result = null;

        try (CloseableHttpResponse response = HTTP_CLIENT.execute(getHttpGet(url))) {

            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("getSets response code: {}", statusCode);

            if (statusCode == 200) {
                String json = EntityUtils.toString(response.getEntity());
                result = OBJECT_MAPPER.readValue(json, ListSetsResult.class);
            } else {
                logger.warn("Non-200 status when calling searchSets: {}", statusCode);
            }
        } catch (IOException e) {
            logger.error("Error on searchSets", e);
            throw e;
        }

        return result;
    }



  private HttpGet getHttpGet(String url) {
    ConfigurationService configurationService = ConfigurationService.getInstance();
      
    int socketTimeout = configurationService.getHttpSocketTimeout();
    int connectionTimeout = configurationService.getHttpConnectionTimeout();
    logger.debug("Init Http cient");
    logger.debug("Set socket timout " + socketTimeout);
    logger.debug("Set connection timout " + connectionTimeout);
    
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(RequestConfig.custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectionTimeout)
            .build());
    return httpGet;
  }

}
