package de.fiz_karlsruhe;

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

  protected boolean validateAgainstOaiXsd(String xml) {
    
    try {
      ClassLoader classLoader = new ListMetadataFormatsIT().getClass().getClassLoader();

      StreamSource[] schemas = { 
        new StreamSource(classLoader.getResourceAsStream("xml.xsd")),
        new StreamSource(classLoader.getResourceAsStream("OAI-PMH.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai-identifier.xsd")),
        new StreamSource(classLoader.getResourceAsStream("simpledc20021212.xsd")),
        new StreamSource(classLoader.getResourceAsStream("oai_dc.xsd")),
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

}
