package com.pulamsi.photomanager.utils;

import android.content.Context;
import android.text.TextUtils;

import com.pulamsi.photomanager.widght.fitsystemwindowlayout.LoadingDialog;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-03
 * Time: 12:13
 * FIXME
 */
public class DialogUtils {

    public static LoadingDialog loadingDialog;

    public static void showLoading(String text, Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        if (!TextUtils.isEmpty(text)){
            loadingDialog.setMessage(text);
        }
        loadingDialog.show();
    }

    public static void showLoadingDefult(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.setMessage("加载中...");
        loadingDialog.show();
    }

    public static void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.hindDialog();

    }

}
