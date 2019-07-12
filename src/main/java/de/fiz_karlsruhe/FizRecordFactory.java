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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;

import ORG.oclc.oai.server.catalog.RecordFactory;
import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.crosswalk.CrosswalkItem;
import ORG.oclc.oai.server.verb.OAIInternalServerError;
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
    super(initCrosswalks(properties));

    repositoryIdentifier = properties.getProperty("FizRecordFactory.repositoryIdentifier");
    if (repositoryIdentifier == null) {
      throw new IllegalArgumentException("FizRecordFactory.repositoryIdentifier is missing from the properties file");
    }
  }

  private static HashMap<String, CrosswalkItem> initCrosswalks(Properties properties)
      throws IOException, JSONException, OAIInternalServerError {
    logger.info("initCrosswalks");
    HashMap<String, CrosswalkItem> crosswalksMap = new HashMap<String, CrosswalkItem>();

    String backendBaseUrl = properties.getProperty("FizOaiBackend.baseURL");
    logger.info("backendBaseUrl: " + backendBaseUrl);
    if (backendBaseUrl == null) {
      throw new IllegalArgumentException("FizOaiBackend.baseURL is missing from the properties file");
    }

    String defaultMetadataPrefix = properties.getProperty("FizRecordFactory.defaultMetadataPrefix");
    logger.info("defaultMetadataPrefix: " + defaultMetadataPrefix);
    if (defaultMetadataPrefix == null) {
      throw new IllegalArgumentException("FizRecordFactory.defaultMetadataPrefix is missing from the properties file");
    }

    //Add crosswalk for the default metadataPrefix 
    Format defaultFormat = BackendService.getInstance(backendBaseUrl).getFormat(defaultMetadataPrefix);
    if (defaultFormat != null) {
      Crosswalk fizDefaultMetadataCrosswalk = new FizDefaultMetadataCrosswalk(defaultFormat.getSchemaLocation());
      CrosswalkItem crosswalkItem = new CrosswalkItem(defaultFormat.getMetadataPrefix(),
          defaultFormat.getSchemaLocation(), defaultFormat.getSchemaNamespace(), fizDefaultMetadataCrosswalk);
      crosswalksMap.put(defaultFormat.getMetadataPrefix(), crosswalkItem);
    }
    //Add further crosswalks for all available transformations
    List<Transformation> backendCrosswalks = BackendService.getInstance(backendBaseUrl).getTransformations();

    if (backendCrosswalks != null) {
      for (Transformation crosswalk : backendCrosswalks) {
        if (crosswalk.getName() == null) {
          logger.warn("skip crosswalk as no name is defined");
          continue;
        }
        logger.info("Add crosswalk" + crosswalk.getName());
        Format formatFrom = BackendService.getInstance(backendBaseUrl).getFormat(crosswalk.getFormatFrom());
        Format formatTo = BackendService.getInstance(backendBaseUrl).getFormat(crosswalk.getFormatTo());

        Crosswalk fizOaiBackendCrosswalk = new FizOaiBackendCrosswalk(formatTo.getSchemaLocation(), crosswalk.getName());
        CrosswalkItem crosswalkItem = new CrosswalkItem(formatTo.getMetadataPrefix(), formatTo.getSchemaLocation(), formatTo.getSchemaNamespace(), fizOaiBackendCrosswalk);
        crosswalksMap.put(formatTo.getMetadataPrefix(), crosswalkItem);
      }
    }

    return crosswalksMap;
  }

  /**
   * Utility method to parse the 'local identifier' from the OAI identifier
   *
   * @param identifier OAI identifier (e.g. oai:oaicat.oclc.org:ID/12345)
   * @return local identifier (e.g. ID/12345).
   */
  public String fromOAIIdentifier(String identifier) {
    try {
      StringTokenizer tokenizer = new StringTokenizer(identifier, ":");
      tokenizer.nextToken();
      tokenizer.nextToken();
      return tokenizer.nextToken();
    } catch (Exception e) {
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
    StringBuffer sb = new StringBuffer();
    sb.append("oai:");
    sb.append(repositoryIdentifier);
    sb.append(":");
    sb.append(getLocalIdentifier(nativeItem));
    return sb.toString();
  }

  /**
   * Extract the local identifier from the native item
   *
   * @param nativeItem native Item object
   * @return local identifier
   */
  public String getLocalIdentifier(Object nativeItem) {
    return ((Item) nativeItem).getIdentifier();
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
    // TODO return ((Item) nativeItem).getSets().iterator();

    return null;
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
    return Boolean.valueOf(((Item) nativeItem).getDeleteFlag());
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
