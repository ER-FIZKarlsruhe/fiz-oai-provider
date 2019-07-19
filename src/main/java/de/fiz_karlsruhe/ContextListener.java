package de.fiz_karlsruhe;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;

@WebListener("fiz-oai-provider context listener")
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

    System.out.println("ContextListener confFolderPath: " + confFolderPath);
    File file = new File(confFolderPath, "fiz-oai-provider-log4j.properties");
    
    try(FileInputStream fis = new FileInputStream(file)) {
      PropertyConfigurator.configure(fis);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    // do nothing
  }
}
