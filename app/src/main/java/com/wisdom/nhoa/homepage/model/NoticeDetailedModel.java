package com.wisdom.nhoa.homepage.model;

import java.io.Serializable;

/**
 * @authorzhanglichao
 * @date2018/3/26 9:23
 * @package_name com.wisdom.nhoa.homepage.model
 */

public class NoticeDetailedModel implements Serializable {

    private String noticetitle ;
    private String noticedep ;
    private String noticecontent ;

    public String getNoticetitle() {
        return noticetitle;
    }

    public String getNoticedep() {
        return noticedep;
    }

    public String getNoticecontent() {
        return noticecontent;
    }

    public void setNoticetitle(String noticetitle) {
        this.noticetitle = noticetitle;
    }

    public void setNoticedep(String noticedep) {
        this.noticedep = noticedep;
    }

    public void setNoticecontent(String noticecontent) {
        this.noticecontent = noticecontent;
    }

    @Override
    public String toString() {
        return "NoticeDetailedModel{" +
                "noticetitle='" + noticetitle + '\'' +
                ", noticedep='" + noticedep + '\'' +
                ", noticecontent='" + noticecontent + '\'' +
                '}';
    }
}
