package com.wisdom.nhoa.sendreceive.model;

/**
 * @author lxd
 * @ProjectName project： 发文选择节点人员model
 * @class package：
 * @class describe：SendIssuePersonModel
 * @time 17:15
 * @change
 */
public class SendIssuePersonModel {
    private String node_id;
    private String user_name;
    private String role_id;
    private String user_id;
    private String node_name;
    private String role_name;

    @Override
    public String toString() {
        return "SendIssuePersonModel{" +
                "node_id='" + node_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", role_id='" + role_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", node_name='" + node_name + '\'' +
                ", role_name='" + role_name + '\'' +
                ", isChecked='" + isChecked + '\'' +
                '}';
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    private String isChecked;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }
}
