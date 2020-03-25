package com.yinfeng.yf_trajectory.launcher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.launcher.launcher_utils.AppInfoIetmAdapter;
import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
import com.yinfeng.yf_trajectory.moudle.login.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class LuancherHomeActivity extends AppCompatActivity {


    private RecyclerView mRecyclerview;
    private AppInfoIetmAdapter appInfoIetmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luancher_home);
        initView();
    }

    private void initView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.include_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(LuancherHomeActivity.this, 4);
        //设置布局管理器
        mRecyclerview.setLayoutManager(layoutManager);
        appInfoIetmAdapter = new AppInfoIetmAdapter(R.layout.app_info_item, getAppsInfo());
        appInfoIetmAdapter.openLoadAnimation();
        mRecyclerview.setAdapter(appInfoIetmAdapter);

        appInfoIetmAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AppUtils.AppInfo g = appInfoIetmAdapter.getData().get(position);
//                ComponentName c = new ComponentName(g.getPackageName(), g.getName());
//                Intent intent = new Intent();
//                intent.setComponent(c);
//                startActivity(intent);

                if (g.getPackageName().equals("")) {
                    ActivityUtils.startActivity(MapActivity.class);
                } else {
                    AppUtils.launchApp(g.getPackageName());
                }
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtils.startActivity(SplashActivity.class);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public static List<AppUtils.AppInfo> getAppsInfo() {
        List<AppUtils.AppInfo> list = new ArrayList<>();
        PackageManager pm = Utils.getApp().getPackageManager();
        if (pm == null) return list;
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : installedPackages) {
            AppUtils.AppInfo ai = getBean(pm, pi);
            if (ai == null) continue;
            list.add(ai);
        }
        return list;
    }

    private static AppUtils.AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
        if (pi == null) return null;
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
            String name = ai.loadLabel(pm).toString();
            Drawable icon = ai.loadIcon(pm);
            String packagePath = ai.sourceDir;
            String versionName = pi.versionName;
            int versionCode = pi.versionCode;
            //第三方应用
            boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) <= 0;
            return new AppUtils.AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
        } else {
            return null;
        }

    }

}
