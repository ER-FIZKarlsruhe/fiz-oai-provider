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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import de.fiz_karlsruhe.FormatRegistry;
import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Item;

public class RecordFactoryTest {

  private static class TestRecordFactory extends RecordFactory {

    @Override
    public String fromOAIIdentifier(String identifier) {
      return identifier;
    }

    @Override
    public String quickCreate(Object nativeItem, String schemaURL, String metadataPrefix)
        throws CannotDisseminateFormatException {
      return null;
    }

    @Override
    public String getOAIIdentifier(Object nativeItem) {
      return "oai:test:" + ((Item) nativeItem).getIdentifier();
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
      return Boolean.TRUE.equals(((Item) nativeItem).getDeleteFlag());
    }

    @Override
    public Iterator getAbouts(Object nativeItem) {
      return null;
    }
  }

  @Test
  public void createHeaderStaticVariantRendersIdentifierDatestampAndSetSpecs() {
    Iterator setSpecs = Arrays.asList("set1", "set2").iterator();

    String[] header = RecordFactory.createHeader("oai:test:1", "2020-01-01", setSpecs, false);

    assertEquals("oai:test:1", header[1]);
    assertTrue(header[0].contains("<identifier>oai:test:1</identifier>"));
    assertTrue(header[0].contains("<datestamp>2020-01-01</datestamp>"));
    assertTrue(header[0].contains("<setSpec>set1</setSpec>"));
    assertTrue(header[0].contains("<setSpec>set2</setSpec>"));
    assertTrue(header[0].startsWith("<header>"));
  }

  @Test
  public void createHeaderStaticVariantMarksDeletedRecords() {
    String[] header = RecordFactory.createHeader("oai:test:1", "2020-01-01", null, true);

    assertTrue(header[0].startsWith("<header status=\"deleted\">"));
  }

  @Test
  public void createHeaderInstanceVariantDerivesComponentsFromNativeItem() {
    TestRecordFactory factory = new TestRecordFactory();
    Item item = new Item();
    item.setIdentifier("123");
    item.setDatestamp("2021-06-01");
    item.setSets(Collections.singletonList("institution"));

    String[] header = factory.createHeader(item);

    assertEquals("oai:test:123", header[1]);
    assertTrue(header[0].contains("<setSpec>institution</setSpec>"));
  }

  @Test
  public void getLocalIdentifierRoundTripsThroughFromOAIIdentifierAndGetOAIIdentifier() {
    TestRecordFactory factory = new TestRecordFactory();
    Item item = new Item();
    item.setIdentifier("123");

    assertEquals("oai:test:123", factory.getLocalIdentifier(item));
  }

  @Test
  public void getSchemaLocationsFiltersToFormatsSupportedByTheItem()
      throws ORG.oclc.oai.server.verb.NoMetadataFormatsException {
    TestRecordFactory factory = new TestRecordFactory();

    Format oaiDc = new Format();
    oaiDc.setMetadataPrefix("oai_dc");
    Format datacite = new Format();
    datacite.setMetadataPrefix("datacite");
    factory.setFormatRegistry(new FormatRegistry(Arrays.asList(oaiDc, datacite), Collections.emptyList()));

    Item item = new Item();
    item.setIdentifier("123");
    item.setDeleteFlag(false);
    item.setFormats(Collections.singletonList("oai_dc"));

    List schemaLocations = factory.getSchemaLocations(item);

    assertEquals(1, schemaLocations.size());
    assertEquals("oai_dc", ((Format) schemaLocations.get(0)).getMetadataPrefix());
  }

  @Test(expected = ORG.oclc.oai.server.verb.NoMetadataFormatsException.class)
  public void getSchemaLocationsRejectsDeletedItems() throws ORG.oclc.oai.server.verb.NoMetadataFormatsException {
    TestRecordFactory factory = new TestRecordFactory();
    Item item = new Item();
    item.setDeleteFlag(true);

    factory.getSchemaLocations(item);
  }
}
