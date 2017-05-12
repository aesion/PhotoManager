package com.pulamsi.photomanager.prestener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.IReleaseDetailsFragment;
import com.pulamsi.photomanager.utils.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-10
 * Time: 15:15
 * FIXME
 */
public class ReleaseDetailsFragmentPrestener extends BasePrestener {

    IReleaseDetailsFragment iReleaseDetailsFragment;


    public ReleaseDetailsFragmentPrestener(IReleaseDetailsFragment iReleaseDetailsFragment) {
        this.iReleaseDetailsFragment = iReleaseDetailsFragment;
    }

    public void requestData(final Map<String, String> requestParameter, final int auditStatus) {
        String url = getString(R.string.serverbaseurl) + getString(R.string.getPasterByMid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String tngouList = jsonObject.getString("list");
                    Gson gson = new Gson();
                    List<Gallery> GalleryList = gson.fromJson(tngouList, new TypeToken<CopyOnWriteArrayList<Gallery>>() {
                    }.getType());
//                    http://www.2cto.com/kf/201403/286536.html
//                    Java ConcurrentModificationException 异常分析与解决方案
                    //根据标识移除不在状态的bean
                    for (Gallery gallery : GalleryList) {
                        if (gallery.getAuditStatus() != auditStatus)
                            GalleryList.remove(gallery);
                    }
                    if (GalleryList.size() == 0) {
                        iReleaseDetailsFragment.showEmpty();
                    } else {
                        iReleaseDetailsFragment.updateListData(GalleryList);
                        iReleaseDetailsFragment.showContent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iReleaseDetailsFragment.showError();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iReleaseDetailsFragment.showError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParameter;
            }
        };
        MyApplication.requestQueue.add(stringRequest);

    }

    public void requestLoadData(final Map<String, String> requestParameter, final int auditStatus) {

        String url = getString(R.string.serverbaseurl) + getString(R.string.getPasterByMid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String tngouList = jsonObject.getString("list");
                    Gson gson = new Gson();
                    List<Gallery> GalleryList = gson.fromJson(tngouList, new TypeToken<CopyOnWriteArrayList<Gallery>>() {
                    }.getType());
                    //根据标识移除不在状态的bean
                    for (Gallery gallery : GalleryList) {
                        if (gallery.getAuditStatus() != auditStatus)
                            GalleryList.remove(gallery);
                    }
                    iReleaseDetailsFragment.loadingListData(GalleryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyApplication.toastor.showLongToast("网络错误");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParameter;
            }
        };
        MyApplication.requestQueue.add(stringRequest);


    }


    /**
     * 删除素材
     * 删除或恢复（软删） 另外，软删除15天内不恢复，将凌晨1点自动彻底删除
     *
     * @param id
     */
    public void deleteMaterial(String id) {
        DialogUtils.showLoading("删除中", iReleaseDetailsFragment.getContext());
//        state=（delete 删除 或 recover 恢复）
        Map<String, String> map = new HashMap<>();
        map.put("pid", id);
        map.put("state", "delete");

        RetrofitApi.init().deleteMaterial(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, retrofit2.Response<DefaultResult> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        iReleaseDetailsFragment.deleteBack();
                    } else {
                        MyApplication.toastor.showToast(response.body().getMessage());
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
}
