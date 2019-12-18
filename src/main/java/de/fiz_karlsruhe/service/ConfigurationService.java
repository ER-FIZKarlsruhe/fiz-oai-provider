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

package de.fiz_karlsruhe.service;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ConfigurationService {

  private static Properties properties;

  private static ConfigurationService INSTANCE;

  final static Logger logger = LogManager.getLogger(ConfigurationService.class);

  private ConfigurationService(Properties properties) {
    ConfigurationService.properties = properties;
  }

  public static ConfigurationService getInstance() {
    if (INSTANCE == null) {
      throw new RuntimeException("Service must be initialized with backendUrl first!");
    }

    return INSTANCE;
  }

  public static ConfigurationService getInstance(Properties properties) {
    if (INSTANCE == null) {
      INSTANCE = new ConfigurationService(properties);
    }

    return INSTANCE;
  }

  public Properties getProperties() {
    return properties;
  }

  public String getBrandingServiceName() {
    String name = properties.getProperty("branding.service.name");

    if (StringUtils.isEmpty(name)) {
      name = "fiz-oai-provider";
    }

    return name;
  }
  
  public String getBrandingServiceUrl() {
    String url = properties.getProperty("branding.service.url");

    if (StringUtils.isEmpty(url)) {
      url = "https://www.fiz-karlsruhe.de";
    }

    return url;
  }
  
  public String getBrandingLogo() {
    String url = properties.getProperty("branding.logo");

    if (StringUtils.isEmpty(url)) {
      url = "banner02.gif";
    }

    return url;
  }
  
  public String getBrandingColor() {
    String color = properties.getProperty("branding.color");

    if (StringUtils.isEmpty(color)) {
      color = "#876234";
    }

    return color;
  }

}
