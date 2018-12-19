package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe：
 * @time 2018/3/30 9:53
 * @change
 */

public class LatelyPersonModel implements Serializable {
    private String username;//人员名称
    private String rolename;//	角色名称
    private String phone;//手机号
    private String depname;//	厅局名称
    private String offname;//	部门名
    private String userid;//	部门名

    @Override
    public String toString() {
        return "LatelyPersonModel{" +
                "username='" + username + '\'' +
                ", rolename='" + rolename + '\'' +
                ", phone='" + phone + '\'' +
                ", depname='" + depname + '\'' +
                ", offname='" + offname + '\'' +
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

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    public String getOffname() {
        return offname;
    }

    public void setOffname(String offname) {
        this.offname = offname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
