package com.yinfeng.yf_trajectory.moudle.bean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：LeaveHistoryActivityBean
 * 创建人：liuguodong
 * 创建时间：2019/11/27 15:00
 * ============================================
 **/
public class LeaveHistoryActivityBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : [{"id":"1a320e2b53ee4fd399e233024a821bd7","createTime":"2019-10-29 18:29:24","updateTime":"2019-11-25 11:51:08","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-10-29 18:30:00","endTime":"2019-10-29 18:40:00","matter":"事假","remark":"啦啦啦"},{"id":"4de529e2e023428e861938bb1452f88c","createTime":"2019-11-27 10:34:28","updateTime":"2019-11-27 10:34:28","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"2","sort":"","delFlag":"0","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-11-28 00:00:00","endTime":"2019-11-29 00:00:00","matter":"事假","remark":"默默哦"},{"id":"4e9fd8ca4ff54f64a79762d4242544cc","createTime":"2019-10-29 18:42:39","updateTime":"2019-11-25 11:51:08","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-10-29 18:43:00","endTime":"2019-10-29 18:54:00","matter":"事假","remark":"我想问一下"},{"id":"5ac0544e655d4688914b3fc9c4d002b6","createTime":"2019-10-29 09:23:43","updateTime":"2019-11-25 11:51:08","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-10-29 09:23:00","endTime":"2019-10-29 09:59:00","matter":"年假","remark":""},{"id":"631bfd8126204e758d25b154537069c7","createTime":"2019-10-09 14:19:49","updateTime":"2019-11-25 11:51:08","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-10-09 00:00:00","endTime":"2019-10-10 00:00:00","matter":"病假","remark":"用户操作"},{"id":"78f487bd92b446b787dcdf22174affd9","createTime":"2019-10-29 19:18:21","updateTime":"2019-11-25 11:51:08","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-10-29 19:19:00","endTime":"2019-10-29 20:00:00","matter":"事假","remark":"哦哦哦"},{"id":"7fefd2445ecf40d3b17b1a1d98f3aab5","createTime":"2019-11-26 14:04:08","updateTime":"2019-11-26 14:04:24","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-11-26 14:04:00","endTime":"2019-11-27 00:00:00","matter":"事假","remark":"娄承春测试取消请假。"},{"id":"9bc3697313ff4de1b7df600a50b1184a","createTime":"2019-11-27 10:34:05","updateTime":"2019-11-27 10:34:15","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-11-27 10:34:00","endTime":"2019-11-28 01:00:00","matter":"事假","remark":"聊了几句"},{"id":"c63eb213d1f149bba164448f20880b7d","createTime":"2019-10-29 18:25:47","updateTime":"2019-11-25 11:51:08","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-10-29 18:25:00","endTime":"2019-10-29 18:28:00","matter":"事假","remark":"测试"},{"id":"cfb9a13b1d0b4fd39e466a54b9c4cdd6","createTime":"2019-11-26 14:07:28","updateTime":"2019-11-26 14:10:31","createBy":"0001A11000000000S8SQ","updateBy":"0001A11000000000S8SQ","state":"0","sort":"","delFlag":"1","ids":"","orderBy":"","user":"","userId":"0001A11000000000S8SQ","startTime":"2019-11-26 14:05:00","endTime":"2019-11-28 00:00:00","matter":"年假","remark":"lcc测试"}]
     * now : 2019-11-28 10:11:39
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
         * id : 1a320e2b53ee4fd399e233024a821bd7
         * createTime : 2019-10-29 18:29:24
         * updateTime : 2019-11-25 11:51:08
         * createBy : 0001A11000000000S8SQ
         * updateBy : 0001A11000000000S8SQ
         * state : 0
         * sort :
         * delFlag : 1
         * ids :
         * orderBy :
         * user :
         * userId : 0001A11000000000S8SQ
         * startTime : 2019-10-29 18:30:00
         * endTime : 2019-10-29 18:40:00
         * matter : 事假
         * remark : 啦啦啦
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
        private String user;
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
