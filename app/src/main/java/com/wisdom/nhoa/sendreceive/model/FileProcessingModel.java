package com.wisdom.nhoa.sendreceive.model;

/**
 * @author lxd
 * @ProjectName project：文件处理单model
 * @class package：
 * @class describe：FileProcessingModel
 * @time 17:14
 * @change
 */

public class FileProcessingModel {
    private String form_id;
    private String value_type;
    private String form_value;
    private String form_name;

    @Override
    public String toString() {
        return "FileProcessingModel{" +
                "form_id='" + form_id + '\'' +
                ", value_type='" + value_type + '\'' +
                ", form_value='" + form_value + '\'' +
                ", form_name='" + form_name + '\'' +
                '}';
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getValue_type() {
        return value_type;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    public String getForm_value() {
        return form_value;
    }

    public void setForm_value(String form_value) {
        this.form_value = form_value;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }


}
