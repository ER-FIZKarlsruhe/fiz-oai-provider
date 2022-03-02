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

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  
  public String getBrandingLogo(ServletContext context) {
    String url = properties.getProperty("branding.logo");

    if (StringUtils.isEmpty(url)) {
      url = context.getRealPath("/banner02.gif");
    }

    return url;
  }
  
  public String getBrandingColor() {
    String color = properties.getProperty("branding.header.color");

    if (StringUtils.isEmpty(color)) {
      color = "#659932";
    }

    return color;
  }
  
  public String getBrandingWelcomeText() {
    String text = properties.getProperty("branding.welcome.text");

    if (StringUtils.isEmpty(text)) {
      text = "This is the OAI provider for FIZ Karlsruhe";
    }

    return text;
  }

  public String getBrandingImpressum() {
    String url = properties.getProperty("branding.imprint.url");

    if (StringUtils.isEmpty(url)) {
      url = "https://www.fiz-karlsruhe.de/de/ueber-uns/impressum-rechtliches";
    }

    return url;
  }
  
  public String getBrandingPrivacy() {
    String url = properties.getProperty("branding.privacy.url");

    if (StringUtils.isEmpty(url)) {
      url = "https://www.fiz-karlsruhe.de/de/ueber-uns/datenschutzerklaerung";
    }

    return url;
  }
  
  public String getBrandingFontFamily() {
    String family = properties.getProperty("branding.font.family");

    if (StringUtils.isEmpty(family)) {
      family = "Arial, Helvetica, Geneva, Verdana, sans-serif;";
    }

    return family;
  }

  public String getBrandingFontColor() {
    String color = properties.getProperty("branding.font.color");

    if (StringUtils.isEmpty(color)) {
      color = "#000;";
    }

    return color;
  }
  
  
}
