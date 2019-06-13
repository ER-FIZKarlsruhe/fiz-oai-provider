package de.fiz_karlsruhe.model;

import java.util.HashMap;
import java.util.List;

public class Item {

  private String identifier;

  private String datestamp;
  
  private String deleteFlag;
  
  private String content;

  private List<String> sets;

  
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
  
  
  public HashMap toNativeitem() {
    HashMap map = new HashMap();
    map.put("localIdentifier", this.identifier);
    map.put("lastModified", this.datestamp);
    map.put("recordBytes", this.content);

    return map;
  }
  
}
