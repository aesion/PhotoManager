package com.pulamsi.photomanager.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.BaseListAdapter;
import com.pulamsi.photomanager.bean.ThemeItem;
import com.pulamsi.photomanager.utils.SharedPreferencesHelper;
import com.pulamsi.photomanager.view.ThemeActicity;
import com.pulamsi.photomanager.viewHolder.ThemeViewholder;

import java.util.ArrayList;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-16
 * Time: 09:35
 * FIXME
 */
public class ThemeAdapter extends BaseListAdapter<ThemeItem> {
    Activity activity;
    ArrayList<SwitchCompat> switchCompatArrayList = new ArrayList<>();

    public ThemeAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.theme_item, null);
        if (view == null) {
            return null;
        }
        return new ThemeViewholder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!(getItem(position) instanceof ThemeItem) || holder == null || !(holder instanceof ThemeViewholder))
            return;

        final ThemeViewholder viewHolder = (ThemeViewholder) holder;
        ThemeItem themeItem = getItem(position);

        viewHolder.themeImg.setBackgroundColor(ContextCompat.getColor(activity, themeItem.color));
        viewHolder.themeName.setText(themeItem.name);

        switchCompatArrayList.add(viewHolder.themeSwitch);
        int themeType = SharedPreferencesHelper.getInstance().getThemeType(activity);
        if (themeType == position){
            viewHolder.themeSwitch.setChecked(true);
            viewHolder.themeSwitch.setClickable(false);
        }


        viewHolder.themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((ThemeActicity) activity).OnChangeTheme(position);
                    for (SwitchCompat switchCompat : switchCompatArrayList) {
                        if (!(viewHolder.themeSwitch == switchCompat)){
                            switchCompat.setChecked(false);
                            switchCompat.setClickable(true);
                        }else {
                            switchCompat.setClickable(false);
                        }
                    }
                }
            }
        });
    }
}
