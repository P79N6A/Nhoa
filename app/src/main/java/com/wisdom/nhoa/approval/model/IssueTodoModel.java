package com.wisdom.nhoa.approval.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe：
 * @time 2018/3/8 10:35
 * @change
 */

public class IssueTodoModel implements Serializable {
    private String insid;
    private String doctitle;
    private String docnumber;
    private String doctype;

    public String getInsid() {
        return insid;
    }

    public void setInsid(String insid) {
        this.insid = insid;
    }

    public String getDoctitle() {
        return doctitle;
    }

    public void setDoctitle(String doctitle) {
        this.doctitle = doctitle;
    }

    public String getDocnumber() {
        return docnumber;
    }

    public void setDocnumber(String docnumber) {
        this.docnumber = docnumber;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }
}
