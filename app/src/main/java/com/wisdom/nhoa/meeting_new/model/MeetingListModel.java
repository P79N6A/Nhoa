package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe： 发布会议的 会议列表model
 * @time 2018/11/6 16:54
 * @change
 */
public class MeetingListModel implements Serializable {
    private String id;
    private String theme;
    private String bdrmName;
    private String crewSize;
    private String staTime;
    private String endTime;
    private String addTime;
    private String status;
    private String meetingid;

    public String getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(String meetingid) {
        this.meetingid = meetingid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getBdrmName() {
        return bdrmName;
    }

    public void setBdrmName(String bdrmName) {
        this.bdrmName = bdrmName;
    }

    public String getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(String crewSize) {
        this.crewSize = crewSize;
    }

    public String getStaTime() {
        return staTime;
    }

    public void setStaTime(String staTime) {
        this.staTime = staTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
