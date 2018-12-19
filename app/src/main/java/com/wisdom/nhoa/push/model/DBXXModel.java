package com.wisdom.nhoa.push.model;

import java.io.Serializable;

/**
 * Created by hanxuefeng on 2018/8/2.
 * DBXX（督办信息）
 */

public class DBXXModel implements Serializable {
    private String name;//督办名称
    private String begintime;//开始时间
    private String percentage;//完成百分比
    private String status;//状态 空 无反馈 , 0进行中，1完成，2超额完成
    private String username;//督办人
    private String endtime;//结束时间
    private String content;//督办内容
    private String createtime;//督办事项创建时间
    private String finishtime;//完成时间 (达到百分比时间)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }
}
