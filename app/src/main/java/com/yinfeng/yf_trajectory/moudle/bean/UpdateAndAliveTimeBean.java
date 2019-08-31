package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：UpdateAndAliveTimeBean
 * 创建人：liuguodong
 * 创建时间：2019/8/22 17:24
 * ============================================
 **/
public class UpdateAndAliveTimeBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : {"updateMinter":"1","helpKeepAliveMinter":"3","helpUpdateMinter":"1","keepAliveMinter":"3","zhushouPullTime":"1","updateHour":"5","helpUpdateHour":"7","keepAliveHour":"5","helpKeepAliveHour":"7"}
     * now : 2019-08-25 19:17:47
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
         * updateMinter : 1
         * helpKeepAliveMinter : 3
         * helpUpdateMinter : 1
         * keepAliveMinter : 3
         * zhushouPullTime : 1
         * updateHour : 5
         * helpUpdateHour : 7
         * keepAliveHour : 5
         * helpKeepAliveHour : 7
         */

        private String updateMinter;
        private String helpKeepAliveMinter;
        private String helpUpdateMinter;
        private String keepAliveMinter;
        private String zhushouPullTime;
        private String updateHour;
        private String helpUpdateHour;
        private String keepAliveHour;
        private String helpKeepAliveHour;

        public String getUpdateMinter() {
            return updateMinter;
        }

        public void setUpdateMinter(String updateMinter) {
            this.updateMinter = updateMinter;
        }

        public String getHelpKeepAliveMinter() {
            return helpKeepAliveMinter;
        }

        public void setHelpKeepAliveMinter(String helpKeepAliveMinter) {
            this.helpKeepAliveMinter = helpKeepAliveMinter;
        }

        public String getHelpUpdateMinter() {
            return helpUpdateMinter;
        }

        public void setHelpUpdateMinter(String helpUpdateMinter) {
            this.helpUpdateMinter = helpUpdateMinter;
        }

        public String getKeepAliveMinter() {
            return keepAliveMinter;
        }

        public void setKeepAliveMinter(String keepAliveMinter) {
            this.keepAliveMinter = keepAliveMinter;
        }

        public String getZhushouPullTime() {
            return zhushouPullTime;
        }

        public void setZhushouPullTime(String zhushouPullTime) {
            this.zhushouPullTime = zhushouPullTime;
        }

        public String getUpdateHour() {
            return updateHour;
        }

        public void setUpdateHour(String updateHour) {
            this.updateHour = updateHour;
        }

        public String getHelpUpdateHour() {
            return helpUpdateHour;
        }

        public void setHelpUpdateHour(String helpUpdateHour) {
            this.helpUpdateHour = helpUpdateHour;
        }

        public String getKeepAliveHour() {
            return keepAliveHour;
        }

        public void setKeepAliveHour(String keepAliveHour) {
            this.keepAliveHour = keepAliveHour;
        }

        public String getHelpKeepAliveHour() {
            return helpKeepAliveHour;
        }

        public void setHelpKeepAliveHour(String helpKeepAliveHour) {
            this.helpKeepAliveHour = helpKeepAliveHour;
        }
    }
}
