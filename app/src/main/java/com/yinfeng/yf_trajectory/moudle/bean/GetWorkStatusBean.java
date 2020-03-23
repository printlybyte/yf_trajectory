package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：GetWorkStatusBean
 * 创建人：liuguodong
 * 创建时间：2019/11/11 10:34
 * ============================================
 **/
public class GetWorkStatusBean {


    /**
     * code : 200
     * message : 操作成功！
     * data : {"isWork":false,"isStartUp":true}
     * now : 2020-03-18 11:43:04
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
         * isWork : false
         * isStartUp : true
         */

        private boolean isWork;
        private boolean isStartUp;

        public boolean isIsWork() {
            return isWork;
        }

        public void setIsWork(boolean isWork) {
            this.isWork = isWork;
        }

        public boolean isIsStartUp() {
            return isStartUp;
        }

        public void setIsStartUp(boolean isStartUp) {
            this.isStartUp = isStartUp;
        }
    }
}
