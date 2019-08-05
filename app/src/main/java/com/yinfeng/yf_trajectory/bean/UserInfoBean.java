package com.yinfeng.yf_trajectory.bean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.bean
 * 类  名：UserInfoBean
 * 创建人：liuguodong
 * 创建时间：2019/8/2 14:17
 * ============================================
 **/
public class UserInfoBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : {"idCard":"371323199305119156","orderBy":null,"updateTime":"2019-06-14 13:42:44","remark":null,"sort":999,"createTime_str":"2019-06-14 13:39:43","delFlag":"0","updateTime_date_str":"2019-06-14","createBy":null,"phone":"15806699526","createTime":"2019-06-14 13:39:43","updateBy":null,"name":"陈刚","updateTime_str":"2019-06-14 13:42:44","ids":null,"sysAttachments":[],"company":"银丰电子商务有限公司","id":"0001A11000000000MU3E","state":"1","department":"技术部","job":"Java开发工程师","createTime_date_str":"2019-06-14"}
     * now : 2019-06-17 11:26:07
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
         * idCard : 371323199305119156
         * orderBy : null
         * updateTime : 2019-06-14 13:42:44
         * remark : null
         * sort : 999
         * createTime_str : 2019-06-14 13:39:43
         * delFlag : 0
         * updateTime_date_str : 2019-06-14
         * createBy : null
         * phone : 15806699526
         * createTime : 2019-06-14 13:39:43
         * updateBy : null
         * name : 陈刚
         * updateTime_str : 2019-06-14 13:42:44
         * ids : null
         * sysAttachments : []
         * company : 银丰电子商务有限公司
         * id : 0001A11000000000MU3E
         * state : 1
         * department : 技术部
         * job : Java开发工程师
         * createTime_date_str : 2019-06-14
         */

        private String idCard;
        private Object orderBy;
        private String updateTime;
        private Object remark;
        private int sort;
        private String createTime_str;
        private String delFlag;
        private String updateTime_date_str;
        private Object createBy;
        private String phone;
        private String createTime;
        private Object updateBy;
        private String name;
        private String updateTime_str;
        private Object ids;
        private String company;
        private String id;
        private String state;
        private String department;
        private String job;
        private String createTime_date_str;
        private List<?> sysAttachments;

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
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

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getCreateTime_str() {
            return createTime_str;
        }

        public void setCreateTime_str(String createTime_str) {
            this.createTime_str = createTime_str;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getUpdateTime_date_str() {
            return updateTime_date_str;
        }

        public void setUpdateTime_date_str(String updateTime_date_str) {
            this.updateTime_date_str = updateTime_date_str;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
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

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getCreateTime_date_str() {
            return createTime_date_str;
        }

        public void setCreateTime_date_str(String createTime_date_str) {
            this.createTime_date_str = createTime_date_str;
        }

        public List<?> getSysAttachments() {
            return sysAttachments;
        }

        public void setSysAttachments(List<?> sysAttachments) {
            this.sysAttachments = sysAttachments;
        }
    }
}
