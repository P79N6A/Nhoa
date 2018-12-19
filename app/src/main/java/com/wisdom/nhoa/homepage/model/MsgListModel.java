package com.wisdom.nhoa.homepage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.model
 * @class describe： 消息列表接口数据源
 * @time 2018/4/2 10:40
 * @change
 */
@Entity
public class MsgListModel  {
    @Id(autoincrement = true)
    private  Long id;
    @Property(nameInDb = "MSGTITLE")
    private String msgtitle;
    @Property(nameInDb = "MSGDESCRIPTION")
    private String msgdiscription;
    @Property(nameInDb = "MESSTYPECODE")
    private String messTypeCode;
    @Property(nameInDb = "DATAID")
    private String dataId;
    @Property(nameInDb = "ISREAD")
    private int isread=0;
    @Generated(hash = 327708835)
    public MsgListModel(Long id, String msgtitle, String msgdiscription,
            String messTypeCode, String dataId, int isread) {
        this.id = id;
        this.msgtitle = msgtitle;
        this.msgdiscription = msgdiscription;
        this.messTypeCode = messTypeCode;
        this.dataId = dataId;
        this.isread = isread;
    }

    @Generated(hash = 596485860)
    public MsgListModel() {
    }

    @Override
    public String toString() {
        return "MsgListModel{" +

                ", msgtitle='" + msgtitle + '\'' +
                ", msgdiscription='" + msgdiscription + '\'' +
                ", messTypeCode='" + messTypeCode + '\'' +
                ", dataId='" + dataId + '\'' +
                '}';
    }



    public String getMsgtitle() {
        return this.msgtitle;
    }

    public void setMsgtitle(String msgtitle) {
        this.msgtitle = msgtitle;
    }

    public String getMsgdiscription() {
        return this.msgdiscription;
    }

    public void setMsgdiscription(String msgdiscription) {
        this.msgdiscription = msgdiscription;
    }

    public String getMessTypeCode() {
        return this.messTypeCode;
    }

    public void setMessTypeCode(String messTypeCode) {
        this.messTypeCode = messTypeCode;
    }

    public String getDataId() {
        return this.dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getIsread() {
        return this.isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
