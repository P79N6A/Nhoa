package com.wisdom.nhoa.approval.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.model
 * @class describe： 办公用品列表model
 * @time 2018/7/16 9:18
 * @change
 */
public class OfficeStuffModel implements Serializable {
    private String id;
    private String name;
    private List<ChildBean> child;

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

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    public static class ChildBean implements Serializable {
        private String id;
        private String numbers;
        private String name;
        private String pid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumbers() {
            return numbers;
        }

        public void setNumbers(String numbers) {
            this.numbers = numbers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }
    }
}
