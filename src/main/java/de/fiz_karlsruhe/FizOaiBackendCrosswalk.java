package de.fiz_karlsruhe;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import de.fiz_karlsruhe.model.Item;

public class FizOaiBackendCrosswalk extends Crosswalk {

  final static Logger logger = LogManager.getLogger(FizOaiBackendCrosswalk.class);

  public FizOaiBackendCrosswalk(String schemaLocation, String crosswalkName) {
    super(schemaLocation);
  }


  /**
   * Can this nativeItem be represented in other formats?
   * 
   * @param nativeItem a record in native format
   * @return true if other formats are possible, false otherwise.
   */
  public boolean isAvailableFor(Object nativeItem) {
    return true;
  }

  public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
    String content = ((Item) nativeItem).getContent().getContent();

    // Replace xml Starttags!
    return content.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
  }
}