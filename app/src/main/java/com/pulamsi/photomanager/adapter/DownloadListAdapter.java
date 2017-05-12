package com.pulamsi.photomanager.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.pulamsi.photomanager.viewHolder.DownloadViewHolder;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-09
 * Time: 12:25
 * FIXME
 */
public class DownloadListAdapter extends RecyclerArrayAdapter<String> {

    public DownloadListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DownloadViewHolder(viewGroup);
    }
}
