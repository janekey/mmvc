package com.janekey.mmvcweb.controller;

import com.janekey.mmvc.annotation.Controller;
import com.janekey.mmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: qi.zheng
 * Date: 13-7-12
 * Time: 下午12:12
 */
@Controller
@RequestMapping("index")
public class IndexController {

    @RequestMapping("abc")
    public String abc(HttpServletRequest request, HttpServletResponse response) {
        return "index.jsp";
    }

}
