package com.pulamsi.photomanager.utils;

import android.content.Context;
import android.view.WindowManager;

import com.pulamsi.photomanager.base.MyApplication;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-12
 * Time: 09:31
 * FIXME
 */
public class DensityUtil {

    public static int getWidth(){
        WindowManager wm = (WindowManager) MyApplication.context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    public static int getHeight(){
        WindowManager wm = (WindowManager) MyApplication.context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px( float dpValue) {
        final float scale = MyApplication.context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip( float pxValue) {
        final float scale = MyApplication.context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
