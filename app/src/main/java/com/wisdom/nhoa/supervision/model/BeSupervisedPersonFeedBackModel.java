package com.wisdom.nhoa.supervision.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.supervision.model
 * @class describe：被督办人反馈信息详细列表
 * @time 2018/7/26 11:09
 * @change
 */
public class BeSupervisedPersonFeedBackModel implements Serializable {

    /**
     * content : 60
     * edittime : 2018-07-20 14:37:42
     * userpercentage : 60
     * worktime : 2018-07-03 - 2018-07-06
     */

    private String content;
    private String edittime;
    private String userpercentage;
    private String worktime;

    @Override
    public String toString() {
        return "BeSupervisedPersonFeedBackModel{" +
                "content='" + content + '\'' +
                ", edittime='" + edittime + '\'' +
                ", userpercentage='" + userpercentage + '\'' +
                ", worktime='" + worktime + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public String getUserpercentage() {
        return userpercentage;
    }

    public void setUserpercentage(String userpercentage) {
        this.userpercentage = userpercentage;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }
}
