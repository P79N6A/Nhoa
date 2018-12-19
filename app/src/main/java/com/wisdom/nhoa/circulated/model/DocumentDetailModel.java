package com.wisdom.nhoa.circulated.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.model
 * @class describe：
 * @time 2018/3/27 16:55
 * @change
 */

public class DocumentDetailModel implements Serializable {
    private String user_name;//
    private String dep_name;//
    private String off_name;//
    private String role_name;//

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

    public String getOff_name() {
        return off_name;
    }

    public void setOff_name(String off_name) {
        this.off_name = off_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    @Override
    public String toString() {
        return "DocumentDetailModel{" +
                "user_name='" + user_name + '\'' +
                ", dep_name='" + dep_name + '\'' +
                ", off_name='" + off_name + '\'' +
                ", role_name='" + role_name + '\'' +
                '}';
    }
}
