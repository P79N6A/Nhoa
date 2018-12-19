package com.wisdom.nhoa.homepage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * @authorzhanglichao
 * @date2018/3/28 9:57
 * @package_name com.wisdom.nhoa.homepage.model
 */

public class DraftListModel {
    private String insid;
    private String docnumber;
    private String doctitle;
    private String doctype ;
    public DraftListModel() {
    }
    public DraftListModel(String insid, String docnumber, String doctitle,
            String doctype) {
        this.insid = insid;
        this.docnumber = docnumber;
        this.doctitle = doctitle;
        this.doctype = doctype;
    }

    public String getInsid() {
        return insid;
    }

    public String getDocnumber() {
        return docnumber;
    }

    public String getDoctitle() {
        return doctitle;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setInsid(String insid) {
        this.insid = insid;
    }

    public void setDocnumber(String docnumber) {
        this.docnumber = docnumber;
    }

    public void setDoctitle(String doctitle) {
        this.doctitle = doctitle;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    @Override
    public String toString() {
        return "DraftListModel{" +
                "insid='" + insid + '\'' +
                ", docnumber='" + docnumber + '\'' +
                ", doctitle='" + doctitle + '\'' +
                ", doctype='" + doctype + '\'' +
                '}';
    }
}
