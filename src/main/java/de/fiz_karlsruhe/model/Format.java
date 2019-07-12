package de.fiz_karlsruhe.model;

public class Format {
  private String metadataPrefix;
  
  private String schemaLocation;
  
  private String schemaNamespace;
  
  private String identifierXpath;

  public String getMetadataPrefix() {
    return metadataPrefix;
  }

  public void setMetadataPrefix(String metadataPrefix) {
    this.metadataPrefix = metadataPrefix;
  }

  public String getSchemaLocation() {
    return schemaLocation;
  }

  public void setSchemaLocation(String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }

  public String getSchemaNamespace() {
    return schemaNamespace;
  }

  public void setSchemaNamespace(String schemaNamespace) {
    this.schemaNamespace = schemaNamespace;
  }

  public String getIdentifierXpath() {
    return identifierXpath;
  }

  public void setIdentifierXpath(String identifierXpath) {
    this.identifierXpath = identifierXpath;
  }

  @Override
  public String toString() {
    return "Format [metadataPrefix=" + metadataPrefix + ", schemaLocation=" + schemaLocation + ", schemaNamespace="
        + schemaNamespace + ", identifierXpath=" + identifierXpath
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((metadataPrefix == null) ? 0 : metadataPrefix.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Format other = (Format) obj;
    if (metadataPrefix == null) {
      if (other.metadataPrefix != null)
        return false;
    } else if (!metadataPrefix.equals(other.metadataPrefix))
      return false;
    return true;
  }
}
