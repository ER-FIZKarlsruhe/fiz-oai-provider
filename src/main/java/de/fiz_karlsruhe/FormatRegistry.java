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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Transformation;

/**
 * FormatRegistry manages all the formats and transformations that are suppported by the provider.
 *
 * @author Jeffrey A. Young
 * @author Stefan Hofmann
 */
public class FormatRegistry {

  private List<Transformation> transformations;

  private List<Format> formats;
  
  public FormatRegistry(List<Format> formats, List<Transformation> transformations) {
    this.formats = formats;
    this.transformations = transformations;
   
    if (formats.size() == 0) {
      System.err.println("No formats have been initialized!");
    }
    
    if (transformations.size() == 0) {
      System.err.println("No transformations have been initialized!");
    }
  }


  public List<Transformation> getTransformations() {
    return transformations;
  }


  public void setTransformations(List<Transformation> transformations) {
    this.transformations = transformations;
  }


  public List<Format> getFormats() {
    return formats;
  }


  public void setFormats(List<Format> formats) {
    this.formats = formats;
  }


  /**
   * Get the metadataPrefix associated with the specified namespace/schema
   *
   * @param namespaceURI the namespaceURI portion of the format specifier
   * @param schemaURL    the schemaURL portion of the format specifier
   * @return a String containing the metadataPrefix value associated with this
   *         pair
   */
  public String getMetadataPrefix(String namespaceURI, String schemaURL) {
    StringBuffer sb = new StringBuffer();
    if (namespaceURI != null) {
      sb.append(namespaceURI);
    }
    sb.append(" ").append(schemaURL);
    
    String location = null;

    Optional<Format> format = this.formats.stream().filter(f -> f.getSchemaLocation().trim().equals(sb.toString().trim())).findFirst();
    if (format.isPresent()) {
      location = format.get().getMetadataPrefix(); 
    }

    return location;
  }

  /**
   * Get the schemaURL associated with the specified metadataPrefix
   *
   * @param metadataPrefix the prefix desired
   * @return a String containing the schemaURL associated with the metadataPrefix
   */
  public String getSchemaURL(String metadataPrefix) {
    String schemaLocation = getSchemaLocation(metadataPrefix);

    if (schemaLocation == null) {
      return null;
    }
    
    StringTokenizer tokenizer = new StringTokenizer(schemaLocation);
    String temp = tokenizer.nextToken();
    try {
      return tokenizer.nextToken();
    } catch (NoSuchElementException e) {
      return temp;
    }
  }

  /**
   * Get the namespaceURI associated with the specified metadataPrefix
   *
   * @param metadataPrefix the prefix desired
   * @return a String containing the namespaceURI associated with the
   *         metadataPrefix
   */
  public String getNamespaceURI(String metadataPrefix) {
    String schemaLocation = getSchemaLocation(metadataPrefix);
    StringTokenizer tokenizer = new StringTokenizer(schemaLocation);
    return tokenizer.nextToken();
  }

  /**
   * Get the namespaceURI/schemaURL associated with the specified metadataPrefix
   * E.g:
   * xsi:schemaLocation="http://www.cafeconleche.org/namespaces/person http://www.elharo.com/person.xsd"
   *
   * @param metadataPrefix the prefix desired
   * @return a String containing the namespaceURI/schemaURL associated with the
   *         metadataPrefix
   */
  public String getSchemaLocation(String metadataPrefix) {
    String location = null;
    Optional<Format> format = this.formats.stream().filter(f -> f.getMetadataPrefix().equals(metadataPrefix)).findFirst();
    if (format.isPresent()) {
      location = format.get().getSchemaLocation(); 
    }
    
    return location;
  }

  /**
   * Does the specified metadataPrefix appears in the list of supportedFormats?
   *
   * @param metadataPrefix the prefix desired
   * @return true if prefix is supported, false otherwise.
   */
  public boolean containsValue(String metadataPrefix) {
    //return (crosswalkMap.get(metadataPrefix) != null);
    //TODO
    return true;
  }

}
