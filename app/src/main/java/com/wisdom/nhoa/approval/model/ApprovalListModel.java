package com.wisdom.nhoa.approval.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe：
 * @time 2018/7/12 17:17
 * @change
 */
public class ApprovalListModel implements Serializable {


/**
 * id	string	ID
 days	string	天数(大于零)  休假天数 /出差天数休假/出差
 applytime	string	申请时间(创建时间)
 status	string	状态 0待审核 1审核通过 2审核退回
 typename	String	休假类型 /物品名称休假/用品申请
 auditorname	String	审核人员
 auditortime	string	审核时间
 username	string	申请人
 starttime	string	开始时间休假/出差
 endtime	string	结束时间休假/出差
 reason	string	申请事由
 remark	string	备注信息
 explain	String	审核人员填写说明
 vehicle	string	交通工具出差
 addr	String	出差地点出差
 type	String	类型  xj:休假  cc : 出差   yp : 用品
 specifications	String	用品规格用品申请
 numbers	String	用品申请数量用品申请

 * */

    private String endtime;
    private String starttime;
    private String status;
    private String reason;
    private String remark;
    private String auditortime;
    private String vehicle;
    private String addr;
    private String type;
    private String specifications;
    private String auditorname;
    private String id;
    private String username;
    private String days;
    private String numbers;
    private String typename;
    private String explain;
    private String applytime;
    private String remindername;
    private String fileurl;
    private String filename;

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

    public String getRemindername() {
        return remindername;
    }

    public void setRemindername(String remindername) {
        this.remindername = remindername;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAuditortime() {
        return auditortime;
    }

    public void setAuditortime(String auditortime) {
        this.auditortime = auditortime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getAuditorname() {
        return auditorname;
    }

    public void setAuditorname(String auditorname) {
        this.auditorname = auditorname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }
}
