package com.lenovo.framework.KnowledgeBase.bean;

import java.io.Serializable;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-11 上午9:28:41
 * @version : V1.0
 */
public class Category implements Serializable {

    private static final long serialVersionUID = -8829182481073789057L;
    private String url;
    private String name;

    public Category(String url, String name) {
        super();
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category [url=" + url + ", name=" + name + "]";
    }

}
