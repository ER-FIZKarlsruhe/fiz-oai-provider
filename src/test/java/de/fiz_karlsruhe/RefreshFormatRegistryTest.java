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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.mockito.MockedStatic;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Transformation;

public class RefreshFormatRegistryTest {

  @Test
  public void runUpdatesRegistryWhenFormatsAndTransformationsArePresent() {
    Properties properties = new Properties();
    FormatRegistry formatRegistry = mock(FormatRegistry.class);
    List<Format> formats = Arrays.asList(new Format());
    List<Transformation> transformations = Arrays.asList(new Transformation());

    try (MockedStatic<FizRecordFactory> factory = mockStatic(FizRecordFactory.class)) {
      factory.when(() -> FizRecordFactory.initFormats(properties)).thenReturn(formats);
      factory.when(() -> FizRecordFactory.initTransformations(properties)).thenReturn(transformations);

      new RefreshFormatRegistry(formatRegistry, properties).run();

      verify(formatRegistry).setFormats(formats);
      verify(formatRegistry).setTransformations(transformations);
    }
  }

  @Test
  public void runSkipsUpdateWhenFormatsAreNull() {
    Properties properties = new Properties();
    FormatRegistry formatRegistry = mock(FormatRegistry.class);

    try (MockedStatic<FizRecordFactory> factory = mockStatic(FizRecordFactory.class)) {
      factory.when(() -> FizRecordFactory.initFormats(properties)).thenReturn(null);
      factory.when(() -> FizRecordFactory.initTransformations(properties)).thenReturn(null);

      new RefreshFormatRegistry(formatRegistry, properties).run();

      verify(formatRegistry, never()).setFormats(any());
      verify(formatRegistry, never()).setTransformations(any());
    }
  }

  @Test
  public void runSkipsUpdateWhenFormatsAreEmpty() {
    Properties properties = new Properties();
    FormatRegistry formatRegistry = mock(FormatRegistry.class);

    try (MockedStatic<FizRecordFactory> factory = mockStatic(FizRecordFactory.class)) {
      factory.when(() -> FizRecordFactory.initFormats(properties)).thenReturn(Collections.emptyList());
      factory.when(() -> FizRecordFactory.initTransformations(properties)).thenReturn(Collections.emptyList());

      new RefreshFormatRegistry(formatRegistry, properties).run();

      verify(formatRegistry, never()).setFormats(any());
      verify(formatRegistry, never()).setTransformations(any());
    }
  }
}
