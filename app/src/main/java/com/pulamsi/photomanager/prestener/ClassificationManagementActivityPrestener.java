package com.pulamsi.photomanager.prestener;

import android.util.Log;

import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.IClassificationManagementActivity;

import java.util.HashMap;
import java.util.List;
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
public class ClassificationManagementActivityPrestener extends BasePrestener {

    private IClassificationManagementActivity iClassificationManagementActivity;

    public ClassificationManagementActivityPrestener(IClassificationManagementActivity iClassificationManagementActivity) {
        this.iClassificationManagementActivity = iClassificationManagementActivity;
    }


    /**
     * 请求分类数据
     */
    public void requestData() {
        iClassificationManagementActivity.showLoading();
        Map<String, String> map = new HashMap<>();
        if (Constants.IS_LOGIN) {
            map.put("mid", Constants.MID);
        }
        RetrofitApi.init().requestPasterList(map).enqueue(new Callback<List<Galleryclass>>() {
            @Override
            public void onResponse(Call<List<Galleryclass>> call, Response<List<Galleryclass>> response) {
                if (response.code() == 200) {
                    iClassificationManagementActivity.dataBack(response.body());
                    iClassificationManagementActivity.showContent();
                } else {
                    iClassificationManagementActivity.showError();
                }
            }

            @Override
            public void onFailure(Call<List<Galleryclass>> call, Throwable t) {
                iClassificationManagementActivity.showError();

            }
        });
    }


    /**
     * 保存服务器
     *
     * @param stringBuffer
     */
    public void saveTheServer(StringBuffer stringBuffer) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", Constants.MID);
        map.put("pcIds", stringBuffer.toString());
        RetrofitApi.init().addSelectPC(map).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.e("", "保存分类到服务器" + response.code() + "+" + response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("", t.toString());
            }
        });

    }
}
