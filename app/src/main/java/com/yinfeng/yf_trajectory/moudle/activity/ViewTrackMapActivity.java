package com.yinfeng.yf_trajectory.moudle.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.ViewTrackMapActivityBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class ViewTrackMapActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener {


    /**
     * 点击选择开始时间
     */
    private TextView mActivityMatterApplicationStartTime;
    /**
     * 点击选择结束时间
     */
    private TextView mActivityMatterApplicationEndTime;
    /**
     * 搜索
     */
    private TextView mActivityMatterApplicationConfirm;
    private FrameLayout mActivityViewTrackMapSelectedTimeGroup;

    @Override
    protected void initView() {
        super.initView();
        initTimeDialog();
        mActivityMatterApplicationStartTime = (TextView) findViewById(R.id.activity_matter_application_start_time);
        mActivityMatterApplicationStartTime.setOnClickListener(this);
        mActivityMatterApplicationEndTime = (TextView) findViewById(R.id.activity_matter_application_end_time);
        mActivityMatterApplicationEndTime.setOnClickListener(this);
        mActivityMatterApplicationConfirm = (TextView) findViewById(R.id.activity_matter_application_confirm);
        mActivityMatterApplicationConfirm.setOnClickListener(this);
        mActivityViewTrackMapSelectedTimeGroup = (FrameLayout) findViewById(R.id.activity_view_track_map_selected_time_group);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_view_track_map;
    }

    @Override
    protected void initData() {
        setTitle("轨迹查看");
        Intent intent = getIntent();
        if (intent != null) {
            mJumpFlag = intent.getStringExtra(ConstantApi.INTENT_FLAG);
            mTimeStart = intent.getStringExtra(ConstantApi.INTENT_KEY);
            mTimeEnd = intent.getStringExtra(ConstantApi.INTENT_KEY_TWO);
        } else {
            showToastC("查询失败，请稍后重试");
            return;
        }

        if (!TextUtils.isEmpty(mJumpFlag) && mJumpFlag.equals(ConstantApi.query_info)) {
            mActivityViewTrackMapSelectedTimeGroup.setVisibility(View.GONE);
            requestDate(0, "查询中...");
        } else if (!TextUtils.isEmpty(mJumpFlag) && mJumpFlag.equals(ConstantApi.query_search)) {
//            showToastC("查询");

        }

    }

    @Override
    protected void initBefore(Bundle savedInstanceState) {
        super.initBefore(savedInstanceState);
        initMap(savedInstanceState);


    }

    private TimePickerDialog mDialogYearMonthDay;

    private void initTimeDialog() {
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.ALL)
                .setTitleStringId("选择时间")
                .setCallBack(this)
                .setCyclic(false)
                .setThemeColor(R.color.p_color_blue)
                .setMinMillseconds(ConstantApi.time_dialog_min_time)
                .setMaxMillseconds(ConstantApi.time_dialog_max_time)
                .build();
    }

    private int mTimeType = 0;

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = ConmonUtils.getDateToString(millseconds);
//        mTime = millseconds / 1000 + "";
        Log.i("", "");
        if (mTimeType == 1) {
            mTimeStart = text;
            mActivityMatterApplicationStartTime.setText(text);

        } else if (mTimeType == 2) {
            mTimeEnd = text;
            mActivityMatterApplicationEndTime.setText(text);
        }
    }


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
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.ic_map_deauful_icon)));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 自定义精度范围的圆形边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//圆圈的颜色,设为透明的时候就可以去掉园区区域了

        //控// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
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
                Logger.v( "地图定位刷新：");
            } else {
                Logger.v( "地图定位刷新 错误");
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        new EventBusUtils().unregister(this);

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

    /**
     * 获取点信息
     */

    private String mJumpFlag = "";
    private String mTimeStart = "";
    private String mTimeEnd = "";

    private void requestDate(final int type, String msg) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        if (type == 1) {
            showProgress(msg);
        }
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token查询失败");
            return;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.clear();
        map.put("startTime", mTimeStart);
        map.put("endTime", mTimeEnd);
          String mNetUrl="";
        if (!TextUtils.isEmpty(mJumpFlag) && mJumpFlag.equals(ConstantApi.query_info)) {
            mNetUrl = Api.API_point_apply_query;
        } else if (!TextUtils.isEmpty(mJumpFlag) && mJumpFlag.equals(ConstantApi.query_search)) {
            mNetUrl = Api.API_point_app_query;
        }
        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(map))
                .url(mNetUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ViewTrackMapActivityBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(ViewTrackMapActivityBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            if (response.getData().getTrack().size() > 2) {
                                DrawTrack(response);
                            } else {
                                showToastC("点位不足，请稍后重试");
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Logger.v( "请求结果：" + GsonUtils.getInstance().toJson(response));
                        if (type == 1) {
                            dismisProgress();
                        }
                    }
                });
    }

    /**
     * 绘制
     */
    private Polyline polyline;

    private void DrawTrack(ViewTrackMapActivityBean response) {
        aMap.clear();
        List<LatLng> latLngs = new ArrayList<LatLng>();
        int mSize = response.getData().getTrack().size();
        for (int i = 0; i < mSize; i++) {
            ViewTrackMapActivityBean.DataBean.TrackBean bean = response.getData().getTrack().get(i);
            Logger.v( "lat: " + bean.getX() + " lng: " + bean.getY());
            double latx = bean.getX();
            double laty = bean.getY();
            latLngs.add(new LatLng(laty, latx));
            if (i == 0) {
                DrawTrackStartMaker(new LatLng(laty, latx));
            }
            if (i == mSize - 1) {
                DrawTrackEndMaker(new LatLng(laty, latx));
                //起点位置和  地图界面大小控制
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 18));
//                可以将地图底图文字设置在添加的覆盖物之上。
//                aMap.setMapTextZIndex(2);
                aMap.addPolyline((new PolylineOptions())
                        //手动数据测试
                        //.add(new LatLng(26.57, 106.71),new LatLng(26.14,105.55),new LatLng(26.58, 104.82), new LatLng(30.67, 104.06))
                        //集合数据
                        .addAll(latLngs)
                        //线的宽度
                        .width(10).setDottedLine(false).geodesic(true)
                        //颜色
                        .color(Color.argb(255, 255, 20, 147)));
            }
        }

    }

    /**
     * 绘制起点
     */
    private void DrawTrackStartMaker(LatLng latLngStart) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLngStart);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_draw_track_start)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);
        addMarkerAnimation(marker);
    }

    private void addMarkerAnimation(Marker marker) {
        Animation animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 360, 0, 0, 0);
        long duration = 1000L;
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        marker.setAnimation(animation);
        marker.startAnimation();
    }

    /**
     * 绘制终点
     */
    private void DrawTrackEndMaker(LatLng latLngEnd) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLngEnd);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_draw_track_stopx)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);
        addMarkerAnimation(marker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_matter_application_start_time:
                mTimeType = 1;
                mDialogYearMonthDay.show(getSupportFragmentManager(), "1");
                break;
            case R.id.activity_matter_application_end_time:
                mTimeType = 2;
                mDialogYearMonthDay.show(getSupportFragmentManager(), "2");
                break;
            case R.id.activity_matter_application_confirm:
                if (TextUtils.isEmpty(mTimeStart)) {
                    showToastC("请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(mTimeEnd)) {
                    showToastC("请选择结束时间");
                    return;
                }

                requestDate(1, "查询中...");
                break;
        }
    }


}
