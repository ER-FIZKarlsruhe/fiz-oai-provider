/*
 * Copyright 2019 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import de.fiz_karlsruhe.service.ConfigurationService;

public class LogoServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * The logo is a server-configured, rarely-changing branding asset, so browsers
   * can safely cache it for a day and only revalidate (via Last-Modified/ETag)
   * afterwards instead of re-downloading it on every page view.
   */
  private static final long MAX_AGE_SECONDS = 86400L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ConfigurationService configurationService = ConfigurationService.getInstance();
    File logoFile = new File(configurationService.getBrandingLogo(request.getServletContext()));

    if (!logoFile.isFile()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    long lastModified = logoFile.lastModified();
    String eTag = "\"" + logoFile.length() + "-" + lastModified + "\"";

    response.setHeader("Cache-Control", "public, max-age=" + MAX_AGE_SECONDS);
    response.setDateHeader("Last-Modified", lastModified);
    response.setHeader("ETag", eTag);

    if (isNotModified(request, lastModified, eTag)) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    response.setContentType("image/jpeg");
    response.setContentLengthLong(logoFile.length());
    Files.copy(logoFile.toPath(), response.getOutputStream());
  }

  private boolean isNotModified(HttpServletRequest request, long lastModified, String eTag) {
    if (eTag.equals(request.getHeader("If-None-Match"))) {
      return true;
    }

    long ifModifiedSince = request.getDateHeader("If-Modified-Since");
    // HTTP dates only carry second precision, so truncate before comparing.
    return ifModifiedSince >= 0 && ifModifiedSince >= (lastModified / 1000) * 1000;
  }
}
