package com.wisdom.nhoa.push.model;

import java.io.Serializable;

/**
 * Created by hanxuefeng on 2018/8/2.
 * HYGL（会议管理）
 */

public class HYGLModel implements Serializable {
    private String meetingtitle;//会议主题
    private String convokedep;//召开部门
    private String compere;//会议发起人
    private String memberid;//参会人员
    private String starttime;//会议开始时间
    private String endtime;//会议结束时间
    private String createtime;//会议创建时间
    private String state;//会议状态
    private String meetingcontent;//会议内容

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMeetingcontent() {
        return meetingcontent;
    }

    public void setMeetingcontent(String meetingcontent) {
        this.meetingcontent = meetingcontent;
    }
}
