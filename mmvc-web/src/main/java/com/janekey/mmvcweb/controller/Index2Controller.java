package com.janekey.mmvcweb.controller;

import com.janekey.mmvc.annotation.Controller;
import com.janekey.mmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: qi.zheng
 * Date: 13-7-12
 * Time: 下午3:33
 */
@Controller
public class Index2Controller {

    @RequestMapping("method")
    public String method2(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("request not null : " + request.getRequestURI());
        return "index.jsp";
    }

}
