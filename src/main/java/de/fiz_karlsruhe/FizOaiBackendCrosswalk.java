package de.fiz_karlsruhe;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;

public class FizOaiBackendCrosswalk extends Crosswalk {
  public FizOaiBackendCrosswalk(String schemaLocation) {
    super(schemaLocation);
  }

  @Override
  public boolean isAvailableFor(Object nativeItem) {
    return false;
  }

  @Override
  public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
    // TODO Auto-generated method stub
    return null;
  }
}
