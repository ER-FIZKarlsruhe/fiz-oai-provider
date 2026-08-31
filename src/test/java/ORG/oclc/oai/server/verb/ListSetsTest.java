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

public class ListSetsTest {

  @Test
  public void constructListsSetsAndOmitsResumptionTokenWhenTheListIsComplete() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Map listSetsResult = new HashMap();
    listSetsResult.put("sets", List.of("<set><setSpec>a</setSpec><setName>Set A</setName></set>").iterator());
    when(catalog.listSets()).thenReturn(listSetsResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListSets.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<ListSets>"));
    assertTrue(result.contains("<setSpec>a</setSpec>"));
    assertFalse(result.contains("resumptionToken"));
  }

  @Test
  public void constructAppendsAResumptionTokenElementWhenTheCatalogReturnsOne() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Map listSetsResult = new HashMap();
    listSetsResult.put("sets", List.of("<set><setSpec>a</setSpec><setName>Set A</setName></set>").iterator());
    Map resumptionMap = new HashMap();
    resumptionMap.put("completeListSize", "42");
    resumptionMap.put("cursor", "0");
    listSetsResult.put("resumptionMap", resumptionMap);
    when(catalog.listSets()).thenReturn(listSetsResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListSets.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("completeListSize=\"42\""));
    assertTrue(result.contains("cursor=\"0\""));
  }

  @Test
  public void constructUsesTheOldResumptionTokenToFetchTheNextPage() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Map listSetsResult = new HashMap();
    listSetsResult.put("sets", List.of("<set><setSpec>b</setSpec><setName>Set B</setName></set>").iterator());
    when(catalog.listSets("tok123")).thenReturn(listSetsResult);
    HttpServletRequest request = VerbTestSupport.mockRequest(params("tok123"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListSets.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<setSpec>b</setSpec>"));
  }

  @Test
  public void constructEmbedsTheErrorWhenTheRepositoryHasNoSetHierarchy() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.listSets()).thenThrow(new NoSetHierarchyException());
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListSets.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"noSetHierarchy\">"));
  }

  @Test
  public void constructEmbedsTheErrorWhenTheResumptionTokenIsBad() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.listSets("badtoken")).thenThrow(new BadResumptionTokenException());
    HttpServletRequest request = VerbTestSupport.mockRequest(params("badtoken"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListSets.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badResumptionToken\">"));
  }

  @Test
  public void constructReturnsBadArgumentWhenTheVerbParamIsMissing() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    HttpServletRequest request = VerbTestSupport.mockRequest(new LinkedHashMap<>());
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = ListSets.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badArgument\">"));
  }

  private static Map<String, String> params(String resumptionToken) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "ListSets");
    if (resumptionToken != null) {
      params.put("resumptionToken", resumptionToken);
    }
    return params;
  }
}
