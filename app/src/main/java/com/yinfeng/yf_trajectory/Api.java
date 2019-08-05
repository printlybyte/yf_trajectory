package com.yinfeng.yf_trajectory;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yinfengtrajectory
 * 类  名：ConstantApi
 * 创建人：liuguodong
 * 创建时间：2019/8/1 16:55
 * ============================================
 **/
public interface Api {
//    String APP_DOMAIN = "http://postman.wanghuifeng.me/mock/19";
    String APP_DOMAIN = "http://47.105.197.148:8111/admin";

     /**
      * post
     *登录
     */
    String API_login=APP_DOMAIN+ "/login";

    /**
     * get
     *获取验证码  注意手机号加在后面
     */
    String API_SmsSendCode = APP_DOMAIN+ "/common/sms/";

    /**
     * post
     *获取个人信息
     */
    String API_user_info = APP_DOMAIN+ "/user/info";



}
