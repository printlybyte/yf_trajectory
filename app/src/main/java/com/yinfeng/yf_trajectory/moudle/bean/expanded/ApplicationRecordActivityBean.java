package com.yinfeng.yf_trajectory.moudle.bean.expanded;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean.expanded
 * 类  名：ApplicationRecordActivityBean
 * 创建人：liuguodong
 * 创建时间：2019/8/6 17:33
 * ============================================
 **/
public class ApplicationRecordActivityBean {

    /**
     * code : 200
     * message : 操作成功！
     * data : {"total":1,"list":[{"reason":"测试原因","address":"测试地址","orderBy":null,"updateTime":"2019-07-24 16:42:38","sort":999,"createTime_str":"2019-07-24 16:42:38","delFlag":"0","title":"测试标题","userId":"0001A11000000000S8SQ","updateTime_date_str":"2019-07-24","createBy":"0001A11000000000S8SQ","createTime":"2019-07-24 16:42:38","updateBy":"0001A11000000000S8SQ","updateTime_str":"2019-07-24 16:42:38","ids":null,"sysAttachments":[],"startTime":"2019-07-24 16:42:36","id":"8db45ab3c57e4370ad0c2024be009032","endTime":"2019-07-24 16:45:24","state":"1","user":{"id":"0001A11000000000S8SQ","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","name":"娄承春","phone":"15069066959","idCard":"","department":"","company":"","job":"","remark":""},"createTime_date_str":"2019-07-24"}],"pageNum":1,"pageSize":15,"size":1,"startRow":0,"endRow":0,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1}
     * now : 2019-07-24 17:17:54
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
         * total : 1
         * list : [{"reason":"测试原因","address":"测试地址","orderBy":null,"updateTime":"2019-07-24 16:42:38","sort":999,"createTime_str":"2019-07-24 16:42:38","delFlag":"0","title":"测试标题","userId":"0001A11000000000S8SQ","updateTime_date_str":"2019-07-24","createBy":"0001A11000000000S8SQ","createTime":"2019-07-24 16:42:38","updateBy":"0001A11000000000S8SQ","updateTime_str":"2019-07-24 16:42:38","ids":null,"sysAttachments":[],"startTime":"2019-07-24 16:42:36","id":"8db45ab3c57e4370ad0c2024be009032","endTime":"2019-07-24 16:45:24","state":"1","user":{"id":"0001A11000000000S8SQ","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","name":"娄承春","phone":"15069066959","idCard":"","department":"","company":"","job":"","remark":""},"createTime_date_str":"2019-07-24"}]
         * pageNum : 1
         * pageSize : 15Person
         * Level0Item
         * Level1Item
         * size : 1
         * startRow : 0
         * endRow : 0
         * pages : 1
         * prePage : 0
         * nextPage : 0
         * isFirstPage : true
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : [1]
         * navigateFirstPage : 1
         * navigateLastPage : 1
         * firstPage : 1
         * lastPage : 1
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int pages;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private int navigateFirstPage;
        private int navigateLastPage;
        private int firstPage;
        private int lastPage;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            /**
             * reason : 测试原因
             * address : 测试地址
             * orderBy : null
             * updateTime : 2019-07-24 16:42:38
             * sort : 999
             * createTime_str : 2019-07-24 16:42:38
             * delFlag : 0
             * title : 测试标题
             * userId : 0001A11000000000S8SQ
             * updateTime_date_str : 2019-07-24
             * createBy : 0001A11000000000S8SQ
             * createTime : 2019-07-24 16:42:38
             * updateBy : 0001A11000000000S8SQ
             * updateTime_str : 2019-07-24 16:42:38
             * ids : null
             * sysAttachments : []
             * startTime : 2019-07-24 16:42:36
             * id : 8db45ab3c57e4370ad0c2024be009032
             * endTime : 2019-07-24 16:45:24
             * state : 1
             * user : {"id":"0001A11000000000S8SQ","createTime":"","updateTime":"","createBy":"","updateBy":"","state":"","sort":"","delFlag":"0","ids":"","orderBy":"","name":"娄承春","phone":"15069066959","idCard":"","department":"","company":"","job":"","remark":""}
             * createTime_date_str : 2019-07-24
             */

            private String reason;
            private String address;
            private Object orderBy;
            private String updateTime;
            private int sort;
            private String createTime_str;
            private String delFlag;
            private String title;
            private String userId;
            private String updateTime_date_str;
            private String createBy;
            private String createTime;
            private String updateBy;
            private String updateTime_str;
            private Object ids;
            private String startTime;
            private String id;
            private String endTime;
            private String state;
            private UserBean user;
            private String createTime_date_str;
            private List<?> sysAttachments;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
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

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
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

            public static class UserBean {
                /**
                 * id : 0001A11000000000S8SQ
                 * createTime :
                 * updateTime :
                 * createBy :
                 * updateBy :
                 * state :
                 * sort :
                 * delFlag : 0
                 * ids :
                 * orderBy :
                 * name : 娄承春
                 * phone : 15069066959
                 * idCard :
                 * department :
                 * company :
                 * job :
                 * remark :
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
                private String name;
                private String phone;
                private String idCard;
                private String department;
                private String company;
                private String job;
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
            }
        }
    }
}
