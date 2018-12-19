package com.wisdom.nhoa.sendreceive.model;

import java.io.Serializable;

/**
 * @author lxd
 * @ProjectName project： 代办详情mudel
 * @class package：
 * @class describe：ToDoModel
 * @time 18:23
 * @change
 */

public class ToDoModel implements Serializable{
//    private String insid;
    private String drafter;
    private String createTime;
    private String nodeName;
    private String nodeId;
    private String processName;
    private String processKey;
    private String processTag;
    private String nodeType;

//    public String getInsid() {
//        return insid;
//    }
//
//    public void setInsid(String insid) {

    @Override
    public String toString() {
        return "ToDoModel{" +
                "drafter='" + drafter + '\'' +
                ", createTime='" + createTime + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", processName='" + processName + '\'' +
                ", processKey='" + processKey + '\'' +
                ", processTag='" + processTag + '\'' +
                ", nodeType='" + nodeType + '\'' +
                '}';
    }
//        this.insid = insid;
//    }

    public String getDrafter() {
        return drafter;
    }

    public void setDrafter(String drafter) {
        this.drafter = drafter;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getProcessTag() {
        return processTag;
    }

    public void setProcessTag(String processTag) {
        this.processTag = processTag;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
