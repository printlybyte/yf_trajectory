package com.yinfeng.yf_trajectory.moudle.bean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：ContactWorkServiceBean
 * 创建人：liuguodong
 * 创建时间：2020/2/19 16:02
 * ============================================
 **/
public class ContactWorkServiceBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : {"date":"2020-02-19 16:12:19","list":[{"id":"0001A110000000000F8V","createTime":"2019-06-28 10:02:56","updateTime":"2020-01-06 08:43:43","createBy":"","updateBy":"0001A11000000000MU3E","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","name":"魏哲祥","phone":"19953153005","idCard":"370405198209132519","iccid":"8986031974531306124","department":"综合管理部","company":"山东银丰投资集团有限公司","job":"行政司机","remark":"","lastLoginTime":"2020-01-06 08:43:43","appVersion":"209","helpVersion":"206","oldPhone":"","lastTrackTime":"2020-01-08 16:03:51","postId":"63800e53d7574bb184952159786227c2","post":{"id":"63800e53d7574bb184952159786227c2","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","parentId":"","parentPath":"","name":"","code":"","weekend":"","holiday":"","startTime":"","endTime":"","startHour":"","endHour":"","startMinute":"","endMinute":"","value":"","remark":"","type":"","datumPoint":"","isWork":0,"label":"","pid":"","children":[]},"isHr":"","addrBookTime":""},{"id":"0001A110000000000HTN","createTime":"2019-06-28 10:02:56","updateTime":"2020-02-14 09:16:56","createBy":"","updateBy":"0001A11000000000MU3E","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","name":"刘祥","phone":"19953153030","idCard":"370121196911247425","iccid":"8986031974531306063","department":"资产运营部","company":"山东银丰投资集团有限公司","job":"资产管理部总经理","remark":"","lastLoginTime":"2020-02-14 09:16:56","appVersion":"209","helpVersion":"206","oldPhone":"15863155000","lastTrackTime":"2020-02-18 18:35:23","postId":"195deee41c6b4279b2ab0db368569c4e","post":{"id":"195deee41c6b4279b2ab0db368569c4e","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","parentId":"","parentPath":"","name":"","code":"","weekend":"","holiday":"","startTime":"","endTime":"","startHour":"","endHour":"","startMinute":"","endMinute":"","value":"","remark":"","type":"","datumPoint":"","isWork":0,"label":"","pid":"","children":[]},"isHr":"","addrBookTime":""}]}
     * now : 2020-02-19 16:12:19
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
         * date : 2020-02-19 16:12:19
         * list : [{"id":"0001A110000000000F8V","createTime":"2019-06-28 10:02:56","updateTime":"2020-01-06 08:43:43","createBy":"","updateBy":"0001A11000000000MU3E","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","name":"魏哲祥","phone":"19953153005","idCard":"370405198209132519","iccid":"8986031974531306124","department":"综合管理部","company":"山东银丰投资集团有限公司","job":"行政司机","remark":"","lastLoginTime":"2020-01-06 08:43:43","appVersion":"209","helpVersion":"206","oldPhone":"","lastTrackTime":"2020-01-08 16:03:51","postId":"63800e53d7574bb184952159786227c2","post":{"id":"63800e53d7574bb184952159786227c2","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","parentId":"","parentPath":"","name":"","code":"","weekend":"","holiday":"","startTime":"","endTime":"","startHour":"","endHour":"","startMinute":"","endMinute":"","value":"","remark":"","type":"","datumPoint":"","isWork":0,"label":"","pid":"","children":[]},"isHr":"","addrBookTime":""},{"id":"0001A110000000000HTN","createTime":"2019-06-28 10:02:56","updateTime":"2020-02-14 09:16:56","createBy":"","updateBy":"0001A11000000000MU3E","state":"1","sort":999,"delFlag":"0","ids":"","orderBy":"","name":"刘祥","phone":"19953153030","idCard":"370121196911247425","iccid":"8986031974531306063","department":"资产运营部","company":"山东银丰投资集团有限公司","job":"资产管理部总经理","remark":"","lastLoginTime":"2020-02-14 09:16:56","appVersion":"209","helpVersion":"206","oldPhone":"15863155000","lastTrackTime":"2020-02-18 18:35:23","postId":"195deee41c6b4279b2ab0db368569c4e","post":{"id":"195deee41c6b4279b2ab0db368569c4e","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","parentId":"","parentPath":"","name":"","code":"","weekend":"","holiday":"","startTime":"","endTime":"","startHour":"","endHour":"","startMinute":"","endMinute":"","value":"","remark":"","type":"","datumPoint":"","isWork":0,"label":"","pid":"","children":[]},"isHr":"","addrBookTime":""}]
         */

        private String date;
        private List<ListBean> list;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 0001A110000000000F8V
             * createTime : 2019-06-28 10:02:56
             * updateTime : 2020-01-06 08:43:43
             * createBy :
             * updateBy : 0001A11000000000MU3E
             * state : 1
             * sort : 999
             * delFlag : 0
             * ids :
             * orderBy :
             * name : 魏哲祥
             * phone : 19953153005
             * idCard : 370405198209132519
             * iccid : 8986031974531306124
             * department : 综合管理部
             * company : 山东银丰投资集团有限公司
             * job : 行政司机
             * remark :
             * lastLoginTime : 2020-01-06 08:43:43
             * appVersion : 209
             * helpVersion : 206
             * oldPhone :
             * lastTrackTime : 2020-01-08 16:03:51
             * postId : 63800e53d7574bb184952159786227c2
             * post : {"id":"63800e53d7574bb184952159786227c2","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","parentId":"","parentPath":"","name":"","code":"","weekend":"","holiday":"","startTime":"","endTime":"","startHour":"","endHour":"","startMinute":"","endMinute":"","value":"","remark":"","type":"","datumPoint":"","isWork":0,"label":"","pid":"","children":[]}
             * isHr :
             * addrBookTime :
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
            private String name;
            private String phone;
            private String idCard;
            private String iccid;
            private String department;
            private String company;
            private String job;
            private String remark;
            private String lastLoginTime;
            private String appVersion;
            private String helpVersion;
            private String oldPhone;
            private String lastTrackTime;
            private String postId;
            private PostBean post;
            private String isHr;
            private String addrBookTime;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public String getIccid() {
                return iccid;
            }

            public void setIccid(String iccid) {
                this.iccid = iccid;
            }

            public String getDepartment() {
                return department;
            }

            public void setDepartment(String department) {
                this.department = department;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getJob() {
                return job;
            }

            public void setJob(String job) {
                this.job = job;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(String lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public String getAppVersion() {
                return appVersion;
            }

            public void setAppVersion(String appVersion) {
                this.appVersion = appVersion;
            }

            public String getHelpVersion() {
                return helpVersion;
            }

            public void setHelpVersion(String helpVersion) {
                this.helpVersion = helpVersion;
            }

            public String getOldPhone() {
                return oldPhone;
            }

            public void setOldPhone(String oldPhone) {
                this.oldPhone = oldPhone;
            }

            public String getLastTrackTime() {
                return lastTrackTime;
            }

            public void setLastTrackTime(String lastTrackTime) {
                this.lastTrackTime = lastTrackTime;
            }

            public String getPostId() {
                return postId;
            }

            public void setPostId(String postId) {
                this.postId = postId;
            }

            public PostBean getPost() {
                return post;
            }

            public void setPost(PostBean post) {
                this.post = post;
            }

            public String getIsHr() {
                return isHr;
            }

            public void setIsHr(String isHr) {
                this.isHr = isHr;
            }

            public String getAddrBookTime() {
                return addrBookTime;
            }

            public void setAddrBookTime(String addrBookTime) {
                this.addrBookTime = addrBookTime;
            }

            public static class PostBean {
                /**
                 * id : 63800e53d7574bb184952159786227c2
                 * createTime :
                 * updateTime :
                 * createBy :
                 * updateBy :
                 * state :
                 * sort :
                 * delFlag : 0
                 * ids :
                 * orderBy :
                 * parentId :
                 * parentPath :
                 * name :
                 * code :
                 * weekend :
                 * holiday :
                 * startTime :
                 * endTime :
                 * startHour :
                 * endHour :
                 * startMinute :
                 * endMinute :
                 * value :
                 * remark :
                 * type :
                 * datumPoint :
                 * isWork : 0
                 * label :
                 * pid :
                 * children : []
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
                private String parentId;
                private String parentPath;
                private String name;
                private String code;
                private String weekend;
                private String holiday;
                private String startTime;
                private String endTime;
                private String startHour;
                private String endHour;
                private String startMinute;
                private String endMinute;
                private String value;
                private String remark;
                private String type;
                private String datumPoint;
                private int isWork;
                private String label;
                private String pid;
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

                public String getWeekend() {
                    return weekend;
                }

                public void setWeekend(String weekend) {
                    this.weekend = weekend;
                }

                public String getHoliday() {
                    return holiday;
                }

                public void setHoliday(String holiday) {
                    this.holiday = holiday;
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

                public String getDatumPoint() {
                    return datumPoint;
                }

                public void setDatumPoint(String datumPoint) {
                    this.datumPoint = datumPoint;
                }

                public int getIsWork() {
                    return isWork;
                }

                public void setIsWork(int isWork) {
                    this.isWork = isWork;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public List<?> getChildren() {
                    return children;
                }

                public void setChildren(List<?> children) {
                    this.children = children;
                }
            }
        }
    }
}
