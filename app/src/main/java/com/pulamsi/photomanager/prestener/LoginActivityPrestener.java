package com.pulamsi.photomanager.prestener;

import android.util.Log;

import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.bean.User;
import com.pulamsi.photomanager.bean.UserDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.helper.RongCloudHelper;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.view.ILoginActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:04
 * FIXME
 */
public class LoginActivityPrestener extends BasePrestener {
    ILoginActivity iLoginActivity;

    public LoginActivityPrestener(ILoginActivity iLoginActivity) {
        this.iLoginActivity = iLoginActivity;
    }

    public void login(final Map<String, String> data) {
        RetrofitApi.init().login(data).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                if (response.code() == 200) {
                    try {
                        DefaultResult result = response.body();
                        if (result.getSuccessful()) {
                            requestUserInformation(result.getId());//请求用户信息
                        }
                    } catch (Exception e) {
                        Log.e("", "登录出错");
                        iLoginActivity.showLoginError();
                    }
                } else {
                    iLoginActivity.showLoginError();
                }
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                iLoginActivity.showLoginError();
            }
        });

    }


    /**
     * 请求用户信息
     */
    public void requestUserInformation(final String mid) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", mid);
        RetrofitApi.init().requestUserInformation(map).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("User",response.body().toString());
                Log.e("MID",mid);
                if (response.code() == 200) {
                    User mUser = response.body();
                    //设置进全局变量
                    Constants.MID = mid;
                    Constants.IS_LOGIN = true;
                    Constants.IMG_URL = mUser.getImgUrl();
                    Constants.NAME = mUser.getName();
                    Constants.AUTO_GRAPH = mUser.getAutograph();
                    Constants.USER_TYPE = mUser.getUserType();
                    Constants.RONGCLOUD_TOKEN = mUser.getRongyunToken();
                    //
                    UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                    User user = userDao.queryBuilder().unique();
                    if (user == null) {
                        user = new User();
                    }
                    user.setMid(Constants.MID);
                    user.setIsLogin(Constants.IS_LOGIN);
                    user.setImgUrl(Constants.IMG_URL);
                    user.setName(Constants.NAME);
                    user.setAutograph(Constants.AUTO_GRAPH);
                    user.setBirthDay(mUser.getBirthDay());
                    user.setUserType(Constants.USER_TYPE);
                    user.setRongyunToken(Constants.RONGCLOUD_TOKEN);
                    userDao.insertOrReplace(user);
                    iLoginActivity.showLoginSuccessful();
                    RongCloudHelper.connect(Constants.RONGCLOUD_TOKEN);//链接融云
                    //设置融云的用户提供者
//                    final String imgUrl = Constants.IMG_URL.contains("http://") ? Constants.IMG_URL : MyApplication.context.getString(R.string.imgbaseurl) + Constants.IMG_URL;
//                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                        @Override
//                        public UserInfo getUserInfo(String s) {
//                            UserInfo userInfo = new UserInfo(Constants.MID, Constants.NAME,Uri.parse(imgUrl));
//                            return userInfo;
//                        }
//                    }, true);
                } else {
                    iLoginActivity.showLoginError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                iLoginActivity.showLoginError();
            }
        });

    }
}
