/*
 * Copyright 2025 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
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

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Adds baseline security response headers to every request handled by this
 * webapp (the OAI-PMH XML/HTML endpoints and the static/JSP browse UI).
 */
public class SecurityHeadersFilter implements Filter {

  private static final String CONTENT_SECURITY_POLICY =
      "default-src 'self'; "
      + "script-src 'self'; "
      + "style-src 'self' 'unsafe-inline'; "
      + "img-src 'self' http://www.openarchives.org; "
      + "object-src 'none'; "
      + "base-uri 'self'; "
      + "form-action 'self'; "
      + "frame-ancestors 'none'";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (response instanceof HttpServletResponse httpResponse) {
      httpResponse.setHeader("X-Content-Type-Options", "nosniff");
      httpResponse.setHeader("X-Frame-Options", "DENY");
      httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
      httpResponse.setHeader("Content-Security-Policy", CONTENT_SECURITY_POLICY);

      if (request instanceof HttpServletRequest httpRequest && httpRequest.isSecure()) {
        // Only advertise HSTS when the current connection is actually TLS, so the
        // header is never sent for a plain-HTTP deployment/health check.
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
      }
    }
    chain.doFilter(request, response);
  }
}
