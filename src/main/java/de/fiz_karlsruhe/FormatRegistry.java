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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.fiz_karlsruhe.model.Format;
import de.fiz_karlsruhe.model.Transformation;

/**
 * FormatRegistry manages all the formats and transformations that are
 * suppported by the provider.
 *
 * @author Jeffrey A. Young
 * @author Stefan Hofmann
 */
public class FormatRegistry {

  final static Logger LOGGER = LogManager.getLogger(FormatRegistry.class);
  
  //Use a synchronized list that can be filled from another thread (RefreshFormatRegistry)
  private List<Transformation> transformations = Collections.synchronizedList(new ArrayList<Transformation>());

  //Use a synchronized list that can be filled from another thread (RefreshFormatRegistry)
  private List<Format> formats = Collections.synchronizedList(new ArrayList<Format>());

  public FormatRegistry(List<Format> formats, List<Transformation> transformations) {
    this.formats = formats;
    this.transformations = transformations;

    if (formats.size() == 0) {
      LOGGER.warn("No formats have been initialized!");
    }

    if (transformations.size() == 0) {
      LOGGER.warn("No transformations have been initialized!");
    }
  }

  public List<Transformation> getTransformations() {
    return transformations;
  }

  public void setTransformations(List<Transformation> transformationsList) {
	if (transformationsList == null || transformationsList.isEmpty()) {
		LOGGER.warn("No transformations set. Do nothing!");
	}
	  
    if (!CollectionUtils.isEqualCollection(this.transformations, transformations)) {
      LOGGER.warn("Transformations differs in backend and registy! Set new value in transformation list!");
	  this.transformations.clear();
	  this.transformations.addAll(transformationsList);
    }
  }

  public List<Format> getFormats() {
    return formats;
  }

  public void setFormats(List<Format> formatList) {
	if (formatList == null || formatList.isEmpty()) {
		LOGGER.warn("No formats set. Do nothing!");
	}
	  
	if (!CollectionUtils.isEqualCollection(this.formats, formatList)) {
		  LOGGER.warn("Formats differs in backend and registy! Set new value in format list!");
  	  this.formats.clear();
  	  this.formats.addAll(formatList);
    }
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
    StringBuilder sb = new StringBuilder();
    if (namespaceURI != null) {
      sb.append(namespaceURI);
    }
    sb.append(" ").append(schemaURL);

    String location = null;

    Optional<Format> format = this.formats.stream()
        .filter(f -> f.getSchemaLocation().trim().equals(sb.toString().trim())).findFirst();
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
   * E.g: xsi:schemaLocation="http://www.cafeconleche.org/namespaces/person
   * http://www.elharo.com/person.xsd"
   *
   * @param metadataPrefix the prefix desired
   * @return a String containing the namespaceURI/schemaURL associated with the
   *         metadataPrefix
   */
  public String getSchemaLocation(String metadataPrefix) {
    String location = null;
    Optional<Format> format = this.formats.stream().filter(f -> f.getMetadataPrefix().equals(metadataPrefix))
        .findFirst();
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
    boolean match = formats.stream().anyMatch(n -> n.getMetadataPrefix().equals(metadataPrefix));

    return match;
  }

}
