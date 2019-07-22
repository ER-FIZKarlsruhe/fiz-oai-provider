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

import java.util.HashMap;
import java.util.List;

public class Item {

  private String identifier;

  private String datestamp;
  
  private String deleteFlag;
  
  private Content content;
  
  private String ingestFormat;

  private List<String> tags;

  private List<Set> sets;
  
  private List<Format> formats;
  
  public String getIdentifier() {
    return identifier;
  }
  
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
  
  public String getDatestamp() {
    return datestamp;
  }
  
  public void setDatestamp(String datestamp) {
    this.datestamp = datestamp;
  }
  
  public String getDeleteFlag() {
    return deleteFlag;
  }
  
  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }
  
  public Content getContent() {
    return content;
  }
  
  public void setContent(Content content) {
    this.content = content;
  }
  
  public List<String> getTags() {
    return tags;
  }
  
  public void setTags(List<String> tags) {
    this.tags = tags;
  }
  
  
  public List<Format> getFormats() {
    return formats;
  }

  public void setFormats(List<Format> formats) {
    this.formats = formats;
  }

  public String getIngestFormat() {
    return ingestFormat;
  }

  public void setIngestFormat(String ingestFormat) {
    this.ingestFormat = ingestFormat;
  }

  public List<Set> getSets() {
    return sets;
  }

  public void setSets(List<Set> sets) {
    this.sets = sets;
  }

  public HashMap<String,Object> toNativeitem() {
    HashMap<String,Object> map = new HashMap<String,Object>();
    map.put("localIdentifier", this.identifier);
    map.put("lastModified", this.datestamp);
    map.put("recordBytes", this.content);

    return map;
  }

  @Override
  public String toString() {
    return "Item [identifier=" + identifier + ", datestamp=" + datestamp + ", deleteFlag=" + deleteFlag + ", content="
        + content + ", tags=" + tags + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
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
    Item other = (Item) obj;
    if (identifier == null) {
      if (other.identifier != null)
        return false;
    } else if (!identifier.equals(other.identifier))
      return false;
    return true;
  }
  
}
