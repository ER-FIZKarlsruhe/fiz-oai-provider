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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Timer;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ORG.oclc.oai.server.catalog.RecordFactory;
import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.Transformation;
import de.fiz_karlsruhe.service.BackendService;

/**
 * FileRecordFactory converts native XML "items" to "record" Strings. This
 * factory assumes the native XML item looks exactly like the <record> element
 * of an OAI GetRecord response, with the possible exception that the <metadata>
 * element contains multiple metadataFormats from which to choose.
 */
public class FizRecordFactory extends RecordFactory {

  private String repositoryIdentifier = null;
  
  private Timer  refreshFormatTimer = null;

  final static Logger logger = LogManager.getLogger(FizRecordFactory.class);

  /**
   * Construct an FileRecordFactory capable of producing the Crosswalk(s)
   * specified in the properties file.
   * 
   * @param properties Contains information to configure the factory:
   *                   specifically, the names of the crosswalk(s) supported
   * @exception IllegalArgumentException Something is wrong with the argument.
   * @throws IOException
   */
  public FizRecordFactory(Properties properties) throws IllegalArgumentException, Exception {
    super();
    
    List<Format> formats = initFormats(properties);
    List<Transformation> transformations = initTransformations(properties);
    this.formatRegistry = new FormatRegistry(formats, transformations);
    
    repositoryIdentifier = properties.getProperty("FizRecordFactory.repositoryIdentifier");
    
    if (repositoryIdentifier == null) {
      logger.warn("FizRecordFactory.repositoryIdentifier is missing from the properties file");
    }
    
    
    String refreshFormatSeconds = properties.getProperty("FizRecordFactory.refreshFormatSeconds");
    if (refreshFormatSeconds == null) {
        logger.warn("FizRecordFactory.refreshFormatSeconds from the properties file. Set 2 minutes default!");
      //By default refresh every 2 minutes
      refreshFormatSeconds = "120";
    }
    
    refreshFormatTimer = new Timer("Timer");
    refreshFormatTimer.schedule(new RefreshFormatRegistry(this.formatRegistry, properties), 60000L, Integer.parseInt(refreshFormatSeconds) * 1000);
  }

  public static List<Format> initFormats(Properties properties) {
    logger.info("initFormats");

    String backendBaseUrl = properties.getProperty("FizOaiBackend.baseURL");
    logger.info("backendBaseUrl: {}", backendBaseUrl);
    if (backendBaseUrl == null) {
      throw new IllegalArgumentException("FizOaiBackend.baseURL is missing from the properties file");
    }

    List<Format> formats = null;
    try {
      formats = BackendService.getInstance(backendBaseUrl).getFormats();
    } catch (IOException e) {
      logger.error("Cannot read supported formats from backend", e);
    }

    return formats;
  }
  
  public static List<Transformation> initTransformations(Properties properties) {
    logger.info("initTransformations");

    String backendBaseUrl = properties.getProperty("FizOaiBackend.baseURL");
    logger.info("backendBaseUrl: {}", backendBaseUrl);
    if (backendBaseUrl == null) {
      throw new IllegalArgumentException("FizOaiBackend.baseURL is missing from the properties file");
    }

    List<Transformation> transformations = null;
    try {
      transformations = BackendService.getInstance(backendBaseUrl).getTransformations();
    } catch (IOException e) {
      logger.error("Cannot read supported transformations from backend", e);
    }
    
    return transformations;
  }

  /**
   * Utility method to parse the 'local identifier' from the OAI identifier
   *
   * @param identifier OAI identifier (e.g. oai:oaicat.oclc.org:ID/12345)
   * @return local identifier (e.g. ID/12345).
   */
  public String fromOAIIdentifier(String identifier) {
    if (StringUtils.isEmpty(repositoryIdentifier)) {
      return identifier;
    }

    try {
      StringTokenizer tokenizer = new StringTokenizer(identifier, ":");
      tokenizer.nextToken();
      tokenizer.nextToken();
      return tokenizer.nextToken();
    } catch (Exception e) {
      logger.error("Cannot parse identifier " + identifier, e);
      return null;
    }
  }

  /**
   * Construct an OAI identifier from the native item
   *
   * @param nativeItem native Item object
   * @return OAI identifier
   */
  public String getOAIIdentifier(Object nativeItem) {
    StringBuilder sb = new StringBuilder();
    if (StringUtils.isNotEmpty(repositoryIdentifier)) {
      sb.append("oai:");
      sb.append(repositoryIdentifier);
      sb.append(":");
    }
    sb.append(getLocalIdentifier(nativeItem));
    return sb.toString();
  }

  /**
   * Extract the local identifier from the native item
   *
   * @param nativeItem native Item object
   * @return local identifier
   */
  @Override
  public String getLocalIdentifier(Object nativeItem) {
    logger.debug("nativeItem: " + nativeItem);
    return ((Item) nativeItem).getIdentifier();
  }
  
  public Timer  getRefreshFormatTimer() {
      return refreshFormatTimer;
  }

  /**
   * get the datestamp from the item
   *
   * @param nativeItem a native item presumably containing a datestamp somewhere
   * @return a String containing the datestamp for the item
   * @exception IllegalArgumentException Something is wrong with the argument.
   */
  public String getDatestamp(Object nativeItem) throws IllegalArgumentException {
    return ((Item) nativeItem).getDatestamp();
  }

  /**
   * get the setspec from the item
   *
   * @param nativeItem a native item presumably containing a setspec somewhere
   * @return a String containing the setspec for the item
   * @exception IllegalArgumentException Something is wrong with the argument.
   */
  public Iterator getSetSpecs(Object nativeItem) throws IllegalArgumentException {
    return ((Item) nativeItem).getSets().iterator();
  }

  /**
   * Get the about elements from the item
   *
   * @param nativeItem a native item presumably containing about information
   *                   somewhere
   * @return a Iterator of Strings containing &lt;about&gt;s for the item
   * @exception IllegalArgumentException Something is wrong with the argument.
   */
  public Iterator getAbouts(Object nativeItem) throws IllegalArgumentException {
    return null;
  }

  /**
   * Is the record deleted?
   *
   * @param nativeItem a native item presumably containing a possible delete
   *                   indicator
   * @return true if record is deleted, false if not
   * @exception IllegalArgumentException Something is wrong with the argument.
   */
  public boolean isDeleted(Object nativeItem) throws IllegalArgumentException {
    if (nativeItem != null) {
      return Boolean.valueOf(((Item) nativeItem).getDeleteFlag());
    }
    
    return false;
  }

  /**
   * Allows classes that implement RecordFactory to override the default create()
   * method. This is useful, for example, if the entire &lt;record&gt; is already
   * packaged as the native record. Return null if you want the default handler to
   * create it by calling the methods above individually.
   * 
   * @param nativeItem the native record
   * @return a String containing the OAI &lt;record&gt; or null if the default
   *         method should be used.
   */
  public String quickCreate(Object nativeItem, String schemaLocation, String metadataPrefix) {
    // Don't perform quick creates
    return null;
  }
  
  
  
}
