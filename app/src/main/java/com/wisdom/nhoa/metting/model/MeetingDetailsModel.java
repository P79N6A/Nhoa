package com.wisdom.nhoa.metting.model;

/**
 * @author lxd
 * @ProjectName project： 会议详情model
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe：MeetingDetailsModel
 * @time 10:04
 * @change
 */

public class MeetingDetailsModel {
    private String meetingtitle;
    private String convokedep;
    private String compere;
    private String memberid;
    private String starttime;
    private String endtime;
    private String createtime;
    private String meetingcontent;
    private String state;
    private String bdrmName;
    private String bdrmAdd;

    @Override
    public String toString() {
        return "MeetingDetailsModel{" +
                "meetingtitle='" + meetingtitle + '\'' +
                ", convokedep='" + convokedep + '\'' +
                ", compere='" + compere + '\'' +
                ", memberid='" + memberid + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", createtime='" + createtime + '\'' +
                ", meetingcontent='" + meetingcontent + '\'' +
                ", state='" + state + '\'' +
                ", bdrmName='" + bdrmName + '\'' +
                ", bdrmAdd='" + bdrmAdd + '\'' +
                '}';
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

    public String getCompere() {
        return compere;
    }

    public void setCompere(String compere) {
        this.compere = compere;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getMeetingcontent() {
        return meetingcontent;
    }

    public void setMeetingcontent(String meetingcontent) {
        this.meetingcontent = meetingcontent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBdrmName() {
        return bdrmName;
    }

    public void setBdrmName(String bdrmName) {
        this.bdrmName = bdrmName;
    }

    public String getBdrmAdd() {
        return bdrmAdd;
    }

    public void setBdrmAdd(String bdrmAdd) {
        this.bdrmAdd = bdrmAdd;
    }
}
