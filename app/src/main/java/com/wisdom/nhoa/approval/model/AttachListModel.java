package com.wisdom.nhoa.approval.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe： 附件列表
 * @time 2018/3/14 15:24
 * @change
 */

public class AttachListModel implements Serializable {
    private String file_real_name;
    private String file_url;
    private String file_type;
    private String createtime;

    public String getFile_real_name() {
        return file_real_name;
    }

    public void setFile_real_name(String file_real_name) {
        this.file_real_name = file_real_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
