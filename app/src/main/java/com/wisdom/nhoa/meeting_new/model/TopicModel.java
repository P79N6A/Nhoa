package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：会议议题model
 * @time 2018/11/9 10:30
 * @change
 */
public class TopicModel implements Serializable {
    private String issueName;
    private String issueFileUrl;
    private String issuePersons;

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueFileUrl() {
        return issueFileUrl;
    }

    public void setIssueFileUrl(String issueFileUrl) {
        this.issueFileUrl = issueFileUrl;
    }

    public String getIssuePersons() {
        return issuePersons;
    }

    public void setIssuePersons(String issuePersons) {
        this.issuePersons = issuePersons;
    }
}
