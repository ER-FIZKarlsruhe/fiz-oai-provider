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

package ORG.oclc.oai.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.fiz_karlsruhe.service.ConfigurationService;

public class OAIHandlerTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private String originalConfFolder;
  private String originalCatalinaBase;

  @Before
  public void saveSystemProperties() {
    originalConfFolder = System.getProperty("oai.provider.conf.folder");
    originalCatalinaBase = System.getProperty("catalina.base");
  }

  @Before
  @After
  public void resetConfigurationServiceSingleton() throws Exception {
    Field instance = ConfigurationService.class.getDeclaredField("INSTANCE");
    instance.setAccessible(true);
    instance.set(null, null);

    Field properties = ConfigurationService.class.getDeclaredField("properties");
    properties.setAccessible(true);
    properties.set(null, null);
  }

  @After
  public void restoreSystemProperties() {
    restoreProperty("oai.provider.conf.folder", originalConfFolder);
    restoreProperty("catalina.base", originalCatalinaBase);
  }

  private static void restoreProperty(String key, String value) {
    if (value == null) {
      System.clearProperty(key);
    } else {
      System.setProperty(key, value);
    }
  }

  @Test
  public void getVERSIONReturnsANonEmptyVersionString() {
    assertTrue(OAIHandler.getVERSION().length() > 0);
  }

  @Test
  public void getConfigFolderPrefersTheExplicitConfProperty() {
    System.clearProperty("catalina.base");
    System.setProperty("oai.provider.conf.folder", temporaryFolder.getRoot().getPath());

    OAIHandler handler = new OAIHandler();

    assertEquals(temporaryFolder.getRoot().getAbsolutePath(), handler.getConfigFolder());
  }

  @Test
  public void getConfigFolderFallsBackToCatalinaBaseConfDirectory() {
    System.clearProperty("oai.provider.conf.folder");
    System.setProperty("catalina.base", temporaryFolder.getRoot().getPath());

    OAIHandler handler = new OAIHandler();

    assertEquals(new File(temporaryFolder.getRoot(), "conf").getAbsolutePath(), handler.getConfigFolder());
  }

  @Test
  public void getConfigFolderIsNullWhenNeitherPropertyIsSet() {
    System.clearProperty("oai.provider.conf.folder");
    System.clearProperty("catalina.base");

    OAIHandler handler = new OAIHandler();

    assertNull(handler.getConfigFolder());
  }

  @Test
  public void readConfigFromFileLoadsPropertiesAndInitializesConfigurationService() throws Exception {
    File propertiesFile = temporaryFolder.newFile("oaicat.properties");
    try (FileWriter writer = new FileWriter(propertiesFile)) {
      writer.write("branding.service.name=Custom Service\n");
    }

    OAIHandler handler = new OAIHandler();
    boolean loaded = handler.readConfigFromFile(temporaryFolder.getRoot().getPath(), "oaicat.properties");

    assertTrue(loaded);
    assertEquals("Custom Service", ConfigurationService.getInstance().getBrandingServiceName());
  }

  @Test
  public void readConfigFromFileReturnsFalseWhenFileIsMissing() {
    OAIHandler handler = new OAIHandler();

    boolean loaded = handler.readConfigFromFile(temporaryFolder.getRoot().getPath(), "does-not-exist.properties");

    assertFalse(loaded);
  }

  @Test
  public void printConfigurationDoesNotThrowAfterConfigIsLoaded() throws Exception {
    File propertiesFile = temporaryFolder.newFile("oaicat.properties");
    try (FileWriter writer = new FileWriter(propertiesFile)) {
      writer.write("some.password=secret\nbranding.service.name=Custom Service\n");
    }
    OAIHandler handler = new OAIHandler();
    handler.readConfigFromFile(temporaryFolder.getRoot().getPath(), "oaicat.properties");

    handler.printConfiguration();
  }
}
