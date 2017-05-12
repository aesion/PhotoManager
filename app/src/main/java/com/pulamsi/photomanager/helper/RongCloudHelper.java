package com.pulamsi.photomanager.helper;

import android.net.Uri;
import android.util.Log;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.User;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-05-06
 * Time: 14:33
 * FIXME
 * 融云帮助类
 */
public class RongCloudHelper {

    /**
     * 链接融云
     * connect() 方法在整个应用只需要调用一次，且必须在主进程调用。如果连接失败， SDK 会自动启动重连机制，进行最多10次重连，
     * 分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。如果仍然没有连接成功，还会在检测网络状态变化时再次重连。应用不需要做额外的重连操作。
     *
     * @param token
     */
    public static void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.e("", "Token 错误>>>"+ Constants.RONGCLOUD_TOKEN);
            }

            @Override
            public void onSuccess(String s) {
                Log.e("", "融云连接成功—————>" + s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("", "融云连接失败—————>token>>>" + errorCode.getMessage());
            }
        });
    }


    /**
     * 请求用户信息
     */
    public static void requestUserInformation(final String mid) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", mid);
        RetrofitApi.init().requestUserInformation(map).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    /**
                     * 刷新用户缓存数据。
                     *
                     * @param userInfo 需要更新的用户缓存数据。
                     */
                    try {
                        User mUser = response.body();
                        final String imgUrl = mUser.getImgUrl().contains("http://") ? mUser.getImgUrl() : MyApplication.context.getString(R.string.imgbaseurl) + mUser.getImgUrl();
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(mid, mUser.getName(), Uri.parse(imgUrl)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("","用户提供者报错，估计是没有头像");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

}
