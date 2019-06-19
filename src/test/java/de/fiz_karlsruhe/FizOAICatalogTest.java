package de.fiz_karlsruhe;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ORG.oclc.oai.server.verb.IdDoesNotExistException;
import de.fiz_karlsruhe.service.BackendService;
import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class FizOAICatalogTest {

  private Properties prop;
  
  
  @Mock
  BackendService backendService;
  
  @Before
  public void init() {
    
    try {
      prop = new Properties();
      File resourcesDirectory = new File("src/test/resources").getAbsoluteFile();
      File propertiesFile = new File(resourcesDirectory, "oaicat.properties");
      InputStream resourcesFile = new FileInputStream(propertiesFile);
      prop.load(resourcesFile);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Test
  public void initCatalogWithoutProperties() throws Exception {
    try {
      FizOAICatalog catalog = new FizOAICatalog(null);
    } catch(Exception e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void initCatalogWithProperties() throws Exception {
      FizOAICatalog catalog = new FizOAICatalog(prop);
      assertTrue(true);
  }
  
  @Test
  public void testGetRecordIdDoesNotExist() throws Exception {
    FizOAICatalog catalog;
    try {
      catalog = (FizOAICatalog) FizOAICatalog.factory(prop, null);      
      catalog.setBackendService(backendService);
      catalog.getRecord("abc", "ii");
      Assert.fail("IdDoesNotExistException expected");
    } catch (IdDoesNotExistException e) {
      //do nothing
    } catch(Throwable t) {
      Assert.fail("Unexpected exception");
    }
     
  }
  
}
