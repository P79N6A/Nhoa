package com.wisdom.nhoa.metting.model;

/**
 * @author lxd
 * @ProjectName project： 会议列表
 * @class package：
 * @class describe：MeetingListModel
 * @time 17:21
 * @change
 */

public class MeetingListModel {

    private String meetingid;
    private String meetingtopic;
    private String createtime;
    private String qrcodeurl;
    private String iscreater;
    private String signstate;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSignstate() {
        return signstate;
    }

    public void setSignstate(String signstate) {
        this.signstate = signstate;
    }

    @Override
    public String toString() {
        return "MeetingListModel{" +
                "Meetingid='" + meetingid + '\'' +
                ", meetingtopic='" + meetingtopic + '\'' +
                ", Createtime='" + createtime + '\'' +
                ", qrcodeurl='" + qrcodeurl + '\'' +
                ", iscreater='" + iscreater + '\'' +
                '}';
    }

    public String getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(String meetingid) {
        this.meetingid = meetingid;
    }

    public String getMeetingtopic() {
        return meetingtopic;
    }

    public void setMeetingtopic(String meetingtopic) {
        this.meetingtopic = meetingtopic;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    public String getIscreater() {
        return iscreater;
    }

    public void setIscreater(String iscreater) {
        this.iscreater = iscreater;
    }
}
