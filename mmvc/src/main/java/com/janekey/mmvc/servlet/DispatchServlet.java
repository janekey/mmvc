package com.janekey.mmvc.servlet;

import com.janekey.mmvc.mapping.Mapping;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * User: qi.zheng
 * Date: 13-7-11
 * Time: 下午5:44
 */
public class DispatchServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(DispatchServlet.class);

    private Map<String, Mapping> mappingMap;

    @Override
    public void init() throws ServletException {
        logger.debug("Initializing servlet '" + getServletName() + "'");

        //init config
        ConfigHandler configHandler = new ConfigHandler(getServletConfig());
        mappingMap = configHandler.getControllerMap();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    /**
     * get the URI from (www.domain.com/uri).
     */
    public String getURI(HttpServletRequest req, HttpServletResponse res) {

        //get URI(for example: /project/check.do)
        String projectName = req.getContextPath();

        String currentURI = req.getRequestURI();

        currentURI = currentURI.replace(projectName, "");

        //get the name of the URI
//        int urlInt = currentURI.indexOf("/");
        String path;
        if (currentURI.contains(".")) {
            path = currentURI.substring(0, currentURI.indexOf("."));
        } else {
            path = "index";
        }
        //(for example: /check)
        return path;
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //get URI
        String uriPath = getURI(request, response);
        if (uriPath != null) {
            try {
                if (mappingMap.containsKey(uriPath)) {
                    //instantiate controller
                    Mapping mapping = mappingMap.get(uriPath);
                    String classpath = mapping.getClassPath();
                    String methodName = mapping.getMethod();
                    Object controller = Class.forName(classpath).newInstance();

                    //execute method
                    Method method = controller.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
//                    Class[] declaredParams = method.getParameterTypes();
//                    Object[] params = new Object[declaredParams.length];
//                    for (int i = 0; i < declaredParams.length; i++) {
//                        if (declaredParams[i] == HttpServletRequest.class) params[i] = request;
//                        if (declaredParams[i] == HttpServletResponse.class) params[i] = response;
//                    }
                    String forward = (String)method.invoke(controller, request, response);
                    System.out.println("forward : " + forward);

                    //forward to the next url.
                    RequestDispatcher view = request.getRequestDispatcher("/" + forward);
                    view.forward(request, response);
                } else {
                    System.out.println("no map url");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
