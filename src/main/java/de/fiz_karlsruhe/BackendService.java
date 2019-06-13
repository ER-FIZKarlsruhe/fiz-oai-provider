package de.fiz_karlsruhe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.crosswalk.CrosswalkItem;
import ORG.oclc.oai.server.verb.OAIInternalServerError;
import ORG.oclc.oai.util.OAIUtil;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.SearchResult;

public class BackendService {
  
  private static String backendBaseUrl;
  
  private static BackendService INSTANCE;
   
  final static Logger logger = LogManager.getLogger(FizOAICatalog.class);
  
  private BackendService(String backendBaseUrl) {
    BackendService.backendBaseUrl = backendBaseUrl;
  }
   
  public static BackendService getInstance(String backendBaseUrl) {
      if(INSTANCE == null) {
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
  
  
  public SearchResult<Item> getIdentifiers() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    
    SearchResult<Item> result = null;
    String url = backendBaseUrl + "/item?content=false";
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        JavaType type = objectMapper.getTypeFactory().constructParametricType(SearchResult.class, Item.class);
        result = objectMapper.readValue(json, type);
      } else {
        throw new IOException(response.getStatusLine().getReasonPhrase());
      }
    } catch (Exception e) {
      logger.error("Error on getIdentifiers", e);
    }
    
    return result;
  }
  
  
  public HashMap<String, CrosswalkItem> getFormats() throws IOException {
    String url = backendBaseUrl + "/format";
    HashMap<String, CrosswalkItem> crosswalksMap = new HashMap<String, CrosswalkItem>();
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {

      String bodyAsString = EntityUtils.toString(response.getEntity());

      JSONParser parser = new JSONParser();
      JSONArray formats = (JSONArray) parser.parse(bodyAsString);

      // loop array
      Iterator<JSONObject> iterator = formats.iterator();
      while (iterator.hasNext()) {
        JSONObject format = (JSONObject) iterator.next();

        String metadataPrefix = (String) format.get("metadataPrefix");
        logger.info(metadataPrefix);

        String schemaLocation = (String) format.get("schemaLocation");
        logger.info(schemaLocation);

        String schemaNamespace = (String) format.get("schemaNamespace");
        logger.info(schemaNamespace);

        String crosswalkStyleSheet = (String) format.get("crosswalkStyleSheet");
        logger.info(crosswalkStyleSheet);

        if (crosswalkStyleSheet.isEmpty()) {
          logger.warn("Skip crosswalk, as no xslt is available!");
          continue;
        }

        String identifierXpath = (String) format.get("identifierXpath");
        logger.info(identifierXpath);
        logger.info("");

        Crosswalk fizOaiBackendCrosswalk = new FizOaiBackendCrosswalk(schemaLocation, crosswalkStyleSheet);

        CrosswalkItem crosswalkItem = new CrosswalkItem(metadataPrefix, schemaLocation, schemaNamespace,
            fizOaiBackendCrosswalk);
        crosswalksMap.put(metadataPrefix, crosswalkItem);
      }
    } catch (Exception e) {
      logger.error("An error occured during initializing the crosswalks", e);
    }
    
    return crosswalksMap;
  }
  
  
  public Map getSets() throws OAIInternalServerError, IOException {
    String url = backendBaseUrl + "/set";
    logger.info("listSets using " + url);

    Map listSetsMap = new HashMap();
    ArrayList sets = new ArrayList();
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(url))) {

      String bodyAsString = EntityUtils.toString(response.getEntity());

      JSONParser parser = new JSONParser();
      JSONArray setsJson = (JSONArray) parser.parse(bodyAsString);

      // loop array
      Iterator<JSONObject> iterator = setsJson.iterator();
      while (iterator.hasNext()) {
        JSONObject set = (JSONObject) iterator.next();
        sets.add(getSetXML(set));
      }

    } catch (IOException | ParseException e) {
      logger.error("Error while getting sets", e);
      throw new OAIInternalServerError(e.getMessage());
    }
    
    listSetsMap.put("sets", sets.iterator());
    return listSetsMap;
  }
  
  
  public String getSetXML(JSONObject setItem) throws IllegalArgumentException {
    String setSpec = (String) setItem.get("spec");
    String setName = (String) setItem.get("name");
    String setDescription = (String) setItem.get("description");

    StringBuffer sb = new StringBuffer();
    sb.append("<set>");
    sb.append("<setSpec>");
    sb.append(setSpec != null ? OAIUtil.xmlEncode(setSpec) : "");
    sb.append("</setSpec>");
    sb.append("<setName>");
    sb.append(setName != null ? OAIUtil.xmlEncode(setName) : "");
    sb.append("</setName>");
    if (setDescription != null) {
      sb.append("<setDescription>");
      sb.append(
          "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">");
      sb.append("<dc:description>");
      sb.append(OAIUtil.xmlEncode(setDescription));
      sb.append("</dc:description>");
      sb.append("</oai_dc:dc>");

      sb.append("</setDescription>");
    }
    sb.append("</set>");

    logger.info("getSetXML: " + sb.toString());
    return sb.toString();
  }
  
}
