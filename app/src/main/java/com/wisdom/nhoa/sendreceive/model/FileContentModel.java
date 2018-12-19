package com.wisdom.nhoa.sendreceive.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.sendreceive.model
 * @class describe：
 * @time 2018/3/27 22:00
 * @change
 */

public class FileContentModel implements Serializable {
    private String doccontext_url;

    @Override
    public String toString() {
        return "FileContentModel{" +
                "doccontext_url='" + doccontext_url + '\'' +
                '}';
    }

    public String getDoccontext_url() {
        return doccontext_url;
    }

    public void setDoccontext_url(String doccontext_url) {
        this.doccontext_url = doccontext_url;
    }
}
