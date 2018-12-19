package com.wisdom.nhoa.sendreceive.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.sendreceive.model
 * @class describe： 构造发送数据接口的大json
 * @time 2018/3/28 19:07
 * @change
 */

public class FormIdValueModel implements Serializable {
    private String form_id;
    private String form_value;

    @Override
    public String toString() {
        return "FormIdValueModel{" +
                "form_id='" + form_id + '\'' +
                ", form_value='" + form_value + '\'' +
                '}';
    }


    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getForm_value() {
        return form_value;
    }

    public void setForm_value(String form_value) {
        this.form_value = form_value;
    }
}
