package de.fiz_karlsruhe.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
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
    String url = backendBaseUrl + "/item/" + URLEncoder.encode(localIdentifier) + "?format=" + URLEncoder.encode(metadataPrefix) + "&content=true";

    logger.info("getItem url: " + url.toString());
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());

        item = objectMapper.readValue(json, Item.class);
      }
    } catch (Exception e) {
      logger.error("Error on getItem", e);
    }

    return item;
  }

  public SearchResult<Item> getItems(boolean withContent, long offset, long rows, String set, String from, String until, String metadataPrefix)
      throws IOException {
    if (metadataPrefix == null || metadataPrefix.isEmpty()) {
      throw new IllegalArgumentException("metadataPrefix must not be null");
    }
    
    ObjectMapper objectMapper = new ObjectMapper();

    SearchResult<Item> result = null;
    StringBuffer url = new StringBuffer();
    url.append(backendBaseUrl + "/item?content=" + withContent);
    url.append("&format=" + URLEncoder.encode(metadataPrefix));
    url.append("&offset=" + offset);
    url.append("&rows=" + rows);

    if (!StringUtils.isEmpty(set)) {
      url.append("&set=" + set);
    }
    
    if (!StringUtils.isEmpty(from)) {
      url.append("&from=" + from);
    }

    if (!StringUtils.isEmpty(until)) {
      url.append("&until=" + until);
    }
    
    logger.info("getItems url: " + url.toString());
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url.toString()))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
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

    logger.info("getFormats url: " + url.toString());
    List<Format> formatList = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        formatList = Arrays.asList(mapper.readValue(json, Format[].class));
      }
    } catch (Exception e) {
      logger.error("Error on getFormats", e);
    }
    
    return formatList;
  }

  public Format getFormat(String metadataPrefix) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    String url = backendBaseUrl + "/format/" + metadataPrefix;

    logger.info("getFormat url: " + url.toString());
    Format format = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
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

    logger.info("getTransformations url: " + url.toString());
    List<Transformation> transformationList = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        transformationList = Arrays.asList(mapper.readValue(json, Transformation[].class));
      }
    } catch (Exception e) {
      logger.error("Error on getTransformations", e);
    }
    
    return transformationList;
  }
  
  
  public List<Set> getSets() throws OAIInternalServerError, IOException {
    ObjectMapper mapper = new ObjectMapper();
    String url = backendBaseUrl + "/set";
    logger.info("getSets url " + url);
    List<Set> setObjects = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        setObjects = Arrays.asList(mapper.readValue(json, Set[].class));
      }
    } catch (Exception e) {
      logger.error("Error on getIdentifiers", e);
    }

    return setObjects;
  }

}
