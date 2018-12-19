package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe：首页待阅
 * @time 2018/4/4 10:13
 * @change
 */

public class ToBeReadModel implements Serializable {
    private String noticeid;
    private String noticetitle;
    private String docUrl;
    private String groupName;
    private String upTime;

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "ToBeReadModel{" +
                "noticeid='" + noticeid + '\'' +
                ", noticetitle='" + noticetitle + '\'' +
                '}';
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getNoticetitle() {
        return noticetitle;
    }

    public void setNoticetitle(String noticetitle) {
        this.noticetitle = noticetitle;
    }
}
