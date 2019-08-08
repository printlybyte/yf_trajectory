//package com.yinfeng.yf_trajectory.moudle.login;
//
//import android.os.CountDownTimer;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.amap.api.location.AMapLocation;
//import com.bumptech.glide.Glide;
//import com.caitiaobang.core.app.app.AppManager;
//import com.caitiaobang.core.app.app.BaseActivity;
//import com.caitiaobang.core.app.app.BaseApplication;
//import com.caitiaobang.core.app.bean.GreendaoAreaBean;
//import com.caitiaobang.core.app.bean.GreendaoCityBean;
//import com.caitiaobang.core.app.bean.GreendaoProvinceBean;
//import com.caitiaobang.core.app.net.GenericsCallback;
//import com.caitiaobang.core.app.net.JsonGenericsSerializator;
//import com.caitiaobang.core.app.utils.ActivityUtils;
//import com.caitiaobang.core.app.utils.ConmonUtil;
//import com.caitiaobang.core.app.utils.EncryptUtils;
//import com.caitiaobang.core.app.utils.NetworkUtils;
//import com.caitiaobang.core.app.utils.ScreenUtils;
//import com.caitiaobang.core.greendao.gen.DaoSession;
//import com.google.gson.Gson;
//import com.orhanobut.hawk.Hawk;
//import com.yinfeng.yf_trajectory.moudle.activity.MainActivity;
//import com.yinfeng.yf_trajectory.R;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.MediaType;
//
//public class InitializeActivity extends BaseActivity implements View.OnClickListener {
//
//
//    private ImageView mActivityInitializeImg;
//    private TextView mActivityInitializeTxt;
//
//    @Override
//    protected int getContentLayoutId() {
//        return R.layout.activity_initialize;
//    }
//
////    LocationUtils mLocation;
//
//    @Override
//    protected void initData() {
//
//
////        List<GreendaoProvinceBean> mList = searchByConditionProvince();
////        if (mList == null || mList.size() == 0) {
////            requestDate(0);
////        }
//
////        mLocation = new LocationUtils();
////        mLocation.setmLocationCallBack(new LocationUtils.LocationCallBack() {
////            @Override
////            public void setLocation(AMapLocation location) {
////                if (location != null) {
////                    if (location.getErrorCode() == 0) {
////                        double lat = location.getLatitude();//获取纬度
////                        double lng = location.getLongitude();//获取经度
////                        // 设置当前地图显示为当前位置
////                        Hawk.put(HawkKey.LOCTION_LAT, lat + "");
////                        Hawk.put(HawkKey.LOCTION_LNG, lng + "");
////                        Hawk.put(HawkKey.LOCTION_AD_CODE, location.getAdCode() + "");
////                        Hawk.put(HawkKey.LOCTION_ADDRESS, location.getAddress() + "");
////                    } else {
////                        LocationErrUtils.getInstance().showErr(location.getErrorCode());
////                        time.cancel();
////                        time = null;
////                        finish();
////                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
////                        Log.e("AmapError   ", "location Error, ErrCode:"
////                                + location.getErrorCode() + ", errInfo:"
////                                + location.getErrorInfo());
////                    }
////                }
////            }
////        });
//    }
//
//    public static List searchByConditionProvince() {
//        DaoSession daoSession = BaseApplication.getDaoInstant();
//        try {
//            return daoSession.queryBuilder(GreendaoProvinceBean.class).list();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        ScreenUtils.setFullScreen(InitializeActivity.this);
//        if (!NetworkUtils.isAvailableByPing()) {
//            showToastC("网络链接不可用");
//            finish();
//            return;
//        }
////        time = new TimeCount(5000, 1000);// 构造CountDownTimer对象
////        time.start();// 开始计时
////        mActivityInitializeImg = (ImageView) findViewById(R.id.activity_initialize_img);
////        mActivityInitializeTxt = (TextView) findViewById(R.id.activity_initialize_txt);
////        mActivityInitializeTxt.setOnClickListener(this);
//
////        ScreenUtils.setFullScreen(InitializeActivity.this);
//
//        Glide.with(mContext).load(R.drawable.splash_start).placeholder(R.drawable.ic_glide_placeholder)
//                .error(R.drawable.ic_glide_error).dontAnimate().into(mActivityInitializeImg);
//
//
//    }
//
////    private TimeCount time;
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            default:
//                break;
////            case R.id.activity_initialize_txt:
////                ActivityUtils.startActivity(MainActivity.class);
////                String isLoginStatus = Hawk.get("already_login_ctb", "");
////                if (!TextUtils.isEmpty(isLoginStatus)) {
////                    AppManager.getInstance().finishAllActivity();
////                    ActivityUtils.startActivity(MainActivity.class);
////                } else {
////                    ActivityUtils.startActivity(LoginActivity.class);
////            ActivityUtils.startActivity(MainActivity.class);
////            ActivityUtils.startActivity(InfoSelectedSexActivity.class);
////            ActivityUtils.startActivity(InfoPullSignatureActivity.class);
////                }
////
////                time.cancel();
////                time = null;
////                finish();
////                break;
//        }
//    }
//
//    // timer Util
//    /* 定义一个倒计时的内部类 */
////    class TimeCount extends CountDownTimer {
////        public TimeCount(long millisInFuture, long countDownInterval) {
////            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
////        }
////
////        @Override
////        public void onFinish() {// 计时完毕时触发
//////            mActivityInitializeTxt.setText("重新验证");
////            mActivityInitializeTxt.setClickable(true);
//////            ActivityUtils.startActivity(MainActivity.class);
////            String isLoginStatus = Hawk.get("already_login_ctb", "");
////            if (!TextUtils.isEmpty(isLoginStatus)) {
////                AppManager.getInstance().finishAllActivity();
//////                ActivityUtils.startActivity(MainActivity.class);
////                ActivityUtils.startActivity(MainActivity.class);
////            } else {
////                ActivityUtils.startActivity(LoginActivity.class);
//////            ActivityUtils.startActivity(MainActivity.class);
//////            ActivityUtils.startActivity(InfoSelectedSexActivity.class);
//////            ActivityUtils.startActivity(InfoPullSignatureActivity.class);
//////
////            }
////
////            finish();
////        }
////
////        @Override
////        public void onTick(long millisUntilFinished) {// 计时过程显示
////            mActivityInitializeTxt.setText("跳过 " + millisUntilFinished / 1000 + "s");
////        }
////
////    }
//
//
////    private void requestDate(final int type) {
////        if (!ConmonUtil.isConnected(mContext)) {
////            showToastC("网络无链接,请稍后在试");
////            return;
////        }
////        String md5 = ("version" + "20180101" + FinalUtils.MDK_KEY).trim();
////        String md5encrypt = EncryptUtils.encryptMD5ToString(md5).toLowerCase();
////        Log.i("testd", "md5：" + md5 + "  md5encrypt: " + md5encrypt);
////        if (type == 1) {
////            showProgress("下载数据中...");
////        }
////        Map<String, Object> map = new LinkedHashMap<>();
////        map.clear();
////        map.put("version", "20180101");
////        map.put("sign", md5encrypt);
////        Log.i("tests", "发送json：" + new Gson().toJson(map));
////        OkHttpUtils
////                .postString()
////                .content(new Gson().toJson(map))
////                .url(Api.AreaGetNewAreasJson)
////                .mediaType(MediaType.parse("application/json; charset=utf-8"))
////                .build()
////                .execute(new GenericsCallback<AllAddressBean>(new JsonGenericsSerializator()) {
////                    @Override
////                    public void onError(Call call, Exception e, int id) {
////                        showToastC("网络异常，请稍后重试" + e.getMessage());
////                        if (type == 1) {
////                            dismisProgress();
////                        }
////                    }
////
////                    @Override
////                    public void onResponse(AllAddressBean response, int id) {
////                        if (response != null && response.isSuccess()) {
////                            //showToastC(response.getMessage());
////                            Log.i("testr", "网络结果：" + new Gson().toJson(response));
////                            insertAreaData6(response);
////                        } else {
////                            //showToastC(response.getMessage());
////                            dismisProgress();
////                        }
////                    }
////                });
////    }
//
//    //市区
////    public void insertAreaData6(AllAddressBean allAddressBean) {
////        BaseApplication.getDaoInstant().startAsyncSession().runInTx(new Runnable() {
////            @Override
////            public void run() {
////                insetDate(allAddressBean);
////            }
////        });
////    }
//
////    private void insetDate(AllAddressBean allAddressBean) {
////        DaoSession daoSession1 = BaseApplication.getDaoInstant();
////        DaoSession daoSession2 = BaseApplication.getDaoInstant();
////        DaoSession daoSession3 = BaseApplication.getDaoInstant();
////        int mAreasSize = allAddressBean.getResult().getAreas().size();
////        for (int i = 0; i < mAreasSize; i++) {
////            AllAddressBean.ResultBean.AreasBean provinceBean = allAddressBean.getResult().getAreas().get(i);
////            GreendaoProvinceBean greendaoProvinceBean = new GreendaoProvinceBean();
////            greendaoProvinceBean.setProvinceName(provinceBean.getProvince());
////            greendaoProvinceBean.setProvinceNameId(allAddressBean.getResult().getAreas().get(i).getProvinceid() + "");
////            greendaoProvinceBean.setNo(i);
////            daoSession1.insertOrReplace(greendaoProvinceBean);//插入或替换
////
////            int mCitySize = provinceBean.getCityBean().size();
////            for (int i1 = 0; i1 < mCitySize; i1++) {
////                AllAddressBean.ResultBean.AreasBean.CityBean cityBean = allAddressBean.getResult().getAreas().get(i).getCityBean().get(i1);
////                GreendaoCityBean cityBeanx = new GreendaoCityBean();
////                cityBeanx.setProvinceName(allAddressBean.getResult().getAreas().get(i).getProvince());
////                cityBeanx.setProvinceNameId(allAddressBean.getResult().getAreas().get(i).getProvinceid() + "");
////                cityBeanx.setCityName(allAddressBean.getResult().getAreas().get(i).getCityBean().get(i1).getCity() + "");
////                cityBeanx.setCityNameId(allAddressBean.getResult().getAreas().get(i).getCityBean().get(i1).getCityid() + "");
////                cityBeanx.setNo(Integer.parseInt(allAddressBean.getResult().getAreas().get(i).getCityBean().get(i1).getCityid()));
////                daoSession2.insertOrReplace(cityBeanx);//插入或替换
////
//////                Log.i("TESTD", "" + cityBean.getCity());
////                int mChildrenSize = cityBean.getChildren().size();
////                for (int i2 = 0; i2 < mChildrenSize; i2++) {
////                    AllAddressBean.ResultBean.AreasBean.CityBean.ChildrenBean childrenBean = allAddressBean.getResult().getAreas().get(i).getCityBean().get(i1).getChildren().get(i2);
////                    GreendaoAreaBean areaBean = new GreendaoAreaBean();
////                    areaBean.setProvinceName(provinceBean.getProvince());
////                    areaBean.setProvinceNameId(provinceBean.getProvinceid());
////                    areaBean.setCityName(cityBean.getCity() + "");
////                    areaBean.setCityNameID(cityBean.getCityid() + "");
////                    areaBean.setAreaName(childrenBean.getArea() + "");
////                    areaBean.setAreaNameId(childrenBean.getAreaid() + "");
////                    areaBean.setNo(Integer.parseInt(childrenBean.getAreaid()));
////                    daoSession3.insertOrReplace(areaBean);//插入或替换
//////                    Log.i("TESTD", "" + childrenBean.getArea() + "");
////                }
////            }
////        }
////
////    }
//
//
//}
