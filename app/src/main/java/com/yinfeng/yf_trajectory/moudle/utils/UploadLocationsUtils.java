package com.yinfeng.yf_trajectory.moudle.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.app.Latte;
import com.caitiaobang.core.app.bean.GreendaoLocationBean;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean_string;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：UploadLocationsUtils
 * 创建人：liuguodong
 * 创建时间：2019/9/9 11:52
 * ============================================
 **/
public class UploadLocationsUtils {


    public static void commitLocationInfoToken(JSONArray jsonArray) {
        String iccid = LattePreference.getValue(ConstantApi.HK_ICCID);
        if (TextUtils.isEmpty(iccid)) {
            Toast.makeText(Latte.getApplicationContext(), "上传数据失败，没有获取到iccid", Toast.LENGTH_SHORT).show();
            Logger.i( "上传数据失败，没有获取到iccid");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pointList", jsonArray);
            jsonObject.put("iccid", iccid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.API_point_insert)
                .tag(Latte.getApplicationContext())
                .upJson(jsonObject)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String data = response.body();//这个就是返回来的结果
                        Timber.i("success:%s", data);
                        Logger.i( "onSuccess：" + data);
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                Logger.i( "上传成功");
                                DaoSession daoSession = BaseApplication.getDaoInstant();
                                daoSession.deleteAll(GreendaoLocationBean.class);
                            } else if (bean.getCode() == ConstantApi.API_REQUEST_ERR_901) {

//                                Toast.makeText(getBaseContext(), "账号在其他地方登陆，密码已泄露，建议重置并重新登录！", Toast.LENGTH_SHORT).show();
                                Logger.i( "上传失败，账号在其他地方登陆");
                                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                                mIntent.putExtra("result", ConstantApi.RECEVIER_901);
                                //发送广播
                                Latte.getApplicationContext().sendBroadcast(mIntent);

                            } else {
                                Logger.i( "上传失败");
                                Logger.i( bean.getMessage());
                                Toast.makeText(Latte.getApplicationContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Latte.getApplicationContext(), "上传数据失败", Toast.LENGTH_SHORT).show();
                            Logger.i( "上传失败，原因数据转化失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(Latte.getApplicationContext(), "上传数据失败onError", Toast.LENGTH_SHORT).show();

                        Logger.i( "onError：" + response.body());
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                Toast.makeText(Latte.getApplicationContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Latte.getApplicationContext(), " 转化失败", Toast.LENGTH_SHORT).show();
                            Logger.i( "Gson转化失败");

                        }
                    }
                });
    }

    /**
     * 提交token数据
     */
//    private void commitLocationInfoPhone(JSONArray jsonArray, String phone) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("pointList", jsonArray);
//            jsonObject.put("phone", phone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        OkGo.<String>post(Api.API_point_insert)
//                .tag(this)
//                .upJson(jsonObject)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        String data = response.body();//这个就是返回来的结果
//                        Timber.i("success:%s", data);
//                        Logger.i("onSuccess：" + data);
//                        try {
//                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
//                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
//                                Logger.i("上传成功");
//                                DaoSession daoSession = BaseApplication.getDaoInstant();
//                                daoSession.deleteAll(GreendaoLocationBean.class);
//                            } else if ( bean.getCode() == ConstantApi.API_REQUEST_ERR_901) {
////                                Toast.makeText(getBaseContext(), "账号在其他地方登陆，密码已泄露，建议重置并重新登录！", Toast.LENGTH_SHORT).show();
//                                Logger.i("账号在其他地方登陆");
//                                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
//                                mIntent.putExtra("result", ConstantApi.RECEVIER_901);
//                                //发送广播
//                                sendBroadcast(mIntent);
//                            } else {
//                                Logger.i(bean.getMessage());
//                                showToastC(bean.getMessage());
//                            }
//                        } catch (Exception e) {
//                            Logger.i("转化失败");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        Logger.i("onError：" + response.body());
//                        try {
//                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
//                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
//                                Toast.makeText(getBaseContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            showToastC("转化失败");
//                            Logger.i("Gson转化失败");
//
//                        }
//                    }
//                });
//    }
}
