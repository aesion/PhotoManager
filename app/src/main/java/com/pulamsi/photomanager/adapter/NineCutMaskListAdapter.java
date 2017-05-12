package com.pulamsi.photomanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:15
 * FIXME
 */
public class NineCutMaskListAdapter extends RecyclerView.Adapter<NineCutMaskListAdapter.MyViewHolder> {
    private Context context;
    private Integer[] data;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private MyViewHolder holder;
    public int layoutPosition;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public NineCutMaskListAdapter(Context context, Integer[] data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.mask_list_item, null);
        holder = new MyViewHolder(itemView);
        return holder;
    }


    @Override

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取当前点击的位置

                layoutPosition = holder.getLayoutPosition();

                notifyDataSetChanged();
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.itemView, layoutPosition);
            }
        });

        Glide.with(context)//更改图片加载框架
                .load(data[position])
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.photo);

        //更改状态
        if (position == layoutPosition) {
            holder.singChoose.setVisibility(View.VISIBLE);
        } else {
            holder.singChoose.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photo, singChoose;

        public MyViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.ic_photo);
            singChoose = (ImageView) itemView.findViewById(R.id.iv_sing_choose);
        }
    }
}