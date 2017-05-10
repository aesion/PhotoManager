package com.pulamsi.photomanager.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.litesuits.common.utils.PackageUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.utils.OtherUtils;
import com.pulamsi.photomanager.utils.ShareUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hugeterry.updatefun.UpdateFunGO;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-16
 * Time: 15:42
 * FIXME
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView version;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bt_update)
    Button update;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    private String home = "http://www.pulamsi.com";

    protected void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle("关于");
        version.setText(String.format("当前版本: %s (Build %s)", PackageUtil.getAppPackageInfo(context).versionName, PackageUtil.getAppPackageInfo(context).versionCode));
    }

    @Override
    public void setStatusBar() {
//        super.setStatusBar();
        StatusBarUtil.setTransparentForImageView(this, toolbar);//此处应为toolbar
//        android:fitsSystemWindows="true" 不可乱用

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about;
    }


    @OnClick(R.id.bt_update)
    public void onUpdate() {
        UpdateFunGO.manualStart(this);
    }


    @OnClick(R.id.bt_bug)
    public void onBug() {
        readyGo(FeedbackActivity.class);
    }


    @OnClick(R.id.bt_home)
    public void onHome() {
        OtherUtils.redirectBrowser(home, AboutActivity.this);
    }

    @OnClick(R.id.bt_share)
    public void onShare() {
        ShareUtils.getInstance().shareApp(AboutActivity.this);
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
    }
}

