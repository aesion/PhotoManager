package com.pulamsi.photomanager.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.library.StatusBarUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.FragmentAdapter;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.MineCount;
import com.pulamsi.photomanager.interfaces.IInfoActivity;
import com.pulamsi.photomanager.prestener.InfoActivityPrestener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-16
 * Time: 17:22
 * FIXME
 */
public class InfoActivity extends BaseActivity implements IInfoActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.civ_img)
    CircleImageView headerImg;

    @BindView(R.id.tv_upload_count)
    TextView uploadCount;

    @BindView(R.id.tv_fav_count)
    TextView favCount;

    @BindView(R.id.tv_signature)
    TextView signature;

    MineCount mineCount;

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

    public void init() {
        InfoActivityPrestener infoActivityPrestener = new InfoActivityPrestener(this);
        infoActivityPrestener.requestInfo();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_info;
    }


    private void setupViewPager(List<Galleryclass> galleryList) {

        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new CollectionFragment());//收藏
        fragments.add(ReleaseFragment.newInstance(mineCount));//发布

        for (Galleryclass galleryclass : galleryList) {
            titles.add(galleryclass.getCatName());
        }
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles, this);
        mViewPager.setAdapter(adapter);
        //1.MODE_SCROLLABLE模式
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//可以滚动的
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void mineCountBack(MineCount mineCount) {
        this.mineCount = mineCount;

        favCount.setText(mineCount.getCollectionCount() + "");
        uploadCount.setText(mineCount.getPasterCount() + "");

        List<Galleryclass> galleryList = new ArrayList<>();
        Galleryclass galleryclass = new Galleryclass();
        galleryclass.setCatName("收藏 " + mineCount.getCollectionCount());

        Galleryclass galleryclass1 = new Galleryclass();
        galleryclass1.setCatName("发布 " + mineCount.getPasterCount());

        galleryList.add(galleryclass);
        galleryList.add(galleryclass1);
        setupViewPager(galleryList);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(InfoActivity.this)//更改图片加载框架
                .load(Constants.IMG_URL.contains("http://") ? Constants.IMG_URL : MyApplication.context.getString(R.string.imgbaseurl) + Constants.IMG_URL)
                .centerCrop()
//                    .placeholder(R.mipmap.ic_login_avatar) 设置了之后图片缓存不会立即出来
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(headerImg);
        toolbarLayout.setTitle(Constants.NAME);
        if (!TextUtils.isEmpty(Constants.AUTO_GRAPH)) {
            signature.setText(Constants.AUTO_GRAPH);
        }
    }

    @OnClick(R.id.civ_img)
    public void onInfoMessage() {
        readyGo(InfoDataActivity.class);
    }


}
