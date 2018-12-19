package com.wisdom.nhoa.circulated.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.model
 * @class describe： 公文传阅群详情页面
 * @time 2018/3/27 13:14
 * @change
 */

public class GroupDetailModel implements Serializable {
    private String group_id;//
    private String group_name;//群标题
    private String group_notice;//群公告
    private String iscreater;//是否创建者(0/1)
    private List<Userlist> userlist;//是否创建者(0/1)

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

    @Override
    public String toString() {
        return "GroupDetailModel{" +
                "group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_notice='" + group_notice + '\'' +
                ", iscreater='" + iscreater + '\'' +
                ", userlist=" + userlist +
                '}';
    }

    public class Userlist implements Serializable {
        private String user_name;//群成员名称
        private String Dep_name;//成员所属部门
        private String Off_name;//成员所属厅局
        private String role_name;//成员角色

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getDep_name() {
            return Dep_name;
        }

        public void setDep_name(String dep_name) {
            Dep_name = dep_name;
        }

        public String getOff_name() {
            return Off_name;
        }

        public void setOff_name(String off_name) {
            Off_name = off_name;
        }

        public String getRole_name() {
            return role_name;
        }

        public void setRole_name(String role_name) {
            this.role_name = role_name;
        }

        @Override
        public String toString() {
            return "Userlist{" +
                    "user_name='" + user_name + '\'' +
                    ", Dep_name='" + Dep_name + '\'' +
                    ", Off_name='" + Off_name + '\'' +
                    ", role_name='" + role_name + '\'' +
                    '}';
        }
    }

}
