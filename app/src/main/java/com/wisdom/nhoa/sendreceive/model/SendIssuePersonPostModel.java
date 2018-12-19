package com.wisdom.nhoa.sendreceive.model;

/**
 * @author lxd
 * @ProjectName project：代办详情发送model
 * @class package：
 * @class describe：SendIssuePersonPostModel
 * @time 17:18
 * @change
 */

public class SendIssuePersonPostModel {
    private String nodeId;
    private String nodeName;
    private String roleName;
    private String userName;
    private String userId;

    @Override
    public String toString() {
        return "SendIssuePersonPostModel{" +
                "nodeId='" + nodeId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
