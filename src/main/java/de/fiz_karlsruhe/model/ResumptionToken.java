package de.fiz_karlsruhe.model;

import ORG.oclc.oai.server.verb.BadResumptionTokenException;

public class ResumptionToken {

  private String metadataPrefix;
  private String from;
  private String until;
  private String set;
  private int offset;
  private int cursor;
  private int total;

  public ResumptionToken() {
  }

  public ResumptionToken(String token) throws BadResumptionTokenException {
    if (token == null || token.isEmpty()) {
      throw new BadResumptionTokenException();
    }

    String[] splits = token.split("&");
    for (String split : splits) {
      String key = split.split("=")[0];
      String value = split.split("=")[1];
//      BeanUtil
    }

  }

  public String getMetadataPrefix() {
    return metadataPrefix;
  }

  public void setMetadataPrefix(String metadataPrefix) {
    this.metadataPrefix = metadataPrefix;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getUntil() {
    return until;
  }

  public void setUntil(String until) {
    this.until = until;
  }

  public String getSet() {
    return set;
  }

  public void setSet(String set) {
    this.set = set;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getCursor() {
    return cursor;
  }

  public void setCursor(int cursor) {
    this.cursor = cursor;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public String getToken() {
    StringBuffer tokenSb = new StringBuffer();
    tokenSb.append("offset=" + offset);
    tokenSb.append("&cursor=" + cursor);

    if (set != null) {
      tokenSb.append("&set=" + set);
    }

    if (from != null) {
      tokenSb.append("&from=" + from);
    }

    if (until != null) {
      tokenSb.append("&until=" + until);
    }

    if (metadataPrefix != null) {
      tokenSb.append("&metadataPrefix=" + metadataPrefix);
    }

    return tokenSb.toString();
  }

}
