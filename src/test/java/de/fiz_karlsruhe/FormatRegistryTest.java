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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Transformation;

public class FormatRegistryTest {

  private Format format(String metadataPrefix, String schemaLocation) {
    Format format = new Format();
    format.setMetadataPrefix(metadataPrefix);
    format.setSchemaLocation(schemaLocation);
    return format;
  }

  private Transformation transformation(String name, String from, String to) {
    Transformation transformation = new Transformation();
    transformation.setName(name);
    transformation.setFormatFrom(from);
    transformation.setFormatTo(to);
    return transformation;
  }

  @Test
  public void constructorWithNullArgumentsLeavesEmptyLists() {
    FormatRegistry registry = new FormatRegistry(null, null);

    assertTrue(registry.getFormats().isEmpty());
    assertTrue(registry.getTransformations().isEmpty());
  }

  @Test
  public void constructorWithEmptyListsLeavesEmptyLists() {
    FormatRegistry registry = new FormatRegistry(Collections.emptyList(), Collections.emptyList());

    assertTrue(registry.getFormats().isEmpty());
    assertTrue(registry.getTransformations().isEmpty());
  }

  @Test
  public void setFormatsIgnoresNull() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    registry.setFormats(null);

    assertEquals(1, registry.getFormats().size());
    assertSame(oaiDc, registry.getFormats().get(0));
  }

  @Test
  public void setFormatsIgnoresEmptyList() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    registry.setFormats(Collections.emptyList());

    assertEquals(1, registry.getFormats().size());
    assertSame(oaiDc, registry.getFormats().get(0));
  }

  @Test
  public void setFormatsSkipsUpdateWhenContentIsUnchanged() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    // A different Format instance but equal by metadataPrefix (Format defines equals()/hashCode() on it).
    Format oaiDcAgain = format("oai_dc", "http://ns http://schema.xsd");
    registry.setFormats(Arrays.asList(oaiDcAgain));

    assertEquals(1, registry.getFormats().size());
    assertSame("content is unchanged, so the original instance must be kept", oaiDc, registry.getFormats().get(0));
  }

  @Test
  public void setFormatsReplacesWhenContentDiffers() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    Format radar = format("radar", "http://radar-ns http://radar.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    registry.setFormats(Arrays.asList(radar));

    assertEquals(1, registry.getFormats().size());
    assertSame(radar, registry.getFormats().get(0));
  }

  @Test
  public void setTransformationsIgnoresNullAndEmpty() {
    Transformation transformation = transformation("radar2datacite", "radar", "datacite");
    FormatRegistry registry = new FormatRegistry(null, Arrays.asList(transformation));

    registry.setTransformations(null);
    registry.setTransformations(Collections.emptyList());

    assertEquals(1, registry.getTransformations().size());
    assertSame(transformation, registry.getTransformations().get(0));
  }

  /**
   * Regression test for the bug where setTransformations() compared this.transformations
   * against itself instead of against the supplied list, so the periodic RefreshFormatRegistry
   * update was silently dropped. With the fix, a genuinely new list of transformations must
   * actually replace the previous ones.
   */
  @Test
  public void setTransformationsAppliesNewValues() {
    Transformation initial = transformation("radar2datacite", "radar", "datacite");
    Transformation updated = transformation("oai_dc2datacite", "oai_dc", "datacite");
    FormatRegistry registry = new FormatRegistry(null, Arrays.asList(initial));

    registry.setTransformations(Arrays.asList(updated));

    assertEquals(1, registry.getTransformations().size());
    assertSame(updated, registry.getTransformations().get(0));
  }

  @Test
  public void getMetadataPrefixResolvesFromNamespaceAndSchema() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    assertEquals("oai_dc", registry.getMetadataPrefix("http://ns", "http://schema.xsd"));
    assertNull(registry.getMetadataPrefix("http://unknown-ns", "http://schema.xsd"));
  }

  @Test
  public void getSchemaURLAndNamespaceURIAreParsedFromSchemaLocation() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    assertEquals("http://ns", registry.getNamespaceURI("oai_dc"));
    assertEquals("http://schema.xsd", registry.getSchemaURL("oai_dc"));
  }

  @Test
  public void getSchemaURLFallsBackToSingleTokenSchemaLocation() {
    Format oaiDc = format("oai_dc", "http://schema-only.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    assertEquals("http://schema-only.xsd", registry.getSchemaURL("oai_dc"));
  }

  @Test
  public void getSchemaLocationReturnsNullForUnknownPrefix() {
    FormatRegistry registry = new FormatRegistry(null, null);

    assertNull(registry.getSchemaLocation("unknown"));
  }

  @Test
  public void containsValueReflectsKnownFormats() {
    Format oaiDc = format("oai_dc", "http://ns http://schema.xsd");
    FormatRegistry registry = new FormatRegistry(Arrays.asList(oaiDc), null);

    assertTrue(registry.containsValue("oai_dc"));
    assertFalse(registry.containsValue("radar"));
  }
}
