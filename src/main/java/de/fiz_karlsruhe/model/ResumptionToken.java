package de.fiz_karlsruhe.model;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ORG.oclc.oai.server.verb.BadResumptionTokenException;

public class ResumptionToken {

  final static Logger logger = LogManager.getLogger(ResumptionToken.class);

  enum ResumptionTokenParameters {
    METADATAPREFIX, FROM, UNTIL, SET, OFFSET, ROWS, TOTAL
  }

  private String metadataPrefix;
  private String from;
  private String until;
  private String set;
  private Integer offset;
  private Integer rows;
  private Integer total;
  private String scrollId;

  public ResumptionToken() {
  }

  public ResumptionToken(String token) throws BadResumptionTokenException {
    if (token == null || token.isEmpty()) {
      logger.error("token is empty");
      throw new BadResumptionTokenException();
    }

    String[] parameterSplits = token.split("&");

    for (String keyValueSplit : parameterSplits) {
      try {
        String key = keyValueSplit.split("=")[0];

        // Check parameter is in enum
        ResumptionTokenParameters.valueOf(key.toUpperCase());
        if (parameterSplits.length == 1) {
          continue;
        }

        String value = keyValueSplit.split("=")[1];

        BeanUtils.setProperty(this, key, value);
      } catch (Exception e) {
        logger.error("cannot extract token parameters");
        throw new BadResumptionTokenException();
      }
    }

    validate();

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

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getRows() {
    return rows;
  }

  public void setRows(Integer cursor) {
    this.rows = cursor;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public String getToken() throws BadResumptionTokenException {
    validate();

    StringBuffer tokenSb = new StringBuffer();
    tokenSb.append("offset=" + offset);
    tokenSb.append("&rows=" + rows);

    if (set != null) {
      tokenSb.append("&set=" + set);
    }

    if (from != null) {
      tokenSb.append("&from=" + from);
    }

    if (total != null) {
      tokenSb.append("&total=" + total);
    }

    if (until != null) {
      tokenSb.append("&until=" + until);
    }

    if (metadataPrefix != null) {
      tokenSb.append("&metadataPrefix=" + metadataPrefix);
    }

    return tokenSb.toString();
  }

  public void validate() throws BadResumptionTokenException {
    if (offset == null || rows == null || total == null) {
      logger.error("token offset, rows and tola must not be null");
      throw new BadResumptionTokenException();
    }

    if (offset < 0 || rows <= 0 || total < 0) {
      logger.error("offset, rows and total must have positive values");
      throw new BadResumptionTokenException();
    }

    Instant frominstant = null;
    Instant untilinstant = null;
    try {
      
      if (from != null && !from.isEmpty()) {
        frominstant = Instant.parse ( from );
      }
      
      if (until != null && !until.isEmpty()) {
        untilinstant = Instant.parse ( until );
      }
      
    } catch (DateTimeParseException e) {
      throw new BadResumptionTokenException();
    }

    if (frominstant != null && untilinstant != null) {
      if (frominstant.compareTo(untilinstant) > 0) {
        throw new BadResumptionTokenException();
      }
    }
    
  }

}
