package com.yinfeng.yf_trajectory;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.bean.UserInfoBean;
import com.yinfeng.yf_trajectory.moudle.MeInfoActivity;
import com.yinfeng.yf_trajectory.moudle.PlayerMusicService;
import com.zhy.http.okhttp.OkHttpUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class MapActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView mActivityMapHeadimg;
    /**
     * 员工姓名
     */
    private TextView mActivityMapName;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initView() {
        super.initView();
        mActivityMapHeadimg = (CircleImageView) findViewById(R.id.activity_map_headimg);
        mActivityMapHeadimg.setOnClickListener(this);
        mActivityMapName = (TextView) findViewById(R.id.activity_map_name);

    }

    @Override
    protected void initData() {

        requestDate(0, "");

    }

    @Override
    protected void initBefore(Bundle savedInstanceState) {
        super.initBefore(savedInstanceState);
        initMap(savedInstanceState);
        initLocation();
    }


    //定位  单独依赖于定位SDK=======================================================================
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private void initLocation() {
        //声明AMapLocationClient类对象
        //声明定位回调监听器
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.setLocationListener(mAMapLocationListener);
        mLocationClient.startLocation();
        //设置场景模式
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }


        startPlayMusicService();
        //设置定位回调监听


    }

    private void stopPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        stopService(intent);
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        startService(intent);
    }

    private int mTempNums = 0;
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Log.i(ConstantApi.LOG_I, "定位SDK更新数据 " + "===" + amapLocation.getLatitude());
                    mTempNums++;
                    Toast.makeText(MapActivity.this, "" + mTempNums, Toast.LENGTH_SHORT).show();
//
//                    LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.i(ConstantApi.LOG_I, "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    //地图=======================================================================
    private MapView mMapView = null;
    private AMap aMap;

    private void initMap(Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        showLocation();

    }


    private void showLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        //控制是否显示定位蓝点
        myLocationStyle.showMyLocation(true);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(onMyLocationChangeListener);


    }

    AMap.OnMyLocationChangeListener onMyLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (location != null) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                Log.i(ConstantApi.LOG_I, "地图定位刷新：");
            } else {
                Log.i(ConstantApi.LOG_I, "地图定位刷新 错误");
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //=======================================================================
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_map_headimg:
                ActivityUtils.startActivity(MeInfoActivity.class);
                break;
        }
    }

    /**
     * 获取个人信息 track-token
     */

    private void requestDate(final int type, String msg) {
        if (!NetworkUtils.isConnected( )) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        if (type == 1) {
            showProgress(msg);
        }
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("个人信息查询失败");
            return;
        }
        OkHttpUtils
                .post()
                .addHeader("track-token", token)

                .url(Api.API_user_info)
                .build()
                .execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(UserInfoBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {

                            UserInfoBean.DataBean bean = response.getData();
                            Hawk.put(ConstantApi.HK_USER_BEAN, response.getData());


                            if (!TextUtils.isEmpty(bean.getIdCard())) {
                                if (ConmonUtils.isSex(bean.getIdCard()) == 2) {
                                    mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_gire);
                                } else if (ConmonUtils.isSex(bean.getIdCard()) == 1) {
                                    mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_man);
                                }
                            }
                            mActivityMapName.setText(bean.getName() == "" ? "无数据" : "您好，" + bean.getName());


                        } else {
                            showToastC(response.getMessage());
                        }

                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }


}
