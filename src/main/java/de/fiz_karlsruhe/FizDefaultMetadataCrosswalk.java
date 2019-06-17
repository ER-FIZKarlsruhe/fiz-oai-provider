package de.fiz_karlsruhe;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import de.fiz_karlsruhe.model.Item;



public class FizDefaultMetadataCrosswalk extends Crosswalk {

  public FizDefaultMetadataCrosswalk(String schemaLocation) {
    super(schemaLocation);
  }

  final static Logger logger = LogManager.getLogger(FizDefaultMetadataCrosswalk.class);
  
  private boolean debug = true;
 

  /**
   * Can this nativeItem be represented in other formats?
   * 
   * @param nativeItem a record in native format
   * @return true if other formats are possible, false otherwise.
   */
  public boolean isAvailableFor(Object nativeItem) {
    return true;
  }

  /**
   * Perform the actual crosswalk.
   *
   * @param nativeItem the native "item". In this case, it is already formatted as
   *                   an OAI <record> element, with the possible exception that
   *                   multiple metadataFormats are present in the <metadata>
   *                   element.
   * @return a String containing the FileMap to be stored within the <metadata>
   *         element.
   * @exception CannotDisseminateFormatException nativeItem doesn't support this
   *                                             format.
   */
  public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
   return ((Item)nativeItem).getContent();
  }
}