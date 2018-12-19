package com.wisdom.nhoa.mine.model;

import java.io.File;
import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.mine.model
 * @class describe： 获得apk版本信息
 * @time 2018/8/24 9:41
 * @change
 */
public class VersionCodeModel implements Serializable {
    private String versionNum;//版本号
    private String forceUpdate;//是否强制升级【0否】【1是】
    private String fileUrl;//应用安装包存储路径
    private String content;//更新内容
    private String addTime;//发布时间

    @Override
    public String toString() {
        return "VersionCodeModel{" +
                "versionNum='" + versionNum + '\'' +
                ", forceUpdate='" + forceUpdate + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", content='" + content + '\'' +
                ", addTime='" + addTime + '\'' +
                '}';
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
