package com.pulamsi.photomanager.utils;

import android.content.Context;

/**
 * SharedPreferences数据处理类
 */
public class SharedPreferencesHelper {

    public static final String THEME_TYPE_KEY = "theme_type_key";
    public static final int THEME_TYPE_DEFAULT = 0;

    public static final String IS_FIRST_IN = "is_first_in";


    private static SharedPreferencesHelper staticInstance;

    public static SharedPreferencesHelper getInstance() {
        if (staticInstance == null) {
            staticInstance = new SharedPreferencesHelper();
        }
        return staticInstance;
    }

    public int getThemeType(Context context) {
        return (int) SharedPreferencesUtils.get(context, THEME_TYPE_KEY, THEME_TYPE_DEFAULT);
    }

    public void setThemeType(Context context, int themeType) {
        SharedPreferencesUtils.put(context, THEME_TYPE_KEY, themeType);
    }

    public boolean getIsFirstIn(Context context) {
        return (boolean) SharedPreferencesUtils.get(context, IS_FIRST_IN, false);
    }

    public void setIsFirstIn(Context context, boolean isfirst) {
        SharedPreferencesUtils.put(context, IS_FIRST_IN, isfirst);
    }
}
