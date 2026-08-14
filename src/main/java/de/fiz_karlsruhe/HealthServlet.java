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

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import de.fiz_karlsruhe.model.Format;

/**
 * Kubernetes liveness/readiness probe endpoints, mapped to /health/live and /health/ready.
 *
 * Liveness only confirms the servlet container can serve a request; it never depends on the
 * backend, so a transient backend outage cannot trigger a pod restart. Readiness additionally
 * requires that OAIHandler finished initializing the AbstractCatalog and that its FormatRegistry
 * holds at least one format, which proves the backend has been reachable (at startup, or via the
 * periodic RefreshFormatRegistry) without this probe making its own backend call.
 */
public class HealthServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    boolean readinessCheck = request.getServletPath().endsWith("/ready");

    if (readinessCheck && !isReady(request.getServletContext())) {
      response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Catalog not yet initialized");
      return;
    }

    response.setContentType("text/plain;charset=UTF-8");
    response.getWriter().write("OK");
  }

  private boolean isReady(ServletContext servletContext) {
    Object catalogAttribute = servletContext.getAttribute("OAIHandler.catalog");
    if (!(catalogAttribute instanceof AbstractCatalog)) {
      return false;
    }

    AbstractCatalog catalog = (AbstractCatalog) catalogAttribute;
    if (catalog.getRecordFactory() == null || catalog.getRecordFactory().getFormatRegistry() == null) {
      return false;
    }

    List<Format> formats = catalog.getRecordFactory().getFormatRegistry().getFormats();
    return formats != null && !formats.isEmpty();
  }
}
