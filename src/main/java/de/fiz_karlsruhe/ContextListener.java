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
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationFactory;

@WebListener("oai context listener")
public class ContextListener implements ServletContextListener {

  /**
   * Initialize log4j when the application is being started
   */
  @Override
  public void contextInitialized(ServletContextEvent event) {
    String confFolderPath = null;

    // Is a dedicated oai-backend conf folder defined?
    String oaiBackendConfRoot = System.getProperty("oai.provider.conf.folder");

    // Catalina conf is fallback
    String tomcatRoot = System.getProperty("catalina.base");

    if (oaiBackendConfRoot != null && !oaiBackendConfRoot.isEmpty()) {
      confFolderPath = new File(oaiBackendConfRoot).getAbsolutePath();
    } else if (tomcatRoot != null && !tomcatRoot.isEmpty()) {
      confFolderPath = new File(tomcatRoot, "conf").getAbsolutePath();
    }

//    File file = new File(confFolderPath, "fiz-oai-provider-log4j.properties");
//    System.out.println("ContextListener confFolderPath: " + confFolderPath);
//    System.out.println("log4j2 configuration: " + file.getAbsolutePath());
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    // do nothing
  }
}
