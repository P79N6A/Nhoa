package com.wisdom.nhoa.circulated.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.model
 * @class describe：
 * @time 2018/3/25 18:04
 * @change
 */

public class CirculatedListModel implements Serializable {
    private String group_id;//群ID
    private String group_name;//群标题
    private String createtime;//创建时间
    private String membercount;//群成员数量
    private String iscreater;//是否创建者(0/1)

    @Override
    public String toString() {
        return "CirculatedListModel{" +
                "group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", createtime='" + createtime + '\'' +
                ", membercount='" + membercount + '\'' +
                ", iscreater='" + iscreater + '\'' +
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getMembercount() {
        return membercount;
    }

    public void setMembercount(String membercount) {
        this.membercount = membercount;
    }

    public String getIscreater() {
        return iscreater;
    }

    public void setIscreater(String iscreater) {
        this.iscreater = iscreater;
    }
}
