package com.pulamsi.photomanager.viewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.helper.RoundTransform;
import com.pulamsi.photomanager.utils.DataUtils;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.SwipeMenuView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:19
 * FIXME
 */
public class LineHomeItemViewHolder extends BaseViewHolder<Gallery> {


    public ImageView img;
    public TextView content;
    public TextView likeNumber;
    public TextView commentNumber;
    public TextView time;
    public Button btnDelete;
    public View mItemView;
    public View swipeContent;
    public FrameLayout framStauts;
    public int position;
    public View view;
    public TextView releaseStaut;


    public LineHomeItemViewHolder(ViewGroup viewGroup) {
        super(viewGroup, R.layout.line_home_list_item);//Item的布局写在这里
        img = $(R.id.iv_img);
        content = $(R.id.tv_content);
        likeNumber = $(R.id.tv_like_number);
        commentNumber = $(R.id.tv_comments_number);
        time = $(R.id.tv_like_time);
        btnDelete = $(R.id.btnDelete);
        swipeContent = $(R.id.swipe_content);
        framStauts = $(R.id.fl_stauts);
        mItemView = this.itemView;

//        view = View.inflate(getContext(), R.layout.item_release_staut, null);
//        releaseStaut = (TextView) view.findViewById(R.id.tv_release_staut);
//        framStauts.addView(view);
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


//        if (data.getAuditStatus() == 0){//不通过
//            view.setVisibility(View.VISIBLE);
//            releaseStaut.setText("审核失败");
//            releaseStaut.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.release_staut_bg_gary));
//        }else if (data.getAuditStatus() == 2){//审核中
//            view.setVisibility(View.VISIBLE);
//            releaseStaut.setText("审核中");
//            releaseStaut.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.release_staut_bg));
//        }else {
//            view.setVisibility(View.GONE);
//        }



        Glide.with(getContext())//更改图片加载框架
                .load(getContext().getString(R.string.imgbaseurl) + data.getUrl())
                .asBitmap()//解决Glide.centerCrop()第一次显示无效
                .transform(new CenterCrop(getContext()), new RoundTransform(getContext(),10))
                .placeholder(R.mipmap.bga_pp_ic_holder_light)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);

        content.setText(data.getTitle());
        likeNumber.setText(data.getCollectCount() + "");
        commentNumber.setText(data.getCommentCount()+"");
        time.setText(DataUtils.stampToDate(data.getReleaseTime() + ""));


        //这句话关掉IOS阻塞式交互效果 并打开左滑
        ((SwipeMenuView) mItemView).setIos(true).setLeftSwipe(true);


        position = this.getAdapterPosition();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    ((SwipeMenuView) mItemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });


        swipeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener && data.getAuditStatus() == 1) {
                    mOnSwipeListener.onItemClick(position);
                }else if (data.getAuditStatus() == 0){//不通过
                    MyApplication.toastor.showToast("审核失败，无法查看");
                }else if (data.getAuditStatus() == 2){//审核中
                    MyApplication.toastor.showToast("审核中，暂时无法查看");
                }
            }
        });


    }


    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onItemClick(int position);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

}
