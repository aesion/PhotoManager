package com.pulamsi.photomanager.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-10
 * Time: 14:20
 * FIXME
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;
    List<String> titles;
    Context context;
    FragmentManager supportFragmentManager;

    public FragmentAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments, List<String> titles, Context context) {
        super(supportFragmentManager);
        this.fragments = fragments;
        this.titles = titles;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        supportFragmentManager.beginTransaction().remove(fragments.get(position));
//        ViewPager在切换的时候，如果频繁销毁和加载Fragment，就容易产生卡顿现象，阻止Fragment的销毁可有效减缓卡顿现象。
    }
}
