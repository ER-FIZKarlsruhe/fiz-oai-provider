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

package de.fiz_karlsruhe.model;

public class Format {
  private String metadataPrefix;
  
  private String schemaLocation;
  
  private String schemaNamespace;
  
  private String identifierXpath;
  
  private String status;

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

  public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

@Override
  public String toString() {
    return "Format [metadataPrefix=" + metadataPrefix + ", schemaLocation=" + schemaLocation + ", schemaNamespace="
        + schemaNamespace + ", identifierXpath=" + identifierXpath + ", status=" + status + "]";
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
