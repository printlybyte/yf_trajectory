package com.caitiaobang.core.app.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.caitiaobang.core.greendao.gen.DaoMaster;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.orhanobut.hawk.Hawk;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        Latte.init(this);
        BGASwipeBackHelper.init(this, null);
        Hawk.init(this).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        setupDatabase();
    }

    private static DaoSession daoSession;

    /**
     * greendao 数据库  配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db" 创建SQLite数据库的SQLiteOpenHelper的具体实现
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "yf_location.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象  GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者  管理所有的Dao对象，Dao对象中存在着增删改查等API
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }



}
