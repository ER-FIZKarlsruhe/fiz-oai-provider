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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Collections;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Test;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import ORG.oclc.oai.server.catalog.RecordFactory;
import de.fiz_karlsruhe.model.Format;

public class HealthServletTest {

  @Test
  public void livenessAlwaysRespondsOkRegardlessOfCatalogState() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletContext servletContext = mock(ServletContext.class);
    PrintWriter writer = mock(PrintWriter.class);
    when(request.getServletPath()).thenReturn("/health/live");
    when(request.getServletContext()).thenReturn(servletContext);
    when(response.getWriter()).thenReturn(writer);

    new HealthServlet().doGet(request, response);

    verify(response, never()).sendError(anyInt(), anyString());
    verify(writer).write("OK");
  }

  @Test
  public void readinessFailsWhenCatalogWasNeverInitialized() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletContext servletContext = mock(ServletContext.class);
    when(request.getServletPath()).thenReturn("/health/ready");
    when(request.getServletContext()).thenReturn(servletContext);
    when(servletContext.getAttribute("OAIHandler.catalog")).thenReturn(null);

    new HealthServlet().doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Catalog not yet initialized");
  }

  @Test
  public void readinessFailsWhenFormatRegistryHasNoFormatsYet() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletContext servletContext = mock(ServletContext.class);
    when(request.getServletPath()).thenReturn("/health/ready");
    when(request.getServletContext()).thenReturn(servletContext);

    AbstractCatalog catalog = mock(AbstractCatalog.class);
    RecordFactory recordFactory = mock(RecordFactory.class);
    FormatRegistry formatRegistry = new FormatRegistry(Collections.emptyList(), Collections.emptyList());
    when(catalog.getRecordFactory()).thenReturn(recordFactory);
    when(recordFactory.getFormatRegistry()).thenReturn(formatRegistry);
    when(servletContext.getAttribute("OAIHandler.catalog")).thenReturn(catalog);

    new HealthServlet().doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Catalog not yet initialized");
  }

  @Test
  public void readinessSucceedsOnceTheCatalogHoldsAtLeastOneFormat() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletContext servletContext = mock(ServletContext.class);
    PrintWriter writer = mock(PrintWriter.class);
    when(request.getServletPath()).thenReturn("/health/ready");
    when(request.getServletContext()).thenReturn(servletContext);
    when(response.getWriter()).thenReturn(writer);

    AbstractCatalog catalog = mock(AbstractCatalog.class);
    RecordFactory recordFactory = mock(RecordFactory.class);
    Format oaiDc = new Format();
    oaiDc.setMetadataPrefix("oai_dc");
    FormatRegistry formatRegistry = new FormatRegistry(Collections.singletonList(oaiDc), Collections.emptyList());
    when(catalog.getRecordFactory()).thenReturn(recordFactory);
    when(recordFactory.getFormatRegistry()).thenReturn(formatRegistry);
    when(servletContext.getAttribute("OAIHandler.catalog")).thenReturn(catalog);

    new HealthServlet().doGet(request, response);

    verify(response, never()).sendError(anyInt(), anyString());
    verify(writer).write("OK");
  }
}
