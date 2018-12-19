package com.wisdom.nhoa.push.model;

import java.io.Serializable;

/**
 * Created by hanxuefeng on 2018/8/2.
 * 出差审批
 */

public class CCSPModel implements Serializable {
    private String days;//天数(大于零)  出差天数
    private String applytime;// 申请时间(创建时间)
    private String status;//状态 0待审核 1审核通过 2审核退回
    private String username;//申请人
    private String starttime;//开始时间
    private String endtime;//结束时间
    private String reason;//申请事由
    private String remark;//备注信息
    private String vehicle;//交通工具
    private String addr;//出差地点
    private String fileurl;//文件连接地址  reception/upload/2ee54760be99409a861f5637eb847ca9.png
    private String filename;//文件名称    例:gqt.png
    private String reminder;//抄送人主键们
    private String remindername;//抄送人姓名

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getRemindername() {
        return remindername;
    }

    public void setRemindername(String remindername) {
        this.remindername = remindername;
    }
}
