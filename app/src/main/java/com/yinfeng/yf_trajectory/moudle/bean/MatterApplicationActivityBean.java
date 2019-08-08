package com.yinfeng.yf_trajectory.moudle.bean;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.bean
 * 类  名：MatterApplicationActivityBean
 * 创建人：liuguodong
 * 创建时间：2019/8/6 17:07
 * ============================================
 **/
public class MatterApplicationActivityBean {

    /**
     * code : 200
     * message : 操作成功！
     * data :
     * now : 2019-06-17 11:34:42
     * success : true
     */

    private int code;
    private String message;
    private String data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
}
