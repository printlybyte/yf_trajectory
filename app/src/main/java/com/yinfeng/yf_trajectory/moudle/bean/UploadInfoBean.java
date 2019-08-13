package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：UploadInfoBean
 * 创建人：liuguodong
 * 创建时间：2019/8/9 18:10
 * ============================================
 **/
public class UploadInfoBean {

       /**
     * code : 200
     * message : 操作成功！
     * data : {"upload":"600","grap":"30"}
     * now : 2019-07-30 14:06:48
     * success : true
     */

    private int code;
    private String message;
    private DataBean data;
    private String now;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * upload : 600
         * grap : 30
         */

        private String upload;
        private String grap;

        public String getUpload() {
            return upload;
        }

        public void setUpload(String upload) {
            this.upload = upload;
        }

        public String getGrap() {
            return grap;
        }

        public void setGrap(String grap) {
            this.grap = grap;
        }
    }
}
