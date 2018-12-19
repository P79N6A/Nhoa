package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：
 * @time 2018/11/7 10:52
 * @change
 */
public class MeetingDetailNewModel implements Serializable {
    private String accMan;
    private String convokedep;
    private String endtime;
    private String starttime;
    private String memberid;
    private String linkTel;
    private String bdrmAdd;
    private String state;
    private String meetingtitle;
    private String master;
    private String attachname;
    private String bdrmName;
    private String createtime;
    private String attachurl;
    private String compere;
    private String meetingcontent;
    private String logMan;
    private String linkman;
    private String notAccMan;
    private String note;
    private List<IssuesBean> issues;

    public String getAccMan() {
        return accMan;
    }

    public void setAccMan(String accMan) {
        this.accMan = accMan;
    }

    public String getConvokedep() {
        return convokedep;
    }

    public void setConvokedep(String convokedep) {
        this.convokedep = convokedep;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getBdrmAdd() {
        return bdrmAdd;
    }

    public void setBdrmAdd(String bdrmAdd) {
        this.bdrmAdd = bdrmAdd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMeetingtitle() {
        return meetingtitle;
    }

    public void setMeetingtitle(String meetingtitle) {
        this.meetingtitle = meetingtitle;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getAttachname() {
        return attachname;
    }

    public void setAttachname(String attachname) {
        this.attachname = attachname;
    }

    public String getBdrmName() {
        return bdrmName;
    }

    public void setBdrmName(String bdrmName) {
        this.bdrmName = bdrmName;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getAttachurl() {
        return attachurl;
    }

    public void setAttachurl(String attachurl) {
        this.attachurl = attachurl;
    }

    public String getCompere() {
        return compere;
    }

    public void setCompere(String compere) {
        this.compere = compere;
    }

    public String getMeetingcontent() {
        return meetingcontent;
    }

    public void setMeetingcontent(String meetingcontent) {
        this.meetingcontent = meetingcontent;
    }

    public String getLogMan() {
        return logMan;
    }

    public void setLogMan(String logMan) {
        this.logMan = logMan;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getNotAccMan() {
        return notAccMan;
    }

    public void setNotAccMan(String notAccMan) {
        this.notAccMan = notAccMan;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<IssuesBean> getIssues() {
        return issues;
    }

    public void setIssues(List<IssuesBean> issues) {
        this.issues = issues;
    }

    public static class IssuesBean implements Serializable {
        private String ssuesFileUrl;
        private String ssuesId;
        private String ssuesName;
        private String ssuesFileName;
        private String ytry;

        public String getSsuesFileUrl() {
            return ssuesFileUrl;
        }

        public void setSsuesFileUrl(String ssuesFileUrl) {
            this.ssuesFileUrl = ssuesFileUrl;
        }

        public String getSsuesId() {
            return ssuesId;
        }

        public void setSsuesId(String ssuesId) {
            this.ssuesId = ssuesId;
        }

        public String getSsuesName() {
            return ssuesName;
        }

        public void setSsuesName(String ssuesName) {
            this.ssuesName = ssuesName;
        }

        public String getSsuesFileName() {
            return ssuesFileName;
        }

        public void setSsuesFileName(String ssuesFileName) {
            this.ssuesFileName = ssuesFileName;
        }

        public String getYtry() {
            return ytry;
        }

        public void setYtry(String ytry) {
            this.ytry = ytry;
        }
    }
}
