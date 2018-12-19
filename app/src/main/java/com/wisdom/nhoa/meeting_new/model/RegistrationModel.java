package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * author：会议签到列表model
 * date: 2018-10-29  lxd
 */
public class RegistrationModel implements Serializable {
        private String createtime;
        private String qrcodeurl;
        private String meetingid;
        private String iscreater;
        private String state;
        private String meetingtopic;
        private String signstate;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getQrcodeurl() {
            return qrcodeurl;
        }

        public void setQrcodeurl(String qrcodeurl) {
            this.qrcodeurl = qrcodeurl;
        }

        public String getMeetingid() {
            return meetingid;
        }

        public void setMeetingid(String meetingid) {
            this.meetingid = meetingid;
        }

        public String getIscreater() {
            return iscreater;
        }

        public void setIscreater(String iscreater) {
            this.iscreater = iscreater;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getMeetingtopic() {
            return meetingtopic;
        }

        public void setMeetingtopic(String meetingtopic) {
            this.meetingtopic = meetingtopic;
        }

        public String getSignstate() {
            return signstate;
        }

        public void setSignstate(String signstate) {
            this.signstate = signstate;
        }
}
