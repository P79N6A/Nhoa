package com.wisdom.nhoa.push.model;

import java.io.Serializable;

/**
 * Created by hanxuefeng on 2018/8/2.
 * YPSP（用品审批）
 */

public class YPSPModel implements Serializable {
    private String applytime;// 申请时间(创建时间)
    private String status;//状态 0待审核 1审核通过 2审核退回
    private String typename;//物品名称
    private String username;//申请人
    private String reason;//申请事由
    private String remark;//备注信息
    private String specifications;//用品规格
    private String numbers;//用品申请数量
    private String reminder;//抄送人主键们
    private String remindername;//抄送人姓名

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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
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
