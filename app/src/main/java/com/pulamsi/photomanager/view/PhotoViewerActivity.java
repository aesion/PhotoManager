package com.pulamsi.photomanager.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.PhotoPagerAdapter;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.utils.ImageUtil;
import com.pulamsi.photomanager.utils.ShareUtils;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.squareup.otto.Subscribe;
import com.umeng.socialize.UMShareAPI;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-14
 * Time: 11:00
 * FIXME
 */
public class PhotoViewerActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tv_item_current)
    TextView currentItem;

    @BindView(R.id.tv_item_total)
    TextView totalItem;

    @BindView(R.id.iv_back)
    ImageView back;

    @BindView(R.id.rl_header)
    RelativeLayout rl_header;

    @BindView(R.id.rl_footer)
    RelativeLayout rl_footer;

    private int position;
    private ArrayList<String> imgs;

    ViewGroup viewGroup;
    boolean isShow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void setStatusBar() {
//        super.setStatusBar();
        StatusBarUtil.setColorNoTranslucent(this, Color.BLACK);//透明状态栏
    }


    private void initData() {
//        UmengTool.getSignature(this);//校验分享的签名
//        UmengTool.checkWx(this);
        viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
        imgs = getIntent().getStringArrayListExtra("imgs");

        position = getIntent().getIntExtra("position", 0);
        LayoutInflater inflater = getLayoutInflater();

        ArrayList<View> viewArrayList = new ArrayList<>();
        for (String img : imgs) {
            View view = inflater.inflate(R.layout.view_pager_photo, null);
            viewArrayList.add(view);
        }

        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(viewArrayList, imgs, PhotoViewerActivity.this);
        viewPager.setAdapter(photoPagerAdapter);
        viewPager.setCurrentItem(position);
        currentItem.setText(position + 1 + "");
        totalItem.setText(imgs.size() + "");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                currentItem.setText(i + 1 + "");
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_photo_viewer;
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }


    @OnClick(R.id.iv_save)
    public void onSave() {
        requestSavePermissions();
    }

    /**
     * 请求SD卡权限
     */
    private void requestSavePermissions() {
        MPermissions.requestPermissions(PhotoViewerActivity.this, Constants.REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(Constants.REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        Save();//准备保存
    }


    /**
     * 保存图片
     */
    private void Save() {
        Glide.with(context)
                .load(imgs.get(position))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        SnackbarUtil.showLoading(viewGroup, "保存中...");
                        ImageUtil.saveImage(context, resource, getWindow().getDecorView(), false, 0);
                    }
                });
    }

    @PermissionDenied(Constants.REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {
        MyApplication.toastor.showToast("对不起，没有读取SD卡的权限，请同意授权");
    }

    @OnClick(R.id.iv_share)
    public void onShare() {
        Glide.with(context)
                .load(imgs.get(position))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ShareUtils.getInstance().shareImage(PhotoViewerActivity.this, resource);
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Subscribe   //订阅事件DataChangedEvent
    public void SubscribeHideDialog(String flag) {
        if (flag.equals(Flag.PHOTO_SHOW_HIDE)) {
            if (isShow) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.top_out);
                animation.setDuration(300);
                animation.setFillAfter(true);
                rl_header.startAnimation(animation);

                Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.umeng_socialize_slide_out_from_bottom);
                animation2.setDuration(300);
                animation2.setFillAfter(true);
                rl_footer.startAnimation(animation2);
                isShow = false;
            } else {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.top_in);
                animation.setDuration(300);
                animation.setFillAfter(true);
                rl_header.startAnimation(animation);

                Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.umeng_socialize_slide_in_from_bottom);
                animation2.setDuration(300);
                animation2.setFillAfter(true);
                rl_footer.startAnimation(animation2);
                isShow = true;
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        //注销到bus事件
        AppBus.getInstance().unregister(this);
    }
}
