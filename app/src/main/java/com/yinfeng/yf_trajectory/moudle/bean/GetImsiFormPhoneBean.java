package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：GetImsiFormPhoneBean
 * 创建人：liuguodong
 * 创建时间：2019/9/9 14:44
 * ============================================
 **/
public class GetImsiFormPhoneBean {


    /**
     * code : 200
     * message : 操作成功！
     * data : {"id":"","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","simCode":"","phone":""}
     * now : 2019-09-09 15:24:42
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
         * id :
         * createTime :
         * updateTime :
         * createBy :
         * updateBy :
         * state :
         * sort :
         * delFlag : 0
         * ids :
         * orderBy :
         * simCode :
         * phone :
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
        private String simCode;
        private String phone;

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

        public String getSimCode() {
            return simCode;
        }

        public void setSimCode(String simCode) {
            this.simCode = simCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
