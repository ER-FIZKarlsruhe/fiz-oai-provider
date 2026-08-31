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

package ORG.oclc.oai.server.verb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Each of these exceptions carries the OAI-PMH error XML fragment as its message; the verb
 * classes rely on {@code getMessage()} returning exactly that fragment so it can be appended
 * straight into the response (see e.g. GetRecord's catch blocks). These tests pin that
 * contract down for every OAI error code so a wording change is caught here rather than in
 * a harvester's response parser.
 */
public class OaiVerbExceptionsTest {

  @Test
  public void badArgumentExceptionCarriesTheBadArgumentErrorCode() {
    assertEquals(
        "<error code=\"badArgument\">The request includes illegal arguments, is missing "
            + "required arguments, includes a repeated argument, or values for arguments "
            + "have an illegal syntax.</error>",
        new BadArgumentException().getMessage());
  }

  @Test
  public void badResumptionTokenExceptionCarriesTheBadResumptionTokenErrorCode() {
    assertEquals(
        "<error code=\"badResumptionToken\">The value of the resumptionToken argument is "
            + "invalid or expired</error>",
        new BadResumptionTokenException().getMessage());
  }

  @Test
  public void cannotDisseminateFormatExceptionEmbedsTheOffendingMetadataPrefix() {
    String message = new CannotDisseminateFormatException("weird_format").getMessage();

    assertTrue(message.startsWith("<error code=\"cannotDisseminateFormat\">"));
    assertTrue(message.contains("\"weird_format\""));
  }

  @Test
  public void idDoesNotExistExceptionEmbedsTheOffendingIdentifier() {
    String message = new IdDoesNotExistException("oai:example.org:missing").getMessage();

    assertTrue(message.startsWith("<error code=\"idDoesNotExist\">"));
    assertTrue(message.contains("\"oai:example.org:missing\""));
  }

  @Test
  public void noItemsMatchExceptionCarriesTheNoRecordsMatchErrorCode() {
    assertEquals(
        "<error code=\"noRecordsMatch\">The combination of the values of the from, until, "
            + "set, and metadataPrefix arguments results in an empty list.</error>",
        new NoItemsMatchException().getMessage());
  }

  @Test
  public void noMetadataFormatsExceptionCarriesTheNoMetadataFormatsErrorCode() {
    assertEquals(
        "<error code=\"noMetadataFormats\">There are no metadata formats available for the "
            + "specified item.</error>",
        new NoMetadataFormatsException().getMessage());
  }

  @Test
  public void noRecordsMatchExceptionInheritsNoItemsMatchExceptionsMessage() {
    NoRecordsMatchException exception = new NoRecordsMatchException();

    assertEquals(new NoItemsMatchException().getMessage(), exception.getMessage());
    // ListIdentifiers/ListRecords only catch NoItemsMatchException, relying on this subtype
    // relationship to also handle NoRecordsMatchException.
    assertTrue(exception instanceof NoItemsMatchException);
  }

  @Test
  public void noSetHierarchyExceptionCarriesTheNoSetHierarchyErrorCode() {
    assertEquals("<error code=\"noSetHierarchy\">The repository does not support sets.</error>",
        new NoSetHierarchyException().getMessage());
  }

  @Test
  public void oaiInternalServerErrorPassesTheMessageThrough() {
    assertEquals("backend unreachable", new OAIInternalServerError("backend unreachable").getMessage());
  }
}
