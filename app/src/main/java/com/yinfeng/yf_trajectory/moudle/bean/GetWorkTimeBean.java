package com.yinfeng.yf_trajectory.moudle.bean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：GetWorkTimeBean
 * 创建人：liuguodong
 * 创建时间：2019/10/25 10:18
 * ============================================
 **/
public class GetWorkTimeBean {


    /**
     * code : 200
     * message : 操作成功！
     * data : {"id":"e0a260fee11545da90367775ff26ead0","createTime":"2019-10-23 11:48:48","updateTime":"2019-10-23 11:48:48","createBy":"","updateBy":"","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","parentId":"1f452be354e1487e9a700061fabc0a26","parentPath":"1,d00c549283074754a844b2620404219c,1f452be354e1487e9a700061fabc0a26,e0a260fee11545da90367775ff26ead0,","name":"出纳","code":"chun","startTime":"07:30","endTime":"18:30","startHour":"07","endHour":"18","startMinute":"30","endMinute":"30","value":"","remark":"","type":"DEFAULT","isWork":1,"children":[]}
     * now : 2019-10-28 11:53:46
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
         * id : e0a260fee11545da90367775ff26ead0
         * createTime : 2019-10-23 11:48:48
         * updateTime : 2019-10-23 11:48:48
         * createBy :
         * updateBy :
         * state : 1
         * sort : 999
         * delFlag : 0
         * ids :
         * orderBy :
         * parentId : 1f452be354e1487e9a700061fabc0a26
         * parentPath : 1,d00c549283074754a844b2620404219c,1f452be354e1487e9a700061fabc0a26,e0a260fee11545da90367775ff26ead0,
         * name : 出纳
         * code : chun
         * startTime : 07:30
         * endTime : 18:30
         * startHour : 07
         * endHour : 18
         * startMinute : 30
         * endMinute : 30
         * value :
         * remark :
         * type : DEFAULT
         * isWork : 1
         * children : []
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
        private String parentId;
        private String parentPath;
        private String name;
        private String code;
        private String startTime;
        private String endTime;
        private String startHour;
        private String endHour;
        private String startMinute;
        private String endMinute;
        private String value;
        private String remark;
        private String type;
        private int isWork;
        private List<?> children;

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

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getParentPath() {
            return parentPath;
        }

        public void setParentPath(String parentPath) {
            this.parentPath = parentPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public String getStartHour() {
            return startHour;
        }

        public void setStartHour(String startHour) {
            this.startHour = startHour;
        }

        public String getEndHour() {
            return endHour;
        }

        public void setEndHour(String endHour) {
            this.endHour = endHour;
        }

        public String getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(String startMinute) {
            this.startMinute = startMinute;
        }

        public String getEndMinute() {
            return endMinute;
        }

        public void setEndMinute(String endMinute) {
            this.endMinute = endMinute;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIsWork() {
            return isWork;
        }

        public void setIsWork(int isWork) {
            this.isWork = isWork;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }
    }
}
