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
     * data : {"date":"2020-03-12 14:57:17","list":[{"phone":"19953153005","name":"魏哲祥(控股)"}]}
     * now : 2020-03-12 14:57:17
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
         * date : 2020-03-12 14:57:17
         * list : [{"phone":"19953153005","name":"魏哲祥(控股)"}]
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
             * phone : 19953153005
             * name : 魏哲祥(控股)
             */

            private String phone;
            private String name;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
