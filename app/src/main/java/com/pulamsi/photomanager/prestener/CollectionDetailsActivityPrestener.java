package com.pulamsi.photomanager.prestener;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.interfaces.ICollectionDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-14
 * Time: 10:57
 * FIXME
 */
public class CollectionDetailsActivityPrestener extends BasePrestener {

    ICollectionDetailsActivity iCollectionDetailsActivity;

    public CollectionDetailsActivityPrestener(ICollectionDetailsActivity iCollectionDetailsActivity) {
        this.iCollectionDetailsActivity = iCollectionDetailsActivity;
    }

    public void requestLoadData(final Map<String, String> requestParameter) {

        for (Map.Entry<String, String> e : requestParameter.entrySet()) {
            Log.e("",e.getKey() + "--" + e.getValue());
        }

        String url = getString(R.string.serverbaseurl) + getString(R.string.preCollectFeed);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String tngouList = jsonObject.getString("list");
                    Gson gson = new Gson();
                    List<Gallery> GalleryList = gson.fromJson(tngouList, new TypeToken<List<Gallery>>() {
                    }.getType());
                    iCollectionDetailsActivity.loadingListData(GalleryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyApplication.toastor.showLongToast("网络错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParameter;
            }
        };
        MyApplication.requestQueue.add(stringRequest);


    }

    public void requestData(final Map<String, String> requestParameter) {
        for (Map.Entry<String, String> e : requestParameter.entrySet()) {
            Log.e("", e.getKey() + "--" + e.getValue());
        }

        String url = getString(R.string.serverbaseurl) + getString(R.string.preCollectFeed);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String tngouList = jsonObject.getString("list");
                    Gson gson = new Gson();
                    List<Gallery> GalleryList = gson.fromJson(tngouList, new TypeToken<List<Gallery>>() {
                    }.getType());
                    if (GalleryList.size() == 0){
                        iCollectionDetailsActivity.showEmpty();
                    }else {
                        iCollectionDetailsActivity.updateListData(GalleryList);
                        iCollectionDetailsActivity.showContent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iCollectionDetailsActivity.showError();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iCollectionDetailsActivity.showError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParameter;
            }
        };
        MyApplication.requestQueue.add(stringRequest);
    }
}
