package com.pulamsi.photomanager.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Config;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.utils.JudgeUtils;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.HomeButtonViewRelativelayout;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.SplashView;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import io.rong.imkit.RongIM;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-22
 * Time: 10:34
 * FIXME
 */
public class HomeTabActivity extends BaseActivity implements HomeButtonViewRelativelayout.OnClickListener {

    /**
     * 再按一次退出程序
     */
    private long exitTime = 0;

    @BindView(R.id.rl_hot_recommend)
    HomeButtonViewRelativelayout hotRecommend;

    @BindView(R.id.rl_find)
    HomeButtonViewRelativelayout find;

    @BindView(R.id.rl_my_store)
    HomeButtonViewRelativelayout myStore;

    @BindView(R.id.rl_trading)
    HomeButtonViewRelativelayout trading;

    @BindView(R.id.rl_raise)
    HomeButtonViewRelativelayout raise;

    @BindView(R.id.rl_share)
    HomeButtonViewRelativelayout share;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home_tab;
    }

    @Override
    public void setStatusBar() {
//        super.setStatusBar();
    }


    private void initViewClick() {
        hotRecommend.setOnClickListener(this);
        find.setOnClickListener(this);
        myStore.setOnClickListener(this);
        trading.setOnClickListener(this);
        raise.setOnClickListener(this);
        share.setOnClickListener(this);
    }


    @Override
    public void initView() {

        initViewClick();
        //引导页
        Config config = GreenDaoHelper.getDaoSession().getConfigDao().queryBuilder().unique();
        if (config.getIsShowGuide()) {
            readyGo(StartActivity.class);
            return;
        }

        if (Constants.isShowSplashView)
            return;

        //闪屏页
        SplashView.simpleShowSplashView(this);
        SplashView.showSplashView(this, 2, R.drawable.splash, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {
            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                initUpdate();//检测更新,引导页时不能自动更新
            }
        });

    }


    private void initUpdate() {
        UpdateKey.API_TOKEN = "fcdaba8a5cc9d62266ba02411cc0e33f";
        UpdateKey.APP_ID = "58aff204ca87a82d5a00003f";
        //下载方式:
        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;//通过Dialog来进行下载
//        UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;默认通过状态栏下载，此参数不能设置，不然不能自动更新！！！！！！
        UpdateFunGO.init(this);

        Constants.isShowSplashView = true;//已经显示过了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(HomeTabActivity.this, mPermissionList, 123);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe   //引导页进入需要初始化更新
    public void startFinish(String flag) {
        if (flag.equals(Flag.INIT_UPDATE)) {
            //引导页进入需要初始化更新
            initUpdate();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
        //注销到bus事件
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppBus.getInstance().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_hot_recommend:
                readyGo(MainActivity.class);
                HomePhotoFragment.isHomeIn = false;
                break;

            case R.id.rl_find:
                readyGo(MainActivity.class);
                HomePhotoFragment.isHomeIn = false;
                break;
            case R.id.rl_my_store:
                if (JudgeUtils.isLogin(HomeTabActivity.this)) {
                    if (Constants.USER_TYPE == 0) {
                        MyApplication.toastor.showToast("没有权限");
                        return;
                    }
                    Intent intent = new Intent(HomeTabActivity.this, MainActivity.class);
                    readyGoAndIntent(intent);
                    HomePhotoFragment.isHomeIn = true;
                }
                break;

            case R.id.rl_trading:
                Intent intent = new Intent(HomeTabActivity.this, MainActivity.class);
                intent.putExtra("pid", "e702ff10-687a-4e35-9a53-d92bf2670ebd");
                readyGoAndIntent(intent);
                HomePhotoFragment.isShareIn = true;
                HomePhotoFragment.isHomeIn = false;
                break;

            case R.id.rl_raise:
                MyApplication.toastor.showToast("功能正在开发，敬请期待！");
                break;

            case R.id.rl_share:

                readyGo(NineCutToolChooseActivity.class);
//                readyGo(NineCutToolActivity.class);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().disconnect();//不设置收不到推送，将会断开链接
    }
}
