package com.pulamsi.photomanager.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.pulamsi.photomanager.utils.ThemeUtils;

import butterknife.ButterKnife;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-10
 * Time: 11:50
 * FIXME
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        init();
        setStatusBar();//沉浸式状态栏
        initView();
    }

    public void setStatusBar() {
        //沉浸式状态栏
        StatusBarUtil.setColorNoTranslucent(this, ThemeUtils.getThemeColorRes(this));//透明状态栏
        //沉浸式状态栏参考此项目https://github.com/laobie/StatusBarUtil

    }


    public void initView() {
    }

    private void init() {
        context = this;
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
    }

    protected abstract int getContentViewLayoutID();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void readyGoAndIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
