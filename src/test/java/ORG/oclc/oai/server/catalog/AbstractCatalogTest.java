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

package ORG.oclc.oai.server.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ORG.oclc.oai.server.verb.BadArgumentException;
import ORG.oclc.oai.server.verb.BadResumptionTokenException;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import ORG.oclc.oai.server.verb.IdDoesNotExistException;
import ORG.oclc.oai.server.verb.NoItemsMatchException;
import ORG.oclc.oai.server.verb.NoSetHierarchyException;
import ORG.oclc.oai.server.verb.OAIInternalServerError;

public class AbstractCatalogTest {

  private static class TestCatalog extends AbstractCatalog {

    private Map listIdentifiersResult;

    @Override
    public Map listSets() {
      return Collections.emptyMap();
    }

    @Override
    public Map listSets(String resumptionToken) {
      return Collections.emptyMap();
    }

    @Override
    public List getSchemaLocations(String identifier) {
      return Collections.emptyList();
    }

    @Override
    public Map listIdentifiers(String from, String until, String set, String metadataPrefix) {
      return listIdentifiersResult;
    }

    @Override
    public Map listIdentifiers(String resumptionToken) {
      return listIdentifiersResult;
    }

    @Override
    public String getRecord(String identifier, String metadataPrefix)
        throws IdDoesNotExistException, CannotDisseminateFormatException {
      return "<record>" + identifier + "</record>";
    }

    @Override
    public void close() {
      // no resources to release
    }
  }

  @Test
  public void toFinestFromPadsPartialDateToDayGranularity() throws BadArgumentException {
    TestCatalog catalog = new TestCatalog();
    catalog.setSupportedGranularityOffset(0); // YYYY-MM-DD

    assertEquals("2020-01-01", catalog.toFinestFrom("2020-01-01"));
  }

  @Test(expected = BadArgumentException.class)
  public void toFinestFromRejectsValueLongerThanGranularity() throws BadArgumentException {
    TestCatalog catalog = new TestCatalog();
    catalog.setSupportedGranularityOffset(0); // YYYY-MM-DD

    catalog.toFinestFrom("2020-01-01T00:00:00Z");
  }

  @Test
  public void toFinestUntilExpandsPartialYearToLastMomentOfDay() throws BadArgumentException {
    TestCatalog catalog = new TestCatalog();
    catalog.setSupportedGranularityOffset(0); // YYYY-MM-DD

    assertEquals("2020-12-31", catalog.toFinestUntil("2020"));
  }

  @Test
  public void toFinestUntilExpandsPartialTimeToEndOfSecondGranularity() throws BadArgumentException {
    TestCatalog catalog = new TestCatalog();
    catalog.setSupportedGranularityOffset(1); // YYYY-MM-DDThh:mm:ssZ

    assertEquals("2020-01-01T23:59:59Z", catalog.toFinestUntil("2020-01-01"));
  }

  @Test(expected = BadArgumentException.class)
  public void toFinestUntilRejectsInvalidGranularity() throws BadArgumentException {
    TestCatalog catalog = new TestCatalog();
    catalog.setSupportedGranularityOffset(0); // YYYY-MM-DD

    catalog.toFinestUntil("2020-1"); // length 6 is explicitly rejected
  }

  @Test
  public void isValidParamAcceptsDefaultRegexByDefault() {
    TestCatalog catalog = new TestCatalog();

    assertTrue(catalog.isValidParam("10.0133/10000386"));
    assertFalse(catalog.isValidParam("bad value with spaces"));
  }

  @Test
  public void isValidParamHonorsCustomRegex() {
    TestCatalog catalog = new TestCatalog();
    catalog.setParamRegex("^[0-9]+$");

    assertTrue(catalog.isValidParam("12345"));
    assertFalse(catalog.isValidParam("abc"));
  }

  @Test
  public void isValidParamAcceptsAnythingWhenRegexIsCleared() {
    TestCatalog catalog = new TestCatalog();
    catalog.setParamRegex("");

    assertTrue(catalog.isValidParam("anything at all!!"));
  }

  @Test
  public void getResumptionMapReturnsNullWhenTokenIsNull() {
    TestCatalog catalog = new TestCatalog();

    assertNull(catalog.getResumptionMap(null));
  }

  @Test
  public void getResumptionMapIncludesOptionalFieldsWhenProvided() {
    TestCatalog catalog = new TestCatalog();

    Map resumptionMap = catalog.getResumptionMap("token123", 42, 7);

    assertEquals("token123", resumptionMap.get("resumptionToken"));
    assertEquals("42", resumptionMap.get("completeListSize"));
    assertEquals("7", resumptionMap.get("cursor"));
  }

  @Test
  public void listRecordsFromCriteriaDelegatesToGetRecordForEachIdentifier()
      throws BadArgumentException, CannotDisseminateFormatException, NoItemsMatchException,
      NoSetHierarchyException, OAIInternalServerError {
    TestCatalog catalog = new TestCatalog();
    Map identifiersMap = new java.util.HashMap();
    identifiersMap.put("identifiers", java.util.Arrays.asList("id1", "id2").iterator());
    identifiersMap.put("resumptionToken", "next-token");
    catalog.listIdentifiersResult = identifiersMap;

    Map result = catalog.listRecords(null, null, null, "oai_dc");

    Iterator records = (Iterator) result.get("records");
    assertEquals("<record>id1</record>", records.next());
    assertEquals("<record>id2</record>", records.next());
    assertFalse(records.hasNext());
    assertEquals("next-token", result.get("resumptionToken"));
  }

  @Test
  public void listRecordsFromCriteriaWrapsIdDoesNotExistAsInternalServerError()
      throws BadArgumentException, CannotDisseminateFormatException, NoItemsMatchException,
      NoSetHierarchyException, OAIInternalServerError {
    TestCatalog catalog = new TestCatalog() {
      @Override
      public String getRecord(String identifier, String metadataPrefix) throws IdDoesNotExistException {
        throw new IdDoesNotExistException(identifier);
      }
    };
    Map identifiersMap = new java.util.HashMap();
    identifiersMap.put("identifiers", java.util.Collections.singletonList("missing-id").iterator());
    catalog.listIdentifiersResult = identifiersMap;

    try {
      catalog.listRecords(null, null, null, "oai_dc");
      fail("OAIInternalServerError expected");
    } catch (OAIInternalServerError e) {
      // expected: AbstractCatalog.listRecords must not let IdDoesNotExistException escape
    }
  }

  @Test
  public void listRecordsByResumptionTokenTranslatesCannotDisseminateIntoBadResumptionToken()
      throws BadResumptionTokenException, OAIInternalServerError {
    TestCatalog catalog = new TestCatalog() {
      @Override
      public String getRecord(String identifier, String metadataPrefix) throws CannotDisseminateFormatException {
        throw new CannotDisseminateFormatException(metadataPrefix);
      }
    };
    Map identifiersMap = new java.util.HashMap();
    identifiersMap.put("identifiers", java.util.Collections.singletonList("id1").iterator());
    identifiersMap.put("metadataPrefix", "oai_dc");
    catalog.listIdentifiersResult = identifiersMap;

    try {
      catalog.listRecords("some-token");
      fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      // expected: a format mismatch surfacing here means the resumptionToken no longer matches
    }
  }
}
