package com.caitiaobang.core.app.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caitiaobang.core.BuildConfig;
import com.caitiaobang.core.R;
import com.caitiaobang.core.app.utils.ScreenUtils;
import com.maning.mndialoglibrary.MProgressBarDialog;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MToast;
import com.noober.background.BackgroundLibrary;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import timber.log.Timber;

/**
 * @author Administrator
 */
public abstract class BaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate {

    public final RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    protected RxPermissions mRxPermissions;
    protected PlaceHolderView mPlaceHolderView;
    //默认检查网络状态
    protected Context mContext;
    //记录用户首次点击返回键的时间

    //    protected BaseAvLoadingAnim baseAvLoadingAnim;
    protected FragmentManager mFragmentManager;
    protected BGASwipeBackHelper mSwipeBackHelper;

    protected TextView mTitle;
    protected ImageView mTitletMore;
    protected ImageView mTitletSearch;
    protected RelativeLayout mEmail;
    protected LinearLayout mBack, mAdd, mDel;
    protected LinearLayout mTitletGroup;
    protected LinearLayout mSharedGroup;
    protected TextView mTitletBtn;
    protected RelativeLayout mActivbarGroup;
    protected TextView mTitletName, mSharedGroupText;
    protected MProgressBarDialog mProgressBarDialog;
    /**
     * 判断是否是快速点击
     */
    private static long lastClickTime;
    public ActResultRequest actResultRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        BackgroundLibrary.inject(this);

        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getContentLayoutId();
            setContentView(layId);
            initSystemBar();
            initBefore();
            initBefore(savedInstanceState);
            initView();
            initData();

            if (BuildConfig.DEBUG) {
                Timber.plant(new Timber.DebugTree());
            } else {
                Timber.plant(new CrashReportingTree());
            }
        } else {
            finish();
        }
    }

    protected void initWidows() {
        //设置屏幕适配 360为设计图尺寸px/2
        if (ScreenUtils.isPortrait()) {
            ScreenUtils.adaptScreen4VerticalSlide(this, 360);
        } else {
            ScreenUtils.adaptScreen4HorizontalSlide(this, 640);
        }
    }

    public void setIntentFinsh(String msg, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra("JUMP_INTENT_KEY", msg);
        setResult(resultCode, intent);
        finish();
    }

    public void setIntentFinsh(String arg1, String arg2, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra("JUMP_INTENT_KEY", arg1);
        intent.putExtra("JUMP_INTENT_KEY_TWO", arg2);
        setResult(resultCode, intent);
        finish();
    }

    public void setIntentFinsh(int msg, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra("JUMP_INTENT_KEY", msg);
        setResult(resultCode, intent);
        finish();
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }


    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    private void initSystemBar() {
        AppManager.getInstance().addActivity(this); //添加到栈中
//        StatusBarUtil.setStatusBarColor(this, R.color.white);
//        StatusBarUtil.statusBarLightMode(this);
    }

    public void setTitle(String titlemsg) {
        if (mTitle == null) {
            mTitle = findViewById(R.id.main_action_bar_title);
        }
        mTitle.setText(titlemsg + "");
    }

    /**
     * @datetime 2018/9/10 17:57
     * @author liuguodong
     * @parmes
     */
//    public abstract String initTitle();
//     public void    initTitle(String title){
//        mTitle.setText(  + "");
//    };

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {
    }

    protected void initBefore(Bundle savedInstanceState) {

    }
    /**
     * 初始化窗口 在界面为初始化之前调用
     */


    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initView() {
        mContext = this;
        ScreenUtils.setPortrait(this);

        actResultRequest = new ActResultRequest(this);
//        baseAvLoadingAnim=new BaseAvLoadingAnim();
        mRxPermissions = new RxPermissions(this);
        mFragmentManager = getSupportFragmentManager();
        mTitle = findViewById(R.id.main_action_bar_title);
        mTitletBtn = findViewById(R.id.include_actionbar_back_btn);
        mActivbarGroup = findViewById(R.id.main_action_bar_group);
        mTitletMore = findViewById(R.id.include_actionbar_back_more);
        mTitletSearch = findViewById(R.id.activity_map_search);
        //返回
        mBack = findViewById(R.id.main_action_bar_back);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }

    /**
     * @param msg 提示信息
     * @ author mac  @times 2018/7/22  下午4:03
     */
    public void showToast(String msg) {
        MToast.makeTextShort(this, "" + msg);
    }

    /**
     * @ author mac  @times 2018/7/22  下午4:12
     * 默认提示数据是加载中
     */
    public void showProgress() {
        MProgressDialog.showProgress(this);
    }

    public void initProgressBarDialog() {
        //新建一个Dialog
        mProgressBarDialog = new MProgressBarDialog.Builder(mContext)
                .setStyle(MProgressBarDialog.MProgressBarDialogStyle_Circle)
                //全屏背景窗体的颜色
//                .setBackgroundWindowColor(getMyColor(R.color.colorDialogWindowBg))
                //View背景的颜色
//                .setBackgroundViewColor(getMyColor(R.color.colorDialogViewBg2))
                //字体的颜色
//                .setTextColor(getMyColor(R.color.colorAccent))
                //View边框的颜色
//                .setStrokeColor(getMyColor(R.color.white))
                //View边框的宽度
                .setStrokeWidth(2)
                //View圆角大小
                .setCornerRadius(10)
                //ProgressBar背景色
                .setProgressbarBackgroundColor(Color.WHITE)
                //ProgressBar 颜色
                .setProgressColor(Color.WHITE)
                //圆形内圈的宽度
                .setCircleProgressBarWidth(2)
                //圆形外圈的宽度
                .setCircleProgressBarBackgroundWidth(2)
                //水平进度条Progress圆角
                .setProgressCornerRadius(0)
                //水平进度条的高度
                .setHorizontalProgressBarHeight(10)
                //dialog动画
//                .setAnimationID(R.style.animate_dialog_custom)
                .build();
    }

    public void showProgressBar(int progress, String msg) {
        if (mProgressBarDialog != null) {
            mProgressBarDialog.showProgress(progress, msg);
        }
    }

    public void dismissProgressBar() {
        if (mProgressBarDialog != null) {
            if (mProgressBarDialog.isShowing()) {
                mProgressBarDialog.dismiss();
                initProgressBarDialog();
            }
        }
    }

    public void showToastC(String msg) {
        if (mContext == null) {
            mContext = BaseActivity.this;
        }
        MToast.makeTextLong(mContext, msg);
    }

    public void showToastC(int msgid) {
        if (mContext == null) {
            mContext = BaseActivity.this;
        }
        MToast.makeTextLong(mContext, getString(msgid));
    }

    /**
     * @param msg 自定义加载中信息
     * @ author mac  @times 2018/7/22  下午4:12
     */
    public void showProgress(String msg) {
        MProgressDialog.showProgress(this, msg + "");
    }

    /**
     * @ author mac  @times 2018/7/22  下午4:12
     * 取消加载
     */
    public void dismisProgress() {
        MProgressDialog.dismissProgress();
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(false);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();

    }


    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * ============================================
     * 描  述：防止快速点击多次打开activity
     * 包  名：
     * 类  名：BaseActivity
     * 创建人：zhangjiye
     * 创建时间：2019/4/2 9:23
     * ============================================
     **/
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }



}