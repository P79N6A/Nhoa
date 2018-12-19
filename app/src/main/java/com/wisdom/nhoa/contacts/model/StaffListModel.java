package com.wisdom.nhoa.contacts.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.model
 * @class describe：
 * @time 2018/3/25 16:55
 * @change
 */

public class StaffListModel implements Serializable {
    private String username ;//人员名称
    private String  rolename ;//角色名称
    private String  phone;//手机号
    private String  userid;//

    @Override
    public String toString() {
        return "StaffListModel{" +
                "username='" + username + '\'' +
                ", rolename='" + rolename + '\'' +
                ", phone='" + phone + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
