package com.pulamsi.photomanager.viewHolder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.CommentAppreciateImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-05
 * Time: 14:35
 * FIXME
 */
public class CommentItemViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView img;
    public TextView name,time,content, number;
    public CommentAppreciateImageView appreciate;
    public ImageView appreciateBg;

    public CommentItemViewHolder(View convertView) {
        super(convertView);
        img = (CircleImageView) convertView.findViewById(R.id.civ_img);
        name = (TextView) convertView.findViewById(R.id.tv_name);
        time = (TextView) convertView.findViewById(R.id.tv_time);
        content = (TextView) convertView.findViewById(R.id.tv_content);
        number = (TextView) convertView.findViewById(R.id.tv_number);
        appreciate = (CommentAppreciateImageView) convertView.findViewById(R.id.iv_appreciate);
        appreciateBg = (ImageView) convertView.findViewById(R.id.iv_appreciate_bg);
        appreciateBg.setColorFilter(Color.RED);
        number.setTextColor(Color.GRAY);
    }
}
