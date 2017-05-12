package com.pulamsi.photomanager.viewHolder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.utils.DensityUtil;

import java.io.File;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-09
 * Time: 12:32
 * FIXME
 */
public class DownloadViewHolder extends BaseViewHolder<String> {

    ImageView img;

    public DownloadViewHolder(ViewGroup viewGroup) {
        super(viewGroup, R.layout.download_item);//Item的布局写在这里
        img = $(R.id.iv_img);
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        if (!new File(data).exists()){
            return;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(data, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.height = imageHeight * (DensityUtil.getWidth() / 2) / imageWidth;//这一句很重要
        layoutParams.width = DensityUtil.getWidth() / 2;//这一句很重要

//            int height = data.getHeight()*width/data.getWidth();//计算View的高度
        img.setLayoutParams(layoutParams);

        Glide.with(getContext())//更改图片加载框架
                .load(data)
                .crossFade()
                .into(img);

    }




}
