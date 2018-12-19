package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：我审核的 列表数据模型
 * @time 2018/11/8 9:18
 * @change
 */
public class MyCheckedApplyMeetingRoomModel implements Serializable {
    private String id;//
    private String theme;//
    private String bdrmName;//
    private String crewSize;//
    private String staTime;//
    private String endTime;//
    private String addTime;//
    private String status;//

    @Override
    public String toString() {
        return "MyCheckedApplyMeetingRoomModel{" +
                "id='" + id + '\'' +
                ", theme='" + theme + '\'' +
                ", bdrmName='" + bdrmName + '\'' +
                ", crewSize='" + crewSize + '\'' +
                ", staTime='" + staTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", addTime='" + addTime + '\'' +
                ", status='" + status + '\'' +
                '}';
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
