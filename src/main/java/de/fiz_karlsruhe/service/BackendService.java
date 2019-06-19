package de.fiz_karlsruhe.service;

import java.io.IOException;
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

import ORG.oclc.oai.server.verb.OAIInternalServerError;
import de.fiz_karlsruhe.FizOAICatalog;
import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.SearchResult;
import de.fiz_karlsruhe.model.Set;

public class BackendService {

  private static String backendBaseUrl;

  private static BackendService INSTANCE;

  final static Logger logger = LogManager.getLogger(FizOAICatalog.class);

  private BackendService(String backendBaseUrl) {
    BackendService.backendBaseUrl = backendBaseUrl;
  }

  public static BackendService getInstance(String backendBaseUrl) {
    if (INSTANCE == null) {
      INSTANCE = new BackendService(backendBaseUrl);
    }

    return INSTANCE;
  }

  public Item getItem(String localIdentifier) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    Item item = null;
    String url = backendBaseUrl + "/item/" + localIdentifier;

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

  public SearchResult<Item> getItems(boolean withContent, long offset, long rows, String set, String from, String until)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    SearchResult<Item> result = null;
    StringBuffer url = new StringBuffer();
    url.append(backendBaseUrl + "/item?content=" + withContent);
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
    List<Format> formatList = null;

    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        formatList = Arrays.asList(mapper.readValue(json, Format[].class));
      }
    } catch (Exception e) {
      logger.error("Error on getIdentifiers", e);
    }

    return formatList;

  }

  public List<Set> getSets() throws OAIInternalServerError, IOException {
    ObjectMapper mapper = new ObjectMapper();
    String url = backendBaseUrl + "/set";
    logger.info("listSets using " + url);
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
