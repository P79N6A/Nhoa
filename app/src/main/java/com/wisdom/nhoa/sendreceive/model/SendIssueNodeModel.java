package com.wisdom.nhoa.sendreceive.model;

/**
 * @author lxd
 * @ProjectName project： 发文选择节点model
 * @class package：
 * @class describe：SendIssueNodeModel
 * @time 17:14
 * @change
 */
public class SendIssueNodeModel {
    private String nodeid;
    private String nodename;
    private String transitionname;
    private String handle;
    private String nodeflag;

    @Override
    public String toString() {
        return "SendIssueNodeModel{" +
                "nodeid='" + nodeid + '\'' +
                ", nodename='" + nodename + '\'' +
                ", transitionname='" + transitionname + '\'' +
                ", handle='" + handle + '\'' +
                ", nodeflag='" + nodeflag + '\'' +
                '}';
    }

    public String getNodename() {
        return nodename;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    public String getTransitionname() {
        return transitionname;
    }

    public void setTransitionname(String transitionname) {
        this.transitionname = transitionname;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getNodeflag() {
        return nodeflag;
    }

    public void setNodeflag(String nodeflag) {
        this.nodeflag = nodeflag;
    }
}
