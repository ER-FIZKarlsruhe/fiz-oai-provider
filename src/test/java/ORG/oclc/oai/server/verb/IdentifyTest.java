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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

import ORG.oclc.oai.server.catalog.AbstractCatalog;

public class IdentifyTest {

  @Test
  public void constructReportsTheConfiguredRepositoryDetails() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Properties properties = new Properties();
    properties.setProperty("Identify.repositoryName", "Test Repository");
    properties.setProperty("Identify.adminEmail", "mailto:admin@example.org");
    properties.setProperty("Identify.earliestDatestamp", "2000-01-01");
    properties.setProperty("Identify.deletedRecord", "no");
    properties.setProperty("AbstractCatalog.granularity", "YYYY-MM-DD");
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "Identify");
    HttpServletRequest request = VerbTestSupport.mockRequest(params);
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = Identify.construct(VerbTestSupport.context(properties, catalog), request, response, null);

    assertTrue(result.contains("<repositoryName>Test Repository</repositoryName>"));
    assertTrue(result.contains("<baseURL>http://example.org/oai</baseURL>"));
    assertTrue(result.contains("<protocolVersion>2.0</protocolVersion>"));
    assertTrue(result.contains("<adminEmail>mailto:admin@example.org</adminEmail>"));
    assertTrue(result.contains("<earliestDatestamp>2000-01-01</earliestDatestamp>"));
    assertTrue(result.contains("<deletedRecord>no</deletedRecord>"));
    assertTrue(result.contains("<granularity>YYYY-MM-DD</granularity>"));
    assertTrue(result.contains("<compression>gzip</compression>"));
    assertTrue(result.contains("<compression>deflate</compression>"));
  }

  @Test
  public void constructEmitsOneAdminEmailElementPerCommaSeparatedAddress() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Properties properties = new Properties();
    properties.setProperty("Identify.adminEmail", "mailto:one@example.org, mailto:two@example.org");
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "Identify");
    HttpServletRequest request = VerbTestSupport.mockRequest(params);
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = Identify.construct(VerbTestSupport.context(properties, catalog), request, response, null);

    assertTrue(result.contains("<adminEmail>mailto:one@example.org</adminEmail>"));
    assertTrue(result.contains("<adminEmail>mailto:two@example.org</adminEmail>"));
  }

  @Test
  public void constructDefaultsGranularityToDayWhenNotConfigured() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "Identify");
    HttpServletRequest request = VerbTestSupport.mockRequest(params);
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = Identify.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<granularity>YYYY-MM-DD</granularity>"));
  }

  @Test
  public void constructUsesTheConfiguredBaseUrlInsteadOfTheRequestUrlWhenSet() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    Properties properties = new Properties();
    properties.setProperty("OAIHandler.baseURL", "https://oai.example.org/provider");
    Map<String, String> params = new LinkedHashMap<>();
    params.put("verb", "Identify");
    HttpServletRequest request = VerbTestSupport.mockRequest(params);
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = Identify.construct(VerbTestSupport.context(properties, catalog), request, response, null);

    assertTrue(result.contains("<baseURL>https://oai.example.org/provider</baseURL>"));
  }

  @Test
  public void constructReturnsBadArgumentWhenTheVerbParamIsMissing() throws Exception {
    AbstractCatalog catalog = VerbTestSupport.mockCatalogAcceptingAllParams();
    HttpServletRequest request = VerbTestSupport.mockRequest(new LinkedHashMap<>());
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = Identify.construct(VerbTestSupport.context(new Properties(), catalog), request, response, null);

    assertTrue(result.contains("<error code=\"badArgument\">"));
  }
}
