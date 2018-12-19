package com.wisdom.nhoa.approval.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe：
 * @time 2018/3/14 13:58
 * @change
 */

public class FileToDoListModel implements Serializable {
    public String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
