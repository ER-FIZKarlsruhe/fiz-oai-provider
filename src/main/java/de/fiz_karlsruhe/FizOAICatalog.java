/**
 * Copyright 2006 OCLC Online Computer Library Center Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fiz_karlsruhe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import ORG.oclc.oai.server.verb.BadResumptionTokenException;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import ORG.oclc.oai.server.verb.IdDoesNotExistException;
import ORG.oclc.oai.server.verb.NoItemsMatchException;
import ORG.oclc.oai.server.verb.NoMetadataFormatsException;
import ORG.oclc.oai.server.verb.NoSetHierarchyException;
import ORG.oclc.oai.server.verb.OAIInternalServerError;
import ORG.oclc.oai.util.OAIUtil;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.ResumptionToken;
import de.fiz_karlsruhe.model.SearchResult;
import de.fiz_karlsruhe.model.Set;
import de.fiz_karlsruhe.service.BackendService;

/**
 * FileSystemOAICatalog is an implementation of AbstractCatalog interface with
 * the data sitting in a directory on a filesystem.
 *
 * @author Ralph LeVan, OCLC Online Computer Library Center
 */

public class FizOAICatalog extends AbstractCatalog {

  final static Logger logger = LogManager.getLogger(FizOAICatalog.class);

  static final boolean debug = false;

  private SimpleDateFormat dateFormatter = new SimpleDateFormat();
  private int maxListSize;

  private String backendBaseUrl;

  private BackendService backendService;

  public FizOAICatalog(Properties properties) {
    showBanner();
    logger.info("Initializing FizOAICatalog...");

    String temp;

    dateFormatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    temp = properties.getProperty("FizOAICatalog.maxListSize");

    if (temp == null) {
      throw new IllegalArgumentException("FizOAICatalog." + "maxListSize is missing from the properties file");
    }
    maxListSize = Integer.parseInt(temp);

    logger.info("in FizOAICatalog(): maxListSize=" + maxListSize);

    backendBaseUrl = properties.getProperty("FizOaiBackend.baseURL");
    if (backendBaseUrl == null) {
      throw new IllegalArgumentException("FizOaiBackend.baseURL is missing from the properties file");
    }
    logger.info("FizOaiBackend.baseURL: " + backendBaseUrl);

    backendService = BackendService.getInstance(backendBaseUrl);
  }

  private void showBanner() {
    logger
        .info("#######                #######    #    ###       ######                                              ");
    logger
        .info("#       # ######       #     #   # #    #        #     # #####   ####  #    # # #####  ###### #####  ");
    logger
        .info("#       #     #        #     #  #   #   #        #     # #    # #    # #    # # #    # #      #    # ");
    logger
        .info("#####   #    #   ##### #     # #     #  #  ##### ######  #    # #    # #    # # #    # #####  #    # ");
    logger
        .info("#       #   #          #     # #######  #        #       #####  #    # #    # # #    # #      #####  ");
    logger
        .info("#       #  #           #     # #     #  #        #       #   #  #    #  #  #  # #    # #      #   #  ");
    logger
        .info("#       # ######       ####### #     # ###       #       #    #  ####    ##   # #####  ###### #    # ");
  }

  /**
   * Retrieve the specified metadata for the specified oaiIdentifier
   *
   * @param oaiIdentifier  the OAI identifier
   * @param metadataPrefix the OAI metadataPrefix
   * @return the Record object containing the result.
   * @exception CannotDisseminateFormatException signals an http status code 400
   *                                             problem
   * @exception IdDoesNotExistException          signals an http status code 404
   *                                             problem
   * @exception OAIInternalServerError           signals an http status code 500
   *                                             problem
   */
  public String getRecord(String oaiIdentifier, String metadataPrefix)
      throws IdDoesNotExistException, CannotDisseminateFormatException, OAIInternalServerError {
    Item nativeItem = null;
    try {
      String localIdentifier = getRecordFactory().fromOAIIdentifier(oaiIdentifier);
      logger.info("local identifier: " + localIdentifier);

      nativeItem = backendService.getItem(localIdentifier);
      logger.debug(nativeItem);

      if (nativeItem == null) {
        throw new IdDoesNotExistException(oaiIdentifier);
      }

      return constructRecord(nativeItem, metadataPrefix);
    } catch (IOException e) {
      logger.error("Cannot read record", e);
      throw new OAIInternalServerError("Database Failure");
    }
  }

  /**
   * Retrieve a list of schemaLocation values associated with the specified
   * oaiIdentifier.
   *
   * We get passed the ID for a record and are supposed to return a list of the
   * formats that we can deliver the record in. Since we are assuming that all the
   * records in the directory have the same format, the response to this is
   * static;
   *
   * @param oaiIdentifier the OAI identifier
   * @return a Vector containing schemaLocation Strings
   * @exception OAIBadRequestException signals an http status code 400 problem
   * @exception OAINotFoundException   signals an http status code 404 problem
   * @exception OAIInternalServerError signals an http status code 500 problem
   */
  public Vector getSchemaLocations(String oaiIdentifier)
      throws IdDoesNotExistException, OAIInternalServerError, NoMetadataFormatsException {
    Item nativeItem = null;
    try {
      String localIdentifier = getRecordFactory().fromOAIIdentifier(oaiIdentifier);
      nativeItem = null; // FIXME getNativeRecord(localIdentifier);
    } catch (Exception e) {
      e.printStackTrace();
      throw new OAIInternalServerError("Database Failure");
    }

    if (nativeItem != null) {
      return getRecordFactory().getSchemaLocations(nativeItem);
    } else {
      throw new IdDoesNotExistException(oaiIdentifier);
    }
  }

  /**
   * Retrieve a list of Identifiers that satisfy the criteria parameters
   *
   * @param from  beginning date in the form of YYYY-MM-DD or null if earliest
   *              date is desired
   * @param until ending date in the form of YYYY-MM-DD or null if latest date is
   *              desired
   * @param set   set name or null if no set is desired
   * @return a Map object containing an optional "resumptionToken" key/value pair
   *         and an "identifiers" Map object. The "identifiers" Map contains OAI
   *         identifier keys with corresponding values of "true" or null depending
   *         on whether the identifier is deleted or not.
   * @throws OAIInternalServerError
   * @exception OAIBadRequestException signals an http status code 400 problem
   */
  public Map listIdentifiers(String from, String until, String set, String metadataPrefix)
      throws NoItemsMatchException, OAIInternalServerError {
    Map<String, Object> listIdentifiersMap = new HashMap<String, Object>();
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<String> identifiers = new ArrayList<String>();
    SearchResult<Item> result = null;

    try {
      result = backendService.getItems(false, 0, maxListSize, set, from, until);

      if (result == null || result.getData().isEmpty()) {
        throw new NoItemsMatchException();
      }

      for (Item item : result.getData()) {
        String[] header = getRecordFactory().createHeader(item);
        headers.add(header[0]);
        identifiers.add(header[1]);
      }

    } catch (IOException e) {
      throw new OAIInternalServerError(e.getMessage());
    }

    int cursorPosition = (result.getOffset() + result.getData().size());

    /*****************************************************************
     * Construct the resumptionToken
     *****************************************************************/
    if (cursorPosition < result.getTotal()) {
      ResumptionToken resumptionToken = new ResumptionToken();
      resumptionToken.setSet(set);
      resumptionToken.setFrom(from);
      resumptionToken.setUntil(until);
      resumptionToken.setOffset(0);
      resumptionToken.setRows(maxListSize);
      resumptionToken.setMetadataPrefix(metadataPrefix);

      listIdentifiersMap.put("resumptionMap",
          getResumptionMap(resumptionToken.toString(), cursorPosition, result.getTotal()));
    }
    listIdentifiersMap.put("headers", headers.iterator());
    listIdentifiersMap.put("identifiers", identifiers.iterator());

    return listIdentifiersMap;
  }

  /**
   * Retrieve the next set of Identifiers associated with the resumptionToken
   *
   * @param resumptionToken implementation-dependent format taken from the
   *                        previous listIdentifiers() Map result.
   * @return a Map object containing an optional "resumptionToken" key/value pair
   *         and an "identifiers" Map object. The "identifiers" Map contains OAI
   *         identifier keys with corresponding values of "true" or null depending
   *         on whether the identifier is deleted or not.
   * @throws OAIInternalServerError
   * @exception OAIBadRequestException signals an http status code 400 problem
   */
  public Map listIdentifiers(String resumptionToken) throws BadResumptionTokenException, OAIInternalServerError {
    Map<String, Object> listIdentifiersMap = new HashMap<String, Object>();
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<String> identifiers = new ArrayList<String>();

    ResumptionToken restoken = new ResumptionToken(resumptionToken);

    int oldCursorPosition = restoken.getRows();

    SearchResult<Item> result = null;
    try {
      result = backendService.getItems(false, oldCursorPosition, maxListSize, restoken.getSet(), restoken.getFrom(),
          restoken.getUntil());

      if (result == null || result.getData().isEmpty()) {
        throw new OAIInternalServerError("Empty resultSet");
      }

      for (Item item : result.getData()) {
        String[] header = getRecordFactory().createHeader(item);
        headers.add(header[0]);
        identifiers.add(header[1]);
      }

    } catch (IOException e) {
      throw new OAIInternalServerError(e.getMessage());
    }

    int newCursorPosition = restoken.getRows() + result.getData().size();

    if (newCursorPosition < result.getTotal()) {
      restoken.setOffset(restoken.getRows());
      restoken.setRows(newCursorPosition);

      listIdentifiersMap.put("resumptionMap",
          getResumptionMap(restoken.toString(), newCursorPosition, result.getTotal()));
    }

    listIdentifiersMap.put("headers", headers.iterator());
    listIdentifiersMap.put("identifiers", identifiers.iterator());
    return listIdentifiersMap;
  }

  /**
   * Utility method to construct a Record object for a specified metadataFormat
   * from a native record
   *
   * @param nativeItem     native item from the backend
   * @param metadataPrefix the desired metadataPrefix for performing the crosswalk
   * @return the <record/> String
   * @exception CannotDisseminateFormatException the record is not available for
   *                                             the specified metadataPrefix.
   */
  private String constructRecord(Item nativeItem, String metadataPrefix) throws CannotDisseminateFormatException {
    String schemaURL = null;
    Iterator setSpecs = getSetSpecs(nativeItem);
    Iterator abouts = getAbouts(nativeItem);

    if (metadataPrefix != null) {
      if ((schemaURL = getCrosswalks().getSchemaURL(metadataPrefix)) == null)
        throw new CannotDisseminateFormatException(metadataPrefix);
    }
    return getRecordFactory().create(nativeItem, schemaURL, metadataPrefix, setSpecs, abouts);
  }

  /**
   * get an Iterator containing the setSpecs for the nativeItem
   *
   * @param rs ResultSet containing the nativeItem
   * @return an Iterator containing the list of setSpec values for this nativeItem
   */
  private Iterator getSetSpecs(Item nativeItem) {
    return null;
  }

  /**
   * get an Iterator containing the abouts for the nativeItem
   *
   * @param rs ResultSet containing the nativeItem
   * @return an Iterator containing the list of about values for this nativeItem
   */
  private Iterator getAbouts(Item nativeItem) {
    return null;
  }

  /**
   * Retrieve a list of records that satisfy the specified criteria
   *
   * @param from           beginning date in the form of YYYY-MM-DD or null if
   *                       earliest date is desired
   * @param until          ending date in the form of YYYY-MM-DD or null if latest
   *                       date is desired
   * @param set            set name or null if no set is desired
   * @param metadataPrefix the OAI metadataPrefix
   * @return a Map object containing an optional "resumptionToken" key/value pair
   *         and a "records" Iterator object. The "records" Iterator contains a
   *         set of Records objects.
   * @exception OAIBadRequestException signals an http status code 400 problem
   * @exception OAIInternalServerError signals an http status code 500 problem
   */
  public Map listRecords(String from, String until, String set, String metadataPrefix)
      throws CannotDisseminateFormatException, OAIInternalServerError, NoItemsMatchException {
    SearchResult<Item> result = null;
    Map<String, Object> listRecordsMap = new HashMap<String, Object>();
    ArrayList<String> records = new ArrayList<String>();

    try {
      result = backendService.getItems(true, 0, maxListSize, set, from, until);

      if (result == null || result.getData().isEmpty()) {
        throw new NoItemsMatchException();
      }

      for (Item item : result.getData()) {
        String record = constructRecord(item, metadataPrefix);
        records.add(record);
      }

    } catch (IOException e) {
      throw new OAIInternalServerError(e.getMessage());
    }

    int cursorPosition = (result.getOffset() + result.getData().size());

    /*****************************************************************
     * Construct the resumptionToken
     *****************************************************************/
    if (cursorPosition < result.getTotal()) {
      ResumptionToken resumptionToken = new ResumptionToken();
      resumptionToken.setSet(set);
      resumptionToken.setFrom(from);
      resumptionToken.setUntil(until);
      resumptionToken.setOffset(0);
      resumptionToken.setRows(maxListSize - 1);
      resumptionToken.setMetadataPrefix(metadataPrefix);

      listRecordsMap.put("resumptionMap",
          getResumptionMap(resumptionToken.toString(), cursorPosition, result.getTotal()));
    }

    listRecordsMap.put("records", records.iterator());
    return listRecordsMap;
  }

  /**
   * Retrieve the next set of records associated with the resumptionToken
   *
   * @param resumptionToken implementation-dependent format taken from the
   *                        previous listRecords() Map result.
   * @return a Map object containing an optional "resumptionToken" key/value pair
   *         and a "records" Iterator object. The "records" Iterator contains a
   *         set of Records objects.
   * @throws NoItemsMatchException
   * @exception OAIBadRequestException signals an http status code 400 problem
   */
  public Map listRecords(String resumptionToken)
      throws BadResumptionTokenException, OAIInternalServerError {
    Map<String, Object> listRecordsMap = new HashMap<String, Object>();
    ArrayList<String> records = new ArrayList<String>();
    SearchResult<Item> result = null;

    ResumptionToken token = new ResumptionToken(resumptionToken);
    token.setOffset(token.getRows());

    try {
      result = backendService.getItems(true, token.getOffset(), maxListSize, token.getSet(), token.getFrom(),
          token.getUntil());

      if (result == null || result.getData().isEmpty()) {
        throw new OAIInternalServerError("There is a problem wit the resumption token. Cannot retrieve any results!");
      }

      for (Item item : result.getData()) {
        String record = constructRecord(item, token.getMetadataPrefix());
        records.add(record);
      }

    } catch (IOException e) {
      throw new OAIInternalServerError(e.getMessage());
    } catch (CannotDisseminateFormatException e) {
      e.printStackTrace();
    }

    int cursorPosition = (result.getOffset() + result.getData().size());

    if (cursorPosition < result.getTotal()) {
      listRecordsMap.put("resumptionMap",
          getResumptionMap(resumptionToken.toString(), cursorPosition, result.getTotal()));
    }

    listRecordsMap.put("records", records.iterator());
    return listRecordsMap;
  }

  public Map listSets() throws NoSetHierarchyException, OAIInternalServerError {
    Map<String, Iterator> setsIterator = new HashMap<String, Iterator>();
    List<String> xmlSets = new ArrayList<String>();

    logger.info("listSets");

    List<Set> backendSetList;
    try {
      backendSetList = backendService.getSets();
    } catch (IOException e) {
      throw new OAIInternalServerError("Cannot retrieve sets from backend");
    }

    for (Set setItem : backendSetList) {
      xmlSets.add(getSetXML(setItem));
    }

    setsIterator.put("sets", xmlSets.iterator());
    return setsIterator;

  }

  public String getSetXML(Set setItem) throws IllegalArgumentException {
    StringBuffer sb = new StringBuffer();
    sb.append("<set>");
    sb.append("<setSpec>");
    sb.append(setItem.getSpec() != null ? OAIUtil.xmlEncode(setItem.getSpec()) : "");
    sb.append("</setSpec>");
    sb.append("<setName>");
    sb.append(setItem.getName() != null ? OAIUtil.xmlEncode(setItem.getName()) : "");
    sb.append("</setName>");
    if (setItem.getDescription() != null) {
      sb.append("<setDescription>");
      sb.append(
          "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">");
      sb.append("<dc:description>");
      sb.append(OAIUtil.xmlEncode(setItem.getDescription()));
      sb.append("</dc:description>");
      sb.append("</oai_dc:dc>");

      sb.append("</setDescription>");
    }
    sb.append("</set>");

    logger.info("getSetXML: " + sb.toString());
    return sb.toString();
  }

  public Map listSets(String resumptionToken) throws BadResumptionTokenException {
    throw new BadResumptionTokenException();
  }

  /**
   * close the repository
   */
  public void close() {
  }

  public BackendService getBackendService() {
    return backendService;
  }

  public void setBackendService(BackendService backendService) {
    this.backendService = backendService;
  }
}
