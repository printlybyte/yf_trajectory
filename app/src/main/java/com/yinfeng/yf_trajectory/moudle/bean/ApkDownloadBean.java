package com.yinfeng.yf_trajectory.moudle.bean;

import java.util.List;

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
     * data : {"downLoadUrl":"http://qiniu.jyncmall.com/2.0.3.apk","orderBy":null,"updateTime":"2018-12-13 18:01:51","updateLog":"支持在线提现,bug修复123","sort":999,"versionName":"2.02","delFlag":"0","versionCode":"203","updateTime_date_str":"2018-12-13","createBy":"","createTime":null,"updateBy":"","appType":"track","updateTime_str":"2018-12-13 18:01:51","ids":null,"sysAttachments":[],"id":"2","state":"1"}
     * now : 2019-09-17 15:09:03
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
         * downLoadUrl : http://qiniu.jyncmall.com/2.0.3.apk
         * orderBy : null
         * updateTime : 2018-12-13 18:01:51
         * updateLog : 支持在线提现,bug修复123
         * sort : 999
         * versionName : 2.02
         * delFlag : 0
         * versionCode : 203
         * updateTime_date_str : 2018-12-13
         * createBy :
         * createTime : null
         * updateBy :
         * appType : track
         * updateTime_str : 2018-12-13 18:01:51
         * ids : null
         * sysAttachments : []
         * id : 2
         * state : 1
         */

        private String downLoadUrl;
        private Object orderBy;
        private String updateTime;
        private String updateLog;
        private int sort;
        private String versionName;
        private String delFlag;
        private String versionCode;
        private String updateTime_date_str;
        private String createBy;
        private Object createTime;
        private String updateBy;
        private String appType;
        private String updateTime_str;
        private Object ids;
        private String id;
        private String state;
        private List<?> sysAttachments;

        public String getDownLoadUrl() {
            return downLoadUrl;
        }

        public void setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
        }

        public Object getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(Object orderBy) {
            this.orderBy = orderBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateLog() {
            return updateLog;
        }

        public void setUpdateLog(String updateLog) {
            this.updateLog = updateLog;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getUpdateTime_date_str() {
            return updateTime_date_str;
        }

        public void setUpdateTime_date_str(String updateTime_date_str) {
            this.updateTime_date_str = updateTime_date_str;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getUpdateTime_str() {
            return updateTime_str;
        }

        public void setUpdateTime_str(String updateTime_str) {
            this.updateTime_str = updateTime_str;
        }

        public Object getIds() {
            return ids;
        }

        public void setIds(Object ids) {
            this.ids = ids;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<?> getSysAttachments() {
            return sysAttachments;
        }

        public void setSysAttachments(List<?> sysAttachments) {
            this.sysAttachments = sysAttachments;
        }
    }
}
