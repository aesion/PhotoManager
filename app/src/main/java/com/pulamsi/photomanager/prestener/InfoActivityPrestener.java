package com.pulamsi.photomanager.prestener;

import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.MineCount;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.IInfoActivity;

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
public class InfoActivityPrestener extends BasePrestener {
    IInfoActivity iInfoActivity;

    public InfoActivityPrestener(IInfoActivity iInfoActivity) {
        this.iInfoActivity = iInfoActivity;
    }


    /**
     * 请求分类的数量
     */
    public void requestInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("mid", Constants.MID);
        RetrofitApi.init().requestMineCount(map).enqueue(new Callback<MineCount>() {
            @Override
            public void onResponse(Call<MineCount> call, Response<MineCount> response) {
                if (response.code() == 200)
                    iInfoActivity.mineCountBack(response.body());
            }

            @Override
            public void onFailure(Call<MineCount> call, Throwable t) {
                //无需处理
            }
        });


    }
}
