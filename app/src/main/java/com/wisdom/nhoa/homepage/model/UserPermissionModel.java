package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe：用户权限查询接口返回model
 * @time 2018/11/13 16:38
 * @change
 */
public class UserPermissionModel implements Serializable {
    private String column_id;
    private String node_name;
    private String status;

    @Override
    public String toString() {
        return "UserPermissionModel{" +
                "column_id='" + column_id + '\'' +
                ", node_name='" + node_name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
