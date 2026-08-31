/*
 * Copyright 2026 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
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

package ORG.oclc.oai.server.verb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

import ORG.oclc.oai.server.catalog.AbstractCatalog;

/**
 * ListRecords mirrors ListIdentifiers' request handling almost exactly (see
 * {@link ListIdentifiersTest} for the fuller set of argument-validation cases); these tests
 * cover the parts that actually differ: the "records" (not "headers") result key and the
 * isHarvestable short-circuit.
 */
public class ListRecordsTest {

  private static final String RECORD =
      "<record><header><identifier>oai:example.org:1</identifier></header><metadata>stub</metadata></record>";

  @Test
  public void constructListsRecordsForTheDefaultDateRangeWhenNoOptionalParamsAreGiven() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    Map listRecordsResult = new HashMap();
    listRecordsResult.put("records", List.of(RECORD).iterator());
    when(catalog.listRecords("0001-01-01", "9999-12-31", null, "oai_dc")).thenReturn(listRecordsResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai_dc", null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListRecords.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<ListRecords>"));
    assertTrue(result.contains(RECORD));
  }

  @Test
  public void constructReportsBadArgumentWhenTheRepositoryIsNotHarvestable() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.isHarvestable()).thenReturn(false);
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai_dc", null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListRecords.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("Database is unavailable for harvesting"));
    assertFalse(result.contains("<ListRecords>"));
  }

  @Test
  public void constructFetchesTheNextPageUsingTheOldResumptionToken() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Map listRecordsResult = new HashMap();
    listRecordsResult.put("records", List.of(RECORD).iterator());
    when(catalog.listRecords("tok123")).thenReturn(listRecordsResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null, "tok123"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListRecords.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains(RECORD));
  }

  @Test
  public void constructEmbedsTheErrorWhenTheResumptionTokenIsBad() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.listRecords("badtoken")).thenThrow(new BadResumptionTokenException());
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null, "badtoken"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListRecords.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badResumptionToken\">"));
  }

  private static Map<String, String> params(String metadataPrefix, String resumptionToken) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "ListRecords");
    if (metadataPrefix != null) {
      params.put("metadataPrefix", metadataPrefix);
    }
    if (resumptionToken != null) {
      params.put("resumptionToken", resumptionToken);
    }
    return params;
  }
}
