package com.pulamsi.photomanager.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.photomanager.R;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-15
 * Time: 15:32
 * FIXME
 */
public abstract class CommonBaseActivity extends BaseActivity {

    ViewGroup mContentViewContainer;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void init();

    @Override
    public void initView() {
        toolbar.setTitle(getString(getToolBarTitleID()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentViewContainer = (ViewGroup) findViewById(R.id.base_content);
        View view = LayoutInflater.from(this).inflate(getContentViewID(), null);
        mContentViewContainer.addView(view);
        init();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_common_main;
    }

    protected abstract int getContentViewID();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBack(){
        finish();
    }

    protected abstract int getToolBarTitleID();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

}
