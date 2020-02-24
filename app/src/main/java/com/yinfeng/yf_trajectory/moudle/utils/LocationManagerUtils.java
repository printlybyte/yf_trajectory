package com.yinfeng.yf_trajectory.moudle.utils;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.caitiaobang.core.app.app.Latte;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：LocationManagerUtils
 * 创建人：liuguodong
 * 创建时间：2020/2/20 10:27
 * ============================================
 **/
public class LocationManagerUtils {

    private static final LocationManagerUtils ourInstance = new LocationManagerUtils();

    public static LocationManagerUtils getInstance() {
        return ourInstance;
    }

    public LocationManagerUtils() {
    }


    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch;

    public void init() {
        destory();
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(Latte.getApplicationContext());
        }
        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(aMapLocationListener);
        mLocationClient.startLocation();
        Log.i("testre", "初始化单次定位组件....");

        geocoderSearch = new GeocodeSearch(Latte.getApplicationContext());
        geocoderSearch.setOnGeocodeSearchListener(geocodeSearchListener);


    }

    GeocodeSearch.OnGeocodeSearchListener geocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            String getLatitude = regeocodeResult.getRegeocodeQuery().getPoint().getLatitude() + "";
            String getLongitude = regeocodeResult.getRegeocodeQuery().getPoint().getLongitude() + "";
            RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
            String addressx = address.getFormatAddress();
            if (locationListenerm != null) {
                locationListenerm.OnLocationListenerm(1, null,getLatitude,getLongitude,addressx);
            }

        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    Log.i("testre", "初始化单次定位成功...." + amapLocation.getAddress() + "==" + amapLocation.getLatitude() + "==" + amapLocation.getLongitude());
                    //可在其中解析amapLocation获取相应内容。
                    // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    LatLonPoint latLng = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                    RegeocodeQuery query = new RegeocodeQuery(latLng, 1000, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    if (locationListenerm != null) {
                        locationListenerm.OnLocationListenerm(0, amapLocation,"","","");
                    }
                    Log.i("testre", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void destory() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            mLocationOption = null;
            mLocationClient = null;
            Log.i("testre", "释放单次定位组件....");
        }

    }


    public interface OnLocationListenerm {
        void OnLocationListenerm(int status, AMapLocation aMapLocation,String lat,String lng,String addr);
    }

    public void setLocationListenerm(OnLocationListenerm onLocationListenerm) {
        this.locationListenerm = onLocationListenerm;
    }

    private static OnLocationListenerm locationListenerm;


}
