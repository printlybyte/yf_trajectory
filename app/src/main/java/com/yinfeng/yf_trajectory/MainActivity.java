package com.yinfeng.yf_trajectory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Connection;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 通过后台服务持续定位
 */
public class MainActivity extends AppCompatActivity {

    private Button buttonStartService;
    private TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        buttonStartService = (Button) findViewById(R.id.button_start_service);
        tvResult = (TextView) findViewById(R.id.tv_result);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConstantApi.RECEIVER_ACTION);
//        registerReceiver(locationChangeBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
//        if (locationChangeBroadcastReceiver != null)
//            unregisterReceiver(locationChangeBroadcastReceiver);

        super.onDestroy();
    }

    /**
     * 启动或者关闭定位服务
     *
     */
    public void startService(View v) {
        if (buttonStartService.getText().toString().equals(getResources().getString(R.string.startLocation))) {
//            startLocationService();

            buttonStartService.setText(R.string.stopLocation);
            tvResult.setText("正在定位...");
        } else {
//            stopLocationService();
//
            buttonStartService.setText(R.string.startLocation);
            tvResult.setText("");
        }
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }


    private Connection mLocationServiceConn = null;

    /**
     * 开始定位服务
     */




//    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ConstantApi.RECEIVER_ACTION)) {
//                String locationResult = intent.getStringExtra("result");
//                if (null != locationResult && !locationResult.trim().equals("")) {
//                    tvResult.setText(locationResult);
//                }
//            }
//        }
//    };

}
