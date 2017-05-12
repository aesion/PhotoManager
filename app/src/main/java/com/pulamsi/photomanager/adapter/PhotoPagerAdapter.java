package com.pulamsi.photomanager.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.Flag;

import java.util.ArrayList;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-14
 * Time: 11:23
 * FIXME
 */
public class PhotoPagerAdapter extends PagerAdapter {

    ArrayList<View> viewArrayList;
    ArrayList<String> imgs;
    Context context;

    public PhotoPagerAdapter(ArrayList<View> viewArrayList, ArrayList<String> imgs, Context context) {
        this.viewArrayList = viewArrayList;
        this.imgs = imgs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return viewArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewArrayList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(viewArrayList.get(position));
        View view = viewArrayList.get(position);
        String imgUrl = imgs.get(position);

        //loading
        final ProgressBar loading = new ProgressBar(context);
        FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingLayoutParams.gravity = Gravity.CENTER;
        loading.setLayoutParams(loadingLayoutParams);
        ((FrameLayout)view).addView(loading);

        PhotoView photoView = (PhotoView) view.findViewById(R.id.img);
        // 启用图片缩放功能
        photoView.enable();

/*        Glide.with(context)//更改图片加载框架
                .load(imgUrl)
//                .placeholder(R.drawable.photo_loading) //占位符 也就是加载中的图片，可放个gif
//                .centerCrop()
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);*/

                Glide.with(context)//更改图片加载框架
                .load(imgUrl)
//                .placeholder(R.drawable.photo_loading) //占位符 也就是加载中的图片，可放个gif
//                .centerCrop()
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new GlideDrawableImageViewTarget(photoView){
                                    @Override
                                    public void onLoadStarted(Drawable placeholder) {
                                        super.onLoadStarted(placeholder);
                               /* if(smallImageView!=null){
                                    smallImageView.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(imgurl).into(smallImageView);
                                }*/
                                        loading.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                                        loading.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                        super.onResourceReady(resource, animation);
                                        loading.setVisibility(View.GONE);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                                    }
                                });



        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppBus.getInstance().post(Flag.PHOTO_SHOW_HIDE);
            }
        });
        return view;
    }
}
