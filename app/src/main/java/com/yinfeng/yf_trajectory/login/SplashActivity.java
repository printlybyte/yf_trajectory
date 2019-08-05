package com.yinfeng.yf_trajectory.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.orhanobut.hawk.Hawk;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yinfeng.yf_trajectory.MapActivity;

public class SplashActivity extends AppCompatActivity {
    protected RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mRxPermissions=new RxPermissions(this);
//        mRxPermissions
//                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE )
//                .subscribe(granted -> {
//                    if (granted) { // Always true pre-M
////                                distinctionUser();
//
//                    } else {
//
//                    }
//                });


        String isLoginStatus = Hawk.get("already_login_yfgj", "");
        if (!TextUtils.isEmpty(isLoginStatus)) {
            AppManager.getInstance().finishAllActivity();
            ActivityUtils.startActivity(MapActivity.class);
        } else {
            ActivityUtils.startActivity(LoginVerActivity.class);
        }
        finish();

    }

}
