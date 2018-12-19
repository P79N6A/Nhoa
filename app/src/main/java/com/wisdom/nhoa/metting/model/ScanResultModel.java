package com.wisdom.nhoa.metting.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.metting.model
 * @class describe：
 * @time 2018/4/17 13:23
 * @change
 */

public class ScanResultModel implements Serializable {
    private String meetingid;// 会议id
    private String meetingtitle;// 会议主题
    private String convokedep;// 召开部门
    private String starttime;// 开始时间
//    private String signstate;// 签到状态
    private String meetingContent;// 会议内容

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }

    public String getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(String meetingid) {
        this.meetingid = meetingid;
    }

    public String getMeetingtitle() {
        return meetingtitle;
    }

    public void setMeetingtitle(String meetingtitle) {
        this.meetingtitle = meetingtitle;
    }

    public String getConvokedep() {
        return convokedep;
    }

    public void setConvokedep(String convokedep) {
        this.convokedep = convokedep;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

//    public String getSignstate() {
//        return signstate;
//    }

//    public void setSignstate(String signstate) {
//        this.signstate = signstate;
//    }


    @Override
    public String toString() {
        return "ScanResultModel{" +
                "meetingid='" + meetingid + '\'' +
                ", meetingtitle='" + meetingtitle + '\'' +
                ", convokedep='" + convokedep + '\'' +
                ", starttime='" + starttime + '\'' +
                ", meetingContent='" + meetingContent + '\'' +
                '}';
    }
}
