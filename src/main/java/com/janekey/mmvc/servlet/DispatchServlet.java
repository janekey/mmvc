package com.janekey.mmvc.servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: qi.zheng
 * Date: 13-7-11
 * Time: 下午5:44
 */
public class DispatchServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(DispatchServlet.class);

    @Override
    public void init() throws ServletException {
        logger.debug("Initializing servlet '" + getServletName() + "'");

        //todo init config
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    public void process(HttpServletRequest request, HttpServletResponse response) {

    }
}
