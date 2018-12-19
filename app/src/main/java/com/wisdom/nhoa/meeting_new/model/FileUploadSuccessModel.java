package com.wisdom.nhoa.meeting_new.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.model
 * @class describe：文件上传成功返回的model
 * @time 2018/11/12 9:48
 * @change
 */
public class FileUploadSuccessModel implements Serializable{


    /**
     * results : {"fileName":"IMG_20181111_101001.jpg","fileUrl":"reception/upload/a7266359d68f4c998cfda6d7f803cf3a/"}
     * error_code : 0
     * error_msg : ok
     */

    private ResultsBean results;
    private int error_code;
    private String error_msg;

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public static class ResultsBean {
        /**
         * fileName : IMG_20181111_101001.jpg
         * fileUrl : reception/upload/a7266359d68f4c998cfda6d7f803cf3a/
         */

        private String fileName;
        private String fileUrl;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
}
