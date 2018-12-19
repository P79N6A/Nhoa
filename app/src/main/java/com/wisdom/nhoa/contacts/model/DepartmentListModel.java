package com.wisdom.nhoa.contacts.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.model
 * @class describe：
 * @time 2018/3/25 15:35
 * @change
 */

public class DepartmentListModel implements Serializable {
    private String depid;//厅局id
    private String depname;//厅局名称

    @Override
    public String toString() {
        return "DepartmentListModel{" +
                "depid='" + depid + '\'' +
                ", depname='" + depname + '\'' +
                '}';
    }

    public String getDepid() {
        return depid;
    }

    public void setDepid(String depid) {
        this.depid = depid;
    }

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }
}
