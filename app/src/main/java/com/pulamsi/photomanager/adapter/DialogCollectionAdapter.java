package com.pulamsi.photomanager.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.pulamsi.photomanager.bean.CollectionTitle;
import com.pulamsi.photomanager.viewHolder.DialogCollectionViewHolder;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-10
 * Time: 11:49
 * FIXME
 */
public class DialogCollectionAdapter extends RecyclerArrayAdapter<CollectionTitle> {


    public DialogCollectionAdapter(Context context) {
        super(context);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DialogCollectionViewHolder(viewGroup);
    }
}
