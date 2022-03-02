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
    } catch (Exception e) {
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
      // do nothing
    } catch (Throwable t) {
      t.printStackTrace();
      Assert.fail("Unexpected exception");
    }

  }

}
