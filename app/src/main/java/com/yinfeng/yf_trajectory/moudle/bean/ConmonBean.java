package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：ConmonBean
 * 创建人：liuguodong
 * 创建时间：2019/8/2 11:33
 * ============================================
 **/
public class ConmonBean {


    /**
     * code : 200
     * message : 登录成功
     * data : Bearer eyJhbGciOiJIUzUxMiJ9.eyJhY2NvdW50SWQiOiIwMDAxQTExMDAwMDAwMDAwTVUzRSIsInJvbGVzIjpbXSwiZXhwIjoxNTYxMzQ0NTQxfQ.E1WYlVC20pCq6iSk8p8t_q5zge2MqBU74TQB5GyMxiMbY3_AR1UcFqSEYDDDRxlPuSLOcH1XJ9r2Idc2Z5h-8Q
     * now : 1560739741513
     * success : true
     */

    private int code;
    private String message;
    private String data;
    private long now;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
