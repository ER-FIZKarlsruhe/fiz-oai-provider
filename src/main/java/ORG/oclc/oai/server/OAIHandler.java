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
package ORG.oclc.oai.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import ORG.oclc.oai.server.verb.OAIInternalServerError;
import ORG.oclc.oai.server.verb.ServerVerb;
import de.fiz_karlsruhe.FizRecordFactory;
import de.fiz_karlsruhe.OaiRuntimeException;
import de.fiz_karlsruhe.service.ConfigurationService;

/**
 * OAIHandler is the primary Servlet for OAICat.
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 * @author Stefan Hofmann, FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
 */
public class OAIHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LogManager.getLogger(OAIHandler.class);

    public static final String PROPERTIES_SERVLET_CONTEXT_ATTRIBUTE = OAIHandler.class.getName() + ".properties";

    private static final String CONFIG_FILENAME = "oaicat.properties";

    private static final String VERSION = "1.5.62";

    private final Properties properties = new Properties();

    /**
     * pathInfo is taken directly from the request URL and used as a cache key here
     * (see getAttributes(String)), so this map must stay bounded and thread-safe:
     * an attacker can otherwise grow it without limit by requesting distinct,
     * non-existent path segments. Access-ordered so "global" and other
     * frequently-used entries stay in the cache while the LRU tail is evicted.
     */
    private static final int MAX_ATTRIBUTES_CACHE_SIZE = 1000;

    protected final Map attributesMap = Collections.synchronizedMap(new LinkedHashMap(16, 0.75f, true) {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ATTRIBUTES_CACHE_SIZE;
        }
    });


    /**
     * Get the VERSION number
     */
    public static String getVERSION() { return VERSION; }

    /**
     * init is called one time when the Servlet is loaded. This is the
     * place where one-time initialization is done. Specifically, we
     * load the properties file for this application, and create the
     * AbstractCatalog object for subsequent use.
     *
     * @param config servlet configuration information
     * @exception ServletException there was a problem with initialization
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            Map attributes = null;
            loadConfiguration();
            attributes = getAttributes(properties);

            attributesMap.put("global", attributes);
        } catch (FileNotFoundException e) {
            LOGGER.error(e);
            throw new ServletException(e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
            throw new ServletException(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error(e);
            throw new ServletException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e);
            throw new ServletException(e.getMessage());
        } catch (Throwable e) {
            LOGGER.error(e);
            throw new ServletException(e.getMessage());
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("destroy called");
        // Flip readiness to unavailable before tearing down the catalog so a rolling
        // deployment stops routing new traffic here first.
        getServletContext().removeAttribute("OAIHandler.catalog");

        HashMap globalAttributes = (HashMap)attributesMap.get("global");
        AbstractCatalog abstractCatalog = (AbstractCatalog)globalAttributes.get("OAIHandler.catalog");

        FizRecordFactory fizRecordFactory = ((FizRecordFactory)abstractCatalog.getRecordFactory());
        if (fizRecordFactory.getRefreshFormatTimer() != null) {
            fizRecordFactory.getRefreshFormatTimer().cancel();
        }

        abstractCatalog.close();
    }

    private void loadConfiguration() {
        if (readConfigFromFile(getConfigFolder(), CONFIG_FILENAME)) {
            printConfiguration();
        }
    }

    protected String getConfigFolder() {
        String confFolderPath = null;

        String oaiBackendConfRoot = System.getProperty("oai.provider.conf.folder");
        String tomcatRoot = System.getProperty("catalina.base");

        if (oaiBackendConfRoot != null && !oaiBackendConfRoot.isEmpty()) {
            confFolderPath = new File(oaiBackendConfRoot).getAbsolutePath();
        } else if (tomcatRoot != null && !tomcatRoot.isEmpty()) {
            confFolderPath = new File(tomcatRoot, "conf").getAbsolutePath();
        }

        LOGGER.info("Use confFolderPath: {}", confFolderPath);

        return confFolderPath;
    }

    protected boolean readConfigFromFile(String folder, String filename) {

        File file = new File(folder, filename);
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            properties.load(reader);
            ConfigurationService.getInstance(properties);
            return true;
        } catch (Exception e) {
            LOGGER.error("Unable to read property file: " + file.getAbsolutePath(),e);
            return false;
        }
    }

    public void printConfiguration() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (entry.getKey().toString().toLowerCase().contains("password")) {
                builder.append(entry.getKey() + " : ***********\n");
            }
            else {
                builder.append(entry.getKey() + " : " + entry.getValue() + "\n");
            }
        }
        LOGGER.info("Using the following configuration: \n" + builder.toString());
    }

    public Map getAttributes(Properties properties) throws Throwable {
        HashMap<String, Object> attributes = new HashMap<>();

        // Copy servlet context attributes
        Enumeration<String> attrNames = getServletContext().getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = attrNames.nextElement();
            attributes.put(attrName, getServletContext().getAttribute(attrName));
        }

        attributes.put("OAIHandler.properties", properties);

        // Load missing verb class
        String missingVerbClassName = properties.getProperty(
                "OAIHandler.missingVerbClassName", "ORG.oclc.oai.server.verb.BadVerb");
        Class<?> missingVerbClass = Class.forName(missingVerbClassName);
        attributes.put("OAIHandler.missingVerbClass", missingVerbClass);

        // Only add version and catalog if service is available
        if (!"true".equals(properties.getProperty("OAIHandler.serviceUnavailable"))) {
            attributes.put("OAIHandler.version", VERSION);
            AbstractCatalog abstractCatalog = AbstractCatalog.factory(properties, getServletContext());
            attributes.put("OAIHandler.catalog", abstractCatalog);
            // Exposed so HealthServlet can report readiness without depending on this servlet instance.
            getServletContext().setAttribute("OAIHandler.catalog", abstractCatalog);
        }

        return attributes;
    }


    /**
     * Templates (the compiled stylesheet) is thread-safe and reusable, so it is
     * cached across requests. Transformer is not thread-safe, so a fresh,
     * cheap-to-create instance is handed to each request instead of sharing one
     * across concurrently running requests.
     */
    public Transformer getTransformer(Properties properties, Map attributes) throws IOException {
        Templates templates = (Templates) attributes.get("OAIHandler.templates");
        if (templates != null) {
            return newTransformer(templates);
        }

        String xsltName = properties.getProperty("OAIHandler.styleSheet");
        if (xsltName == null) {
            return null;
        }

        InputStream is = null;
        try {
            if (xsltName.startsWith("http://") || xsltName.startsWith("https://")) {
                is = new URL(xsltName).openStream();
            } else {
                // 1. Try ServletContext first (standard web location)
                URL xsltUrl = getServletContext().getResource(xsltName);
                is = xsltUrl.openStream();

                if (is == null) {
                    throw new FileNotFoundException("Stylesheet not found in Context or Classpath: " + xsltName);
                }
            }

            StreamSource xslSource = new StreamSource(is);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            try {
                tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
                templates = tFactory.newTemplates(xslSource);
            } catch (Exception e) {
                throw new OaiRuntimeException(e);
            }
            attributes.put("OAIHandler.templates", templates);
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return newTransformer(templates);
    }

    private Transformer newTransformer(Templates templates) {
        try {
            return templates.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new OaiRuntimeException(e);
        }
    }

    public Map getAttributes(String pathInfo) {
        Map attributes = null;
        LOGGER.debug("pathInfo=" + pathInfo);
        if (pathInfo != null && pathInfo.length() > 0) {
            if (attributesMap.containsKey(pathInfo)) {
                LOGGER.debug("attributesMap containsKey");
                attributes = (HashMap) attributesMap.get(pathInfo);
            } else {
                LOGGER.debug("!attributesMap containsKey");
                try {
                    String fileName = pathInfo.substring(1) + ".properties";
                    LOGGER.debug("attempting load of " + fileName);
                    try (InputStream in = Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(fileName)) {
                        if (in != null) {
                            LOGGER.debug("file found");
                            Properties fileProperties = new Properties();
                            fileProperties.load(in);
                            attributes = getAttributes(fileProperties);
                        } else {
                            LOGGER.debug("file not found");
                        }
                    }
                    attributesMap.put(pathInfo, attributes);
                } catch (Throwable e) {
                    LOGGER.debug("Couldn't load file", e);
                }
            }
        }
        if (attributes == null) {
            LOGGER.debug("use global attributes");
            attributes = (HashMap) attributesMap.get("global");
        }
        return attributes;
    }

    /**
     * Peform the http GET action. Note that POST is shunted to here as well.
     */
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {
        Map attributes = getAttributes(request.getPathInfo());
        if (!filterRequest(request, response)) {
            return;
        }
        LOGGER.debug("attributes=" + attributes);
        Properties requestProperties =
                (Properties) attributes.get("OAIHandler.properties");
        boolean monitor = false;
        if (requestProperties.getProperty("OAIHandler.monitor") != null) {
            monitor = true;
        }
        boolean serviceUnavailable = isServiceUnavailable(requestProperties);
        String extensionPath = requestProperties.getProperty("OAIHandler.extensionPath", "/extension");

        Map serverVerbs = ServerVerb.getVerbs();
        Map extensionVerbs = ServerVerb.getExtensionVerbs(requestProperties);



        request.setCharacterEncoding("UTF-8");

        Date then = null;
        if (monitor) {
            then = new Date();
        }

        if (serviceUnavailable) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Sorry. This server is down for maintenance");
        } else {
            try {
                String result;

                boolean isUiRequest = "true".equalsIgnoreCase(request.getParameter("renderHtml"));

                if (isUiRequest) {
                    response.setContentType("text/html; charset=UTF-8");
                    Transformer transformer = getTransformer(requestProperties, attributes);
                    result = getResult(
                            attributes,
                            request,
                            response,
                            transformer,
                            serverVerbs,
                            extensionVerbs,
                            extensionPath
                    );
                } else {
                    response.setContentType("text/xml; charset=UTF-8");
                    result = getResult(
                            attributes,
                            request,
                            response,
                            null,
                            serverVerbs,
                            extensionVerbs,
                            extensionPath
                    );
                }

                Writer out = getWriter(request, response);
                out.write(result);
                out.close();
            } catch (FileNotFoundException e) {
                LOGGER.warn("SC_NOT_FOUND: " + e.getMessage(),e);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } catch (TransformerException e) {
                LOGGER.error(e.getMessage() ,e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (OAIInternalServerError e) {
                LOGGER.error(e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (SocketException e) {
                LOGGER.error(e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        if (monitor) {
            StringBuilder reqUri = new StringBuilder(request.getRequestURI().toString());
            String queryString = request.getQueryString();
            if (queryString != null) {
                reqUri.append("?").append(queryString);
            }
            Runtime rt = Runtime.getRuntime();
            LOGGER.info(rt.freeMemory() + "/" + rt.totalMemory() + " "
                    + ((new Date()).getTime()-then.getTime()) + "ms: "
                    + reqUri.toString());
        }
    }
    
    /**
     * Should the server report itself down for maintenance? Override this
     * method if you want to do this check another way.
     * @param properties
     * @return true=service is unavailable, false=service is available
     */
    protected boolean isServiceUnavailable(Properties properties) {
        if (properties.getProperty("OAIHandler.serviceUnavailable") != null) {
            return true;
        }
        return false;
    }

    /**
     * Override to do any prequalification; return false if
     * the response should be returned immediately, without
     * further action.
     * 
     * @param request
     * @param response
     * @return false=return immediately, true=continue
     */
    protected boolean filterRequest(HttpServletRequest request,
                                    HttpServletResponse response) {
        return true;
    }

    public static String getResult(Map attributes,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   Transformer serverTransformer,
                                   Map serverVerbs,
                                   Map extensionVerbs,
                                   String extensionPath)
            throws Throwable {
        try {
            boolean isExtensionVerb = extensionPath.equals(request.getPathInfo());
            String verb = request.getParameter("verb");
            LOGGER.debug("OAIHandler.g<etResult: verb=>" + verb + "<");

            String result;
            Class verbClass = null;
            if (isExtensionVerb) {
                verbClass = (Class)extensionVerbs.get(verb);
            } else {
                verbClass = (Class)serverVerbs.get(verb);
            }
            if (verbClass == null) {
                verbClass = (Class) attributes.get("OAIHandler.missingVerbClass");
            }
            Method construct = verbClass.getMethod("construct",
                    new Class[] {HashMap.class,
                            HttpServletRequest.class,
                            HttpServletResponse.class,
                            Transformer.class});
            result = invokeConstruct(construct, attributes, request, response, serverTransformer);

            LOGGER.debug(result);

            return result;
        } catch (NoSuchMethodException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OAIInternalServerError(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OAIInternalServerError(e.getMessage());
        }
    }

    private static String invokeConstruct(Method construct, Map attributes, HttpServletRequest request,
            HttpServletResponse response, Transformer serverTransformer) throws Throwable {
        try {
            return (String) construct.invoke(null,
                    new Object[] {attributes,
                            request,
                            response,
                            serverTransformer});
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
            throw e.getTargetException();
        }
    }
    
    /**
     * Get a response Writer depending on acceptable encodings
     * @param request the servlet's request information
     * @param response the servlet's response information
     * @exception IOException an I/O error occurred
     */
    public static Writer getWriter(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Writer out;
        String encodings = request.getHeader("Accept-Encoding");
        LOGGER.debug("encodings={}", encodings);

        if (encodings != null && encodings.indexOf("gzip") != -1) {
            response.setHeader("Content-Encoding", "gzip");
            out = new OutputStreamWriter(new GZIPOutputStream(response.getOutputStream()), "UTF-8");
        } else if (encodings != null && encodings.indexOf("deflate") != -1) {
            response.setHeader("Content-Encoding", "deflate");
            out = new OutputStreamWriter(new DeflaterOutputStream(response.getOutputStream()), "UTF-8");
        } else {
            out = response.getWriter();
        }
        return out;
    }
    
    /**
     * Peform a POST action. Actually this gets shunted to GET
     *
     * @param request the servlet's request information
     * @param response the servlet's response information
     * @exception IOException an I/O error occurred
     */
    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {
        doGet(request, response);
    }
}
