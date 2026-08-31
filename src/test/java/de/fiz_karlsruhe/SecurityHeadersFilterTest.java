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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

public class SecurityHeadersFilterTest {

  private static final String EXPECTED_CSP =
      "default-src 'self'; "
      + "script-src 'self'; "
      + "style-src 'self' 'unsafe-inline'; "
      + "img-src 'self' http://www.openarchives.org; "
      + "object-src 'none'; "
      + "base-uri 'self'; "
      + "form-action 'self'; "
      + "frame-ancestors 'none'";

  private final SecurityHeadersFilter filter = new SecurityHeadersFilter();

  @Test
  public void doFilterSetsBaselineSecurityHeadersAndInvokesTheChain() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isSecure()).thenReturn(false);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);

    filter.doFilter(request, response, chain);

    verify(response).setHeader("X-Content-Type-Options", "nosniff");
    verify(response).setHeader("X-Frame-Options", "DENY");
    verify(response).setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
    verify(response).setHeader("Content-Security-Policy", EXPECTED_CSP);
    verify(chain).doFilter(request, response);
  }

  @Test
  public void doFilterOmitsHstsOverPlainHttp() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isSecure()).thenReturn(false);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);

    filter.doFilter(request, response, chain);

    verify(response, never()).setHeader(eq("Strict-Transport-Security"), any());
  }

  @Test
  public void doFilterAddsHstsHeaderWhenTheConnectionIsSecure() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isSecure()).thenReturn(true);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);

    filter.doFilter(request, response, chain);

    verify(response).setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
  }

  @Test
  public void doFilterStillInvokesTheChainWhenTheResponseIsNotAnHttpServletResponse() throws Exception {
    ServletRequest request = mock(ServletRequest.class);
    ServletResponse response = mock(ServletResponse.class);
    FilterChain chain = mock(FilterChain.class);

    filter.doFilter(request, response, chain);

    verify(chain).doFilter(request, response);
  }
}
