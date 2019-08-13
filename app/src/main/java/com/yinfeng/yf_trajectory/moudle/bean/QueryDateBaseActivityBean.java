package com.yinfeng.yf_trajectory.moudle.bean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：QueryDateBaseActivityBean
 * 创建人：liuguodong
 * 创建时间：2019/8/13 10:25
 * ============================================
 **/
public class QueryDateBaseActivityBean {
    public List<DataBean> getDataBeans() {
        return dataBeans;
    }

    public void setDataBeans(List<DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    private List<DataBean> dataBeans;

    public static class DataBean {
        private String lat;
        private String lng;
        private String address;
        private String time;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
