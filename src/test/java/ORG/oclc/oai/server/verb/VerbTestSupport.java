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

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import de.fiz_karlsruhe.FormatRegistry;
import de.fiz_karlsruhe.model.Format;

/**
 * Shared setup for the {@code ORG.oclc.oai.server.verb} unit tests: every {@code construct}
 * method takes the same (context, request, response, transformer) shape and validates
 * arguments through {@link ServerVerb#hasBadArguments}, so the mock wiring for that is
 * factored out here rather than repeated in each verb's test class.
 */
final class VerbTestSupport {

  private VerbTestSupport() {
  }

  static HashMap context(Properties properties, AbstractCatalog catalog) {
    HashMap context = new HashMap();
    context.put("OAIHandler.properties", properties);
    context.put("OAIHandler.catalog", catalog);
    return context;
  }

  /**
   * A request stub whose getParameter/getParameterNames/getParameterValues are wired
   * consistently from a single param map, matching what hasBadArguments inspects.
   */
  static HttpServletRequest mockRequest(Map<String, String> params) {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.org/oai"));
    when(request.getParameterNames()).thenAnswer(invocation -> Collections.enumeration(params.keySet()));
    for (Map.Entry<String, String> entry : params.entrySet()) {
      when(request.getParameter(entry.getKey())).thenReturn(entry.getValue());
      when(request.getParameterValues(entry.getKey())).thenReturn(new String[] {entry.getValue()});
    }
    return request;
  }

  /**
   * A catalog mock that is harvestable and accepts any parameter value/date, so tests can
   * focus on the verb-specific branch under test instead of AbstractCatalog's own validation
   * (already covered by AbstractCatalogTest).
   */
  static AbstractCatalog mockCatalogAcceptingAllParams() throws Exception {
    AbstractCatalog catalog = mock(AbstractCatalog.class);
    when(catalog.isValidParam(anyString())).thenReturn(true);
    when(catalog.isHarvestable()).thenReturn(true);
    when(catalog.toFinestFrom(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    when(catalog.toFinestUntil(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    return catalog;
  }

  static FormatRegistry oaiDcFormatRegistry() {
    Format format = new Format();
    format.setMetadataPrefix("oai_dc");
    format.setSchemaLocation("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    format.setSchemaNamespace("http://www.openarchives.org/OAI/2.0/oai_dc/");
    return new FormatRegistry(List.of(format), Collections.emptyList());
  }
}
