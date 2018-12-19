package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：申请会议室详情界面
 * @time 2018/10/24 13:11
 * @change
 */
public class ApplyMeetingRoomDetailModel implements Serializable {
    private String id;
    private String staTime;
    private String theme;
    private String crewSize;
    private String endTime;
    private String addTime;
    private String bdrmName;
    private String status;
    private String bdrmAdd;
    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getBdrmAdd() {
        return bdrmAdd;
    }

    public void setBdrmAdd(String bdrmAdd) {
        this.bdrmAdd = bdrmAdd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaTime() {
        return staTime;
    }

    public void setStaTime(String staTime) {
        this.staTime = staTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(String crewSize) {
        this.crewSize = crewSize;
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

    public String getBdrmName() {
        return bdrmName;
    }

    public void setBdrmName(String bdrmName) {
        this.bdrmName = bdrmName;
    }
}
