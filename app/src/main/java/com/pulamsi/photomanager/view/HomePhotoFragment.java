package com.pulamsi.photomanager.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.FragmentAdapter;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.interfaces.IMainactivity;
import com.pulamsi.photomanager.prestener.MainActivityPrestener;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-12-13
 * Time: 15:50
 * FIXME
 */
public class HomePhotoFragment extends Fragment implements IMainactivity,DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener {
    ViewPager mViewPager;
    TabLayout mTabLayout;
    MainActivityPrestener pMainActivity;
    List<String> titles;
    FragmentAdapter adapter;
    ImageView addClassify;

    StatusUIManager statusUIManager;

    View contentView;

    public static boolean isHomeIn;
    public static boolean isShareIn;

    List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, null);
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView = view.findViewById(R.id.view_content);
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);//拿到Activity的mTabLayout
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        addClassify = (ImageView) getActivity().findViewById(R.id.iv_add_classify);
        statusUIManager = new StatusUIManager(getContext(), contentView);
        statusUIManager.setOnRetryClickListener(this);

        pMainActivity = new MainActivityPrestener(this);
        pMainActivity.init(false);
    }

    //再也不想看到这段代码了 - -，因为我自己也看不懂，乱七八槽


    private void setupViewPager(List<Galleryclass> galleryList) {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();

       if (isHomeIn){//是否从首页进来
           addClassify.setVisibility(View.GONE);//隐藏加号

            if (Constants.USER_TYPE != 0){
                for (Galleryclass galleryclass : galleryList) {
                    if (galleryclass.getId().equals("0dce233d-ef3b-4bac-882d-d94d178150a0") || galleryclass.getId().equals("07967f13-d862-41f1-88d5-3a6027ff50a6")){
                        titles.add(galleryclass.getCatName());
                        fragments.add(ListFragment.InstanceFragment(galleryclass.getId()));
                    }
                }
            }
       }else {
           addClassify.setVisibility(View.VISIBLE);//显示加号

           titles.add("最新动态");
           fragments.add(new HotFragment());

           for (Galleryclass galleryclass : galleryList) {
                   titles.add(galleryclass.getCatName());
                   fragments.add(ListFragment.InstanceFragment(galleryclass.getId()));
           }

        }


        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        adapter = new FragmentAdapter(supportFragmentManager, fragments, titles, getActivity());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setTabsFromPagerAdapter(adapter);//不能设置这个否则刷新TAB出错
//        //1.MODE_SCROLLABLE模式
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//可以滚动的
        mViewPager.setOffscreenPageLimit(1);//参数为预加载数量，系统最小值为1。慎用！预加载数量过多低端机子受不了


        if (isShareIn){
            boolean isShow = false;
            isShareIn = false;
            for (int i = 0; i < galleryList.size(); i++) {
                if (galleryList.get(i).getId().equals("e702ff10-687a-4e35-9a53-d92bf2670ebd")){
                    selectTab(i + 1);
                    isShow = true;
                }
            }
            if (!isShow){
                MyApplication.toastor.showToast("您尚未添加此栏目");
            }
        }
    }

    @Override
    public void setContent(List<Galleryclass> GalleryList) {
        setupViewPager(GalleryList);//设置内容
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        statusUIManager.showLoading();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError() {
        statusUIManager.showError();
    }

    @Override
    public void showContent() {
        statusUIManager.showContentView();

    }


    /**
     * 选中哪个
     *
     * @param selectId
     */
    public void selectTab(int selectId) {
        if (selectId < titles.size())
            mTabLayout.getTabAt(selectId).select();
    }

    /**
     * 更新栏目
     */
    public void setChangeView(boolean isNetwork) {
        pMainActivity.init(isNetwork);
    }


    /**
     * 重新加载
     */
    @Override
    public void onClick() {
        pMainActivity.init(true);
    }
}
