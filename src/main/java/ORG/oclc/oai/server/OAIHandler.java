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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ORG.oclc.oai.server.catalog.AbstractCatalog;
import ORG.oclc.oai.server.verb.OAIInternalServerError;
import ORG.oclc.oai.server.verb.ServerVerb;
import de.fiz_karlsruhe.FizRecordFactory;
import de.fiz_karlsruhe.service.ConfigurationService;

/**
 * OAIHandler is the primary Servlet for OAICat.
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 * @author Stefan Hofmann, FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
 */
public class OAIHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    final static Logger LOGGER = LogManager.getLogger(OAIHandler.class);
    
    public static final String PROPERTIES_SERVLET_CONTEXT_ATTRIBUTE = OAIHandler.class.getName() + ".properties";
    
    private static final String CONFIG_FILENAME = "oaicat.properties";
    
    private static final String VERSION = "1.5.62";
    private static boolean debug = false;

    private final Properties properties = new Properties();

    protected final HashMap attributesMap = new HashMap();

    
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
            HashMap attributes = null;
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
        HashMap globalAttributes = (HashMap)attributesMap.get("global");
        AbstractCatalog abstractCatalog = (AbstractCatalog)globalAttributes.get("OAIHandler.catalog");
        ((FizRecordFactory)abstractCatalog.getRecordFactory()).getRefreshFormatTimer().cancel();
        abstractCatalog.close();
    }

    private void loadConfiguration() {
      if (readConfigFromFile(getConfigFolder(), CONFIG_FILENAME)) {
          printConfiguration();
      }
  }
    
    protected String getConfigFolder() {
      String confFolderPath = null;
      
      //Is a dedicated oai-backend conf folder defined?
      String oaiBackendConfRoot = System.getProperty("oai.provider.conf.folder");

      //Catalina conf is fallback
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
      try {
          Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
          try {
              properties.load(reader);
              
              //Init ConfigurationService as singleton
              ConfigurationService configService = ConfigurationService.getInstance(properties);

              return true;
          } finally {
              reader.close();
          }
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
    
    public HashMap getAttributes(Properties properties)
    throws Throwable {
        HashMap attributes = new HashMap();
        Enumeration attrNames = getServletContext().getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = (String)attrNames.nextElement();
            attributes.put(attrName, getServletContext().getAttribute(attrName));
        }
        attributes.put("OAIHandler.properties", properties);
//        String temp = properties.getProperty("OAIHandler.debug");
//        if ("true".equals(temp)) debug = true;
        String missingVerbClassName = properties.getProperty("OAIHandler.missingVerbClassName", "ORG.oclc.oai.server.verb.BadVerb");
        Class missingVerbClass = Class.forName(missingVerbClassName);
        attributes.put("OAIHandler.missingVerbClass", missingVerbClass);
        if (!"true".equals(properties.getProperty("OAIHandler.serviceUnavailable"))) {
            attributes.put("OAIHandler.version", VERSION);
            AbstractCatalog abstractCatalog = AbstractCatalog.factory(properties, getServletContext());
            attributes.put("OAIHandler.catalog", abstractCatalog);
        }
        boolean forceRender = false;
        if ("true".equals(properties.getProperty("OAIHandler.forceRender"))) {
            forceRender = true;
        }
        String xsltName = properties.getProperty("OAIHandler.styleSheet");
        String appBase = properties.getProperty("OAIHandler.appBase");
        if (appBase == null) appBase = "webapps";
        if (xsltName != null
                && ("true".equalsIgnoreCase(properties.getProperty("OAIHandler.renderForOldBrowsers"))
                        || forceRender)) {
            InputStream is;
            try {
                is = new FileInputStream(appBase + "/" + xsltName);
            } catch (FileNotFoundException e) {
                // This is a silly way to skip the context name in the xsltName
                is = new FileInputStream(getServletContext().getRealPath(xsltName.substring(xsltName.indexOf("/", 1)+1)));
            }
            StreamSource xslSource = new StreamSource(is);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = tFactory.newTransformer(xslSource);
            attributes.put("OAIHandler.transformer", transformer);
        }
        return attributes;
    }
    
    public HashMap getAttributes(String pathInfo) {
        HashMap attributes = null;
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
                    InputStream in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(fileName);
                    if (in != null) {
                        LOGGER.debug("file found");
                        Properties fileProperties = new Properties();
                        fileProperties.load(in);
                        attributes = getAttributes(fileProperties);
                    } else {
                        LOGGER.debug("file not found");
                    }
                    attributesMap.put(pathInfo, attributes);
                } catch (Throwable e) {
                    LOGGER.debug("Couldn't load file", e);
                    // do nothing
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
     * The verb widget is taken from the request and used to invoke an
     * OAIVerb object of the corresponding kind to do the actual work of the verb.
     *
     * @param request the servlet's request information
     * @param response the servlet's response information
     * @exception IOException an I/O error occurred
     */
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
    throws IOException {
        HashMap attributes = getAttributes(request.getPathInfo());
        if (!filterRequest(request, response)) {
            return;
        }
        LOGGER.debug("attributes=" + attributes);
        Properties properties =
            (Properties) attributes.get("OAIHandler.properties");
        boolean monitor = false;
        if (properties.getProperty("OAIHandler.monitor") != null) {
            monitor = true;
        }
        boolean serviceUnavailable = isServiceUnavailable(properties);
        String extensionPath = properties.getProperty("OAIHandler.extensionPath", "/extension");
        
        HashMap serverVerbs = ServerVerb.getVerbs(properties);
        HashMap extensionVerbs = ServerVerb.getExtensionVerbs(properties);
        
        Transformer transformer =
            (Transformer) attributes.get("OAIHandler.transformer");
        
        boolean forceRender = false;
        if ("true".equals(properties.getProperty("OAIHandler.forceRender"))) {
            forceRender = true;
        }
        
        request.setCharacterEncoding("UTF-8");

        Date then = null;
        if (monitor) {
          then = new Date();
        }
        
        Enumeration headerNames = request.getHeaderNames();
        LOGGER.debug("OAIHandler.doGet: ");
        while (headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            LOGGER.debug(headerName);
            LOGGER.debug(": ");
            LOGGER.debug(request.getHeader(headerName));
        }
            
        if (serviceUnavailable) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
            "Sorry. This server is down for maintenance");
        } else {
            try {
                String userAgent = request.getHeader("User-Agent");
                if (userAgent == null) {
                    userAgent = "";
                } else {
                    userAgent = userAgent.toLowerCase();
                }
                Transformer serverTransformer = null;
                if (transformer != null) {
                    
                    // return HTML if the client is an old browser
                    if (forceRender
                            || userAgent.indexOf("opera") != -1
                            || (userAgent.startsWith("mozilla")
                                    && userAgent.indexOf("msie 6") == -1
                            /* && userAgent.indexOf("netscape/7") == -1 */)) {
                        serverTransformer = transformer;
                    }
                }
                String result = getResult(attributes, request, response, serverTransformer, serverVerbs, extensionVerbs, extensionPath);

                Writer out = getWriter(request, response);
                out.write(result);
                out.close();
            } catch (FileNotFoundException e) {
                LOGGER.warn("SC_NOT_FOUND: " + e.getMessage(),e);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } catch (TransformerException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (OAIInternalServerError e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (SocketException e) {
                LOGGER.error(e.getMessage(),e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (Throwable e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        if (monitor) {
            StringBuilder reqUri = new StringBuilder(request.getRequestURI().toString());
            String queryString = request.getQueryString();   // d=789
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

    public static String getResult(HashMap attributes,
            HttpServletRequest request,
            HttpServletResponse response,
            Transformer serverTransformer,
            HashMap serverVerbs,
            HashMap extensionVerbs,
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
            try {
                result = (String)construct.invoke(null,
                        new Object[] {attributes,
                        request,
                        response,
                        serverTransformer});
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }

            LOGGER.debug(result);

            return result;
        } catch (NoSuchMethodException e) {
            throw new OAIInternalServerError(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new OAIInternalServerError(e.getMessage());
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
