package com.wisdom.nhoa.circulated.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.model
 * @class describe：公文传阅管理群成员页面
 * @time 2018/3/27 13:14
 * @change
 */

public class GroupManagementModel implements Serializable {

    private String group_id;//
    private String group_name;//
    private String group_notice;//
    private String iscreater;//
    private List<Userlist> userlist;//　　

    @Override
    public String toString() {
        return "GroupManagementModel{" +
                "group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_notice='" + group_notice + '\'' +
                ", iscreater='" + iscreater + '\'' +
                ", userlist=" + userlist +
                '}';
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_notice() {
        return group_notice;
    }

    public void setGroup_notice(String group_notice) {
        this.group_notice = group_notice;
    }

    public String getIscreater() {
        return iscreater;
    }

    public void setIscreater(String iscreater) {
        this.iscreater = iscreater;
    }

    public List<Userlist> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<Userlist> userlist) {
        this.userlist = userlist;
    }

    public class Userlist {
        private String user_id;//
        private String username;//
        private String rolename;//
        private String offname;//
        private String depname;//
        private String issave;//

        @Override
        public String toString() {
            return "Userlist{" +
                    "user_id='" + user_id + '\'' +
                    ", username='" + username + '\'' +
                    ", rolename='" + rolename + '\'' +
                    ", offname='" + offname + '\'' +
                    ", depname='" + depname + '\'' +
                    ", issave='" + issave + '\'' +
                    '}';
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getOffname() {
            return offname;
        }

        public void setOffname(String offname) {
            this.offname = offname;
        }

        public String getDepname() {
            return depname;
        }

        public void setDepname(String depname) {
            this.depname = depname;
        }

        public String getIssave() {
            return issave;
        }

        public void setIssave(String issave) {
            this.issave = issave;
        }
    }

}

