package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：ApkDownloadBean
 * 创建人：liuguodong
 * 创建时间：2019/8/14 18:38
 * ============================================
 **/
public class ApkDownloadBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : {"appVersion":{"id":"7","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","downLoadUrl":"new","updateLog":"助手1.1","versionCode":"2","versionName":"1.0.1","appType":"help"},"newest":false}
     * now : 2019-08-25 19:09:46
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
         * appVersion : {"id":"7","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","downLoadUrl":"new","updateLog":"助手1.1","versionCode":"2","versionName":"1.0.1","appType":"help"}
         * newest : false
         */

        private AppVersionBean appVersion;
        private boolean newest;

        public AppVersionBean getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(AppVersionBean appVersion) {
            this.appVersion = appVersion;
        }

        public boolean isNewest() {
            return newest;
        }

        public void setNewest(boolean newest) {
            this.newest = newest;
        }

        public static class AppVersionBean {
            /**
             * id : 7
             * createTime :
             * updateTime :
             * createBy :
             * updateBy :
             * state : 1
             * sort : 999
             * delFlag : 0
             * ids :
             * orderBy :
             * downLoadUrl : new
             * updateLog : 助手1.1
             * versionCode : 2
             * versionName : 1.0.1
             * appType : help
             */

            private String id;
            private String createTime;
            private String updateTime;
            private String createBy;
            private String updateBy;
            private String state;
            private int sort;
            private String delFlag;
            private String ids;
            private String orderBy;
            private String downLoadUrl;
            private String updateLog;
            private String versionCode;
            private String versionName;
            private String appType;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getDelFlag() {
                return delFlag;
            }

            public void setDelFlag(String delFlag) {
                this.delFlag = delFlag;
            }

            public String getIds() {
                return ids;
            }

            public void setIds(String ids) {
                this.ids = ids;
            }

            public String getOrderBy() {
                return orderBy;
            }

            public void setOrderBy(String orderBy) {
                this.orderBy = orderBy;
            }

            public String getDownLoadUrl() {
                return downLoadUrl;
            }

            public void setDownLoadUrl(String downLoadUrl) {
                this.downLoadUrl = downLoadUrl;
            }

            public String getUpdateLog() {
                return updateLog;
            }

            public void setUpdateLog(String updateLog) {
                this.updateLog = updateLog;
            }

            public String getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(String versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getAppType() {
                return appType;
            }

            public void setAppType(String appType) {
                this.appType = appType;
            }
        }
    }
}
