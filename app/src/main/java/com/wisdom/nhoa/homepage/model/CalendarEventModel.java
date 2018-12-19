package com.wisdom.nhoa.homepage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * @authorzhanglichao
 * @date2018/3/28 9:57
 * @package_name com.wisdom.nhoa.homepage.model
 */
@Entity
public class CalendarEventModel {
    @Id(autoincrement = true)
    private Long sqid;
    @Property(nameInDb = "SCHEDULEID")
    private String id;
    @Property(nameInDb = "DATETXT")
    private String datetxt;
    @Property(nameInDb = "STARTTIME")
    private String starttime;
    @Property(nameInDb = "ENDTIME")
    private String endtime;
    @Property(nameInDb = "CALENDARID")
    private int calendarid;
    @Property(nameInDb = "TITLE")
    private String title;
    @Property(nameInDb = "ISALLDAY")
    private boolean isallday;
    @Property(nameInDb = "LOCATION")
    private String location;
    public boolean  getIsallday() {
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Generated(hash = 1353600764)
    public CalendarEventModel(Long sqid, String id, String datetxt, String starttime, String endtime,
            int calendarid, String title, boolean isallday, String location) {
        this.sqid = sqid;
        this.id = id;
        this.datetxt = datetxt;
        this.starttime = starttime;
        this.endtime = endtime;
        this.calendarid = calendarid;
        this.title = title;
        this.isallday = isallday;
        this.location = location;
    }

    @Generated(hash = 90097044)
    public CalendarEventModel() {
    }
    public void setDatetxt(String datetxt) {
        this.datetxt = datetxt;
    }
    public String getStarttime() {
        return this.starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String getEndtime() {
        return this.endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public int getCalendarid() {
        return this.calendarid;
    }
    public void setCalendarid(int calendarid) {
        this.calendarid = calendarid;
    }

    @Override
    public String toString() {
        return "CalendarEventModel{" +
                "sqid=" + sqid +
                ", id='" + id + '\'' +
                ", datetxt='" + datetxt + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", content='" + calendarid + '\'' +
                '}';
    }

    public String getDatetxt() {
        return this.datetxt;
    }
    public Long getSqid() {
        return this.sqid;
    }
    public void setSqid(Long sqid) {
        this.sqid = sqid;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
