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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Test;

import ORG.oclc.oai.server.catalog.AbstractCatalog;

public class ServerVerbTest {

  @Test
  public void createResponseDateFormatsInUtc() {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.clear();
    calendar.set(2020, Calendar.MARCH, 15, 13, 30, 45);

    String responseDate = ServerVerb.createResponseDate(calendar.getTime());

    assertEquals("2020-03-15T13:30:45Z", responseDate);
  }

  @Test
  public void getVerbsListsAllStandardOaiVerbs() {
    Map verbs = ServerVerb.getVerbs();

    assertEquals(GetRecord.class, verbs.get("GetRecord"));
    assertEquals(Identify.class, verbs.get("Identify"));
    assertEquals(ListIdentifiers.class, verbs.get("ListIdentifiers"));
    assertEquals(ListMetadataFormats.class, verbs.get("ListMetadataFormats"));
    assertEquals(ListRecords.class, verbs.get("ListRecords"));
    assertEquals(ListSets.class, verbs.get("ListSets"));
    assertEquals(6, verbs.size());
  }

  @Test
  public void getExtensionVerbsReturnsEmptyMapWhenNoneAreConfigured() {
    Properties properties = new Properties();

    Map extensionVerbs = ServerVerb.getExtensionVerbs(properties);

    assertTrue(extensionVerbs.isEmpty());
  }

  @Test
  public void getExtensionVerbsSkipsEntriesThatCannotBeLoaded() {
    Properties properties = new Properties();
    properties.setProperty("ExtensionVerbs.Bogus", "this.class.does.not.Exist");

    Map extensionVerbs = ServerVerb.getExtensionVerbs(properties);

    assertTrue(extensionVerbs.isEmpty());
  }

  @Test
  public void getRequestElementRendersValidParamsAndOmitsUnknownOnes() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterNames()).thenReturn(enumerationOf("verb", "metadataPrefix", "unknownParam"));
    when(request.getParameter("verb")).thenReturn("ListRecords");
    when(request.getParameter("metadataPrefix")).thenReturn("oai_dc");
    List validParamNames = Arrays.asList("verb", "metadataPrefix");

    String element = ServerVerb.getRequestElement(request, validParamNames, "http://example.org/oai");

    assertEquals("<request verb=\"ListRecords\" metadataPrefix=\"oai_dc\">http://example.org/oai</request>", element);
  }

  @Test
  public void getRequestElementXmlEncodesTheSetParamOnlyWhenRequested() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    // getRequestElement is invoked twice below; a plain Enumeration is single-use, so return a
    // fresh one on each call instead of exhausting the same instance on the first invocation.
    when(request.getParameterNames()).thenAnswer(invocation -> enumerationOf("set"));
    when(request.getParameter("set")).thenReturn("a&b");
    List validParamNames = Collections.singletonList("set");

    String rawSet = ServerVerb.getRequestElement(request, validParamNames, "http://example.org/oai", false);
    String encodedSet = ServerVerb.getRequestElement(request, validParamNames, "http://example.org/oai", true);

    assertTrue(rawSet.contains("set=\"a&b\""));
    assertTrue(encodedSet.contains("set=\"a&amp;b\""));
  }

  @Test
  public void hasBadArgumentsIsTrueWhenARequiredParamIsMissing() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("metadataPrefix")).thenReturn(null);
    AbstractCatalog catalog = mock(AbstractCatalog.class);
    Iterator requiredParamNames = Collections.singletonList("metadataPrefix").iterator();

    boolean bad = ServerVerb.hasBadArguments(request, requiredParamNames,
        Collections.singletonList("metadataPrefix"), catalog);

    assertTrue(bad);
  }

  @Test
  public void hasBadArgumentsIsTrueForUnknownParamNames() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterNames()).thenReturn(enumerationOf("unknownParam"));
    when(request.getParameterValues("unknownParam")).thenReturn(new String[] { "x" });
    AbstractCatalog catalog = mock(AbstractCatalog.class);

    boolean bad = ServerVerb.hasBadArguments(request, Collections.emptyIterator(),
        Collections.singletonList("metadataPrefix"), catalog);

    assertTrue(bad);
  }

  @Test
  public void hasBadArgumentsIsTrueWhenCatalogRejectsTheParamValue() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameterNames()).thenReturn(enumerationOf("metadataPrefix"));
    when(request.getParameterValues("metadataPrefix")).thenReturn(new String[] { "bad value" });
    AbstractCatalog catalog = mock(AbstractCatalog.class);
    when(catalog.isValidParam("bad value")).thenReturn(false);

    boolean bad = ServerVerb.hasBadArguments(request, Collections.emptyIterator(),
        Collections.singletonList("metadataPrefix"), catalog);

    assertTrue(bad);
  }

  @Test
  public void hasBadArgumentsIsFalseForAWellFormedRequest() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("metadataPrefix")).thenReturn("oai_dc");
    when(request.getParameterNames()).thenReturn(enumerationOf("metadataPrefix"));
    when(request.getParameterValues("metadataPrefix")).thenReturn(new String[] { "oai_dc" });
    AbstractCatalog catalog = mock(AbstractCatalog.class);
    when(catalog.isValidParam("oai_dc")).thenReturn(true);
    Iterator requiredParamNames = Collections.singletonList("metadataPrefix").iterator();

    boolean bad = ServerVerb.hasBadArguments(request, requiredParamNames,
        Collections.singletonList("metadataPrefix"), catalog);

    assertFalse(bad);
  }

  private static Enumeration enumerationOf(String... values) {
    return Collections.enumeration(Arrays.asList(values));
  }
}
