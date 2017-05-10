package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.ThemeAdapter;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.bean.ThemeItem;
import com.pulamsi.photomanager.utils.SharedPreferencesHelper;
import com.pulamsi.photomanager.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-15
 * Time: 15:22
 * FIXME
 */
public class ThemeActicity extends CommonBaseActivity {

    ThemeAdapter themeAdapter;

    private boolean isRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        isRefresh = getIntent().getBooleanExtra("isRefresh",false);

        LRecyclerView lRecyclerView = (LRecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        final String[] themeArray = getResources().getStringArray(R.array.array_theme);
        final String[] themeArrayValues = getResources().getStringArray(
                R.array.array_theme_values);
        final List<ThemeItem> data = new ArrayList<>();
        for (int i = 0; i < themeArray.length; i++) {
            ThemeItem themeItem = new ThemeItem();
            themeItem.color = ThemeUtils.getThemeColorRes(themeArrayValues[i]);
            themeItem.name = themeArray[i];
            themeItem.value = Integer.parseInt(themeArrayValues[i]);
            data.add(themeItem);
        }

        lRecyclerView.setLayoutManager(layoutManager);

        themeAdapter = new ThemeAdapter(ThemeActicity.this);
        themeAdapter.addDataList(data);

        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(themeAdapter);
        lRecyclerView.setAdapter(mLRecyclerViewAdapter);
        lRecyclerView.setPullRefreshEnabled(false);


    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_theme_change;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.change_title;
    }


    @Override
    public void onBack() {
        if (!isRefresh){
            finish();
        }else {
            Intent intent = new Intent(ThemeActicity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            http://blog.csdn.net/u010142437/article/details/22405813 activity启动FLAG之FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent);
            finish();
        }

    }

    public void OnChangeTheme(int position) {
        ThemeItem themeItem = themeAdapter.getDataList().get(position);
        if (themeItem.value == SharedPreferencesHelper.getInstance().getThemeType(ThemeActicity.this)) {
            return;
        }
        SharedPreferencesHelper.getInstance().setThemeType(ThemeActicity.this, themeItem.value);
        //延迟刷新，避免出现切换按钮动画没完成的卡顿画面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新本界面
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.putExtra("isRefresh",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        }, 200);
    }

}
