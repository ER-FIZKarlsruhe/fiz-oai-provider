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

package de.fiz_karlsruhe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;
import de.fiz_karlsruhe.model.Transformation;

public class FizRecordFactoryTest {

  private FizRecordFactory factory;

  @Before
  public void init() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("FizOaiBackend.baseURL", "http://localhost:1080/");
    properties.setProperty("FizRecordFactory.repositoryIdentifier", "fiz-karlsruhe.de");
    properties.setProperty("FizRecordFactory.refreshFormatSeconds", "999999");

    // The constructor reaches out to the backend to seed the FormatRegistry; stub it out so this
    // test exercises only FizRecordFactory's own record-mapping logic, not BackendService/HTTP.
    try (MockedStatic<FizRecordFactory> mocked = mockStatic(FizRecordFactory.class)) {
      mocked.when(() -> FizRecordFactory.initFormats(properties))
          .thenReturn(Collections.singletonList(new Format()));
      mocked.when(() -> FizRecordFactory.initTransformations(properties))
          .thenReturn(Collections.singletonList(new Transformation()));

      factory = new FizRecordFactory(properties);
    }
  }

  @After
  public void tearDown() {
    factory.shutdown();
  }

  @Test
  public void fromOAIIdentifierExtractsLocalIdentifierAfterRepositoryPrefix() {
    assertEquals("10.0133/10000386", factory.fromOAIIdentifier("oai:fiz-karlsruhe.de:10.0133/10000386"));
  }

  @Test
  public void fromOAIIdentifierReturnsNullForMalformedIdentifier() {
    assertNull(factory.fromOAIIdentifier("not-a-valid-oai-identifier"));
  }

  @Test
  public void getOAIIdentifierPrependsRepositoryIdentifier() {
    Item item = new Item();
    item.setIdentifier("10.0133/10000386");

    assertEquals("oai:fiz-karlsruhe.de:10.0133/10000386", factory.getOAIIdentifier(item));
  }

  @Test
  public void getLocalIdentifierReturnsRawItemIdentifier() {
    Item item = new Item();
    item.setIdentifier("10.0133/10000386");

    assertEquals("10.0133/10000386", factory.getLocalIdentifier(item));
  }

  @Test
  public void getDatestampReturnsItemDatestamp() {
    Item item = new Item();
    item.setDatestamp("2021-06-01T00:00:00Z");

    assertEquals("2021-06-01T00:00:00Z", factory.getDatestamp(item));
  }

  @Test
  public void getSetSpecsReturnsItemSets() {
    Item item = new Item();
    item.setSets(Arrays.asList("institution", "institution:hannover"));

    Iterator setSpecs = factory.getSetSpecs(item);

    assertEquals("institution", setSpecs.next());
    assertEquals("institution:hannover", setSpecs.next());
    assertFalse(setSpecs.hasNext());
  }

  @Test
  public void getAboutsIsAlwaysNull() {
    assertNull(factory.getAbouts(new Item()));
  }

  @Test
  public void isDeletedReflectsItemDeleteFlag() {
    Item deletedItem = new Item();
    deletedItem.setDeleteFlag(true);
    Item activeItem = new Item();
    activeItem.setDeleteFlag(false);

    assertTrue(factory.isDeleted(deletedItem));
    assertFalse(factory.isDeleted(activeItem));
  }

  @Test
  public void isDeletedReturnsFalseForNullNativeItem() {
    assertFalse(factory.isDeleted(null));
  }

  @Test
  public void quickCreateAlwaysDefersToDefaultCreateLogic() {
    assertNull(factory.quickCreate(new Item(), "schema", "oai_dc"));
  }
}
