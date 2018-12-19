package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe：
 * @time 2018/3/25 14:25
 * @change
 */

public class NoticeModel implements Serializable {


    private String noticeid;
    private String noticetitle;
    private String createtime;

    @Override
    public String toString() {
        return "NoticeModel{" +
                "Noticeid='" + noticeid + '\'' +
                ", noticetitle='" + noticetitle + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        noticeid = noticeid;
    }

    public String getNoticetitle() {
        return noticetitle;
    }

    public void setNoticetitle(String noticetitle) {
        this.noticetitle = noticetitle;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
