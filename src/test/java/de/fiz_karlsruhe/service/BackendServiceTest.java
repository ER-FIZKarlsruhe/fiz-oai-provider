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

package de.fiz_karlsruhe.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.ListSetsResult;
import de.fiz_karlsruhe.model.SearchResult;
import de.fiz_karlsruhe.model.Set;
import de.fiz_karlsruhe.model.Transformation;

/**
 * Talks to the MockServer instance started by the mockserver-maven-plugin (see pom.xml,
 * port 1080) that also backs the *IT integration tests, reusing the request/response
 * expectations already registered by MockServerUtil so this test doesn't have to duplicate
 * or risk conflicting with them.
 */
public class BackendServiceTest {

  private static final String BACKEND_BASE_URL = "http://localhost:1080/";

  private static BackendService backendService;

  private static MockServerClient mockServerClient;

  @BeforeClass
  public static void init() {
    // Both are singletons: getInstance(...) is a no-op if another test already initialized
    // them, which is fine as long as it points at the same shared MockServer instance.
    ConfigurationService.getInstance(new Properties());
    backendService = BackendService.getInstance(BACKEND_BASE_URL);
    mockServerClient = new MockServerClient("localhost", 1080);
  }

  @Test
  public void getFormatsReturnsAllRegisteredFormats() throws Exception {
    List<Format> formats = backendService.getFormats();

    assertNotNull(formats);
    assertEquals(3, formats.size());
    assertTrue(formats.stream().anyMatch(f -> "oai_dc".equals(f.getMetadataPrefix())));
  }

  @Test
  public void getFormatReturnsSpecificFormat() throws Exception {
    Format format = backendService.getFormat("oai_dc");

    assertNotNull(format);
    assertEquals("http://www.openarchives.org/OAI/2.0/oai_dc.xsd", format.getSchemaLocation());
  }

  @Test
  public void getTransformationsReturnsCrosswalks() throws Exception {
    List<Transformation> transformations = backendService.getTransformations();

    assertNotNull(transformations);
    assertFalse(transformations.isEmpty());
    assertTrue(transformations.stream().anyMatch(t -> "datacite".equals(t.getFormatTo())));
  }

  @Test
  public void getItemWithFormatReturnsRequestedRepresentation() throws Exception {
    Item item = backendService.getItem("10.0133/10000386", "oai_dc");

    assertNotNull(item);
    assertEquals("10.0133/10000386", item.getIdentifier());
    assertEquals("oai_dc", item.getContent().getFormat());
  }

  @Test
  public void getItemWithoutFormatReturnsDefaultRepresentation() throws Exception {
    Item item = backendService.getItem("10.0133/10000386");

    assertNotNull(item);
    assertEquals("10.0133/10000386", item.getIdentifier());
  }

  @Test
  public void getItemsWithoutContentReturnsSearchResult() throws Exception {
    SearchResult<Item> result = backendService.getItems(false, null, 50, "fiz", null, null, "oai_dc");

    assertNotNull(result);
    assertEquals(4, result.getTotal());
  }

  @Test
  public void getItemsWithContentReturnsSearchResult() throws Exception {
    SearchResult<Item> result = backendService.getItems(true, null, 50, "fiz", null, null, "oai_dc");

    assertNotNull(result);
    assertEquals(4, result.getTotal());
  }

  @Test
  public void getSetsReturnsAllSets() throws Exception {
    mockServerClient.when(request().withMethod("GET").withPath("/set")).respond(response().withStatusCode(200)
        .withBody("[{\"name\": \"Deutsche Fotothek\", \"spec\": \"institution\"}]"));

    List<Set> sets = backendService.getSets();

    assertNotNull(sets);
    assertEquals(1, sets.size());
    assertEquals("institution", sets.get(0).getSpec());
  }

  @Test
  public void searchSetsWithoutTokenReturnsSets() throws Exception {
    ListSetsResult result = backendService.searchSets(null);

    assertNotNull(result);
    assertNotNull(result.getSets());
    assertTrue(result.getSets().stream().anyMatch(s -> "institution".equals(s.getSpec())));
  }

  /**
   * Regression test for the fix that URL-encodes the client-supplied resumptionToken before
   * putting it into the outbound backend URL (previously it was concatenated raw, so an
   * unencoded '&' or '=' in the token would be misinterpreted as extra query parameters).
   */
  @Test
  public void searchSetsUrlEncodesResumptionToken() throws Exception {
    String token = "a&b=c";

    backendService.searchSets(token);

    HttpRequest[] recorded = mockServerClient.retrieveRecordedRequests(request().withPath("/set/search"));
    assertTrue(recorded.length > 0);
    HttpRequest lastRequest = recorded[recorded.length - 1];
    assertEquals(token, lastRequest.getFirstQueryStringParameter("resumptionToken"));
  }
}
