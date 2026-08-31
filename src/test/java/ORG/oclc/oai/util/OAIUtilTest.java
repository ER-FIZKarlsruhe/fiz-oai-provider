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

package ORG.oclc.oai.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class OAIUtilTest {

  // --- xmlEncode -----------------------------------------------------------------

  @Test
  public void xmlEncodeEscapesAllFiveXmlSpecialCharacters() {
    String encoded = OAIUtil.xmlEncode("<a & b> \"quoted\" 'apos'");

    assertEquals("&lt;a &amp; b&gt; &quot;quoted&quot; &apos;apos&apos;", encoded);
  }

  @Test
  public void xmlEncodeLeavesPlainTextUnchanged() {
    assertEquals("plain text 123", OAIUtil.xmlEncode("plain text 123"));
  }

  @Test
  public void xmlEncodeOfAnEmptyStringIsEmpty() {
    assertEquals("", OAIUtil.xmlEncode(""));
  }

  // --- toLCCNDisplay ---------------------------------------------------------------

  @Test
  public void toLCCNDisplayFormatsTheDocumentedExample() {
    // The example straight from this method's own javadoc.
    assertEquals("n2001-50268", OAIUtil.toLCCNDisplay("n 2001050268"));
  }

  @Test
  public void toLCCNDisplayUsesATwoCharPrefixWhenTheThirdCharacterIsADigit() {
    assertEquals("ab1234-567", OAIUtil.toLCCNDisplay("ab1234567"));
  }

  @Test
  public void toLCCNDisplayUsesAThreeCharPrefixWhenTheThirdCharacterIsNotADigit() {
    assertEquals("abc12-34567", OAIUtil.toLCCNDisplay("abc1234567"));
  }

  @Test
  public void toLCCNDisplayDropsLeadingZerosFromTheSerialNumber() {
    // The serial number is round-tripped through Integer.parseInt/toString, so
    // padding zeros are not preserved in the display format.
    assertEquals("ab1234-7", OAIUtil.toLCCNDisplay("ab1234007"));
  }

  // --- parse / getThreadedDocumentBuilder -------------------------------------------

  @Test
  public void parseReturnsADocumentForWellFormedXml() throws Exception {
    Document document = OAIUtil.parse(streamOf("<root><child>hello</child></root>"));

    assertEquals("root", document.getDocumentElement().getNodeName());
  }

  @Test(expected = SAXException.class)
  public void parseRejectsADoctypeDeclaration() throws Exception {
    // Regression guard for the XXE hardening: disallow-doctype-decl must stay enabled.
    String xxeXml = "<?xml version=\"1.0\"?><!DOCTYPE root [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]>"
        + "<root>&xxe;</root>";

    OAIUtil.parse(streamOf(xxeXml));
  }

  @Test
  public void getThreadedDocumentBuilderReturnsAFreshInstanceEachCall() throws Exception {
    // DocumentBuilder is not thread-safe, so callers must get a new one every time
    // rather than a cached/shared instance.
    DocumentBuilder first = OAIUtil.getThreadedDocumentBuilder();
    DocumentBuilder second = OAIUtil.getThreadedDocumentBuilder();

    assertNotSame(first, second);
  }

  private static ByteArrayInputStream streamOf(String xml) {
    return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
  }
}
