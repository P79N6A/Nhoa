package com.wisdom.nhoa.approval.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe：组织架构，在提交审批申请的时候进行选择的位置
 * @time 2018/7/18 17:16
 * @change
 */
public class OrganizationModel implements Serializable {
    private String id;
    private String name;
    private List<ChiledBean> chiled;

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

    public List<ChiledBean> getChiled() {
        return chiled;
    }

    public void setChiled(List<ChiledBean> chiled) {
        this.chiled = chiled;
    }

    public static class ChiledBean implements Serializable {
        private String id;
        private String name;
        private String parent_id;

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

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }
    }
}
