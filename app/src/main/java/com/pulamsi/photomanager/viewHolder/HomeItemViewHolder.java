package com.pulamsi.photomanager.viewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.utils.DensityUtil;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:19
 * FIXME
 */
public class HomeItemViewHolder extends BaseViewHolder<Gallery> {


    public ImageView img,video;
    public TextView content;
    public TextView likeNumber;

    public HomeItemViewHolder(ViewGroup viewGroup) {
        super(viewGroup, R.layout.home_list_item);//Item的布局写在这里
        img = $(R.id.iv_img);
        video = $(R.id.iv_video);
        content = $(R.id.tv_content);
        likeNumber = $(R.id.tv_like_number);
    }


    @Override
    public void setData(final Gallery data) {
        super.setData(data);
//        Glide.with(getContext())
//                .load(getContext().getString(R.string.serverbaseurl) + data.getUrl())
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
////                .override(DensityUtil.dip2px(120), DensityUtil.dip2px(80)) // 压缩
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        int imageHeight = resource.getHeight();
//                        int imageWidth = resource.getWidth();
//                        Log.e("",imageHeight+"<<>>"+data.getSourceImgHeight());
//                        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
//                        layoutParams.height = imageHeight * (DensityUtil.getWidth() / 2) / imageWidth;//这一句很重要,android:scaleType="centerCrop",这句还是得加上去
////                        layoutParams.height = DensityUtil.getWidth() / 2;//这一句很重要
//                        img.setLayoutParams(layoutParams);
//                        img.setImageBitmap(resource);
//                    }
//                });


     if (data.getSourceImgHeight() <= 0 || data.getSourceImgWidth() <= 0 || data.getSourceImgHeight()>2560){
         img.getLayoutParams().height = DensityUtil.getWidth() / 2;//这一句很重要
         img.getLayoutParams().width = DensityUtil.getWidth() / 2;//这一句很重要
         img.setScaleType(ImageView.ScaleType.CENTER_CROP);
     }else {
//         Log.e("",DensityUtil.dip2px(8)+"<<<");因为有padding
         img.getLayoutParams().height = (data.getSourceImgHeight() - DensityUtil.dip2px(8)) * ((DensityUtil.getWidth() / 2) - DensityUtil.dip2px(8)) / data.getSourceImgWidth();//这一句很重要
//         img.getLayoutParams().width = data.getSourceImgWidth();//这一句很重要
         img.getLayoutParams().width = DensityUtil.getWidth() / 2;//这一句很重要
         img.setScaleType(ImageView.ScaleType.FIT_XY);
     }


        Glide.with(getContext())//更改图片加载框架
                .load(getContext().getString(R.string.imgbaseurl) + data.getUrl())
//                .placeholder(R.drawable.ic_defult_pic)设置之后图片不会立即出来
                .crossFade()
                .override(img.getLayoutParams().height / 2, img.getLayoutParams().width / 2) // 压缩
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);

        content.setText(data.getTitle());
        likeNumber.setText(data.getCollectCount()+"");

        if (data.getPostType() == 0){
            video.setVisibility(View.GONE);
        }else if (data.getPostType() == 2){
            video.setVisibility(View.VISIBLE);
        }
    }

}
