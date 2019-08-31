package com.caitiaobang.core.app.storge;




import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.caitiaobang.core.app.app.Latte;


/**
 * Created by liugudoong on 2017/4/22
 */

public final class LattePreference {

    /**
     * 提示:
     * Activity.getPreferences(int mode)生成 Activity名.xml 用于Activity内部存储
     * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml
     * Context.getSharedPreferences(String name,int mode)生成name.xml
     */
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(Latte.getApplicationContext());
    private static final String APP_PREFERENCES_KEY = "profile";


    public static void saveKey(String key, String value) {
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = Latte.getApplicationContext().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString(key, value);
        //步骤4：提交
        editor.commit();
    }

    public static String getValue(String key) {
        SharedPreferences sharedPreferences = Latte.getApplicationContext().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, "");
    }

    public static void clear() {
        SharedPreferences sharedPreferences = Latte.getApplicationContext().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    public static void setAppProfile(String val) {
        getAppPreference()
                .edit()
                .putString(APP_PREFERENCES_KEY, val)
                .apply();
    }

    public static String getAppProfile() {
        return getAppPreference().getString(APP_PREFERENCES_KEY, null);
    }

//    public static JSONObject getAppProfileJson() {
//        final String profile = getAppProfile();
//        return JSON.parseObject(profile);
//    }



    public static void removeAppProfile() {
        getAppPreference()
                .edit()
                .remove(APP_PREFERENCES_KEY)
                .apply();
    }

    public static void removeCostomProfile(String key) {
        getAppPreference()
                .edit()
                .remove(key)
                .apply();
    }

    public static void clearAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    public static void setAppFlag(String key, boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public static boolean getAppFlag(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }

    public static void addCustomAppProfile(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }
    public static void addCustomAppProfile2(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static String getCustomAppProfile(String key) {
        return getAppPreference().getString(key, "");
    }
    public static String getCustomAppProfile2(String key) {
        return getAppPreference().getString(key, "no");
    }


}