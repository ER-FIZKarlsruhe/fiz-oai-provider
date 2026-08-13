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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Properties;

import jakarta.servlet.ServletContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * ConfigurationService is a classic lazy singleton (static INSTANCE field), so every
 * test resets that field via reflection first to get a clean, independently configurable
 * instance instead of leaking state between tests/test classes.
 */
public class ConfigurationServiceTest {

  @Before
  @After
  public void resetSingleton() throws Exception {
    Field instance = ConfigurationService.class.getDeclaredField("INSTANCE");
    instance.setAccessible(true);
    instance.set(null, null);

    Field properties = ConfigurationService.class.getDeclaredField("properties");
    properties.setAccessible(true);
    properties.set(null, null);
  }

  @Test
  public void getInstanceWithoutPriorInitializationThrows() {
    try {
      ConfigurationService.getInstance();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertEquals("Service must be initialized with backendUrl first!", e.getMessage());
    }
  }

  @Test
  public void getInstanceIsInitializedOnlyOnce() {
    ConfigurationService first = ConfigurationService.getInstance(new Properties());
    Properties secondProperties = new Properties();
    secondProperties.setProperty("branding.service.name", "should be ignored");
    ConfigurationService second = ConfigurationService.getInstance(secondProperties);

    assertSame(first, second);
    assertEquals("fiz-oai-provider", second.getBrandingServiceName());
  }

  @Test
  public void brandingGettersFallBackToDefaultsWhenPropertyIsMissing() {
    ConfigurationService service = ConfigurationService.getInstance(new Properties());

    assertEquals("fiz-oai-provider", service.getBrandingServiceName());
    assertEquals("https://www.fiz-karlsruhe.de", service.getBrandingServiceUrl());
    assertEquals("#659932", service.getBrandingColor());
    assertEquals("This is the OAI provider for FIZ Karlsruhe", service.getBrandingWelcomeText());
    assertEquals("https://www.fiz-karlsruhe.de/de/ueber-uns/impressum-rechtliches", service.getBrandingImpressum());
    assertEquals("https://www.fiz-karlsruhe.de/de/ueber-uns/datenschutzerklaerung", service.getBrandingPrivacy());
    assertEquals("Arial, Helvetica, Geneva, Verdana, sans-serif;", service.getBrandingFontFamily());
    assertEquals("#000;", service.getBrandingFontColor());
    assertEquals(15000, service.getHttpSocketTimeout());
    assertEquals(15000, service.getHttpConnectionTimeout());
  }

  @Test
  public void brandingGettersReturnConfiguredValuesWhenPresent() {
    Properties properties = new Properties();
    properties.setProperty("branding.service.name", "Custom Service");
    properties.setProperty("branding.service.url", "https://custom.example");
    properties.setProperty("branding.header.color", "#123456");
    properties.setProperty("branding.welcome.text", "Welcome!");
    properties.setProperty("branding.imprint.url", "https://custom.example/imprint");
    properties.setProperty("branding.privacy.url", "https://custom.example/privacy");
    properties.setProperty("branding.font.family", "Comic Sans MS");
    properties.setProperty("branding.font.color", "#abcdef");
    properties.setProperty("http.socket.timeout", "5000");
    properties.setProperty("http.connection.timeout", "6000");
    ConfigurationService service = ConfigurationService.getInstance(properties);

    assertEquals("Custom Service", service.getBrandingServiceName());
    assertEquals("https://custom.example", service.getBrandingServiceUrl());
    assertEquals("#123456", service.getBrandingColor());
    assertEquals("Welcome!", service.getBrandingWelcomeText());
    assertEquals("https://custom.example/imprint", service.getBrandingImpressum());
    assertEquals("https://custom.example/privacy", service.getBrandingPrivacy());
    assertEquals("Comic Sans MS", service.getBrandingFontFamily());
    assertEquals("#abcdef", service.getBrandingFontColor());
    assertEquals(5000, service.getHttpSocketTimeout());
    assertEquals(6000, service.getHttpConnectionTimeout());
  }

  @Test
  public void getBrandingLogoUsesConfiguredValueWithoutTouchingServletContext() {
    Properties properties = new Properties();
    properties.setProperty("branding.logo", "/opt/oai/custom-logo.jpg");
    ConfigurationService service = ConfigurationService.getInstance(properties);
    ServletContext context = Mockito.mock(ServletContext.class);

    String logo = service.getBrandingLogo(context);

    assertEquals("/opt/oai/custom-logo.jpg", logo);
    Mockito.verifyNoInteractions(context);
  }

  @Test
  public void getBrandingLogoFallsBackToServletContextRealPath() {
    ConfigurationService service = ConfigurationService.getInstance(new Properties());
    ServletContext context = Mockito.mock(ServletContext.class);
    Mockito.when(context.getRealPath("/banner02.gif")).thenReturn("/webapp/banner02.gif");

    String logo = service.getBrandingLogo(context);

    assertEquals("/webapp/banner02.gif", logo);
  }
}
