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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

import ORG.oclc.oai.server.catalog.AbstractCatalog;

public class GetRecordTest {

  @Test
  public void constructReturnsTheRecordWhenTheCatalogHasIt() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    when(catalog.getRecord("oai:example.org:1", "oai_dc")).thenReturn("<record>stub</record>");
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai:example.org:1", "oai_dc"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = GetRecord.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<GetRecord><record>stub</record></GetRecord>"));
  }

  @Test
  public void constructReturnsBadArgumentWhenIdentifierIsMissing() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null, "oai_dc"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = GetRecord.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badArgument\">"));
  }

  @Test
  public void constructReturnsCannotDisseminateFormatWhenPrefixIsUnknown() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai:example.org:1", "unknown_prefix"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = GetRecord.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"cannotDisseminateFormat\">"));
  }

  @Test
  public void constructReturnsIdDoesNotExistWhenTheCatalogHasNoRecord() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    when(catalog.getRecord("oai:example.org:missing", "oai_dc")).thenReturn(null);
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai:example.org:missing", "oai_dc"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = GetRecord.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"idDoesNotExist\">"));
  }

  private static Map<String, String> params(String identifier, String metadataPrefix) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "GetRecord");
    if (identifier != null) {
      params.put("identifier", identifier);
    }
    if (metadataPrefix != null) {
      params.put("metadataPrefix", metadataPrefix);
    }
    return params;
  }
}
