package com.wisdom.nhoa.mine.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.mine.model
 * @class describe：
 * @time 2018/3/25 11:32
 * @change
 */

public class LoginModel implements Serializable {
    private String uid;//用户的id
    private String access_token;//不能使用本返回值里的UID字段来做登录识别。
    private String expires_in;//access_token的生命周期。服务端生成的access_token最大生命周期不能超过30天。
    private String organization;//登陆人所属地市名称
    private String user_name;//用户名
    private String phone;//用户名
    private String passsword;
    private String login_name;
    private String simple_name;
    private String department_name;
    private String role_name;

    public String getSimple_name() {
        return simple_name;
    }

    public void setSimple_name(String simple_name) {
        this.simple_name = simple_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "uid='" + uid + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", organization='" + organization + '\'' +
                ", user_name='" + user_name + '\'' +
                ", phone='" + phone + '\'' +
                ", passsword='" + passsword + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
