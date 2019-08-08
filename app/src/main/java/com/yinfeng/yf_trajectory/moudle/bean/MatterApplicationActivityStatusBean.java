package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：MatterApplicationActivityStatusBean
 * 创建人：liuguodong
 * 创建时间：2019/8/7 10:06
 * ============================================
 **/
public class MatterApplicationActivityStatusBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : {"apply":{"id":"84e6e3dc6f2b402f962371016daa83e8","createTime":"2019-07-24 18:50:13","updateTime":"2019-07-24 18:50:13","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":999,"delFlag":"0","ids":"","orderBy":"","title":"特殊申请","startTime":"2019-07-24 18:50:13","endTime":"","user":"","userId":"0001A11000000000S8SQ","reason":"吃饭","address":"济南"},"status":"1"}
     * now : 2019-07-24 18:59:13
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
         * apply : {"id":"84e6e3dc6f2b402f962371016daa83e8","createTime":"2019-07-24 18:50:13","updateTime":"2019-07-24 18:50:13","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":999,"delFlag":"0","ids":"","orderBy":"","title":"特殊申请","startTime":"2019-07-24 18:50:13","endTime":"","user":"","userId":"0001A11000000000S8SQ","reason":"吃饭","address":"济南"}
         * status : 1
         */

        private ApplyBean apply;
        private String status;

        public ApplyBean getApply() {
            return apply;
        }

        public void setApply(ApplyBean apply) {
            this.apply = apply;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class ApplyBean {
            /**
             * id : 84e6e3dc6f2b402f962371016daa83e8
             * createTime : 2019-07-24 18:50:13
             * updateTime : 2019-07-24 18:50:13
             * createBy : 0001A11000000000S8SQ
             * updateBy : 0001A11000000000S8SQ
             * state : 0
             * sort : 999
             * delFlag : 0
             * ids :
             * orderBy :
             * title : 特殊申请
             * startTime : 2019-07-24 18:50:13
             * endTime :
             * user :
             * userId : 0001A11000000000S8SQ
             * reason : 吃饭
             * address : 济南
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
            private String title;
            private String startTime;
            private String endTime;
            private String user;
            private String userId;
            private String reason;
            private String address;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
