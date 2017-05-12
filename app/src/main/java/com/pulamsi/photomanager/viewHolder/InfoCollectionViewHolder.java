package com.pulamsi.photomanager.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.bean.CollectionTitle;
import com.pulamsi.photomanager.helper.RoundTransform;
import com.pulamsi.photomanager.utils.DensityUtil;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-10
 * Time: 11:55
 * FIXME
 */
public class InfoCollectionViewHolder extends BaseViewHolder<CollectionTitle> {

    public TextView number;
    public TextView name;
    public TextView subhead;
    public ImageView img;

    public InfoCollectionViewHolder(ViewGroup viewGroup) {
        super(viewGroup, R.layout.info_collection_item);//Item的布局写在这里
        name = $(R.id.tv_name);//记得写上面的泛型，谢谢，不然会强制类型抓换
        img = $(R.id.iv_img);
        number = $(R.id.tv_number);
        subhead = $(R.id.tv_subhead);
    }

    @Override
    public void setData(CollectionTitle collectionTitle) {
        super.setData(collectionTitle);
        name.setText(collectionTitle.getPreTypeName());
        number.setText(collectionTitle.getNumber());
        subhead.setText("公开 · " + collectionTitle.getNumber() + "个内容");

        Glide.with(getContext())//更改图片加载框架
                .load(getContext().getString(R.string.imgbaseurl) + collectionTitle.getImage())
                .asBitmap()//解决Glide.centerCrop()第一次显示无效
                .transform(new CenterCrop(getContext()), new RoundTransform(getContext(), 10))
                .override(DensityUtil.dip2px(120),DensityUtil.dip2px(80))
                .placeholder(R.mipmap.bga_pp_ic_holder_light)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }
}
