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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Transformer;

import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.fiz_karlsruhe.OaiRuntimeException;
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

  @Test
  public void getTransformerReturnsNullWhenNoStyleSheetIsConfigured() throws Throwable {
    OAIHandler handler = new OAIHandler();

    Transformer transformer = handler.getTransformer(new Properties(), new HashMap());

    assertNull(transformer);
  }

  @Test
  public void getTransformerLoadsStylesheetFromServletContextAndCachesTheCompiledTemplates() throws Throwable {
    File xslFile = temporaryFolder.newFile("stylesheet.xsl");
    writeIdentityHtmlStylesheet(xslFile);
    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getResource("/stylesheet.xsl")).thenReturn(xslFile.toURI().toURL());
    OAIHandler handler = new OAIHandler();
    wireServletContext(handler, servletContext);
    Properties properties = new Properties();
    properties.setProperty("OAIHandler.styleSheet", "/stylesheet.xsl");
    Map attributes = new HashMap();

    Transformer first = handler.getTransformer(properties, attributes);
    Transformer second = handler.getTransformer(properties, attributes);

    assertNotNull(first);
    assertNotNull(second);
    // Templates (the compiled stylesheet) is cached in the attributes map, so the second
    // call must reuse it instead of re-reading and recompiling the stylesheet resource.
    assertNotSame(first, second);
    assertNotNull(attributes.get("OAIHandler.templates"));
    verify(servletContext, times(1)).getResource("/stylesheet.xsl");
  }

  @Test(expected = FileNotFoundException.class)
  public void getTransformerThrowsFileNotFoundWhenTheStylesheetResourceIsMissing() throws Throwable {
    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getResource("/missing.xsl")).thenReturn(null);
    OAIHandler handler = new OAIHandler();
    wireServletContext(handler, servletContext);
    Properties properties = new Properties();
    properties.setProperty("OAIHandler.styleSheet", "/missing.xsl");

    handler.getTransformer(properties, new HashMap());
  }

  @Test(expected = OaiRuntimeException.class)
  public void getTransformerRejectsStylesheetsThatReferenceAnExternalDtd() throws Throwable {
    File externalDtd = temporaryFolder.newFile("external.dtd");
    try (FileWriter writer = new FileWriter(externalDtd)) {
      writer.write("<!ENTITY xxe \"leaked-secret\">");
    }
    File xslFile = temporaryFolder.newFile("xxe-stylesheet.xsl");
    try (FileWriter writer = new FileWriter(xslFile)) {
      writer.write("<?xml version=\"1.0\"?>\n"
          + "<!DOCTYPE xsl:stylesheet SYSTEM \"" + externalDtd.toURI() + "\">\n"
          + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
          + "  <xsl:template match=\"/\"><html>&xxe;</html></xsl:template>\n"
          + "</xsl:stylesheet>\n");
    }
    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getResource("/xxe-stylesheet.xsl")).thenReturn(xslFile.toURI().toURL());
    OAIHandler handler = new OAIHandler();
    wireServletContext(handler, servletContext);
    Properties properties = new Properties();
    properties.setProperty("OAIHandler.styleSheet", "/xxe-stylesheet.xsl");

    // getTransformer sets ACCESS_EXTERNAL_DTD="" on the TransformerFactory specifically to
    // block this; a regression here would silently re-open the XXE hole closed in b649cae.
    handler.getTransformer(properties, new HashMap());
  }

  @Test
  public void getWriterUsesGzipWhenAcceptEncodingIncludesGzip() throws Throwable {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Encoding")).thenReturn("gzip, deflate");
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(response.getOutputStream()).thenReturn(new NoopServletOutputStream());

    OAIHandler.getWriter(request, response).close();

    verify(response).setHeader("Content-Encoding", "gzip");
  }

  @Test
  public void getWriterUsesDeflateWhenAcceptEncodingIncludesOnlyDeflate() throws Throwable {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Encoding")).thenReturn("deflate");
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(response.getOutputStream()).thenReturn(new NoopServletOutputStream());

    OAIHandler.getWriter(request, response).close();

    verify(response).setHeader("Content-Encoding", "deflate");
  }

  @Test
  public void getWriterFallsBackToThePlainResponseWriterWhenNoAcceptEncodingIsSent() throws Throwable {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Encoding")).thenReturn(null);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter printWriter = new PrintWriter(new StringWriter());
    when(response.getWriter()).thenReturn(printWriter);

    OAIHandler.getWriter(request, response);

    verify(response).getWriter();
  }

  @Test
  public void getWriterSkipsGzipWhenItIsExplicitlyExcludedWithAZeroQvalue() throws Throwable {
    // RFC 7231: a coding listed with qvalue 0 is explicitly not acceptable, even though the
    // old substring-only check would have matched "gzip" here and used it anyway.
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Encoding")).thenReturn("gzip;q=0, deflate");
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(response.getOutputStream()).thenReturn(new NoopServletOutputStream());

    OAIHandler.getWriter(request, response).close();

    verify(response).setHeader("Content-Encoding", "deflate");
  }

  @Test
  public void getWriterFallsBackToPlainWhenAllCodingsAreExcludedWithZeroQvalues() throws Throwable {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Encoding")).thenReturn("gzip;q=0, deflate;q=0");
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter printWriter = new PrintWriter(new StringWriter());
    when(response.getWriter()).thenReturn(printWriter);

    OAIHandler.getWriter(request, response);

    verify(response).getWriter();
  }

  @Test
  public void getWriterHonorsAWildcardQvalueWhenGzipIsNotListedExplicitly() throws Throwable {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Encoding")).thenReturn("*;q=0.5");
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(response.getOutputStream()).thenReturn(new NoopServletOutputStream());

    OAIHandler.getWriter(request, response).close();

    verify(response).setHeader("Content-Encoding", "gzip");
  }

  @Test
  public void isEncodingAcceptableTreatsAMissingQvalueAsFullyAcceptable() {
    assertTrue(OAIHandler.isEncodingAcceptable("gzip, deflate", "gzip"));
    assertTrue(OAIHandler.isEncodingAcceptable("gzip, deflate", "deflate"));
  }

  @Test
  public void isEncodingAcceptableIsFalseWhenTheHeaderIsAbsent() {
    assertFalse(OAIHandler.isEncodingAcceptable(null, "gzip"));
  }

  @Test
  public void doGetPassesANonNullTransformerToTheVerbAndSetsHtmlContentTypeWhenRenderHtmlIsTrue() throws Throwable {
    File xslFile = temporaryFolder.newFile("stylesheet.xsl");
    writeIdentityHtmlStylesheet(xslFile);
    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getResource("/stylesheet.xsl")).thenReturn(xslFile.toURI().toURL());
    OAIHandler handler = new OAIHandler();
    wireServletContext(handler, servletContext);

    Properties requestProperties = new Properties();
    requestProperties.setProperty("OAIHandler.styleSheet", "/stylesheet.xsl");
    requestProperties.setProperty("ExtensionVerbs.RecordingVerb", RecordingVerb.class.getName());
    HashMap globalAttributes = new HashMap();
    globalAttributes.put("OAIHandler.properties", requestProperties);
    handler.attributesMap.put("global", globalAttributes);

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getPathInfo()).thenReturn("/extension");
    when(request.getParameter("verb")).thenReturn("RecordingVerb");
    when(request.getParameter("renderHtml")).thenReturn("true");
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

    RecordingVerb.lastTransformer = null;
    handler.doGet(request, response);

    verify(response).setContentType("text/html; charset=UTF-8");
    assertNotNull(RecordingVerb.lastTransformer);
  }

  @Test
  public void doGetPassesANullTransformerAndSetsXmlContentTypeWhenRenderHtmlIsAbsent() throws Throwable {
    OAIHandler handler = new OAIHandler();

    Properties requestProperties = new Properties();
    requestProperties.setProperty("ExtensionVerbs.RecordingVerb", RecordingVerb.class.getName());
    HashMap globalAttributes = new HashMap();
    globalAttributes.put("OAIHandler.properties", requestProperties);
    handler.attributesMap.put("global", globalAttributes);

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getPathInfo()).thenReturn("/extension");
    when(request.getParameter("verb")).thenReturn("RecordingVerb");
    when(request.getParameter("renderHtml")).thenReturn(null);
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

    RecordingVerb.lastTransformer = "unset";
    handler.doGet(request, response);

    verify(response).setContentType("text/xml; charset=UTF-8");
    assertNull(RecordingVerb.lastTransformer);
  }

  @Test
  public void getResultReturnsBadVerbWhenTheVerbParameterIsRepeated() throws Throwable {
    // Per the OAI-PMH spec, a repeated verb argument is itself a badVerb condition - it must
    // not fall through to whichever verb class the first value happens to resolve to and be
    // evaluated there as a badArgument instead.
    HashMap attributes = new HashMap();
    attributes.put("OAIHandler.missingVerbClass", ORG.oclc.oai.server.verb.BadVerb.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.org/oai"));
    when(request.getParameter("verb")).thenReturn("Identify");
    when(request.getParameterValues("verb")).thenReturn(new String[] {"Identify", "GetRecord"});
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = OAIHandler.getResult(attributes, request, response, null,
        ORG.oclc.oai.server.verb.ServerVerb.getVerbs(), new HashMap(), "/extension");

    assertTrue(result.contains("<error code=\"badVerb\">Illegal verb</error>"));
  }

  private static void writeIdentityHtmlStylesheet(File file) throws Exception {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write("<?xml version=\"1.0\"?>\n"
          + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
          + "  <xsl:output method=\"html\"/>\n"
          + "  <xsl:template match=\"/\"><html><body><xsl:value-of select=\"/root/text()\"/>"
          + "</body></html></xsl:template>\n"
          + "</xsl:stylesheet>\n");
    }
  }

  private static void wireServletContext(OAIHandler handler, ServletContext servletContext) throws Exception {
    ServletConfig servletConfig = mock(ServletConfig.class);
    when(servletConfig.getServletContext()).thenReturn(servletContext);
    when(servletConfig.getServletName()).thenReturn("oai");
    Field configField = GenericServlet.class.getDeclaredField("config");
    configField.setAccessible(true);
    configField.set(handler, servletConfig);
  }

  /**
   * A minimal extension verb, wired up via the {@code ExtensionVerbs.*} property mechanism
   * ({@link ORG.oclc.oai.server.verb.ServerVerb#getExtensionVerbs}), used to observe what
   * {@link OAIHandler#doGet} actually hands down to a verb's {@code construct} method.
   */
  public static class RecordingVerb {
    static volatile Object lastTransformer = "unset";

    public static void init(Properties properties) {
      // no-op: required by the ExtensionVerbs contract
    }

    public static String construct(HashMap context, HttpServletRequest request, HttpServletResponse response,
        Transformer transformer) {
      lastTransformer = transformer;
      return "<root>hello</root>";
    }
  }

  private static class NoopServletOutputStream extends jakarta.servlet.ServletOutputStream {
    @Override
    public void write(int b) {
      // discarded: only used to satisfy the getWriter() gzip/deflate wrapping under test
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
      // not needed for this test
    }
  }
}
