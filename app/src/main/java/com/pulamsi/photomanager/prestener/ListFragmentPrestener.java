package com.pulamsi.photomanager.prestener;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.interfaces.IListFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:04
 * FIXME
 */
public class ListFragmentPrestener extends BasePrestener {

    IListFragment iListFragment;

    public ListFragmentPrestener(IListFragment iListFragment) {
        this.iListFragment = iListFragment;
    }

    public void requestData(final Map<String, String> requestParameter) {

        for (Map.Entry<String, String> e : requestParameter.entrySet()) {
            Log.e("",e.getKey() + "--" + e.getValue());
        }
        String url = getString(R.string.serverbaseurl) + getString(R.string.getPasterList);
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
                        iListFragment.showEmpty();
                    }else {
                        iListFragment.updateListData(GalleryList);
                        iListFragment.showContent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iListFragment.showError();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iListFragment.showError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParameter;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.requestQueue.add(stringRequest);


    }

    public void requestLoadData(final Map<String, String> requestParameter) {

        for (Map.Entry<String, String> e : requestParameter.entrySet()) {
            Log.e("",e.getKey() + "--" + e.getValue());
        }
        String url = getString(R.string.serverbaseurl) + getString(R.string.getPasterList);
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
                    iListFragment.loadingListData(GalleryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iListFragment.loadingError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParameter;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.requestQueue.add(stringRequest);


    }
}
