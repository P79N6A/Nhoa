package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：会议室列表model
 * @time 2018/10/24 13:44
 * @change
 */
public class MeetingRoomModel implements Serializable {
    private String id;
    private String bdrmAdd;
    private String bdrmName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBdrmAdd() {
        return bdrmAdd;
    }

    public void setBdrmAdd(String bdrmAdd) {
        this.bdrmAdd = bdrmAdd;
    }

    public String getBdrmName() {
        return bdrmName;
    }

    public void setBdrmName(String bdrmName) {
        this.bdrmName = bdrmName;
    }
}
