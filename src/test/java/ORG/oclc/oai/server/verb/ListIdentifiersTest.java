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

public class ListIdentifiersTest {

  private static final String HEADER =
      "<header><identifier>oai:example.org:1</identifier><datestamp>2020-01-01</datestamp></header>";

  @Test
  public void constructListsHeadersForTheDefaultDateRangeWhenNoOptionalParamsAreGiven() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    Map listIdentifiersResult = new HashMap();
    listIdentifiersResult.put("headers", List.of(HEADER).iterator());
    when(catalog.listIdentifiers("0001-01-01", "9999-12-31", null, "oai_dc")).thenReturn(listIdentifiersResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai_dc", null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListIdentifiers.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<ListIdentifiers>"));
    assertTrue(result.contains(HEADER));
  }

  @Test
  public void constructReportsBadArgumentWhenTheRepositoryIsNotHarvestable() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.isHarvestable()).thenReturn(false);
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai_dc", null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListIdentifiers.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("Database is unavailable for harvesting"));
    assertFalse(result.contains("<ListIdentifiers>"));
  }

  @Test
  public void constructReturnsBadArgumentWhenMetadataPrefixIsMissing() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null, null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListIdentifiers.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badArgument\">"));
  }

  @Test
  public void constructReturnsCannotDisseminateFormatWhenPrefixIsUnknown() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    HttpServletRequest request = VerbTestSupport.mockRequest(params("unknown_prefix", null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListIdentifiers.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"cannotDisseminateFormat\">"));
  }

  @Test
  public void constructFetchesTheNextPageUsingTheOldResumptionToken() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Map listIdentifiersResult = new HashMap();
    listIdentifiersResult.put("headers", List.of(HEADER).iterator());
    when(catalog.listIdentifiers("tok123")).thenReturn(listIdentifiersResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null, "tok123"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListIdentifiers.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains(HEADER));
  }

  @Test
  public void constructEmbedsTheErrorWhenTheResumptionTokenIsBad() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.listIdentifiers("badtoken")).thenThrow(new BadResumptionTokenException());
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null, "badtoken"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListIdentifiers.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badResumptionToken\">"));
  }

  private static Map<String, String> params(String metadataPrefix, String resumptionToken) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "ListIdentifiers");
    if (metadataPrefix != null) {
      params.put("metadataPrefix", metadataPrefix);
    }
    if (resumptionToken != null) {
      params.put("resumptionToken", resumptionToken);
    }
    return params;
  }
}
