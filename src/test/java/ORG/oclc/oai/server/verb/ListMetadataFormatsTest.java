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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import de.fiz_karlsruhe.model.Format;

public class ListMetadataFormatsTest {

  @Test
  public void constructListsEveryRegisteredFormatWhenNoIdentifierIsGiven() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getFormatRegistry()).thenReturn(VerbTestSupport.oaiDcFormatRegistry());
    HttpServletRequest request = VerbTestSupport.mockRequest(params(null));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListMetadataFormats.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<metadataPrefix>oai_dc</metadataPrefix>"));
    assertTrue(result.contains("<schema>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</schema>"));
    assertTrue(result.contains("<metadataNamespace>http://www.openarchives.org/OAI/2.0/oai_dc/</metadataNamespace>"));
  }

  @Test
  public void constructListsOnlyTheFormatsAvailableForTheGivenIdentifier() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Format itemFormat = new Format();
    itemFormat.setMetadataPrefix("radar");
    itemFormat.setSchemaLocation("http://schema.datacite.org/radar.xsd");
    itemFormat.setSchemaNamespace("http://schema.datacite.org/radar/");
    when(catalog.getSchemaLocations("oai:example.org:1")).thenReturn(List.of(itemFormat));
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai:example.org:1"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListMetadataFormats.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<metadataPrefix>radar</metadataPrefix>"));
    assertTrue(result.contains("<schema>http://schema.datacite.org/radar.xsd</schema>"));
  }

  @Test
  public void constructEmbedsTheErrorWhenTheIdentifierDoesNotExist() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    when(catalog.getSchemaLocations("oai:example.org:missing"))
        .thenThrow(new IdDoesNotExistException("oai:example.org:missing"));
    HttpServletRequest request = VerbTestSupport.mockRequest(params("oai:example.org:missing"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListMetadataFormats.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"idDoesNotExist\">"));
  }

  @Test
  public void constructReturnsBadArgumentWhenTheVerbParamIsMissing() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    HttpServletRequest request = VerbTestSupport.mockRequest(new LinkedHashMap<>());
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result =
        ListMetadataFormats.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badArgument\">"));
  }

  private static Map<String, String> params(String identifier) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "ListMetadataFormats");
    if (identifier != null) {
      params.put("identifier", identifier);
    }
    return params;
  }
}
