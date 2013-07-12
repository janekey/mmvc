package com.janekey.mmvc.mapping;

/**
 * User: qi.zheng
 * Date: 13-7-12
 * Time: 上午11:52
 */
public class Mapping {

    private String url;
    private String classPath;
    private String method;

    public Mapping(String url, String classPath, String method) {
        this.url = url;
        this.classPath = classPath;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
