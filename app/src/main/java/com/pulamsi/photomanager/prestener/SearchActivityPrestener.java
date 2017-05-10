package com.pulamsi.photomanager.prestener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.android.log.Log;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.PasterLabel;
import com.pulamsi.photomanager.interfaces.ISearchActivity;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:04
 * FIXME
 */
public class SearchActivityPrestener extends BasePrestener {

    private ISearchActivity iSearchActivity;

    public SearchActivityPrestener(ISearchActivity iSearchActivity) {
        this.iSearchActivity = iSearchActivity;
    }


    /**
     * 请求热门标签
     */
    public void requestHotTag() {
        String url = getString(R.string.serverbaseurl) + getString(R.string.getDefaultLabel);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    Gson gson = new Gson();
                    List<PasterLabel> pasterLabelList = gson.fromJson(response, new TypeToken<List<PasterLabel>>() {
                    }.getType());
                    iSearchActivity.HotTagBack(pasterLabelList);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "标签包装出错");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(error.toString());
            }
        });
        MyApplication.requestQueue.add(stringRequest);

    }
}
