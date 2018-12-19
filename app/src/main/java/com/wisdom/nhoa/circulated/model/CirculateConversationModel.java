package com.wisdom.nhoa.circulated.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.model
 * @class describe：公文传阅，对话列表页面
 * @time 2018/3/26 15:00
 * @change
 */

public class CirculateConversationModel implements Serializable {
    private String creater;//发件人
    private String filename;//文件名
    private String createtime;//发送时间
    private String fileid;//文件ID
    private String filepath;//下载的文件URL
    private String iscreater;//是否创建者(0/1)是 0   否 1
    private String group_id;
    private String group_name;

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getIscreater() {
        return iscreater;
    }

    public void setIscreater(String iscreater) {
        this.iscreater = iscreater;
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

    @Override
    public String toString() {
        return "CirculateConversationModel{" +
                "creater='" + creater + '\'' +
                ", filename='" + filename + '\'' +
                ", createtime='" + createtime + '\'' +
                ", fileid='" + fileid + '\'' +
                ", filepath='" + filepath + '\'' +
                ", iscreater='" + iscreater + '\'' +
                ", group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                '}';
    }
}
