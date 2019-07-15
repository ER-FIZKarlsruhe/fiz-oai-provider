package de.fiz_karlsruhe;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.service.BackendService;

public class FizOaiBackendCrosswalk extends Crosswalk {

  private String formatFrom;
  
  private String formatTo;
  
  final static Logger logger = LogManager.getLogger(FizOaiBackendCrosswalk.class);

  public FizOaiBackendCrosswalk( String schemaLocation, String crosswalkName, String formatFrom, String formatTo) {
    super(schemaLocation);
    this.formatFrom = formatFrom;
    this.formatTo = formatTo;
  }


  /**
   * Can this nativeItem be represented in other formats?
   * 
   * @param nativeItem a record in native format
   * @return true if other formats are possible, false otherwise.
   */
  public boolean isAvailableFor(Object nativeItem) {
    Item item = (Item) nativeItem;
    
    return item.getIngestFormat().equals(formatFrom);
  }

  public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
    String metadata = null;
    Item item = (Item) nativeItem;
    Item crosswalkItem;
    try {
      crosswalkItem = BackendService.getInstance().getItem(item.getIdentifier(), formatTo);
      metadata = crosswalkItem.getContent().getContent().replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Replace xml Starttags!
    return metadata;
  }
}