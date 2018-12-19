package com.wisdom.nhoa.supervision.model;

import java.io.Serializable;
import java.util.List;

/**
 * @authorzhanglichao
 * @date2018/7/25 13:27
 * @package_name com.wisdom.nhoa.supervision.model
 */
public class SupervisionModel implements Serializable{


    /**
     * child : [{"content":"","addendumid":"197a9c1242b942698d925b33bcfd4961","username":"审批人员一","edittime":"","userid":"44D084D5-C1E3-4808-9408-0C862C7EED7A","userpercentage":"0","frequency":0,"supervisionid":"1c866b6226474db5a5a54b4cbfb44fac","worktime":""},{"content":"测试添加百分比1","addendumid":"e598f7d2c8c24b819788d49d2943d56c","username":"秘书长","edittime":"2018-07-22 09:49:50","userid":"118FFA73-910C-4E90-B192-8D718320DB8C","userpercentage":"50","frequency":3,"supervisionid":"1c866b6226474db5a5a54b4cbfb44fac","worktime":"2018-01-01 - 2018-01-05"}]
     * finishtime :
     * createtime : 2018-07-21 23:40:46
     * content : 测试接口修改1
     * endtime : 2018-01-05
     * id : 1c866b6226474db5a5a54b4cbfb44fac
     * username : 秘书长
     * status : 0
     * percentage : 25.00
     * begintime : 2018-01-01
     * name : 测试接口修改1
     */

    private String finishtime;
    private String createtime;
    private String content;
    private String endtime;
    private String id;
    private String username;
    private String status;
    private String percentage;
    private String begintime;
    private String name;
    private List<ChildBean> child;

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
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

    public static class ChildBean implements  Serializable{
        /**
         * content :
         * addendumid : 197a9c1242b942698d925b33bcfd4961
         * username : 审批人员一
         * edittime :
         * userid : 44D084D5-C1E3-4808-9408-0C862C7EED7A
         * userpercentage : 0
         * frequency : 0
         * supervisionid : 1c866b6226474db5a5a54b4cbfb44fac
         * worktime :
         */

        private String content;
        private String addendumid;
        private String username;
        private String edittime;
        private String userid;
        private String userpercentage;
        private int frequency;
        private String supervisionid;
        private String worktime;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddendumid() {
            return addendumid;
        }

        public void setAddendumid(String addendumid) {
            this.addendumid = addendumid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEdittime() {
            return edittime;
        }

        public void setEdittime(String edittime) {
            this.edittime = edittime;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUserpercentage() {
            return userpercentage;
        }

        public void setUserpercentage(String userpercentage) {
            this.userpercentage = userpercentage;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public String getSupervisionid() {
            return supervisionid;
        }

        public void setSupervisionid(String supervisionid) {
            this.supervisionid = supervisionid;
        }

        public String getWorktime() {
            return worktime;
        }

        public void setWorktime(String worktime) {
            this.worktime = worktime;
        }
    }

    @Override
    public String toString() {
        return "SupervisionModel{" +
                "finishtime='" + finishtime + '\'' +
                ", createtime='" + createtime + '\'' +
                ", content='" + content + '\'' +
                ", endtime='" + endtime + '\'' +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                ", percentage='" + percentage + '\'' +
                ", begintime='" + begintime + '\'' +
                ", name='" + name + '\'' +
                ", child=" + child +
                '}';
    }
}
