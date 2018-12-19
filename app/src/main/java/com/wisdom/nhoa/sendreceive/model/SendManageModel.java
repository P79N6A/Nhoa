package com.wisdom.nhoa.sendreceive.model;

import java.io.Serializable;
/**
 * @author lxd
 * @ProjectName project：发文列表
 * @class package：
 * @class describe：SendManageModel
 * @time 17:19
 * @change
 */

public class SendManageModel implements Serializable{
    private String insid;
    private String doctitle;
    private String docnumber;
    private String doctype;

    @Override
    public String toString() {
        return "SendManageModel{" +
                "insid='" + insid + '\'' +
                ", doctitle='" + doctitle + '\'' +
                ", docnumber='" + docnumber + '\'' +
                ", doctype='" + doctype + '\'' +
                '}';
    }

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
