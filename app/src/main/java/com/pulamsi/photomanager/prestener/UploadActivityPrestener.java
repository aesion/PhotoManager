package com.pulamsi.photomanager.prestener;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.android.log.Log;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.PasterLabel;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.IUploadActivity;
import com.pulamsi.photomanager.utils.DialogUtils;
import com.pulamsi.photomanager.utils.ImageDispose;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:04
 * FIXME
 */
public class UploadActivityPrestener extends BasePrestener {

    IUploadActivity iUploadActivity;

    boolean isHotTag;
    boolean isClassify;

    public UploadActivityPrestener(IUploadActivity iUploadActivity) {
        this.iUploadActivity = iUploadActivity;
    }

    public void requestData() {
        iUploadActivity.showLoading();
        RequestClassify();
        RequestHotTag();
    }

    private void RequestHotTag() {
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
                    iUploadActivity.initHotTag(pasterLabelList);
                    isHotTag = true;
                    iUploadActivity.showContent(isHotTag,isClassify);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "标签包装出错");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iUploadActivity.showError();
                Log.i(error.toString());
            }
        });
        MyApplication.requestQueue.add(stringRequest);
    }

    private void RequestClassify() {
        /**
         * 参数：mid 会员id
         返回参数：list 、cid 、 title
         */
        String url = getString(R.string.serverbaseurl) + getString(R.string.showPasterCat);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null)
                    return;
                try {
                    Gson gson = new Gson();
                    List<Galleryclass> galleryList = gson.fromJson(response, new TypeToken<List<Galleryclass>>() {
                    }.getType());
                    iUploadActivity.initClassify(galleryList);
                    isClassify = true;
                    iUploadActivity.showContent(isHotTag,isClassify);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "分类包装出错");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iUploadActivity.showError();
                Log.i(error.toString());
            }
        });
        MyApplication.requestQueue.add(stringRequest);
    }


    public void commit(ArrayList<String> data,Map<String, String> maps) {
        DialogUtils.showLoading("正在上传中,请耐心等待...", iUploadActivity.getContext());
        MultipartBody.Part[] parts = new MultipartBody.Part[data.size()];//多张图片参数
        for (int i = 0; i < data.size(); i++) {
            File file = new File(data.get(i));//创建File
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            parts[i] = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }

        RetrofitApi.init().updateImage(parts, maps).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, retrofit2.Response<DefaultResult> response) {
                DialogUtils.hideLoading();
                if (response.code() == 200) {
                    if (response.body().getSuccessful()) {
                        iUploadActivity.successBack();
                    } else {
                        MyApplication.toastor.showToast(response.body().getMessage());
                    }
                } else {
                    MyApplication.toastor.showToast("上传失败，服务器出错！");
                }
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                DialogUtils.hideLoading();
                MyApplication.toastor.showToast("上传失败，请检查网络！");
            }
        });
    }


    String SAVE_IMGDIR = Environment.getExternalStorageDirectory().getPath() + "/Pulamsi/ce.mp4";


    public void commitVideo(File videoFile, Map<String, String> maps) {
        DialogUtils.showLoading("正在上传中,请耐心等待...", iUploadActivity.getContext());

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
        byte[] bytes = ImageDispose.Bitmap2Bytes(bitmap);
        //        2、创建RequestBody，其中`multipart/form-data`为编码类型
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
//        3、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        MultipartBody.Part image = MultipartBody.Part.createFormData("file1", "", requestFile2);






        //        1、根据地址拿到File
        File file = new File(SAVE_IMGDIR);
//        2、创建RequestBody，其中`multipart/form-data`为编码类型
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
//        3、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        MultipartBody.Part video = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);



        RetrofitApi.init().updateVideo(image,video,maps).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, retrofit2.Response<DefaultResult> response) {
                DialogUtils.hideLoading();
                if (response.code() == 200){
                    if (response.body().getSuccessful()){
                        iUploadActivity.successBack();
                    }else {
                        MyApplication.toastor.showToast(response.body().getMessage());
                    }
                }else {
                    MyApplication.toastor.showToast("上传失败，服务器出错！");
                }
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                DialogUtils.hideLoading();
                MyApplication.toastor.showToast("上传失败，请检查网络！");
            }
        });
    }


}
