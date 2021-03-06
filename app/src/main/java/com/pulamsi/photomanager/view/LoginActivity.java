package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.prestener.LoginActivityPrestener;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.trycatch.mysnackbar.Prompt;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.Map;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-29
 * Time: 14:47
 * FIXME
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.iv_wechat)
    ImageView weChat;

    @BindView(R.id.iv_qq)
    ImageView qq;

    UMShareAPI mShareAPI;
    LoginActivityPrestener loginActivityPrestener;
    private boolean isGoBack;


    protected void init() {
        isGoBack = getIntent().getBooleanExtra("isGoBack", false);

        loginActivityPrestener = new LoginActivityPrestener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle("登录");
        mShareAPI = UMShareAPI.get(this);//微信授权
        weChat.setOnClickListener(this);
        qq.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void setStatusBar() {
//        super.setStatusBar();
//        StatusBarUtil.setTranslucentForImageView(this, mViewNeedOffset);
        StatusBarUtil.setTransparentForImageView(this, toolbar);//此处应为toolbar
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wechat:
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.iv_qq:
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
        }
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoging();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            for (String key : data.keySet()) {
                Log.e("key = " + key + "    value= " + data.get(key));
            }
            loginActivityPrestener.login(data);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SnackbarUtil.showPrompt(getWindow().getDecorView(), "授权失败", Prompt.ERROR);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SnackbarUtil.showPrompt(getWindow().getDecorView(), "用户取消", Prompt.WARNING);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoginSuccessful() {
        AppBus.getInstance().post(Flag.REFRESH_CLASSIFICATION);
        SnackbarUtil.showLoadSuccess("登录成功");
        if (isGoBack) {
            finish();
        } else {
            readyGo(InfoActivity.class);
            finish();
        }
    }

    @Override
    public void showLoging() {
        SnackbarUtil.showLoading(getWindow().getDecorView(),"登录中...");
    }

    @Override
    public void showLoginError() {
        SnackbarUtil.showLoadError("登录失败");
    }
}
