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
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.GalleryclassDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.interfaces.IMainactivity;
import com.pulamsi.photomanager.view.HomePhotoFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-10
 * Time: 14:34
 * FIXME
 */
public class MainActivityPrestener extends BasePrestener {

    IMainactivity iMainactivity;

    public MainActivityPrestener(IMainactivity iMainactivity) {
        this.iMainactivity = iMainactivity;
    }

    /**
     *
     * @param isNetwork
     * 是否拿网络的
     * true为去网络获取分类
     * 否则拿本地的
     */
    public void init(boolean isNetwork) {

        if (HomePhotoFragment.isHomeIn){

        }else {
            if (!isNetwork){//是否拿网络的
                GalleryclassDao galleryclassDao = GreenDaoHelper.getDaoSession().getGalleryclassDao();
                List<Galleryclass> galleryclasses = galleryclassDao.loadAll();
                if (galleryclasses != null && galleryclasses.size() != 0) {
                    iMainactivity.setContent(galleryclasses);
                    return;
                }
            }
        }


        /**
         * 参数：mid 会员id
         返回参数：list 、cid 、 title
         */
        iMainactivity.showLoading();
        String url = getString(R.string.serverbaseurl) + getString(R.string.showPasterCat);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    Gson gson = new Gson();
                    List<Galleryclass> GalleryList = gson.fromJson(response, new TypeToken<List<Galleryclass>>() {
                    }.getType());

                    if (!HomePhotoFragment.isHomeIn) {
                        //保存分类缓存
                    GalleryclassDao galleryclassDao = GreenDaoHelper.getDaoSession().getGalleryclassDao();
                    galleryclassDao.deleteAll();//插入之前删除以前的
                    galleryclassDao.insertInTx(GalleryList);//插入所有的
                    }
                    iMainactivity.setContent(GalleryList);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "分类包装出错");
                }
                iMainactivity.showContent();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                MyApplication.toastor.showLongToast("网络错误");
                iMainactivity.showError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if (Constants.IS_LOGIN) {
                    map.put("mid", Constants.MID);
                }
                if (HomePhotoFragment.isHomeIn){
                    map.put("level", "1");
                }
                return map;
            }
        };
        MyApplication.requestQueue.add(stringRequest);
    }
}
