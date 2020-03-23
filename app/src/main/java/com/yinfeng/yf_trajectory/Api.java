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
    String APP_DOMAIN = "http://47.104.98.97/admin";
//    String APP_DOMAIN = "http://47.105.197.148:8111/admin";

    /**
     * post
     * 登录
     */
    String API_login = APP_DOMAIN + "/login";

    /**
     * get
     * 获取验证码  注意手机号加在后面
     */
    String API_SmsSendCode = APP_DOMAIN + "/common/sms/";

    /**
     * post
     * 获取个人信息
     */
    String API_user_info = APP_DOMAIN + "/user/info";


    /**
     * post
     * 外出申请开始 or 结束
     */
    String API_apply_applyBegin = APP_DOMAIN + "/apply/applyBegin/insert";
    String API_apply_applyEnd = APP_DOMAIN + "/apply/applyEnd/insert";
    /**
     * get
     * 外出申请状态
     */
    String API_apply_judge = APP_DOMAIN + "/apply/judge";

    /**
     * post
     * 外出记录
     */
    String API_apply_query = APP_DOMAIN + "/apply/query";

    /**
     * post
     * 上传坐标点
     */
    String API_point_insert = APP_DOMAIN + "/point/insert";


    /**
     * post
     * 获取坐标点
     */
    String API_point_app_query = APP_DOMAIN + "/point/app/query";
    /**
     * post
     * 获取申请记录坐标点
     */
    String API_point_apply_query = APP_DOMAIN + "/point/apply/query";
    /**
     * post
     * 获取坐标点上传频率
     */
    String API_point_getFrequency = APP_DOMAIN + "/point/getFrequency";

    /**
     * post
     * 获取升级信息
     */
    String API_appVersion_judge = APP_DOMAIN + "/appVersion/last";


    String API_appVersion_getUpdateAndAliveTime = APP_DOMAIN + "/appVersion/getUpdateAndAliveTime";


    /**
     * 根据手机卡串号获取手机号
     */
    String commonGetPhoneByCode = APP_DOMAIN + "/common/getPhoneByCode";

    /**
     * 绑定手机串号和手机号
     */
    String commonAddPhone = APP_DOMAIN + "/common/addPhone";
    String appVersionGetNewVersion = APP_DOMAIN + "/appVersion/getNewVersion";


    /**
     * 记录当前动作
     */
    String commonrecordOperate = APP_DOMAIN + "/common/recordOperate";
    String commongetMatter = APP_DOMAIN + "/common/getMatter";
    String commongetWorkTime = APP_DOMAIN + "/common/getWorkTime";
    String commonjudgeLeave = APP_DOMAIN + "/common/judgeLeave";

    String commonJudgeIsWork = APP_DOMAIN + "/common/judgeIsWorkV2";


    /**
     * 取消请假
     */
    String commonCancelLeave = APP_DOMAIN + "/common/cancelLeave";


    /**
     * 请假列表
     */
    String commonleaveList = APP_DOMAIN + "/common/leaveList";


    /**
     * 查询通讯录
     */
//    String appQueryByAddressBook = APP_DOMAIN + "/user/appQueryByAddressBook";
    String appQueryByAddressBook = APP_DOMAIN + "/user/appQueryByAddressBookV2";



    String offWork = APP_DOMAIN + "/user/offwork/offWork";


    String offWork_callback = APP_DOMAIN + "/user/callBackByAddrBook";



}
