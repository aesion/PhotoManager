package com.pulamsi.photomanager.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.viewHolder.HomeItemViewHolder;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:15
 * FIXME
 */
public class HomeListAdapter extends RecyclerArrayAdapter<Gallery> {

    Activity activity;

    public HomeListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new HomeItemViewHolder(viewGroup);
    }

}
