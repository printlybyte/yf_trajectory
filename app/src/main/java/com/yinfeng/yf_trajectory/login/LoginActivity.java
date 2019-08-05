//package com.yinfeng.yf_trajectory.login;
//
//import android.content.Intent;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.caitiaobang.core.app.app.BaseActivity;
//import com.caitiaobang.core.app.net.GenericsCallback;
//import com.caitiaobang.core.app.net.JsonGenericsSerializator;
//import com.caitiaobang.core.app.utils.ActivityUtils;
//import com.caitiaobang.core.app.utils.ConmonUtil;
//import com.caitiaobang.core.app.utils.EncryptUtils;
//import com.caitiaobang.core.app.utils.RegexUtils;
//import com.caitiaobang.core.app.utils.StatusBarUtil;
//import com.caitiaobang.pro.Api;
//import com.caitiaobang.pro.FinalUtils;
//import com.caitiaobang.pro.HawkKey;
//import com.caitiaobang.pro.MainActivity;
//import com.caitiaobang.pro.R;
//import com.caitiaobang.pro.activity.bean.LoginBean;
//import com.caitiaobang.pro.activity.utils.InstallAppUtils;
//import com.google.gson.Gson;
//import com.orhanobut.hawk.Hawk;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import java.util.HashMap;
//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.wechat.friends.Wechat;
//import okhttp3.Call;
//
///**
// * ============================================
// * 描  述：
// * 包  名：com.caitiaobang.pro.activity.moudle.login
// * 类  名：LoginActivity
// * 创建人：lgd
// * 创建时间：2019/6/26 14:10
// * ============================================
// **/
//public class LoginActivity extends BaseActivity implements View.OnClickListener {
//
//
//    /**
//     * 获取验证码
//     */
//    private Button mActivityLoginConfirm;
//    /**
//     * 注册
//     */
//    private TextView mActivityLoginRegister;
//    /**
//     * 密码登录
//     */
//    private TextView mActivityPasswordLogin;
//    /**
//     * 请输入手机号
//     */
//    private EditText mActivityLoginPhone;
//    private ImageView mActivityLoginQqLogin;
//    private ImageView mActivityLoginWechatLogin;
//    private ImageView mActivityLoginWeiboLogin;
//
//    @Override
//    protected int getContentLayoutId() {
//        return R.layout.activity_login;
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        StatusBarUtil.transparencyBar(LoginActivity.this);
//        StatusBarUtil.statusBarLightMode(LoginActivity.this);
//        mActivityLoginConfirm = (Button) findViewById(R.id.activity_login_confirm);
//        mActivityLoginConfirm.setOnClickListener(this);
//        mActivityLoginRegister = (TextView) findViewById(R.id.activity_login_register);
//        mActivityLoginRegister.setOnClickListener(this);
//        mActivityPasswordLogin = (TextView) findViewById(R.id.activity_password_login);
//        mActivityPasswordLogin.setOnClickListener(this);
//
////        OkGo.<String>post("https://www.zhitiaobang.com/app/public/home/Sms/sendCode")//
////                .tag(this)//
//////                .headers("header1", "headerValue1")//
////                .params("mobile", "17600065050")//
////                .params("type", "101")//101: app注册; 102: app登录; 103 app修改密码; 104 app修改手机号
////                .params("sign", "e1cda47df3391f3fa2d349ac19231759")//101: app注册; 102: app登录; 103 app修改密码; 104 app修改手机号
//////                .isMultipart(true)         //强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
////                .execute(new StringCallback() {
////                    @Override
////                    public void onSuccess(Response<String> response) {
////                        String data = response.body();//这个就是返回来的结果
////                        Timber.i("success:%s", data);
////                        ConmonBean bean = new Gson().fromJson(data, ConmonBean.class);
////                        if (bean.isSuccess()) {
////                            MToast.makeTextLong(mContext, bean.getMessage());
//////                            setButtonStatusOn();
////                        } else {
////                            MToast.makeTextLong(mContext, bean.getMessage());
////                            Log.i("TESTD",""+ bean.getMessage());
//////                            setButtonStatusOn();
////                            showToastC(bean.getMessage());
////                        }
////
////
////                    }
////                    @Override
////                    public void onError(Response<String> response) {
////                        super.onError(response);
////                        ConmonBean bean = new Gson().fromJson(response.body(), ConmonBean.class);
////                        MToast.makeTextLong(mContext, bean.getMessage());
//////                        setButtonStatusOn();
////                    }
////                });
//        mActivityLoginPhone = (EditText) findViewById(R.id.activity_login_phone);
//        mActivityLoginQqLogin = (ImageView) findViewById(R.id.activity_login_qq_login);
//        mActivityLoginQqLogin.setOnClickListener(this);
//        mActivityLoginWechatLogin = (ImageView) findViewById(R.id.activity_login_wechat_login);
//        mActivityLoginWechatLogin.setOnClickListener(this);
//        mActivityLoginWeiboLogin = (ImageView) findViewById(R.id.activity_login_weibo_login);
//        mActivityLoginWeiboLogin.setOnClickListener(this);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            default:
//                break;
//            case R.id.activity_login_confirm:
//                String mPhone = mActivityLoginPhone.getText().toString().trim();
//                if (TextUtils.isEmpty(mPhone)) {
//                    showToastC("请输入手机号");
//                    return;
//                }
//                if (!RegexUtils.isMobileExact(mPhone)) {
//                    showToastC("请输入正确的手机号格式");
//                    return;
//                }
//                Hawk.put(HawkKey.LOGIN_PHONE, mPhone);
//                startActivity(new Intent(LoginActivity.this, GetVerifyCodeActivity.class).putExtra("jump_flag", mPhone));
//                break;
//            case R.id.activity_login_register:
//                ActivityUtils.startActivity(RegisterActivity.class);
//                break;
//            case R.id.activity_password_login:
//                String mJumpPhone = mActivityLoginPhone.getText().toString();
//                startActivity(new Intent(LoginActivity.this, LoginVerActivity.class).putExtra(FinalUtils.JUMP_INTENT_KEY, mJumpPhone));
//
//                break;
//            case R.id.activity_login_qq_login:
//                if (InstallAppUtils.isQQClientAvailable(LoginActivity.this)) {
//                    showToastC("正在打开QQ...");
//                    final Platform wechat = ShareSDK.getPlatform(QQ.NAME);
//                    /*final Platform qq = ShareSDK.getPlatform(QQ.NAME);*/
//                    /*final Platform sinaweibo = ShareSDK.getPlatform(SinaWeibo.NAME);*/
//                    if (wechat.isClientValid()) {
//                        //客户端可用
//                    }
//                    if (wechat.isAuthValid()) {
//                        wechat.removeAccount(true);
//                    }
//                    wechat.setPlatformActionListener(new PlatformActionListener() {
//                        @Override
//                        public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
//                            /*platform.getDb().exportData()获取用户数据*/
//                            Log.i("ShareSDK", "onComplete ---->  登录成功" + platform.getDb().exportData());
//                            platform.getDb().getUserId();
//                            String mUserId = platform.getDb().getUserId();
//                            String mUserName = platform.getDb().getUserName();
//                            String mUserIcon = platform.getDb().getUserIcon();
//                            requestUserLogin(1, "3", mUserId, mUserIcon, mUserName);
//                            // 这里授权成功跳转到程序主界面了
//                        }
//
//                        @Override
//                        public void onError(Platform platform, int i, Throwable throwable) {
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.toString());
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.getStackTrace().toString());
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.getMessage());
//                        }
//
//                        @Override
//                        public void onCancel(Platform platform, int i) {
//                            Log.i("ShareSDK", "onCancel ---->  登录取消");
//                        }
//                    });
//                    wechat.SSOSetting(false);
//                    wechat.showUser(null);
//                } else {
//                    showToastC("检测到当前手机未安装QQ客户端，请前往应用商店下载最新版");
//                }
//                break;
//            case R.id.activity_login_wechat_login:
//                if (InstallAppUtils.isWeixinAvilible(LoginActivity.this)) {
//                    showToastC("正在打开微信...");
//                    final Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                    /*final Platform qq = ShareSDK.getPlatform(QQ.NAME);*/
//                    /*final Platform sinaweibo = ShareSDK.getPlatform(SinaWeibo.NAME);*/
//                    if (wechat.isClientValid()) {
//                        //客户端可用
//                    }
//                    if (wechat.isAuthValid()) { wechat.removeAccount(true);
//                    }
//
//                    wechat.setPlatformActionListener(new PlatformActionListener() {
//                        @Override
//                        public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
//                            /*platform.getDb().exportData()获取用户数据*/
//                            Log.i("ShareSDK", "onComplete ---->  登录成功" + platform.getDb().exportData());
//                            String mUserId = platform.getDb().getUserId();
//                            String mUserName = platform.getDb().getUserName();
//                            String mUserIcon = platform.getDb().getUserIcon();
//                            requestUserLogin(1, "2", mUserId, mUserIcon, mUserName);
//                            // 这里授权成功跳转到程序主界面了
//                        }
//
//                        @Override
//                        public void onError(Platform platform, int i, Throwable throwable) {
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.toString());
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.getStackTrace().toString());
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.getMessage());
//                        }
//
//                        @Override
//                        public void onCancel(Platform platform, int i) {
//                            Log.i("ShareSDK", "onCancel ---->  登录取消");
//                        }
//                    });
//                    wechat.SSOSetting(false);
//                    wechat.showUser(null);
//                } else {
//                    showToastC("检测到当前手机未安装微信客户端，请前往应用商店下载最新版");
//                }
//
//                break;
//            case R.id.activity_login_weibo_login:
//                if (InstallAppUtils.isSinaInstalled(LoginActivity.this)) {
//                    showToastC("正在打开微博...");
//                    final Platform sinaweibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                    if (sinaweibo.isClientValid()) {
//                        //客户端可用
//                    }
//                    if (sinaweibo.isAuthValid()) {
//                        sinaweibo.removeAccount(true);
//                    }
//                    sinaweibo.setPlatformActionListener(new PlatformActionListener() {
//                        @Override
//                        public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
//                            /*platform.getDb().exportData()获取用户数据*/
//                            Log.i("ShareSDK", "onComplete ---->  登录成功" + platform.getDb().exportData());
//                            String mUserId = platform.getDb().getUserId();
//                            String mUserName = platform.getDb().getUserName();
//                            String mUserIcon = platform.getDb().getUserIcon();
//                            requestUserLogin(1, "2", mUserId, mUserIcon, mUserName);
//                            // 这里授权成功跳转到程序主界面了
//                        }
//
//                        @Override
//                        public void onError(Platform platform, int i, Throwable throwable) {
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.toString());
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.getStackTrace().toString());
//                            Log.i("ShareSDK", "onError ---->  登录失败" + throwable.getMessage());
//
//                        }
//
//                        @Override
//                        public void onCancel(Platform platform, int i) {
//                            Log.i("ShareSDK", "onCancel ---->  登录取消");
//                        }
//                    });
//                    sinaweibo.SSOSetting(false);
//                    sinaweibo.showUser(null);
//
//                } else {
//                    showToastC("检测到当前手机未安装微博客户端，请前往应用商店下载最新版");
//                }
//                break;
//        }
//    }
//
//
//    /**
//     * 登录 0:账号密码;1:账号验证码;2:微信;3:qq;4:微博
//     */
//
//
//    private void requestUserLogin(final int type, String mType, String mAccount, String headimgUrl, String username) {
//        if (!ConmonUtil.isConnected(mContext)) {
//            showToastC("网络无链接,请稍后在试");
//            return;
//        }
//        String md5 = ("account" + mAccount + "headimgUrl" + headimgUrl + "type" + mType + "username" + username + FinalUtils.MDK_KEY).trim();
//        String md5encrypt = EncryptUtils.encryptMD5ToString(md5).toLowerCase();
//        Log.i("testd", "md5：" + md5 + "  md5encrypt: " + md5encrypt);
//        if (type == 1) {
//            showProgress("登陆中...");
//        }
//        OkHttpUtils
//                .post()
//                .addParams("account", mAccount)
//                .addParams("headimgUrl", headimgUrl)
//                .addParams("type", mType)
//                .addParams("username", username)
//                .addParams("sign", md5encrypt)
//                .url(Api.UserLogin)
//                .build()
//                .execute(new GenericsCallback<LoginBean>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        showToastC("网络异常，请稍后重试" + e.getMessage());
//                        if (type == 1) {
//                            dismisProgress();
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(LoginBean response, int id) {
//                        if (type == 1) {
//                            dismisProgress();
//                        }
//                        if (response != null && response.isSuccess()) {
//                            //showToastC(response.getMessage());
//                            Log.i("testr", "网络结果：" + new Gson().toJson(response));
//
//                            showToastC("登录成功");
//                            Hawk.put("already_login_ctb", "1");
//                            Hawk.put(HawkKey.BASE_USER_BEAN, response.getResult());
//                            ActivityUtils.startActivity(MainActivity.class);
//
//                        } else {
//                            showToastC(response.getMessage());
//                        }
//                        dismisProgress();
//                    }
//                });
//    }
//
//}
