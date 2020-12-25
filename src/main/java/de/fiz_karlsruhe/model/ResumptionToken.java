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

import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.apache.commons.beanutils.BeanUtils;
import  org.apache.logging.log4j.LogManager;
import  org.apache.logging.log4j.Logger;

import ORG.oclc.oai.server.verb.BadResumptionTokenException;

public class ResumptionToken {

  final static Logger logger = LogManager.getLogger(ResumptionToken.class);

  enum ResumptionTokenParameters {
    METADATAPREFIX, FROM, UNTIL, SET, LASTITEMID, ROWS, TOTAL
  }

  private String metadataPrefix;
  private String from;
  private String until;
  private String set;
  private Integer rows;
  private Long total;
  private String lastItemId;

  public ResumptionToken() {
  }

  public ResumptionToken(String token) throws BadResumptionTokenException {
    if (token == null || token.isEmpty()) {
      logger.error("token is empty");
      throw new BadResumptionTokenException();
    }

    String[] parameterSplits = token.split("@@");

    for (String keyValueSplit : parameterSplits) {
      try {
        String key = keyValueSplit.split("=", 2)[0];

        // Check parameter is in enum
        ResumptionTokenParameters.valueOf(key.toUpperCase());
        if (parameterSplits.length == 1) {
          continue;
        }

        String value = keyValueSplit.split("=", 2)[1];

        BeanUtils.setProperty(this, key, value);
      } catch (Exception e) {
        logger.error("cannot extract token parameters");
        throw new BadResumptionTokenException();
      }
    }

    validate();

  }

  public String getLastItemId() {
    return lastItemId;
  }

  public void setLastItemId(String lastItemId) {
    this.lastItemId = lastItemId;
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

  public Integer getRows() {
    return rows;
  }

  public void setRows(Integer cursor) {
    this.rows = cursor;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public String getToken() throws BadResumptionTokenException {
    validate();

    StringBuilder tokenSb = new StringBuilder();
    tokenSb.append("rows=" + rows);

    tokenSb.append("@@lastItemId=" + lastItemId);
    
    if (set != null) {
      tokenSb.append("@@set=" + set);
    }

    if (from != null) {
      tokenSb.append("@@from=" + from);
    }

    if (total != null) {
      tokenSb.append("@@total=" + total);
    }

    if (until != null) {
      tokenSb.append("@@until=" + until);
    }

    if (metadataPrefix != null) {
      tokenSb.append("@@metadataPrefix=" + metadataPrefix);
    }

    return tokenSb.toString();
  }

  public void validate() throws BadResumptionTokenException {
    if (lastItemId == null || rows == null || total == null) {
      logger.error("token offset, rows and tola must not be null");
      throw new BadResumptionTokenException();
    }

    if (rows <= 0 || total < 0) {
      logger.error("offset, rows and total must have positive values");
      throw new BadResumptionTokenException();
    }

    Instant frominstant = null;
    Instant untilinstant = null;

    try {

      if (from != null && !from.isEmpty()) {
        frominstant = Instant.parse(from);
      }

      if (until != null && !until.isEmpty()) {
        untilinstant = Instant.parse(until);
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

  @Override
  public String toString() {
    try {
      return getToken();
    } catch (BadResumptionTokenException e) {
      return "InvalidToken";
    }
  }

}
