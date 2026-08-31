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

package de.fiz_karlsruhe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ORG.oclc.oai.server.catalog.RecordFactory;
import ORG.oclc.oai.server.verb.BadResumptionTokenException;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import ORG.oclc.oai.server.verb.IdDoesNotExistException;
import ORG.oclc.oai.server.verb.NoItemsMatchException;
import ORG.oclc.oai.server.verb.NoMetadataFormatsException;
import de.fiz_karlsruhe.model.Content;
import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.ListSetsResult;
import de.fiz_karlsruhe.model.ResumptionToken;
import de.fiz_karlsruhe.model.SearchResult;
import de.fiz_karlsruhe.model.Set;
import de.fiz_karlsruhe.service.BackendService;
import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class FizOAICatalogTest {

  private static final String OAI_IDENTIFIER = "oai:fiz-karlsruhe.de:10.0133/1";
  private static final String LOCAL_IDENTIFIER = "10.0133/1";

  private Properties prop;

  @Mock
  BackendService backendService;

  @Before
  public void init() throws IOException {
    prop = new Properties();
    File resourcesDirectory = new File("src/test/resources").getAbsoluteFile();
    File propertiesFile = new File(resourcesDirectory, "oaicat.properties");
    try (InputStream resourcesFile = new FileInputStream(propertiesFile)) {
      prop.load(resourcesFile);
    }
  }

  @Test
  public void initCatalogWithoutProperties() throws Exception {
    try {
      FizOAICatalog catalog = new FizOAICatalog(null);
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  @Test
  public void initCatalogWithProperties() throws Exception {
    FizOAICatalog catalog = new FizOAICatalog(prop);
    assertTrue(true);
  }

  @Test
  public void testGetRecordIdDoesNotExist() throws Exception {
    FizOAICatalog catalog;
    try {
      catalog = (FizOAICatalog) FizOAICatalog.factory(prop, null);
      catalog.setBackendService(backendService);
      catalog.getRecord("abc", "ii");
      Assert.fail("IdDoesNotExistException expected");
    } catch (IdDoesNotExistException e) {
      // do nothing
    } catch (Throwable t) {
      throw new AssertionError("Unexpected exception", t);
    }

  }

  // --- getRecord -----------------------------------------------------------------

  @Test
  public void getRecordReturnsTheConstructedRecordWhenTheItemExists() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", "<?xml version=\"1.0\"?><oai_dc:dc>content</oai_dc:dc>");
    when(backendService.getItem(LOCAL_IDENTIFIER, "oai_dc")).thenReturn(item);

    String record = catalog.getRecord(OAI_IDENTIFIER, "oai_dc");

    assertTrue(record.contains("<identifier>" + OAI_IDENTIFIER + "</identifier>"));
    assertTrue(record.contains("<datestamp>2020-01-01</datestamp>"));
    assertTrue(record.contains("<metadata><oai_dc:dc>content</oai_dc:dc></metadata>"));
    assertFalse(record.contains("status=\"deleted\""));
  }

  @Test
  public void getRecordReturnsAHeaderOnlyRecordForADeletedItem() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", true, null, null);
    when(backendService.getItem(LOCAL_IDENTIFIER, "oai_dc")).thenReturn(item);

    String record = catalog.getRecord(OAI_IDENTIFIER, "oai_dc");

    assertTrue(record.contains("status=\"deleted\""));
    assertFalse(record.contains("<metadata>"));
  }

  @Test(expected = IdDoesNotExistException.class)
  public void getRecordThrowsIdDoesNotExistWhenTheBackendHasNoItem() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItem(LOCAL_IDENTIFIER, "oai_dc")).thenReturn(null);

    catalog.getRecord(OAI_IDENTIFIER, "oai_dc");
  }

  @Test(expected = CannotDisseminateFormatException.class)
  public void getRecordThrowsCannotDisseminateFormatWhenTheItemHasNoContent() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, null, null);
    when(backendService.getItem(LOCAL_IDENTIFIER, "oai_dc")).thenReturn(item);

    catalog.getRecord(OAI_IDENTIFIER, "oai_dc");
  }

  @Test(expected = ORG.oclc.oai.server.verb.OAIInternalServerError.class)
  public void getRecordWrapsABackendIOExceptionAsInternalServerError() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItem(LOCAL_IDENTIFIER, "oai_dc")).thenThrow(new IOException("backend down"));

    catalog.getRecord(OAI_IDENTIFIER, "oai_dc");
  }

  // --- getSchemaLocations ----------------------------------------------------------

  @Test
  public void getSchemaLocationsReturnsOnlyTheFormatsAvailableForTheItem() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", null);
    item.setFormats(List.of("oai_dc"));
    when(backendService.getItem(LOCAL_IDENTIFIER)).thenReturn(item);

    List locations = catalog.getSchemaLocations(OAI_IDENTIFIER);

    assertEquals(1, locations.size());
    assertEquals("oai_dc", ((Format) locations.get(0)).getMetadataPrefix());
  }

  @Test(expected = IdDoesNotExistException.class)
  public void getSchemaLocationsThrowsIdDoesNotExistWhenTheBackendHasNoItem() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItem(LOCAL_IDENTIFIER)).thenReturn(null);

    catalog.getSchemaLocations(OAI_IDENTIFIER);
  }

  @Test(expected = NoMetadataFormatsException.class)
  public void getSchemaLocationsThrowsNoMetadataFormatsForADeletedItem() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", true, null, null);
    when(backendService.getItem(LOCAL_IDENTIFIER)).thenReturn(item);

    catalog.getSchemaLocations(OAI_IDENTIFIER);
  }

  @Test(expected = ORG.oclc.oai.server.verb.OAIInternalServerError.class)
  public void getSchemaLocationsWrapsABackendExceptionAsInternalServerError() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItem(LOCAL_IDENTIFIER)).thenThrow(new IOException("backend down"));

    catalog.getSchemaLocations(OAI_IDENTIFIER);
  }

  // --- listIdentifiers(from, until, set, metadataPrefix) ----------------------------

  @Test(expected = CannotDisseminateFormatException.class)
  public void listIdentifiersThrowsCannotDisseminateFormatForAnUnregisteredPrefix() throws Exception {
    FizOAICatalog catalog = newCatalog(new FormatRegistry(Collections.emptyList(), Collections.emptyList()));

    catalog.listIdentifiers("0001-01-01", "9999-12-31", null, "unknown");
  }

  @Test(expected = NoItemsMatchException.class)
  public void listIdentifiersThrowsNoItemsMatchWhenTheBackendReturnsNothing() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItems(false, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc")).thenReturn(null);

    catalog.listIdentifiers("0001-01-01", "9999-12-31", null, "oai_dc");
  }

  @Test
  public void listIdentifiersReturnsHeadersAndOmitsTheResumptionTokenWhenTheListIsComplete() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", null);
    when(backendService.getItems(false, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc"))
        .thenReturn(searchResult(List.of(item), 1, null));

    Map result = catalog.listIdentifiers("0001-01-01", "9999-12-31", null, "oai_dc");

    Iterator headers = (Iterator) result.get("headers");
    assertTrue(headers.hasNext());
    assertTrue(((String) headers.next()).contains(OAI_IDENTIFIER));
    assertFalse(result.containsKey("resumptionMap"));
  }

  @Test
  public void listIdentifiersAddsAResumptionTokenWhenMoreResultsRemain() throws Exception {
    // Regression coverage for the date-only-granularity bug fixed in ResumptionToken:
    // with plain "YYYY-MM-DD" from/until (this project's documented granularity default),
    // this used to throw OAIInternalServerError instead of returning a token.
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", null);
    when(backendService.getItems(false, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc"))
        .thenReturn(searchResult(List.of(item), 250, "mark-1"));

    Map result = catalog.listIdentifiers("0001-01-01", "9999-12-31", null, "oai_dc");

    assertTrue(result.containsKey("resumptionMap"));
    Map resumptionMap = (Map) result.get("resumptionMap");
    assertEquals("250", resumptionMap.get("completeListSize"));
  }

  @Test(expected = ORG.oclc.oai.server.verb.OAIInternalServerError.class)
  public void listIdentifiersWrapsABackendIOExceptionAsInternalServerError() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItems(false, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc"))
        .thenThrow(new IOException("backend down"));

    catalog.listIdentifiers("0001-01-01", "9999-12-31", null, "oai_dc");
  }

  // --- listIdentifiers(resumptionToken) ----------------------------------------------

  @Test
  public void listIdentifiersResumesFromAPreviousToken() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", null);
    when(backendService.getItems(false, "mark-1", 100, null, "0001-01-01", "9999-12-31", "oai_dc"))
        .thenReturn(searchResult(List.of(item), 250, null));

    Map result = catalog.listIdentifiers(resumptionToken("mark-1"));

    Iterator headers = (Iterator) result.get("headers");
    assertTrue(headers.hasNext());
    assertTrue(((String) headers.next()).contains(OAI_IDENTIFIER));
  }

  @Test(expected = BadResumptionTokenException.class)
  public void listIdentifiersThrowsBadResumptionTokenWhenTheBackendReturnsNothing() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItems(false, "mark-1", 100, null, "0001-01-01", "9999-12-31", "oai_dc")).thenReturn(null);

    catalog.listIdentifiers(resumptionToken("mark-1"));
  }

  // --- listRecords(from, until, set, metadataPrefix) --------------------------------

  @Test(expected = CannotDisseminateFormatException.class)
  public void listRecordsThrowsCannotDisseminateFormatForAnUnregisteredPrefix() throws Exception {
    FizOAICatalog catalog = newCatalog(new FormatRegistry(Collections.emptyList(), Collections.emptyList()));

    catalog.listRecords("0001-01-01", "9999-12-31", null, "unknown");
  }

  @Test(expected = NoItemsMatchException.class)
  public void listRecordsThrowsNoItemsMatchWhenTheBackendReturnsNothing() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItems(true, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc")).thenReturn(null);

    catalog.listRecords("0001-01-01", "9999-12-31", null, "oai_dc");
  }

  @Test
  public void listRecordsReturnsRecordsAndAddsAResumptionTokenWhenMoreResultsRemain() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", "<?xml version=\"1.0\"?><oai_dc:dc>content</oai_dc:dc>");
    SearchResult<Item> backendResult = searchResult(List.of(item), 250, "mark-1");
    backendResult.setSize(100);
    when(backendService.getItems(true, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc")).thenReturn(backendResult);

    Map result = catalog.listRecords("0001-01-01", "9999-12-31", null, "oai_dc");

    Iterator records = (Iterator) result.get("records");
    assertTrue(records.hasNext());
    assertTrue(((String) records.next()).contains("<oai_dc:dc>content</oai_dc:dc>"));
    assertTrue(result.containsKey("resumptionMap"));
  }

  @Test(expected = ORG.oclc.oai.server.verb.OAIInternalServerError.class)
  public void listRecordsWrapsABackendIOExceptionAsInternalServerError() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItems(true, null, 100, null, "0001-01-01", "9999-12-31", "oai_dc"))
        .thenThrow(new IOException("backend down"));

    catalog.listRecords("0001-01-01", "9999-12-31", null, "oai_dc");
  }

  // --- listRecords(resumptionToken) --------------------------------------------------

  @Test
  public void listRecordsResumesFromAPreviousToken() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Item item = item(LOCAL_IDENTIFIER, "2020-01-01", false, "oai_dc", "<?xml version=\"1.0\"?><oai_dc:dc>content</oai_dc:dc>");
    when(backendService.getItems(true, "mark-1", 100, null, "0001-01-01", "9999-12-31", "oai_dc"))
        .thenReturn(searchResult(List.of(item), 1, null));

    Map result = catalog.listRecords(resumptionToken("mark-1"));

    Iterator records = (Iterator) result.get("records");
    assertTrue(records.hasNext());
    assertTrue(((String) records.next()).contains("<oai_dc:dc>content</oai_dc:dc>"));
  }

  @Test(expected = BadResumptionTokenException.class)
  public void listRecordsThrowsBadResumptionTokenWhenTheBackendReturnsNothing() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.getItems(true, "mark-1", 100, null, "0001-01-01", "9999-12-31", "oai_dc")).thenReturn(null);

    catalog.listRecords(resumptionToken("mark-1"));
  }

  // --- listSets ----------------------------------------------------------------------

  @Test
  public void listSetsRendersEachSetAsXmlAndExposesTheBackendsResumptionToken() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Set set = new Set();
    set.setSpec("setA");
    set.setName("Set A");
    when(backendService.searchSets(null)).thenReturn(new ListSetsResult(List.of(set), "next-token", 0, 1));

    Map result = catalog.listSets();

    Iterator sets = (Iterator) result.get("sets");
    String setXml = (String) sets.next();
    assertTrue(setXml.contains("<setSpec>setA</setSpec>"));
    assertTrue(setXml.contains("<setName>Set A</setName>"));
    assertEquals("next-token", result.get("resumptionToken"));
  }

  @Test
  public void listSetsIncludesTheSetDescriptionWhenPresent() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    Set set = new Set();
    set.setSpec("setA");
    set.setName("Set A");
    set.setDescription("A description");
    when(backendService.searchSets("tok")).thenReturn(new ListSetsResult(List.of(set), null, 0, 1));

    Map result = catalog.listSets("tok");

    Iterator sets = (Iterator) result.get("sets");
    String setXml = (String) sets.next();
    assertTrue(setXml.contains("<setDescription>"));
    assertTrue(setXml.contains("<dc:description>A description</dc:description>"));
  }

  @Test(expected = ORG.oclc.oai.server.verb.OAIInternalServerError.class)
  public void listSetsWrapsABackendExceptionAsInternalServerError() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());
    when(backendService.searchSets(null)).thenThrow(new IOException("backend down"));

    catalog.listSets();
  }

  // --- close ---------------------------------------------------------------------------

  @Test
  public void closeIsANoOpWhenTheRecordFactoryIsNotAFizRecordFactory() throws Exception {
    FizOAICatalog catalog = newCatalog(oaiDcFormatRegistry());

    catalog.close();
  }

  // --- fixtures --------------------------------------------------------------------------

  private FizOAICatalog newCatalog(FormatRegistry formatRegistry) {
    FizOAICatalog catalog = new FizOAICatalog(prop);
    catalog.setBackendService(backendService);
    FakeRecordFactory recordFactory = new FakeRecordFactory();
    recordFactory.setFormatRegistry(formatRegistry);
    catalog.setRecordFactory(recordFactory);
    return catalog;
  }

  private static FormatRegistry oaiDcFormatRegistry() {
    Format format = new Format();
    format.setMetadataPrefix("oai_dc");
    format.setSchemaLocation("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    format.setSchemaNamespace("http://www.openarchives.org/OAI/2.0/oai_dc/");
    return new FormatRegistry(List.of(format), Collections.emptyList());
  }

  private static Item item(String localIdentifier, String datestamp, boolean deleted, String contentFormat,
      String contentXml) {
    Item item = new Item();
    item.setIdentifier(localIdentifier);
    item.setDatestamp(datestamp);
    item.setDeleteFlag(deleted);
    item.setSets(Collections.emptyList());
    if (contentXml != null) {
      Content content = new Content();
      content.setFormat(contentFormat);
      content.setContent(contentXml);
      item.setContent(content);
    }
    return item;
  }

  private static SearchResult<Item> searchResult(List<Item> data, long total, String searchMark) {
    SearchResult<Item> result = new SearchResult<>();
    result.setData(data);
    result.setTotal(total);
    result.setSearchMark(searchMark);
    return result;
  }

  private static String resumptionToken(String searchMark) throws BadResumptionTokenException {
    ResumptionToken token = new ResumptionToken();
    token.setSearchMark(searchMark);
    token.setRows(100);
    token.setTotal(250L);
    token.setFrom("0001-01-01");
    token.setUntil("9999-12-31");
    token.setMetadataPrefix("oai_dc");
    return token.getToken();
  }

  /**
   * A minimal RecordFactory stand-in that mirrors FizRecordFactory's identifier handling
   * without its side effects (a live BackendService network call and a background
   * ScheduledExecutorService), so these tests stay fast, offline, and thread-clean.
   */
  private static class FakeRecordFactory extends RecordFactory {
    @Override
    public String fromOAIIdentifier(String identifier) {
      String[] parts = identifier.split(":", 3);
      return parts.length == 3 ? parts[2] : null;
    }

    @Override
    public String getOAIIdentifier(Object nativeItem) {
      return "oai:fiz-karlsruhe.de:" + ((Item) nativeItem).getIdentifier();
    }

    @Override
    public String getDatestamp(Object nativeItem) {
      return ((Item) nativeItem).getDatestamp();
    }

    @Override
    public Iterator getSetSpecs(Object nativeItem) {
      return ((Item) nativeItem).getSets().iterator();
    }

    @Override
    public boolean isDeleted(Object nativeItem) {
      return nativeItem != null && Boolean.TRUE.equals(((Item) nativeItem).getDeleteFlag());
    }

    @Override
    public Iterator getAbouts(Object nativeItem) {
      return null;
    }

    @Override
    public String quickCreate(Object nativeItem, String schemaURL, String metadataPrefix) {
      return null;
    }
  }

}
