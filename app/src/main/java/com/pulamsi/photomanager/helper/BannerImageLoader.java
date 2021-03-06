package com.pulamsi.photomanager.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-12-16
 * Time: 11:23
 * FIXME
 */
public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context)
                .load(path)
                .into(imageView);
    }
}
