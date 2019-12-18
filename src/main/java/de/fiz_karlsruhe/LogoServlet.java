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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fiz_karlsruhe.service.ConfigurationService;

public class LogoServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("image/jpeg");
    
    ConfigurationService configurationService = ConfigurationService.getInstance();
    
    ServletOutputStream out;
    out = response.getOutputStream();
    FileInputStream fin = new FileInputStream(configurationService.getBrandingLogo());

    BufferedInputStream bin = new BufferedInputStream(fin);
    BufferedOutputStream bout = new BufferedOutputStream(out);
    int ch = 0;
    ;
    while ((ch = bin.read()) != -1) {
      bout.write(ch);
    }

    bin.close();
    fin.close();
    bout.close();
    out.close();
  }
}