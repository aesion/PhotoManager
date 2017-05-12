package com.pulamsi.photomanager.prestener;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.CollectionTitle;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.ICollectionFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-10
 * Time: 17:19
 * FIXME
 */
public class CollectionFragmentPrestener {

    private ICollectionFragment iCollectionFragment;

    public CollectionFragmentPrestener(ICollectionFragment iCollectionFragment) {
        this.iCollectionFragment = iCollectionFragment;
    }


    /**
     * 获取收藏夹列表
     */
    public void requestDate() {
        iCollectionFragment.showLoading();

        HashMap<String, String> map = new HashMap<>();
        map.put("mid", Constants.MID);

        RetrofitApi.init().requestCollection(map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    List<CollectionTitle> collectionTitleList = new Gson().fromJson(jsonObject.get("list").toString(), new TypeToken<List<CollectionTitle>>() {
                    }.getType());
                    if (collectionTitleList.size() == 0) {
                        iCollectionFragment.showEmpty();
                    } else {
                        iCollectionFragment.updateCollectionData(collectionTitleList);
                        iCollectionFragment.showContent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("", "收藏夹列表包装出错");
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iCollectionFragment.showError();
                Log.e("", t.toString() + "<<收藏接口");
            }
        });
    }
}
