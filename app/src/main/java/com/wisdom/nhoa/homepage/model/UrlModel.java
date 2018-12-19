package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * Created by HanXueFeng on 2017/11/8.
 */

public class UrlModel implements Serializable {
    private String name;
    private String type;
    private String url;
    private String insId;
    private String sign_name;

    @Override
    public String toString() {
        return "UrlModel{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", insId='" + insId + '\'' +
                ", sign_name='" + sign_name + '\'' +
                '}';
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
