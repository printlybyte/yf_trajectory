package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：IsLeaveStatusBean
 * 创建人：liuguodong
 * 创建时间：2019/10/25 18:16
 * ============================================
 **/
public class IsLeaveStatusBean {


    /**
     * code : 200
     * message : 操作成功！
     * data : {"id":"9ed10be3ea484a5db69872b5ee7e5ddb","createTime":"2020-02-24 10:21:31","updateTime":"2020-02-24 10:21:31","createBy":"35904e8fd4b04a6d91aff3406a7ae322","updateBy":"35904e8fd4b04a6d91aff3406a7ae322","state":"1","sort":"","delFlag":"0","ids":"","orderBy":"","userId":"35904e8fd4b04a6d91aff3406a7ae322","startTime":"2020-02-24 10:22:00","endTime":"2020-02-26 00:00:00","matter":"请假","remark":"哦哦哦"}
     * now : 2020-02-24 10:51:46
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
         * id : 9ed10be3ea484a5db69872b5ee7e5ddb
         * createTime : 2020-02-24 10:21:31
         * updateTime : 2020-02-24 10:21:31
         * createBy : 35904e8fd4b04a6d91aff3406a7ae322
         * updateBy : 35904e8fd4b04a6d91aff3406a7ae322
         * state : 1
         * sort :
         * delFlag : 0
         * ids :
         * orderBy :
         * userId : 35904e8fd4b04a6d91aff3406a7ae322
         * startTime : 2020-02-24 10:22:00
         * endTime : 2020-02-26 00:00:00
         * matter : 请假
         * remark : 哦哦哦
         */

        private String id;
        private String createTime;
        private String updateTime;
        private String createBy;
        private String updateBy;
        private String state;
        private String sort;
        private String delFlag;
        private String ids;
        private String orderBy;
        private String userId;
        private String startTime;
        private String endTime;
        private String matter;
        private String remark;

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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getMatter() {
            return matter;
        }

        public void setMatter(String matter) {
            this.matter = matter;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
