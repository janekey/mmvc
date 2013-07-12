package com.janekey.mmvc.servlet;

import com.janekey.mmvc.mapping.Mapping;
import com.janekey.mmvc.mapping.MappingHandler;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: qi.zheng
 * Date: 13-7-12
 * Time: 下午12:09
 */
public class ConfigHandler {

    private final Logger logger = Logger.getLogger(ConfigHandler.class);
    // All controller map.
    private Map<String, Mapping> controllerMap = new HashMap<String, Mapping>();

    public ConfigHandler(ServletConfig servletConfig) {
        File configFile = null;
        try {
            String webInf = servletConfig.getServletContext().getRealPath("WEB-INF");
            File wiFile = new File(webInf);
            // filter file in WEB-INF directory which end with '-mmvc.xml'
            FilenameFilter configFileFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("-mmvc.xml");
                }
            };
            configFile = wiFile.listFiles(configFileFilter)[0];
        } catch (Exception e) {
            logger.error("could not find config file *-mmvc.xml", e);
            return;
        }
        if (configFile == null || !configFile.exists()) {
            logger.error("could not find config file *-mmvc.xml");
            return;
        }

        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(configFile);
            Element root = doc.getRootElement();

            List cElementList = root.elements("controller");
            for (Object cElement : cElementList) {
                if (cElement instanceof Element) {
                    String controllerClasspath = ((Element)cElement).attributeValue("package");
                    MappingHandler mappingHandler  = new MappingHandler(controllerClasspath);
                    controllerMap.putAll(mappingHandler.getControllerMap());
                }
            }
        } catch (DocumentException e) {
            logger.error("Parse config xml file error("+configFile.getAbsolutePath()+") ", e);
        }
    }

    public Map<String, Mapping> getControllerMap() {
        return controllerMap;
    }

    public void setControllerMap(Map<String, Mapping> controllerMap) {
        this.controllerMap = controllerMap;
    }
}
