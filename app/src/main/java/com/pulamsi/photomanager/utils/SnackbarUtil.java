package com.pulamsi.photomanager.utils;

import android.graphics.Color;
import android.view.View;

import com.pulamsi.photomanager.base.MyApplication;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-18
 * Time: 14:22
 * FIXME
 */
public class SnackbarUtil {

    //其中View应该这样获取才行
//    final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();

    public static TSnackbar snackBar;

    public static void showLoading(View view, String message) {
        snackBar = TSnackbar.make(view, message, TSnackbar.LENGTH_INDEFINITE, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
        snackBar.setMessageTextColor(Color.WHITE);
        snackBar.setPromptThemBackground(Prompt.SUCCESS);
        snackBar.addIconProgressLoading(0, true, false);
        snackBar.show();
    }

    public static void showLoadSuccess(String message) {
        if (snackBar != null)
            snackBar.setPromptThemBackground(Prompt.SUCCESS).setText(message).setDuration(TSnackbar.LENGTH_SHORT).show();
    }

    public static void showLoadError(String message) {
        if (snackBar != null)
            snackBar.setPromptThemBackground(Prompt.ERROR).setText(message).setDuration(TSnackbar.LENGTH_SHORT).show();
    }


    public static void showLoadDismiss() {
        if (snackBar != null)
            snackBar.dismiss();
    }


    /**
     * Prompt.SUCCESS
     * Prompt.ERROR
     * Prompt.WARNING
     *
     * @param view
     * @param message
     */
    public static void showPrompt(View view, String message, Prompt prompt) {
        snackBar = TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
        snackBar.setMessageTextColor(Color.WHITE);
        snackBar.setPromptThemBackground(prompt);
        snackBar.show();
    }

    public static void show(View view, String message) {
        snackBar = TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT, TSnackbar.APPEAR_FROM_BOTTOM_TO_TOP);
        snackBar.setMessageTextColor(Color.WHITE);
        snackBar.setBackgroundColor(ThemeUtils.getThemeColorRes(MyApplication.context));
        snackBar.show();
    }

}
