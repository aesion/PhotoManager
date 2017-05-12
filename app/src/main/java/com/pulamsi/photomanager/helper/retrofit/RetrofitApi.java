package com.pulamsi.photomanager.helper.retrofit;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.MineCount;
import com.pulamsi.photomanager.bean.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public class RetrofitApi {
    private static RetrofitInterFace retrofitInterFace;

    /**
     * 普通下载请求
     * @return
     */
    public static RetrofitInterFace init() {
        if (retrofitInterFace == null) {
            retrofitInterFace = new Retrofit.Builder()
                    .baseUrl(MyApplication.context.getString(R.string.serverbaseurl))
                            //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                            //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitInterFace.class);
        }
        return retrofitInterFace;
    }



    public interface RetrofitInterFace {

        //单张图片上传
        @Multipart
        @POST("android/paster/addPaster.html")
        Call<DefaultResult> updateImage(@Part MultipartBody.Part part);

        //多张图片上传
        @Multipart
        @POST("android/paster/addPaster.html")
        Call<DefaultResult> updateImage(@Part MultipartBody.Part[] parts);

        //图文上传
        @Multipart
        @POST("android/paster/addPaster.html")
        Call<DefaultResult> updateImage(@Part MultipartBody.Part[] parts,@QueryMap Map<String, String> maps);

        //视频上传
        @Multipart
        @POST("android/paster/addPaster.html")
        Call<DefaultResult> updateVideo(@Part MultipartBody.Part mainImg,@Part MultipartBody.Part part,@QueryMap Map<String, String> maps);

        //微信登录
        @FormUrlEncoded
        @POST("android/lsmobileLogin/WXsubmit.html")
        Call<DefaultResult> login(@FieldMap Map<String,String> map);

        //图文详情
        @FormUrlEncoded
        @POST("android/paster/showPaster.html")
        Call<Gallery> getImageDetailData(@FieldMap Map<String,String> map);


        //添加或删除收藏接口,第一次调用保存，第二次调用删除
        @FormUrlEncoded
        @POST("android/preCollectFeed/save.html")
        Call<Boolean> collection(@FieldMap Map<String,String> map);


        //点赞或取消赞
        @FormUrlEncoded
        @POST("android/paster/goodsDownOrUp.html")
        Call<Boolean> appreciate(@FieldMap Map<String,String> map);


        //获取贴评论列表
        @FormUrlEncoded
        @POST("android/comments/showCommentsPJ.html")
        Call<String> commentList(@FieldMap Map<String,String> map);

        //发布或者回复评论
        @FormUrlEncoded
        @POST("android/comments/save.html")
        Call<DefaultResult> submitComment(@FieldMap Map<String,String> map);


        //按标题搜索
        @FormUrlEncoded
        @POST("android/paster/getPasterByTitle.html")
        Call<String> requestSearch(@FieldMap Map<String,String> map);

        //按标签搜索
        @FormUrlEncoded
        @POST("android/paster/getPasterByLabelName.html")
        Call<String> requestTagSearch(@FieldMap Map<String,String> map);


        //意见反馈
        @FormUrlEncoded
        @POST("android/lsopinion/submit.html")
        Call<DefaultResult> commitFeedBack(@FieldMap Map<String,String> map);


        //获取帖子数量和收藏数量
        @FormUrlEncoded
        @POST("android/paster/getCountByMid.html")
        Call<MineCount> requestMineCount(@FieldMap Map<String,String> map);


        //获取栏目管理的分类
        @FormUrlEncoded
        @POST("android/paster/showPasterCat2.html")
        Call<List<Galleryclass>> requestPasterList(@FieldMap Map<String,String> map);


        //获取我的收藏列表（标题那一层）
        @FormUrlEncoded
        @POST("android/preCollectFeed/showPreType.html")
        Call<String> requestCollection(@FieldMap Map<String,String> map);

        //创建的收藏夹
        @FormUrlEncoded
        @POST("android/preCollectFeed/ptSave.html")
        Call<DefaultResult> requestCreateCollection(@FieldMap Map<String,String> map);

        //修改分类是否挑选及顺序(保存修改的分类)
        @FormUrlEncoded
        @POST("android/paster/addSelectPC.html")
        Call<Boolean> addSelectPC(@FieldMap Map<String,String> map);

         //删除或恢复（软删） 另外，软删除15天内不恢复，将凌晨1点自动彻底删除
        @FormUrlEncoded
        @POST("android/paster/pasterDeleteOrRecover.html")
        Call<DefaultResult> deleteMaterial(@FieldMap Map<String,String> map);

        //上传头像接口
        @FormUrlEncoded
        @POST("android/lsmember/headPro.html")
        Call<DefaultResult> setHeadPro(@FieldMap Map<String,String> map);

        //修改用户资料
        @FormUrlEncoded
        @POST("android/lsmember/updateMember.html")
        Call<DefaultResult> updateMember(@FieldMap Map<String,String> map);

        //请求用户信息
        @FormUrlEncoded
        @POST("android/lsmember/showMember.html")
        Call<User> requestUserInformation(@FieldMap Map<String,String> map);

        //评论点赞和取消点赞
        @FormUrlEncoded
        @POST("android/comments/goodsDownOrUp.html")
        Call<Boolean> requestCommentAppreciate(@FieldMap Map<String,String> map);

        //设为推荐或者取消推荐
        @FormUrlEncoded
        @POST("android/paster/setUpPrecommended.html")
        Call<DefaultResult> requestHot(@FieldMap Map<String,String> map);


        //下载文件，例如Video文件
        @GET
        Call<ResponseBody> downloadFileWithVideo(@Url String fileUrl);
    }

}