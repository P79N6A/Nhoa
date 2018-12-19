package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe：
 * @time 2018/3/25 13:32
 * @change
 */

public class HomePageStaticModel implements Serializable {
    private String backlognum;//收发文在办的数量
    private String pendingnum;//未定，返回0即可
    private String noticenum;//未查阅公告数量
    private String draftnum;//收发文已保存未开始流转的草稿数量

    public String getBacklognum() {
        return backlognum;
    }

    public void setBacklognum(String backlognum) {
        this.backlognum = backlognum;
    }

    public String getPendingnum() {
        return pendingnum;
    }

    public void setPendingnum(String pendingnum) {
        this.pendingnum = pendingnum;
    }

    public String getNoticenum() {
        return noticenum;
    }

    public void setNoticenum(String noticenum) {
        this.noticenum = noticenum;
    }

    public String getDraftnum() {
        return draftnum;
    }

    public void setDraftnum(String draftnum) {
        this.draftnum = draftnum;
    }

    @Override
    public String toString() {
        return "HomePageStaticModel{" +
                "backlognum='" + backlognum + '\'' +
                ", pendingnum='" + pendingnum + '\'' +
                ", noticenum='" + noticenum + '\'' +
                ", draftnum='" + draftnum + '\'' +
                '}';
    }



}
