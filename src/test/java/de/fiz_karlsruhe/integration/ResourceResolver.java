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

package de.fiz_karlsruhe.integration;

import java.io.InputStream;
import java.util.Objects;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import org.apache.xerces.dom.DOMInputImpl;

public class ResourceResolver implements LSResourceResolver {

  private String basePath;

  public ResourceResolver(String basePath) {
      this.basePath = basePath;
  }

  @Override
  public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
      // systemId may be a bare filename or an absolute URL (e.g. schemaLocation="https://dublincore.org/.../dc.xsd");
      // in both cases resolve the file name against local classpath resources instead of hitting the network.
      String fileName = extractFileName(systemId);
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(buildPath(fileName));
      if (resourceAsStream == null) {
          resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
      }
      Objects.requireNonNull(resourceAsStream, String.format("Could not find the specified xsd file: %s", systemId));
      return new DOMInputImpl(publicId, systemId, baseURI, resourceAsStream, "UTF-8");
  }

  private String buildPath(String fileName) {
      return basePath == null ? fileName : String.format("%s/%s", basePath, fileName);
  }

  private String extractFileName(String systemId) {
      int lastSlash = Math.max(systemId.lastIndexOf('/'), systemId.lastIndexOf('\\'));
      return lastSlash >= 0 ? systemId.substring(lastSlash + 1) : systemId;
  }
}