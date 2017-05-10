package com.pulamsi.photomanager.view;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.bean.Config;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.helper.BannerLocalImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.Arrays;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-17
 * Time: 11:50
 * FIXME
 */
public class StartActivity extends BaseActivity {

    TextView start;
    Integer[] images;
    Config config;

    @Override
    public void initView() {
        config = GreenDaoHelper.getDaoSession().getConfigDao().queryBuilder().unique();
        start = (TextView) findViewById(R.id.tv_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setIsShowGuide(false);
                GreenDaoHelper.getDaoSession().getConfigDao().update(config);
                AppBus.getInstance().post(Flag.INIT_UPDATE);
                finish();
            }
        });
        Banner banner = (Banner)findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new BannerLocalImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(false);
        banner.setScrollContainer(false);
//        banner.setBannerAnimation(Transformer.Accordion);//切换动画
        //设置图片集合

        //资源文件
        images = new Integer[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3,R.mipmap.guide_4,R.mipmap.guide_5};
        banner.setImages(Arrays.asList(images));
        //banner设置方法全部调用完毕时最后调用
        banner.start();//不能删除
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                            if (position == images.length){
                                AnimationUtils animationUtils = new AnimationUtils();
                                Animation animation = animationUtils.loadAnimation(context, R.anim.umeng_socialize_fade_in);
                                start.setAnimation(animation);
                                start.setVisibility(View.VISIBLE);
                            }else {
                                start.setVisibility(View.GONE);
                            }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setStatusBar() {
//        super.setStatusBar();
//        StatusBarUtil.setColorNoTranslucent(this, Color.BLACK);//透明状态栏
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_start;
    }
}
