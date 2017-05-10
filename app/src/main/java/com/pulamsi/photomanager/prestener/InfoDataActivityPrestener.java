package com.pulamsi.photomanager.prestener;

import android.util.Log;

import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.bean.User;
import com.pulamsi.photomanager.bean.UserDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.IInfoDataActivity;
import com.pulamsi.photomanager.utils.DialogUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-01
 * Time: 11:21
 * FIXME
 */
public class InfoDataActivityPrestener extends BasePrestener {

    IInfoDataActivity iInfoDataActivity;

    public InfoDataActivityPrestener(IInfoDataActivity iInfoDataActivity) {
        this.iInfoDataActivity = iInfoDataActivity;
    }

    public void requestAvatar(String avatarBase64) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", Constants.MID);
        map.put("imgStr", avatarBase64);
        DialogUtils.showLoadingDefult(iInfoDataActivity.getContext());
        RetrofitApi.init().setHeadPro(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        iInfoDataActivity.backHeadSuccess();
                    } else {
                        MyApplication.toastor.showToast("上传失败");
                    }
                } else {
                    MyApplication.toastor.showToast("服务器错误");
                }
                DialogUtils.hideLoading();
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                MyApplication.toastor.showToast("网络错误");
                DialogUtils.hideLoading();
            }
        });
    }


    /**
     * 修改个人资料
     */
    public void requestUpdateData(Map<String, String> map) {
        map.put("mid", Constants.MID);
        DialogUtils.showLoadingDefult(iInfoDataActivity.getContext());
        RetrofitApi.init().updateMember(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                        if (response.code() == 200){
                            if (response.isSuccessful()) {
                                MyApplication.toastor.showToast("修改成功");
                                iInfoDataActivity.backUpdateDataSuccess();
                            } else {
                                MyApplication.toastor.showToast(response.message());
                            }
                        } else {
                            MyApplication.toastor.showToast("服务器错误");
                        }
                DialogUtils.hideLoading();
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                MyApplication.toastor.showToast("网络错误");
                DialogUtils.hideLoading();
            }
        });

    }


    /**
     * 获取个人资料
     */
    public void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("mid", Constants.MID);
        RetrofitApi.init().requestUserInformation(map).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Log.e("", response.body().toString());
                    User mUser = response.body();
                    //设置进全局变量
                    Constants.IMG_URL = mUser.getImgUrl();
                    Constants.NAME = mUser.getName();
                    Constants.AUTO_GRAPH = mUser.getAutograph();
                    //
                    UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                    User user = userDao.queryBuilder().unique();
                    if (user == null) {
                        user = new User();
                    }
                    user.setImgUrl(Constants.IMG_URL);
                    user.setName(Constants.NAME);
                    user.setAutograph(Constants.AUTO_GRAPH);
                    user.setSex(mUser.getSex());
                    user.setBirthDay(mUser.getBirthDay());
                    userDao.update(user);
                    iInfoDataActivity.backDataSuccess(mUser);
                } else {
                    iInfoDataActivity.showError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                iInfoDataActivity.showError();
            }
        });

    }
}
