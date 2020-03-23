package com.yinfeng.yf_trajectory.moudle.utils;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.moudle.bean.ChanelLeaveBean;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
import com.yinfeng.yf_trajectory.moudle.bean.GetWorkStatusBean;
import com.yinfeng.yf_trajectory.moudle.bean.GetWorkTimeBean;
import com.yinfeng.yf_trajectory.moudle.bean.IsLeaveStatusBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：WorkUtils
 * 创建人：liuguodong
 * 创建时间：2019/11/25 10:07
 * ============================================
 **/
public class WorkUtils {

    private static final WorkUtils ourInstance = new WorkUtils();

    public static WorkUtils getInstance() {
        return ourInstance;
    }

    public WorkUtils() {
    }

    /**
     * isWorkStatus==0 异常  ==1 工作日   ==2 非工作日
     */
    public void getIsWorkDay() {
        Logger.i("查看是否是工作日 API: " + Api.commonJudgeIsWork);
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            if (onWorkListenerI != null) {
                onWorkListenerI.failure("token  null");
            }
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.commonJudgeIsWork)
                .build()
                .execute(new GenericsCallback<GetWorkStatusBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (onWorkListenerI != null) {
                            onWorkListenerI.failure("未知异常：" + e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(GetWorkStatusBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            IBundleBean bundleBean = new IBundleBean();
                            if (response.getData().isIsWork()) {
                                bundleBean.setIsWorkDay(1);
                            } else {
                                bundleBean.setIsWorkDay(2);
                            }
                            if (onWorkListenerI != null) {
                                onWorkListenerI.successful(1, bundleBean);
                            }
                        } else {
                            if (onWorkListenerI != null) {
                                onWorkListenerI.failure("" + response.getMessage());
                            }
                        }
                        Logger.i("请求结果：查看是否是工作日" + GsonUtils.getInstance().toJson(response));
                    }
                });

    }


    /**
     * 0不处于请假中，1是处于请假状态
     */
    public void getJudgeLeave() {
        Logger.i("查看是否是请假中 API: " + Api.commonjudgeLeave);
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            if (onWorkListenerI != null) {
                onWorkListenerI.failure("token  null");
            }
            ;
            return;
        }
        OkGo.<String>get(Api.commonjudgeLeave)
                .headers("track-token", token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            IsLeaveStatusBean bean = new Gson().fromJson(response.body().toString(), IsLeaveStatusBean.class);
                            if (bean != null && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS && bean.isSuccess()) {
                                IBundleBean bundleBean = new IBundleBean();
                                if (bean.getData().getState().equals("1")) {
                                    bundleBean.setIsLeaveing(1);
                                    LattePreference.saveKey(ConstantApi.leave_time_status, "1");
                                    if (!TextUtils.isEmpty(bean.getData().getStartTime())) {
                                        LattePreference.saveKey(ConstantApi.leave_time_start, bean.getData().getStartTime() + "");
                                    }
                                    if (!TextUtils.isEmpty(bean.getData().getEndTime())) {
                                        LattePreference.saveKey(ConstantApi.leave_time_end, "" + bean.getData().getEndTime() + "");
                                    }


                                } else if (bean.getData().getState().equals("0")) {
                                    bundleBean.setIsLeaveing(0);
                                    LattePreference.saveKey(ConstantApi.leave_time_status, "0");
                                }
                                if (onWorkListenerI != null) {
                                    onWorkListenerI.successful(1, bundleBean);
                                }

                            }
                            Logger.i("请求结果：查看是否是请假中" + response.body().toString());

                        } catch (Exception e) {
                            if (onWorkListenerI != null) {
                                onWorkListenerI.failure("judgeLeave 解析异常");
                            }
                            ;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (onWorkListenerI != null) {
                            onWorkListenerI.failure("" + response.message());
                        }
                    }
                });
    }


    /**
     * 获取上下班时间 与是否工作中..   1 工作中...  0 异常   2 可以下班
     */

    public void getWorkTimeStatus() {
        Logger.i("API:获取上下班时间  上下班状态 " + Api.commongetWorkTime);
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            if (onWorkListenerI != null) {
                onWorkListenerI.failure("token  null");
            }
            ;
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.commongetWorkTime)
                .build()
                .execute(new GenericsCallback<GetWorkTimeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (onWorkListenerI != null) {
                            onWorkListenerI.failure("未知异常：" + e.getMessage());
                        }
                        ;
                    }

                    @Override
                    public void onResponse(GetWorkTimeBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            GetWorkTimeBean.DataBean bean = response.getData();
                            LattePreference.saveKey(ConstantApi.work_time_start, response.getData().getStartTime() + "");
                            LattePreference.saveKey(ConstantApi.work_time_end, response.getData().getEndTime() + "");
                            IBundleBean bundleBean = new IBundleBean();
                            if (bean.getIsWork() == 1) {
                                bundleBean.setIsWorking(1);
                            } else {
                                bundleBean.setIsWorking(2);
                            }
                            if (onWorkListenerI != null) {
                                onWorkListenerI.successful(1, bundleBean);
                            }
                        } else {
                            if (onWorkListenerI != null) {
                                onWorkListenerI.failure(response.getMessage() + "");
                            }
                            ;
                        }
                        Logger.i("请求结果：获取上下班时间  上下班状态" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    public void getCancelLeave() {
        Logger.i("API: 取消请假" + Api.commonCancelLeave);
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            if (onWorkListenerI != null) {
                onWorkListenerI.failure("token  null");
            }
            ;
            return;
        }
        OkHttpUtils
                .post()
                .addHeader("track-token", token)
                .url(Api.commonCancelLeave)
                .build()
                .execute(new GenericsCallback<ChanelLeaveBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (onWorkListenerI != null) {
                            onWorkListenerI.failure("未知异常：" + e.getMessage());
                        }
                        ;
                    }

                    @Override
                    public void onResponse(ChanelLeaveBean response, int id) {
                        IBundleBean bundleBean = new IBundleBean();
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {

                            bundleBean.setStatus(true);
                            if (onWorkListenerI != null) {
                                onWorkListenerI.successful(1, bundleBean);
                            }
                        } else {
                            if (onWorkListenerI != null) {
                                bundleBean.setStatus(false);
                                onWorkListenerI.failure(response.getMessage() + "");
                            }
                        }
                        Logger.i("请求结果：取消请假" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    /**
     * 下班成功回调
     */
    public void getOffWork() {
        Logger.i("API: 下班回调" + Api.offWork);
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            if (onWorkListenerI != null) {
                onWorkListenerI.failure("token  null");
            }
            ;
            return;
        }
        OkHttpUtils
                .post()
                .addHeader("track-token", token)
                .url(Api.offWork)
                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (onWorkListenerI != null) {
                            onWorkListenerI.failure("未知异常：" + e.getMessage());
                        }
                        ;
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        IBundleBean bundleBean = new IBundleBean();
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            if (onWorkListenerI != null) {
                                onWorkListenerI.successful(1, bundleBean);
                            }
                        } else {
                            if (onWorkListenerI != null) {
                                onWorkListenerI.failure(response.getMessage() + "");
                            }
                        }
                        Logger.i("请求结果：下班成功？" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    public void getOffWork_callback() {
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            Logger.i("通讯录同步失败回调接口 token==null ");
            return;
        }
        Map<String, String> mMapValue = new ConcurrentHashMap<>();
        String dateS = TimeUtils.millis2String(System.currentTimeMillis());
        mMapValue.put("date", dateS);
        Logger.i("dateS: " + dateS + "   offWork_callback:" + Api.offWork_callback);
        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(mMapValue))
                .url(Api.offWork_callback)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()

//        OkHttpUtils
//                .post()
//                .addHeader("track-token", token)
//                .addParams("date", "")
//                .url(Api.offWork_callback)
//                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("通讯录同步失败 回调接口访问失败");
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {

                            Logger.i("失败回调接口 " + new Gson().toJson(response));

                            Logger.i("通讯录同步失败 回调接口访问成功");
                        } else {
                            Logger.i("通讯录同步失败 回调接口访问失败");

                        }
                    }
                });
    }


    /**
     * 判断各方法的 返回状态
     *
     * @status
     */
    public interface OnWorkListenerI {
        void successful(int status, IBundleBean bundleBean);

        void failure(String msg);

    }

    public void setOnWorkListener(OnWorkListenerI onLocationListenerm) {
        this.onWorkListenerI = onLocationListenerm;
    }

    private OnWorkListenerI onWorkListenerI;

    public class IBundleBean {
        //isWorkStatus==0 异常  ==1 工作日   ==2 非工作日
        public int isWorkDay;

        //0不处于请假中，1是处于请假状态
        public int isLeaveing;

        //获取上下班时间 与是否工作中..   1 工作中...  0 异常   2 可以下班
        public int isWorking;

        public boolean isStatus;

        //下班之后调用一下  不关注结果
        public boolean isRemarkWorkDown;

        public boolean isRemarkWorkDown() {
            return isRemarkWorkDown;
        }

        public void setRemarkWorkDown(boolean remarkWorkDown) {
            isRemarkWorkDown = remarkWorkDown;
        }


        public boolean isStatus() {
            return isStatus;
        }

        public void setStatus(boolean status) {
            isStatus = status;
        }

        public int getIsWorking() {
            return isWorking;
        }

        public void setIsWorking(int isWorking) {
            this.isWorking = isWorking;
        }

        public int getIsLeaveing() {
            return isLeaveing;
        }

        public void setIsLeaveing(int isLeaveing) {
            this.isLeaveing = isLeaveing;
        }

        public int getIsWorkDay() {
            return isWorkDay;
        }

        public void setIsWorkDay(int isWorkDay) {
            this.isWorkDay = isWorkDay;
        }
    }

}
