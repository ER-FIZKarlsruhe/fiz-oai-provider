package de.fiz_karlsruhe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import ORG.oclc.oai.server.verb.OAIInternalServerError;
import ORG.oclc.oai.util.OAIUtil;

public class FizOaiBackendCrosswalk extends Crosswalk {
  private boolean debug = true;
  protected Transformer transformer = null;

  public FizOaiBackendCrosswalk(String schemaLocation, String xsltUrl) throws OAIInternalServerError {
    this(schemaLocation, (String) null, xsltUrl);
  }

  public FizOaiBackendCrosswalk(String schemaLocation, String contentType, String xsltUrl)
      throws OAIInternalServerError {
    this(schemaLocation, contentType, (String) null, xsltUrl);
  }

  public FizOaiBackendCrosswalk(String schemaLocation, String contentType, String docType, String xsltUrl)
      throws OAIInternalServerError {
    this(schemaLocation, contentType, docType, (String) null, xsltUrl);
  }

  /**
   * The constructor assigns the schemaLocation associated with this crosswalk.
   * Since the crosswalk is trivial in this case, no properties are utilized.
   *
   * @param properties properties that are needed to configure the crosswalk.
   */
  public FizOaiBackendCrosswalk(String schemaLocation, String contentType, String docType, String encoding,
      String xsltUrl) throws OAIInternalServerError {
    // super("http://www.openarchives.org/OAI/2.0/oai_dc/
    // http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    super(schemaLocation, contentType, docType, encoding);

    if (xsltUrl != null) {

      try {
        URL url = new URL(xsltUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StreamSource xslSource = new StreamSource(in);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        this.transformer = tFactory.newTransformer(xslSource);
        this.transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        this.transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        this.transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      } catch (Exception e) {
        e.printStackTrace();
        throw new OAIInternalServerError(e.getMessage());
      }
    }
  }

  /**
   * Can this nativeItem be represented in other formats?
   * 
   * @param nativeItem a record in native format
   * @return true if other formats are possible, false otherwise.
   */
  public boolean isAvailableFor(Object nativeItem) {
    return true;
  }

  /**
   * Perform the actual crosswalk.
   *
   * @param nativeItem the native "item". In this case, it is already formatted as
   *                   an OAI <record> element, with the possible exception that
   *                   multiple metadataFormats are present in the <metadata>
   *                   element.
   * @return a String containing the FileMap to be stored within the <metadata>
   *         element.
   * @exception CannotDisseminateFormatException nativeItem doesn't support this
   *                                             format.
   */
  public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
    try {
      String xmlRec = null;
      if (nativeItem instanceof HashMap) {
        HashMap recordMap = (HashMap) nativeItem;
        xmlRec = (String) recordMap.get("recordString");
        if (xmlRec == null) {
          xmlRec = new String((byte[]) recordMap.get("recordBytes"), "UTF-8");
        }
        xmlRec = xmlRec.trim();
      } else if (nativeItem instanceof String) {
        xmlRec = (String) nativeItem;
      } else if (nativeItem instanceof Document) {
        xmlRec = OAIUtil.toString((Document) nativeItem);
      } else {
        throw new Exception("Unrecognized nativeItem");
      }

      if (debug) {
        System.out.println("XSLTCrosswalk.createMetadata: xmlRec=" + xmlRec);
      }
      if (xmlRec.startsWith("<?")) {
        int offset = xmlRec.indexOf("?>");
        xmlRec = xmlRec.substring(offset + 2);
      }
      if (debug) {
        System.out.println("XSLTCrosswalk.createMetadata: transformer=" + transformer);
      }
      if (transformer != null) {
        StringReader stringReader = new StringReader(xmlRec);
        StreamSource streamSource = new StreamSource(stringReader);
        StringWriter stringWriter = new StringWriter();
        synchronized (this) {
          transformer.transform(streamSource, new StreamResult(stringWriter));
        }
        if (debug) {
          System.out.println("XSLTCrosswalk.createMetadata: return=" + stringWriter.toString());
        }
        return stringWriter.toString();
      } else {
        return xmlRec;
      }
    } catch (Exception e) {
      if (debug) {
        e.printStackTrace();
      }
      throw new CannotDisseminateFormatException(e.getMessage());
    }
  }
}