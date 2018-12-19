package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe： 申请会议室列表 model
 * @time 2018/10/24 9:49
 * @change
 */
public class ApplyMeetingRoomListModel implements Serializable {
    private String id;
    private String theme;
    private String staTime;
    private String endTime;
    private String status;
    //会议状态code（为空或null：等待审批；0：审批退回；1：审批通过；2：已发布）
    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    @Override
    public String toString() {
        return "ApplyMeetingRoomListModel{" +
                "id='" + id + '\'' +
                ", theme='" + theme + '\'' +
                ", staTime='" + staTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
}
