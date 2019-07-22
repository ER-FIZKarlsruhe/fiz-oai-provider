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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BaseIT {

  final static Logger logger = LogManager.getLogger(ListMetadataFormatsIT.class);

  public static String TEST_OAI_URL = "http://localhost:8999/fiz-oai-provider/OAIHandler";

  protected boolean validateAgainstOaiDcXsd(String xml) {
    
    try {
      ClassLoader classLoader = new ListMetadataFormatsIT().getClass().getClassLoader();

      StreamSource[] schemas = { 
        new StreamSource(classLoader.getResourceAsStream("xml.xsd")),
        new StreamSource(classLoader.getResourceAsStream("OAI-PMH.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai-identifier.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai_dc/simpledc20021212.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai_dc/oai_dc.xsd")),
      };
      
      InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());

      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = factory.newSchema(schemas);
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xmlStream));
      return true;
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
      return false;
    }
  }
  
  protected boolean validateAgainstDataciteXsd(String xml) {
    
    try {
      ClassLoader classLoader = new ListMetadataFormatsIT().getClass().getClassLoader();

      StreamSource[] schemas = { 
        new StreamSource(classLoader.getResourceAsStream("xml.xsd")),
        new StreamSource(classLoader.getResourceAsStream("OAI-PMH.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai-identifier.xsd")),
        new StreamSource(classLoader.getResourceAsStream("datacite/datacite.xsd")),         
      };
      
      InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());

      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      factory.setResourceResolver(new ResourceResolver("datacite"));
      Schema schema = factory.newSchema(schemas);
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xmlStream));
      return true;
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
      return false;
    }
  }
  
  protected boolean validateAgainstRadarXsd(String xml) {
    
    try {
      ClassLoader classLoader = new ListMetadataFormatsIT().getClass().getClassLoader();

      StreamSource[] schemas = { 
        new StreamSource(classLoader.getResourceAsStream("xml.xsd")),
        new StreamSource(classLoader.getResourceAsStream("OAI-PMH.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai-identifier.xsd")),
        new StreamSource(classLoader.getResourceAsStream("radar/RadarTypes.xsd")),
        new StreamSource(classLoader.getResourceAsStream("radar/RadarElements.xsd")),
        new StreamSource(classLoader.getResourceAsStream("radar/RadarDataset.xsd")),
//        new StreamSource(classLoader.getResourceAsStream("radar/dcterms.xsd")),

//        new StreamSource(classLoader.getResourceAsStream("datacite/datacite.xsd"))
     
      };
      
      InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());

      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      //factory.setResourceResolver(new ResourceResolver("datacite"));
      Schema schema = factory.newSchema(schemas);
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xmlStream));
      return true;
    } catch (Exception ex) {
      System.err.println(xml);
      System.err.println(ex.getMessage());
      ex.printStackTrace();
      return false;
    }
  }

}
