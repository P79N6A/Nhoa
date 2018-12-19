package com.wisdom.nhoa.metting.model;

/**
 * @author lxd
 * @ProjectName project： 会议签名
 * @class package：
 * @class describe：MeetingSignUserModel
 * @time 17:21
 * @change
 */
public class MeetingSignUserModel {
    private String user_name;
    private String dep_name;

    @Override
    public String toString() {
        return "MeetingSignUserModel{" +
                "user_name='" + user_name + '\'' +
                ", dep_name='" + dep_name + '\'' +
                ", role_name='" + role_name + '\'' +
                '}';
    }

    private String role_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
