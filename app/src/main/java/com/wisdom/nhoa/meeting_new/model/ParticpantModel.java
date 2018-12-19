package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author lxd
 * @ProjectName project： 签到人员model
 * @class package：
 * @class describe：ParticpantModel
 * @time 17:23
 * @change
 */
public class ParticpantModel implements Serializable{
    private String user_id;
    private String user_name;
    private String depname;
    private String role_name;
    private String off_name;

    @Override
    public String toString() {
        return "ParticpantModel{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", depname='" + depname + '\'' +
                ", role_name='" + role_name + '\'' +
                ", off_name='" + off_name + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getOff_name() {
        return off_name;
    }

    public void setOff_name(String off_name) {
        this.off_name = off_name;
    }
}
