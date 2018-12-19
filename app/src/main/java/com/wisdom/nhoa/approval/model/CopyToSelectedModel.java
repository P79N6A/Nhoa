package com.wisdom.nhoa.approval.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe：存储选中的抄送人信息
 * @time 2018/7/25 10:04
 * @change
 */
public class CopyToSelectedModel implements Serializable {
    private String id;
    private String name;
    private List<Map<Integer, String>> ischeck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<Integer, String>> getIscheck() {
        return ischeck;
    }

    public void setIscheck(List<Map<Integer, String>> ischeck) {
        this.ischeck = ischeck;
    }
}
