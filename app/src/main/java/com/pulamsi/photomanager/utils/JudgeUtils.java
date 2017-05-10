package com.pulamsi.photomanager.utils;

import android.app.Activity;
import android.content.Intent;

import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.view.LoginActivity;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-05
 * Time: 11:50
 * FIXME
 */
public class JudgeUtils {

    public static boolean isLogin(Activity activity) {
        if (!Constants.IS_LOGIN) {
            MyApplication.toastor.showToast("您还未登录!");
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("isGoBack",true);//是否需要回到来源界面
            activity.startActivity(intent);
            return false;
        }
        return true;
    }

}
