package com.pulamsi.photomanager.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.viewHolder.LineHomeItemViewHolder;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:15
 * FIXME
 */
public class LineHomeListAdapter extends RecyclerArrayAdapter<Gallery> {

    Activity activity;

    public LineHomeListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        LineHomeItemViewHolder lineHomeItemViewHolder = new LineHomeItemViewHolder(viewGroup);
        lineHomeItemViewHolder.setOnDelListener(new LineHomeItemViewHolder.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(pos);
                }
            }

            @Override
            public void onItemClick(int position) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onItemClick(position);
                }
            }
        });
        return lineHomeItemViewHolder;
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
