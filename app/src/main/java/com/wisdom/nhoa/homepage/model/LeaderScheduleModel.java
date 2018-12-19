package com.wisdom.nhoa.homepage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @authorzhanglichao
 * @date2018/7/20 16:18
 * @package_name com.wisdom.nhoa.homepage.model
 */
@Entity
public class LeaderScheduleModel {

    /**
     * endtime : 2018-07-16 12:00:00
     * content : 审批人员三
     * id : a187a3c69c564acabf1ec3b7fdbaf059
     * username : 秘书长
     * title : 审批人员三
     * starttime : 2018-07-13 08:02:02
     * ownername : 审批人员三
     */
   @Property(nameInDb = "ENDTIME")
    private String endtime;
    @Property(nameInDb = "CALENDARID")
    private int calendarid;
    @Property(nameInDb = "ID")
    private String id;
    @Property(nameInDb = "USERNAME")
    private String username;
    @Property(nameInDb = "TITLE")
    private String title;
    @Property(nameInDb = "STARTTIME")
    private String starttime;
    @Property(nameInDb = "OWNERNAME")
    private String ownername;
    @Property(nameInDb = "DATETXT")
    private String datetxt;
    @Property(nameInDb = "ISALLDAY")
    private boolean isallday;
    @Property(nameInDb = "LOCATION")
    private String location;
    @Generated(hash = 1794954919)
    public LeaderScheduleModel(String endtime, int calendarid, String id,
            String username, String title, String starttime, String ownername,
            String datetxt, boolean isallday, String location) {
        this.endtime = endtime;
        this.calendarid = calendarid;
        this.id = id;
        this.username = username;
        this.title = title;
        this.starttime = starttime;
        this.ownername = ownername;
        this.datetxt = datetxt;
        this.isallday = isallday;
        this.location = location;
    }

    @Generated(hash = 1172680884)
    public LeaderScheduleModel() {
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getCalendarid() {
        return calendarid;
    }

    public void setCalendarid(int calendarid) {
        this.calendarid = calendarid;
    }

    public boolean getIsallday() {
        return isallday;
    }

    public void setIsallday(boolean isallday) {
        this.isallday = isallday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getDatetxt() {
        return this.datetxt;
    }

    public void setDatetxt(String datetxt) {
        this.datetxt = datetxt;
    }

    @Override
    public String toString() {
        return "LeaderScheduleModel{" +
                "endtime='" + endtime + '\'' +
                ", calendarid=" + calendarid +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", starttime='" + starttime + '\'' +
                ", ownername='" + ownername + '\'' +
                ", datetxt='" + datetxt + '\'' +
                ", isallday=" + isallday +
                ", location='" + location + '\'' +
                '}';
    }
}
