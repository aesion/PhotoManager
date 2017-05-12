package com.pulamsi.photomanager.viewHolder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.bean.CollectionTitle;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-10
 * Time: 11:55
 * FIXME
 */
public class DialogCollectionViewHolder extends BaseViewHolder<CollectionTitle> {

    public TextView name;
    public TextView subhead;

    public DialogCollectionViewHolder(ViewGroup viewGroup) {
        super(viewGroup, R.layout.dialog_collection_item);//Item的布局写在这里
        name = $(R.id.tv_name);//记得写上面的泛型，谢谢，不然会强制类型抓换
        subhead = $(R.id.tv_subhead);
    }

    @Override
    public void setData(CollectionTitle collectionTitle) {
        super.setData(collectionTitle);
        name.setText(collectionTitle.getPreTypeName());
        subhead.setText("公开 · "+collectionTitle.getNumber()+"个内容");
    }
}
