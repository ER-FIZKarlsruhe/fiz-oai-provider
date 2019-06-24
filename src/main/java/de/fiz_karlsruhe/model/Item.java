package de.fiz_karlsruhe.model;

import java.util.HashMap;
import java.util.List;

public class Item {

  private String identifier;

  private String datestamp;
  
  private String deleteFlag;
  
  private String content;

  private List<String> sets;

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
  
  public String getContent() {
    return content;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public List<String> getSets() {
    return sets;
  }
  
  public void setSets(List<String> sets) {
    this.sets = sets;
  }
  
  
  public List<Format> getFormats() {
    return formats;
  }

  public void setFormats(List<Format> formats) {
    this.formats = formats;
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
        + content + ", sets=" + sets + "]";
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
