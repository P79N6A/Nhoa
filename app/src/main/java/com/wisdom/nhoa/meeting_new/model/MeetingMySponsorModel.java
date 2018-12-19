package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：会议  我发起的列表
 * @time 2018/10/23 17:06
 * @change
 */
public class MeetingMySponsorModel implements Serializable {
    private String emUrl;
    private String id;
    private String user_name;
    private String launchTime;
    private String staTime;
    private String state;
    private String sureStatus;
    private String meetName;
    private String emName;
    private String endTime;
    private String note;

    public String getEmUrl() {
        return emUrl;
    }

    public void setEmUrl(String emUrl) {
        this.emUrl = emUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public String getStaTime() {
        return staTime;
    }

    public void setStaTime(String staTime) {
        this.staTime = staTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSureStatus() {
        return sureStatus;
    }

    public void setSureStatus(String sureStatus) {
        this.sureStatus = sureStatus;
    }

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getEmName() {
        return emName;
    }

    public void setEmName(String emName) {
        this.emName = emName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
