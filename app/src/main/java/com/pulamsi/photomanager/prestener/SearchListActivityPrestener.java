package com.pulamsi.photomanager.prestener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.android.log.Log;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.ISearchListActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
public class SearchListActivityPrestener extends BasePrestener {

    private ISearchListActivity iSearchListActivity;

    public SearchListActivityPrestener(ISearchListActivity iSearchListActivity) {
        this.iSearchListActivity = iSearchListActivity;
    }

    /**
     * 请求搜索
     * 参数：pageNumber，pageSize，title（标题）
     */
    public void requestSearch(Map<String, String> map, boolean isTag) {
        iSearchListActivity.showLoading();
        Call<String> searchCall = null;
        if (isTag) {//是否为标签
            searchCall = RetrofitApi.init().requestTagSearch(map);//是标签
        } else {
            searchCall = RetrofitApi.init().requestSearch(map);//不是标签
        }

        searchCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
//                    Log.e("",response.body());
                    JSONObject jsonObject = new JSONObject(response.body());
                    String tngouList = jsonObject.getString("list");
                    Gson gson = new Gson();
                    List<Gallery> GalleryList = gson.fromJson(tngouList, new TypeToken<List<Gallery>>() {
                    }.getType());
                    if (GalleryList.size() == 0) {
                        iSearchListActivity.showEmpty();
                    } else {
                        iSearchListActivity.updateListData(GalleryList);
                        iSearchListActivity.showContent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iSearchListActivity.showError();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(t.toString());
            }
        });
    }


    /**
     * 加载更多
     *
     * @param map
     * @param isTag
     */
    public void requestLoadSearch(Map<String, String> map, boolean isTag) {
        Call<String> searchCall = null;
        if (isTag) {//是否为标签
            searchCall = RetrofitApi.init().requestTagSearch(map);//是标签
        } else {
            searchCall = RetrofitApi.init().requestSearch(map);//不是标签
        }
        searchCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    String tngouList = jsonObject.getString("list");
                    Gson gson = new Gson();
                    List<Gallery> GalleryList = gson.fromJson(tngouList, new TypeToken<List<Gallery>>() {
                    }.getType());
                    iSearchListActivity.loadingListData(GalleryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    iSearchListActivity.showError();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(t.toString());
            }
        });

    }
}
