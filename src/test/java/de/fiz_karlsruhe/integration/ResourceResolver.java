package de.fiz_karlsruhe.integration;

import java.io.InputStream;
import java.util.Objects;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;

public class ResourceResolver implements LSResourceResolver {

  private String basePath;

  public ResourceResolver(String basePath) {
      this.basePath = basePath;
  }

  @Override
  public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
      // note: in this sample, the XSD's are expected to be in the root of the classpath
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(buildPath(systemId));
      Objects.requireNonNull(resourceAsStream, String.format("Could not find the specified xsd file: %s", systemId));
      return new DOMInputImpl(publicId, systemId, baseURI, resourceAsStream, "UTF-8");
  }

  private String buildPath(String systemId) {
      return basePath == null ? systemId : String.format("%s/%s", basePath, systemId);
  }
}