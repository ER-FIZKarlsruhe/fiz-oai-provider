/**
 * Copyright 2006 OCLC Online Computer Library Center Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ORG.oclc.oai.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Utility methods for OAICat and OAIHarvester
 */
public class OAIUtil {
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    static {
        dbFactory.setNamespaceAware(true);
    }
    
    /**
     * XML encode a string.
     * @param s any String
     * @return the String with &amp;, &lt;, and &gt; encoded for use in XML.
     */
    public static String xmlEncode(String s) {
      StringBuilder sb = new StringBuilder();

        for (int i=0; i<s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&apos;");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Convert a packed LCCN String to MARC display format.
     * @param packedLCCN an LCCN String in packed storage format (e.g. 'n&nbsp;&nbsp;2001050268').
     * @return  an LCCN String in MARC display format (e.g. 'n2001-50268').
     */
    public static String toLCCNDisplay(String packedLCCN) {
    StringBuilder sb = new StringBuilder();
	if (Character.isDigit(packedLCCN.charAt(2))) {
	    sb.append(packedLCCN.substring(0, 2).trim());
	    sb.append(packedLCCN.substring(2, 6));
	    sb.append("-");
	    int i = Integer.parseInt(packedLCCN.substring(6).trim());
	    sb.append(Integer.toString(i));
	} else {
	    sb.append(packedLCCN.substring(0, 3).trim());
	    sb.append(packedLCCN.substring(3, 5));
	    sb.append("-");
	    int i = Integer.parseInt(packedLCCN.substring(5).trim());
	    sb.append(Integer.toString(i));
	}
	return sb.toString();
    }

    /**
     * convert a packed LCCN to display format.
     * @param packedLCCN an LCCN String in packed storage format (e.g. 'n&nbsp;&nbsp;2001050268').
     * @return  an LCCN String in MARC display format (e.g. 'n2001-50268').
     * @deprecated use toLCCNDisplay() instead.
     */
    public static String getLCCN(String packedLCCN) {
	return toLCCNDisplay(packedLCCN);
    }

    public static Document parse(InputStream is)
    throws SAXException, IOException, ParserConfigurationException {
        return getThreadedDocumentBuilder().parse(is);
    }

    public static DocumentBuilder getThreadedDocumentBuilder()
    throws ParserConfigurationException {
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        return builder;
    }

}
