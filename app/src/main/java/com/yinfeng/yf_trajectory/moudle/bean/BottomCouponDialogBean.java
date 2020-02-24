package com.yinfeng.yf_trajectory.moudle.bean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：BottomCouponDialogBean
 * 创建人：liuguodong
 * 创建时间：2019/10/23 14:08
 * ============================================
 **/
public class BottomCouponDialogBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : [{"id":"248c8d9b36a84887a513586f254cefea","createTime":"2019-10-23 10:54:24","updateTime":"2019-10-23 10:54:24","createBy":"1","updateBy":"1","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","attachments":[],"createUser":"","parentId":"a6e5434329344c5a953d7fab7294e0d5","parentPath":"a6e5434329344c5a953d7fab7294e0d5,248c8d9b36a84887a513586f254cefea,","name":"年假","code":"nianjia","value":"年假","remark":"年假","children":[]},{"id":"f62da15698604b599d17174d5b45e756","createTime":"2019-10-23 10:54:10","updateTime":"2019-10-23 10:54:10","createBy":"1","updateBy":"1","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","attachments":[],"createUser":"","parentId":"a6e5434329344c5a953d7fab7294e0d5","parentPath":"a6e5434329344c5a953d7fab7294e0d5,f62da15698604b599d17174d5b45e756,","name":"丧假","code":"sangjia","value":"丧假","remark":"丧假","children":[]},{"id":"5b6f4dd86a6841e990d201896469dbe0","createTime":"2019-10-23 10:53:56","updateTime":"2019-10-23 10:53:56","createBy":"1","updateBy":"1","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","attachments":[],"createUser":"","parentId":"a6e5434329344c5a953d7fab7294e0d5","parentPath":"a6e5434329344c5a953d7fab7294e0d5,5b6f4dd86a6841e990d201896469dbe0,","name":"婚假","code":"hunjia","value":"婚假","remark":"婚假","children":[]},{"id":"14f835ce710f45e896387903544ab282","createTime":"2019-10-23 10:53:40","updateTime":"2019-10-23 10:53:40","createBy":"1","updateBy":"1","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","attachments":[],"createUser":"","parentId":"a6e5434329344c5a953d7fab7294e0d5","parentPath":"a6e5434329344c5a953d7fab7294e0d5,14f835ce710f45e896387903544ab282,","name":"事假","code":"shijia","value":"事假","remark":"事假","children":[]},{"id":"9f67849420f5482cb4067f867a5bd3e5","createTime":"2019-10-23 10:53:24","updateTime":"2019-10-23 10:53:24","createBy":"1","updateBy":"1","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","attachments":[],"createUser":"","parentId":"a6e5434329344c5a953d7fab7294e0d5","parentPath":"a6e5434329344c5a953d7fab7294e0d5,9f67849420f5482cb4067f867a5bd3e5,","name":"病假","code":"bingjia","value":"病假","remark":"病假","children":[]}]
     * now : 2019-10-23 14:06:40
     * success : true
     */

    private int code;
    private String message;
    private String now;
    private boolean success;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 248c8d9b36a84887a513586f254cefea
         * createTime : 2019-10-23 10:54:24
         * updateTime : 2019-10-23 10:54:24
         * createBy : 1
         * updateBy : 1
         * state : 1
         * sort : 999
         * delFlag : 0
         * ids :
         * orderBy :
         * attachments : []
         * createUser :
         * parentId : a6e5434329344c5a953d7fab7294e0d5
         * parentPath : a6e5434329344c5a953d7fab7294e0d5,248c8d9b36a84887a513586f254cefea,
         * name : 年假
         * code : nianjia
         * value : 年假
         * remark : 年假
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
        private String createUser;
        private String parentId;
        private String parentPath;
        private String name;
        private String code;
        private String value;
        private String remark;
        private boolean selected;
        private List<?> attachments;
        private List<?> children;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

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

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
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

        public List<?> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<?> attachments) {
            this.attachments = attachments;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }
    }
}
