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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

/**
 * BadVerb is what OAIHandler falls back to for an unrecognized/missing verb parameter
 * ({@code OAIHandler.missingVerbClass}), so its response needs to stay a well-formed
 * OAI-PMH error document even though, unlike the other verbs, it never validates its
 * own arguments.
 */
public class BadVerbTest {

  @Test
  public void constructBuildsAWellFormedBadVerbErrorResponse() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.org/oai"));
    HttpServletResponse response = mock(HttpServletResponse.class);

    String result = BadVerb.construct(new HashMap(), request, response, null);

    assertTrue(result.contains("<request>http://example.org/oai</request>"));
    assertTrue(result.contains("<error code=\"badVerb\">Illegal verb</error>"));
    assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"));
    assertTrue(result.endsWith("</OAI-PMH>"));
  }
}
