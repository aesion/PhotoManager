package com.pulamsi.photomanager.prestener;

import android.content.Intent;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.android.log.Log;
import com.litesuits.common.utils.RandomUtil;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.CommentsBean;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.interfaces.IImageDetailsActivity;
import com.pulamsi.photomanager.utils.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:04
 * FIXME
 */
public class ImageDetailActivityPrestener extends BasePrestener {

    private IImageDetailsActivity iImageDetailsActivity;

    public ImageDetailActivityPrestener(IImageDetailsActivity iImageDetailsActivity) {
        this.iImageDetailsActivity = iImageDetailsActivity;
    }

    public void requestData(String pid) {
        iImageDetailsActivity.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("pid", pid);
        if (Constants.IS_LOGIN)
            map.put("mid", Constants.MID);

        RetrofitApi.init().getImageDetailData(map).enqueue(new Callback<Gallery>() {
            @Override
            public void onResponse(Call<Gallery> call, Response<Gallery> response) {
                if (response.code() == 200) {
                    iImageDetailsActivity.showContent();
                    iImageDetailsActivity.updateData(response.body());
                } else {
                    iImageDetailsActivity.showError();
                    MyApplication.toastor.showToast("服务器错误");
                }
            }

            @Override
            public void onFailure(Call<Gallery> call, Throwable t) {
                iImageDetailsActivity.showError();
                MyApplication.toastor.showToast("网络错误");
            }
        });

    }

    /**
     * 收藏
     *
     * @param map
     */
    public void collection(Map<String, String> map) {
        RetrofitApi.init().collection(map).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.code() == 200) {
                    iImageDetailsActivity.collectionToggle(response.body());
                } else {
                    MyApplication.toastor.showToast("操作失败");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                MyApplication.toastor.showToast("收藏失败");
            }
        });
    }


    /**
     * 评论列表
     *
     * @param pid
     */

    public void requestCommentList(String pid) {
        Map<String, String> map = new HashMap<>();
        map.put("pid", pid);
        map.put("pageNumber", "1");
        map.put("pageSize", "200");
        if (Constants.IS_LOGIN){
            map.put("mid", Constants.MID);
        }

        RetrofitApi.init().commentList(map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        Gson gson = new Gson();
                        List<CommentsBean> commentsBeans = gson.fromJson(jsonObject.get("list").toString(), new TypeToken<List<CommentsBean>>() {
                        }.getType());
                        iImageDetailsActivity.updateCommentData(commentsBeans);
                    } catch (JSONException e) {
                        Log.e("", "出错");
                    }
                }else {
                    MyApplication.toastor.showToast("服务器错误");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                MyApplication.toastor.showToast(t.toString());
                Log.i(t.toString());
            }
        });
    }


    /**
     * 点赞
     *
     * @param map
     */
    public void appreciate(Map<String, String> map) {

        RetrofitApi.init().appreciate(map).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                iImageDetailsActivity.appreciateToggle(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                MyApplication.toastor.showToast("点赞失败");
            }
        });
    }


    /**
     * 提价评论
     *
     * @param map
     */
    public void submitComment(Map<String, String> map) {
        RetrofitApi.init().submitComment(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                if (response.code() == 200){
                    iImageDetailsActivity.submitCommentBack(response.body().getSuccessful());
                }else {
                    MyApplication.toastor.showToast("评论失败");
                    iImageDetailsActivity.submitCommentBack(false);
                }
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                MyApplication.toastor.showToast("评论失败");
                iImageDetailsActivity.submitCommentBack(false);
            }
        });
    }



    /**
     * 推荐帖子
     *
     * @param map
     */
    public void requestHot(Map<String, String> map) {
        RetrofitApi.init().requestHot(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                if (response.code() == 200){
                    if (response.body().getSuccessful()){
                        MyApplication.toastor.showToast(response.body().getMessage());
                    }else {
                        MyApplication.toastor.showToast(response.body().getMessage());
                    }
                }else {
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


    /**
     * 下载视频
     * @param fileUrl
     */
    public void downloadFileWithVideo(String fileUrl){
        Call<ResponseBody> call = RetrofitApi.init().downloadFileWithVideo(fileUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), RandomUtil.getRandomLetters(6) + ".mp4");
                    android.util.Log.e("Download", "下载是否成功? " + writtenToDisk);
                    if (!writtenToDisk){
                        iImageDetailsActivity.downloadVideoFailure();
                        MyApplication.toastor.showToast("写入失败");
                    }
                } else {
                    android.util.Log.e("Download", "下载服务器错误");
                    MyApplication.toastor.showToast("服务器错误");
                    iImageDetailsActivity.downloadVideoFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                android.util.Log.e("Download", "error :" + t.getMessage());
                MyApplication.toastor.showToast("网络错误");
                iImageDetailsActivity.downloadVideoFailure();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String savaName) {
        try {
            String DOWNLOAD_FILE_WITH_VIDEO = Environment.getExternalStorageDirectory().getPath() + "/Pulamsi" + "/Video";
            // todo change the file location/name according to your needs
            File fileDir = new File(DOWNLOAD_FILE_WITH_VIDEO);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            try {
                InputStream is = body.byteStream();
                File file = new File(fileDir, savaName);
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();

                    fileSizeDownloaded += len;
                    Log.e("下载文件", "下载中: " + fileSizeDownloaded / (fileSize / 100) + " of " + fileSize);
                    if (((int) (fileSizeDownloaded/(fileSize / 100)) % 5) == 0)
                        iImageDetailsActivity.downloadVideoProgress((int) (fileSizeDownloaded/(fileSize/100)),100);
                }

                fos.close();
                bis.close();
                is.close();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
