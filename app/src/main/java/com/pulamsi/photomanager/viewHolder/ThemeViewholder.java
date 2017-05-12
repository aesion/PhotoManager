package com.pulamsi.photomanager.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-16
 * Time: 09:53
 * FIXME
 */
public class ThemeViewholder extends RecyclerView.ViewHolder {

    public TextView themeName;
    public ImageView themeImg;
    public SwitchCompat themeSwitch;

    public ThemeViewholder(View view) {
        super(view);
        themeName = (TextView) view.findViewById(R.id.name);
        themeImg = (ImageView) view.findViewById(R.id.img);
        themeSwitch = (SwitchCompat) view.findViewById(R.id.switchCompat);
    }
}
