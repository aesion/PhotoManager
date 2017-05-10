package com.pulamsi.photomanager.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.FragmentAdapter;
import com.pulamsi.photomanager.base.CommonBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-16
 * Time: 14:50
 * 发布帖列表，戴审核状态
 * FIXME
 */
public class InfoReleaseActivity extends CommonBaseActivity {
    String status;
    String title;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void init() {
        status = getIntent().getStringExtra("status");
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);

        tabLayout = (TabLayout) findViewById(R.id.tb_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vp_viewpager);

        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        titles.add("审核成功");
        titles.add("审核中");
        titles.add("审核失败");

//        auditStatus;// 审核状态 0 不通过 1通过 2待审核
        fragments.add(ReleaseDetailsFragment.newInstance(status,1));
        fragments.add(ReleaseDetailsFragment.newInstance(status,2));
        fragments.add(ReleaseDetailsFragment.newInstance(status,0));

        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles, this);
        viewPager.setAdapter(adapter);
        //1.MODE_SCROLLABLE模式
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//可以滚动的
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_info_release;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.listview_loading;
    }

}
