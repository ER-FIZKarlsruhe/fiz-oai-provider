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

package de.fiz_karlsruhe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

  private int maxListSize;

  private String backendBaseUrl;

  private BackendService backendService;

  public FizOAICatalog(Properties properties) {
    showBanner();
    logger.info("Initializing FizOAICatalog...");

    String temp = properties.getProperty("FizOAICatalog.maxListSize");
    setParamRegex("(.*?)");
    if (temp == null) {
      throw new IllegalArgumentException("FizOAICatalog." + "maxListSize is missing from the properties file");
    }
    maxListSize = Integer.parseInt(temp);

    logger.info("in FizOAICatalog(): maxListSize={}", maxListSize);

    backendBaseUrl = properties.getProperty("FizOaiBackend.baseURL");
    if (backendBaseUrl == null) {
      throw new IllegalArgumentException("FizOaiBackend.baseURL is missing from the properties file");
    }
    logger.info("FizOaiBackend.baseURL: {}", backendBaseUrl);

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
   * @exception CannotDisseminateFormatException signals an http status code 400 problem
   * @exception IdDoesNotExistException          signals an http status code 404 problem
   * @exception OAIInternalServerError           signals an http status code 500 problem
   */
  @Override
  public String getRecord(String oaiIdentifier, String metadataPrefix)
      throws IdDoesNotExistException, CannotDisseminateFormatException, OAIInternalServerError {
    Item nativeItem = null;
    try {
      String localIdentifier = getRecordFactory().fromOAIIdentifier(oaiIdentifier);
      if (localIdentifier == null) {
        throw new IdDoesNotExistException(oaiIdentifier);
      } 

      nativeItem = backendService.getItem(localIdentifier, metadataPrefix);
      logger.debug(nativeItem);

      if (nativeItem == null) {
        throw new IdDoesNotExistException(oaiIdentifier);
      }
      
      if (!nativeItem.getDeleteFlag() && nativeItem.getContent() == null) {
          throw new CannotDisseminateFormatException(metadataPrefix);
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
   * @exception IdDoesNotExistException signals ID not found
   * @exception OAIInternalServerError signals an unexpected exception
   * @exception OAIInternalServerError signals an http status code 500 problem
   */
  @Override
  public Vector getSchemaLocations(String oaiIdentifier)
      throws IdDoesNotExistException, OAIInternalServerError, NoMetadataFormatsException {
    Item nativeItem = null;
    try {
      String localIdentifier = getRecordFactory().fromOAIIdentifier(oaiIdentifier);
      nativeItem = backendService.getItem(localIdentifier);
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
   * @throws CannotDisseminateFormatException
   * @exception NoItemsMatchException signals no items found
   * @exception OAIInternalServerError signals an http status code 500 problem
   * @exception CannotDisseminateFormatException signals an unknown metadataPrefix
   */
  @Override
  public Map listIdentifiers(String from, String until, String set, String metadataPrefix)
      throws NoItemsMatchException, OAIInternalServerError, CannotDisseminateFormatException {
    Map<String, Object> listIdentifiersMap = new HashMap<>();
    ArrayList<String> headers = new ArrayList<>();
    ArrayList<String> identifiers = new ArrayList<>();
    SearchResult<Item> result = null;
    String schemaLocation = getFormatRegistry().getSchemaLocation(metadataPrefix);
    if (schemaLocation == null) {
      throw new CannotDisseminateFormatException("Unknown metadataPrefix");
    }

    try {
      result = backendService.getItems(false, null, maxListSize, set, from, until, metadataPrefix);

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

    /*****************************************************************
     * Construct the resumptionToken
     *****************************************************************/
    if (result.getTotal() > maxListSize && StringUtils.isNotBlank(result.getSearchMark())) {
      ResumptionToken resumptionToken = new ResumptionToken();
      resumptionToken.setSearchMark(result.getSearchMark());
      resumptionToken.setSet(set);
      resumptionToken.setFrom(from);
      resumptionToken.setUntil(until);
      resumptionToken.setRows(maxListSize);
      resumptionToken.setTotal(result.getTotal());
      resumptionToken.setMetadataPrefix(metadataPrefix);

      try {
        listIdentifiersMap.put("resumptionMap",
            getResumptionMap(resumptionToken.getToken(), (int) result.getTotal(), -1));
      } catch (BadResumptionTokenException e) {
        throw new OAIInternalServerError("An error occured while creating the ResumptionToken");
      }
    }
    listIdentifiersMap.put("headers", headers.iterator());
    listIdentifiersMap.put("identifiers", identifiers.iterator());

    return listIdentifiersMap;
  }

  /**
   * Retrieve the next set of Identifiers associated with the resumptionToken
   *
   * @param resumptionTokenParam implementation-dependent format taken from the
   *                        previous listIdentifiers() Map result.
   * @return a Map object containing an optional "resumptionToken" key/value pair
   *         and an "identifiers" Map object. The "identifiers" Map contains OAI
   *         identifier keys with corresponding values of "true" or null depending
   *         on whether the identifier is deleted or not.
   * @throws OAIInternalServerError
   * @exception BadResumptionTokenException signals an invalid resumption token
   * @exception OAIInternalServerError signals an unexpected exception
   */
  @Override
  public Map listIdentifiers(String resumptionTokenParam) throws BadResumptionTokenException, OAIInternalServerError {
    Map<String, Object> listIdentifiersMap = new HashMap<>();
    ArrayList<String> headers = new ArrayList<>();
    ArrayList<String> identifiers = new ArrayList<>();

    ResumptionToken restoken = new ResumptionToken(resumptionTokenParam);

    SearchResult<Item> result = null;
    try {
      result = backendService.getItems(false, restoken.getSearchMark(), maxListSize, restoken.getSet(),
          restoken.getFrom(), restoken.getUntil(), restoken.getMetadataPrefix());

      if (result == null || result.getData().isEmpty()) {
        throw new OAIInternalServerError("Empty resultSet");
      }

      for (Item item : result.getData()) {
        logger.debug("FizOAICatalog item: " + item);
        String[] header = getRecordFactory().createHeader(item);
        headers.add(header[0]);
        identifiers.add(header[1]);
      }

    } catch (IOException e) {
      throw new OAIInternalServerError(e.getMessage());
    }

    if (StringUtils.isNotBlank(result.getSearchMark())) {
      restoken.setSearchMark(result.getSearchMark());

      listIdentifiersMap.put("resumptionMap", getResumptionMap(restoken.toString(), (int) result.getTotal(), -1));
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
    Iterator<String> setSpecs = getSetSpecs(nativeItem);

    if (metadataPrefix != null) {
      schemaURL = getFormatRegistry().getSchemaURL(metadataPrefix);
      if (schemaURL == null)
        throw new CannotDisseminateFormatException(metadataPrefix);
    }
    return getRecordFactory().create(nativeItem, schemaURL, metadataPrefix, setSpecs, null);
  }

  /**
   * get an Iterator containing the setSpecs for the nativeItem
   *
   * @param nativeItem Item
   * @return an Iterator containing the list of setSpec values for this nativeItem
   */
  private Iterator<String> getSetSpecs(Object nativeItem) throws IllegalArgumentException {
    return ((Item) nativeItem).getSets().iterator();
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
   * @exception NoItemsMatchException signals no items not found
   * @exception CannotDisseminateFormatException signals an http status code 400 problem
   * @exception OAIInternalServerError signals an http status code 500 problem
   */
  @Override
  public Map listRecords(String from, String until, String set, String metadataPrefix)
      throws CannotDisseminateFormatException, OAIInternalServerError, NoItemsMatchException {
    SearchResult<Item> result = null;
    Map<String, Object> listRecordsMap = new HashMap<>();
    ArrayList<String> records = new ArrayList<>();

    String schemaLocation = getFormatRegistry().getSchemaLocation(metadataPrefix);
    if (schemaLocation == null) {
      throw new CannotDisseminateFormatException("Unknown metadataPrefix");
    }

    try {
      result = backendService.getItems(true, null, maxListSize, set, from, until, metadataPrefix);

      if (result == null || result.getData().isEmpty()) {
        logger.info("No items found ");
        throw new NoItemsMatchException();
      } else {
        logger.info("Number of items found: {}", result.getData().size());
      }

      for (Item item : result.getData()) {
        if (item != null) {
          String record = constructRecord(item, metadataPrefix);
          records.add(record);
        } else {
          logger.info("Item is null. Skip it");
        }
      }

    } catch (IOException e) {
      throw new OAIInternalServerError(e.getMessage());
    }

    /*****************************************************************
     * Construct the resumptionToken
     *****************************************************************/
    if (result.getTotal() > maxListSize && StringUtils.isNotBlank(result.getSearchMark())) {
      ResumptionToken resumptionToken = new ResumptionToken();
      resumptionToken.setSearchMark(result.getSearchMark());
      resumptionToken.setSet(set);
      resumptionToken.setFrom(from);
      resumptionToken.setUntil(until);
      resumptionToken.setTotal(result.getTotal());
      resumptionToken.setRows(result.getSize());
      resumptionToken.setMetadataPrefix(metadataPrefix);

      listRecordsMap.put("resumptionMap", getResumptionMap(resumptionToken.toString(), (int) result.getTotal(), -1));
    }

    listRecordsMap.put("records", records.iterator());
    return listRecordsMap;
  }

  /**
   * Retrieve the next set of records associated with the resumptionToken
   *
   * @param resumptionTokenParam implementation-dependent format taken from the
   *                        previous listRecords() Map result.
   * @return a Map object containing an optional "resumptionToken" key/value pair
   *         and a "records" Iterator object. The "records" Iterator contains a
   *         set of Records objects.
   * @throws NoItemsMatchException
   * @exception BadResumptionTokenException signals an invalid resumption token
   * @exception OAIInternalServerError signals an unexpected exception
   */
  @Override
  public Map listRecords(String resumptionTokenParam) throws BadResumptionTokenException, OAIInternalServerError {
    logger.info("listRecords resumptionToken: {}", resumptionTokenParam);
    Map<String, Object> listRecordsMap = new HashMap<>();
    ArrayList<String> records = new ArrayList<>();
    SearchResult<Item> result = null;

    ResumptionToken token = new ResumptionToken(resumptionTokenParam);

    try {
      result = backendService.getItems(true, token.getSearchMark(), maxListSize, token.getSet(), token.getFrom(),
          token.getUntil(), token.getMetadataPrefix());

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


    if (result.getTotal() > maxListSize && StringUtils.isNotBlank(result.getSearchMark())) {
      token.setSearchMark(result.getSearchMark());
      listRecordsMap.put("resumptionMap",
          getResumptionMap(token.toString(), (int) result.getTotal(), -1));
    }

    listRecordsMap.put("records", records.iterator());
    return listRecordsMap;
  }

  @Override
  public Map listSets() throws NoSetHierarchyException, OAIInternalServerError {
    Map<String, Iterator> setsIterator = new HashMap<>();
    List<String> xmlSets = new ArrayList<>();

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

  private String getSetXML(Set setItem) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();
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

    logger.info("getSetXML: {}", sb.toString());
    return sb.toString();
  }

  @Override
  public Map listSets(String resumptionToken) throws BadResumptionTokenException {
    throw new BadResumptionTokenException();
  }

  /**
   * close the repository
   */
  @Override
  public void close() {
  }

  public BackendService getBackendService() {
    return backendService;
  }

  public void setBackendService(BackendService backendService) {
    this.backendService = backendService;
  }
}
