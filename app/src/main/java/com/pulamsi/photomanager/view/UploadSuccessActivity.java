package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-22
 * Time: 13:58
 * FIXME
 * Fragment的setUserVisibleHint在这种情况下是不会调用的,所以很烦
 */
public class UploadSuccessActivity extends CommonBaseActivity implements View.OnClickListener {

    FrameLayout backInfo;

    FrameLayout backHome;


    @Override
    protected void init() {
        backInfo = (FrameLayout) findViewById(R.id.fl_back_info);
        backHome = (FrameLayout) findViewById(R.id.fl_back_home);
        backHome.setOnClickListener(this);
        backInfo.setOnClickListener(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.upload_success_activity;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.upload_success_title;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_back_info:
                Intent intent = new Intent(UploadSuccessActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.fl_back_home:
                finish();
                break;
        }
    }
}
