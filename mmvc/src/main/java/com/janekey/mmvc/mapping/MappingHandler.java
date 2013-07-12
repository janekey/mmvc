package com.janekey.mmvc.mapping;

import com.janekey.mmvc.annotation.Controller;
import com.janekey.mmvc.annotation.RequestMapping;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * User: qi.zheng
 * Date: 13-7-12
 * Time: 上午11:49
 */
public class MappingHandler {

    private final Logger logger = Logger.getLogger(MappingHandler.class);
    private Map<String, Mapping> controllerMap = new HashMap<String, Mapping>();

    public MappingHandler(String packageClasspath) {
        try {
            List<Class> cotrollerClasses = getClassesByPackageName(packageClasspath);
            for (Class<?> controllerClass : cotrollerClasses) {
                Controller conAnno = controllerClass.getAnnotation(Controller.class);
                // Get real controller class in the package.
                if (conAnno == null)
                    continue;

                // Class request mapping url
                RequestMapping reqMAnno = controllerClass.getAnnotation(RequestMapping.class);
                String[] classUrl = null;
                if (reqMAnno != null) {
                    classUrl = reqMAnno.value();
                }

                Method[] methods = controllerClass.getDeclaredMethods();
                if (methods != null) {
                    for (Method method : methods) {
                        // Method request mapping url
                        RequestMapping rmAnno = method.getAnnotation(RequestMapping.class);
                        if (rmAnno != null) {
                            if (method.getReturnType() != String.class) {
                                logger.error("Error method, method return type must be String. " + controllerClass.getName() + "-" + method.getName(), new Exception());
                                continue;
                            }
                            Class[] params = method.getParameterTypes();
                            if (params == null || params.length != 2 ||
                                    params[0] != HttpServletRequest.class || params[1] != HttpServletResponse.class) {
                                logger.error("Error method, method parameters must be HttpServletRequest, HttpServletResponse. " + controllerClass.getName() + "-" + method.getName(), new Exception());
                                continue;
                            }

                            String[] methodUrl = rmAnno.value();
                            if (methodUrl != null) {
                                List<String> urlList = new ArrayList<String>();
                                // class hasn't url mapping
                                if (classUrl == null || classUrl.length == 0) {
                                    for (String url : methodUrl) {
                                        if (!url.startsWith("/"))
                                            url = "/" + url;
                                        urlList.add(url);
                                        logger.info("Mapped Url [" + url + "]");
                                    }
                                } else {
                                    for (String url1 : classUrl) {
                                        if (!url1.startsWith("/"))
                                            url1 = "/" + url1;
                                        for (String url : methodUrl) {
                                            if (!url.startsWith("/"))
                                                url = "/" + url;
                                            urlList.add(url1 + url);
                                            logger.info("Mapped Url [" + url1 + url + "]");
                                        }
                                    }
                                }
                                for (String url : urlList) {
                                    Mapping mapping = new Mapping(url, controllerClass.getName(), method.getName());
                                    controllerMap.put(url, mapping);
                                }
                            }

                        }
                    }
                }



            }
        } catch (Exception e) {
            logger.error("controller package error (" + packageClasspath + ") ", e);
        }
    }

    /**
     * Find all classes in the package.
     */
    private List<Class> getClassesByPackageName(String packageName)
            throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // recursion all class in the package
                    classes.addAll(findClasses(file, packageName + '.' + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + "."
                            + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    public Map<String, Mapping> getControllerMap() {
        return controllerMap;
    }

    public void setControllerMap(Map<String, Mapping> controllerMap) {
        this.controllerMap = controllerMap;
    }
}
