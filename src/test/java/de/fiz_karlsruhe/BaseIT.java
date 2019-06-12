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

  
  protected boolean validateAgainstOaiXsd(String xml) {
    try {
      ClassLoader classLoader = new ListMetadataFormatsIT().getClass().getClassLoader();

      InputStream oaiPmhXsd = classLoader.getResourceAsStream("OAI-PMH.xsd");
      InputStream oaiIdentifierXsd = classLoader.getResourceAsStream("oai-identifier.xsd");
      InputStream oaiDcXsd = classLoader.getResourceAsStream("oai_dc.xsd");
      InputStream simpleDcXsd = classLoader.getResourceAsStream("simpledc20021212.xsd");
      
      InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
      
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      
      StreamSource[] schemas = {new StreamSource(oaiPmhXsd), new StreamSource(oaiIdentifierXsd), new StreamSource(oaiDcXsd), new StreamSource(simpleDcXsd)};
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
