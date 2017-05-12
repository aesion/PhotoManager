package com.pulamsi.photomanager.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.litesuits.android.log.Log;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.CommentApadter;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.CommentsBean;
import com.pulamsi.photomanager.bean.Config;
import com.pulamsi.photomanager.bean.DataChangedEvent;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.interfaces.IImageDetailsActivity;
import com.pulamsi.photomanager.prestener.ImageDetailActivityPrestener;
import com.pulamsi.photomanager.utils.DataUtils;
import com.pulamsi.photomanager.utils.DeleteUtils;
import com.pulamsi.photomanager.utils.DialogUtils;
import com.pulamsi.photomanager.utils.ImageUtil;
import com.pulamsi.photomanager.utils.JudgeUtils;
import com.pulamsi.photomanager.utils.OtherUtils;
import com.pulamsi.photomanager.utils.ShareUtils;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.CollectionBottomDialog;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.EditTextDialog;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.FlowLayout;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.ImageViewAppreciate;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.ImageViewLike;
import com.squareup.otto.Subscribe;
import com.trycatch.mysnackbar.Prompt;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-03
 * Time: 14:47
 * FIXME
 */
public class ImageDetailsActivity extends CommonBaseActivity implements BGANinePhotoLayout.Delegate, IImageDetailsActivity, DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener, View.OnClickListener ,EditTextDialog.OnSubmitCommentListener{

    private static final long TIME_DELAY = 15000;
    private String pid;
    private StatusUIManager statusUIManager;
    private StatusUIManager listUIManager;
    private FlowLayout hisFlowLayout;
    private ImageDetailActivityPrestener imageDetailActivityPrestener;
    private ArrayList<String> imgs;
    private ImageViewLike imageViewLike;
    private ImageViewAppreciate imageViewAppreciate;
    private Gallery gallery;
    private RecyclerView commentList;
    private CommentApadter commentApadter;
    private ImageView share;
    private TextView content, time, like, appreciate, comment,chat;
    private TextView submitComment;
    private MaterialDialog dialog;
    private int shareNum;
    private Config config;
    private BGANinePhotoLayout bgaNinePhotoLayout;
    private EditTextDialog editTextDialog;

    private MenuItem item;
    private String state;
    private JCVideoPlayerStandard videoplayer;


    @Override
    protected void init() {
        pid = getIntent().getStringExtra("pid");
        statusUIManager = new StatusUIManager(context, findViewById(R.id.view_content));
        listUIManager = new StatusUIManager(context, findViewById(R.id.rv_comment_list));
        hisFlowLayout = (FlowLayout) findViewById(R.id.tag_flowLayout);
        imageViewLike = (ImageViewLike) findViewById(R.id.ivl_like);
        share = (ImageView) findViewById(R.id.iv_share);
        content = (TextView) findViewById(R.id.tv_content);
        time = (TextView) findViewById(R.id.tv_time);
        like = (TextView) findViewById(R.id.tv_like);
        chat = (TextView) findViewById(R.id.tv_chat);
        comment = (TextView) findViewById(R.id.tv_comment);
        submitComment = (TextView) findViewById(R.id.tv_comment_bottom);
        submitComment.setOnClickListener(this);
        appreciate = (TextView) findViewById(R.id.tv_appreciate);
        imageViewAppreciate = (ImageViewAppreciate) findViewById(R.id.ivl_appreciate);
        commentList = (RecyclerView) findViewById(R.id.rv_comment_list);
        videoplayer = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        bgaNinePhotoLayout = (BGANinePhotoLayout) this.findViewById(R.id.npl_item_moment_photos);

        commentList.setLayoutManager(new LinearLayoutManager(context));
        commentList.setHasFixedSize(true);
        commentApadter = new CommentApadter(ImageDetailsActivity.this);
        commentList.setAdapter(commentApadter);

        imageViewLike.setOnClickListener(this);
        imageViewAppreciate.setOnClickListener(this);
        share.setOnClickListener(this);
        imageDetailActivityPrestener = new ImageDetailActivityPrestener(this);
        statusUIManager.setOnRetryClickListener(this);
        imageDetailActivityPrestener.requestData(pid);
        imageDetailActivityPrestener.requestCommentList(pid);

        editTextDialog = new EditTextDialog();

        config = GreenDaoHelper.getDaoSession().getConfigDao().queryBuilder().unique();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.image_details;
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout bgaNinePhotoLayout, View view, int i, String s, List<String> list) {
        Intent intent = new Intent(ImageDetailsActivity.this, PhotoViewerActivity.class);
        intent.putStringArrayListExtra("imgs", imgs);
        intent.putExtra("position", i);
        context.startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        item = menu.findItem(R.id.action_hot);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hot:
                if (Constants.IS_LOGIN){
                    Map<String, String> map = new HashMap<>();
                    map.put("pid", gallery.getId());
                    map.put("mid", Constants.MID);
                    map.put("state", state);
                    imageDetailActivityPrestener.requestHot(map);
                    DialogUtils.showLoadingDefult(context);
                }else {
                    MyApplication.toastor.showToast("未登录");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showLoading() {
        statusUIManager.showLoading();
    }

    @Override
    public void showEmpty() {
        statusUIManager.showEmpty();
    }

    @Override
    public void showError() {
        statusUIManager.showError();
    }

    @Override
    public void showContent() {
        statusUIManager.showContentView();
//        showGuide();//显示引导分享遮罩
    }

//    private void showGuide() {
//        if (!config.getIsImageDetailShowGuide()) {
//            return;
//        }
//
//        GuideHelper guideHelper = new GuideHelper(ImageDetailsActivity.this);
//        GuideHelper.TipData tipData1 = new GuideHelper.TipData(R.mipmap.tip1,  Gravity.TOP, share);
//        guideHelper.addPage(tipData1);
//        guideHelper.show(false);
//
//        config.setIsImageDetailShowGuide(false);
//        GreenDaoHelper.getDaoSession().getConfigDao().update(config);
//    }

    /**
     * 重新加载
     */
    @Override
    public void onClick() {
        imageDetailActivityPrestener.requestData(pid);
    }



    /**
     * 更新数据
     * @param gallery
     */
    @Override
    public void updateData(final Gallery gallery) {
        this.gallery = gallery;
        // 类型 0文字+图片 1文字 2视频
        if (gallery.getPostType() == 0){
            bgaNinePhotoLayout.setVisibility(View.VISIBLE);
            videoplayer.setVisibility(View.GONE);

            imgs = new ArrayList<>();
            for (Map.Entry<String, String> e : gallery.getPasterImages().entrySet()) {
                imgs.add(getString(R.string.imgbaseurl) + e.getValue());
            }
            bgaNinePhotoLayout.setDelegate(this);
            try {
                bgaNinePhotoLayout.setData(imgs);
            } catch (Exception e) {
                Log.e("", "java.lang.IllegalArgumentException: You cannot start a load for a destroyed\n又是这货报错,我加了this");
                showError();
            }
        }else if (gallery.getPostType() == 2){
            bgaNinePhotoLayout.setVisibility(View.GONE);
            videoplayer.setVisibility(View.VISIBLE);

            videoplayer.setUp(getContext().getString(R.string.imgbaseurl) + gallery.getVideo()
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

            Glide.with(getContext())//更改图片加载框架
                    .load(getContext().getString(R.string.imgbaseurl) + gallery.getUrl())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(videoplayer.thumbImageView);
        }

        if (gallery.getPasterCatId().equals("e702ff10-687a-4e35-9a53-d92bf2670ebd")){//根据分类ID判断是否为换货
            chat.setVisibility(View.VISIBLE);
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(gallery.getMemberId())){
                        MyApplication.toastor.showToast("用户ID为空");
                    }else {
                        sendMessage(gallery, gallery.getMemberId());
                    }
                }
            });
        }


        content.setText(gallery.getContent() + "");
        time.setText(DataUtils.stampToDate(gallery.getReleaseTime() + ""));
        like.setText(gallery.getCollectCount() + "");
        appreciate.setText(gallery.getThumbs_upCount() + "");
        comment.setText(gallery.getCommentCount() + "");
        imageViewAppreciate.setIsChenked(gallery.isGoods());
        imageViewLike.setIsChenked(gallery.isCollection());
        toolbar.setTitle(gallery.getTitle());

        hisFlowLayout.setListData(gallery.getLabels());
        hisFlowLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
            @Override
            public void TagClick(TextView tv, String text) {
                readyGo("#" + text + "#");//标签必须加上
            }
        });


        //设为推荐按钮
        if (Constants.USER_TYPE == 3){
            item.setVisible(true);
            // 是否推荐 0为否 1是
            if (gallery.getIsRecommended() == 0){
                item.setTitle("设为推荐");
                state = "recommended";
            }else if (gallery.getIsRecommended() == 1){
                item.setTitle("取消推荐");
                state = "noRecommended";
            }
        }
    }


    /**
     * 融云发送商品信息
     * @param gallery
     * @param memberId
     */
    private void sendMessage(final Gallery gallery, String memberId) {
        RichContentMessage richContentMessage = RichContentMessage.obtain(gallery.getTitle(), gallery.getContent(), getString(R.string.imgbaseurl)+gallery.getUrl());
        //"9517" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
        //Conversation.ConversationType.PRIVATE 为会话类型。
        Message myMessage = Message.obtain(memberId, Conversation.ConversationType.PRIVATE, richContentMessage);

        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                RongIM.getInstance().startPrivateChat(ImageDetailsActivity.this, gallery.getMemberId(), gallery.getMemberName());
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });

    }

    /**
     * 跳转搜索
     *
     * @param query
     */
    private void readyGo(String query) {
        Intent intent = new Intent(ImageDetailsActivity.this, SearchListActivity.class);
        intent.putExtra("search", query);
        readyGoAndIntent(intent);
    }

    /**
     * 收藏切换
     *
     * @param body
     */
    @Override
    public void collectionToggle(Boolean body) {
        if (body != null && body) {
            imageViewLike.toggle();
            if (imageViewLike.getIsChenked()) {
                like.setText(Integer.parseInt(like.getText().toString()) + 1 + "");
                SnackbarUtil.showPrompt(getWindow().getDecorView(), "收藏成功", Prompt.SUCCESS);
            } else {
                like.setText(Integer.parseInt(like.getText().toString()) - 1 + "");
                SnackbarUtil.showPrompt(getWindow().getDecorView(), "取消收藏", Prompt.WARNING);
            }
        } else {
            MyApplication.toastor.showToast("操作失败");
        }
    }

    /**
     * 添加到收藏夹
     *
     * @param ptid
     */
    public void addCollection(String ptid) {
        Map<String, String> map = new HashMap<>();
        map.put("pid", gallery.getId());
        map.put("mid", Constants.MID);
        map.put("cid", gallery.getPasterCatId());
        map.put("ptid", ptid);
        imageDetailActivityPrestener.collection(map);
    }


    /**
     * 点赞切换
     *
     * @param body
     */
    @Override
    public void appreciateToggle(Boolean body) {
        imageViewAppreciate.setClickable(true);//
        if (body != null && body) {
            imageViewAppreciate.toggle();
            if (imageViewAppreciate.getIsChenked()) {
                appreciate.setText(Integer.parseInt(appreciate.getText().toString()) + 1 + "");
            } else {
                appreciate.setText(Integer.parseInt(appreciate.getText().toString()) - 1 + "");
            }
        } else {
            MyApplication.toastor.showToast("点赞失败");
        }
    }


    /**
     * 评论的回调
     *
     * @param successful
     */
    @Override
    public void submitCommentBack(Boolean successful) {
        if (successful) {
            SnackbarUtil.showLoadSuccess("评论成功");
            imageDetailActivityPrestener.requestCommentList(pid);
            comment.setText(Integer.parseInt(comment.getText().toString()) + 1 + "");
        } else {
            SnackbarUtil.showLoadError("评论失败");
        }
    }


    /**
     * 更新评论列表
     *
     * @param body
     */
    @Override
    public void updateCommentData(List<CommentsBean> body) {
        if (body == null || body.size() == 0) {
            listUIManager.showEmpty();
        } else {
            listUIManager.showContentView();
            commentApadter.clearDataList();
            commentApadter.addDataList(body);
            commentApadter.notifyDataSetChanged();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivl_like://收藏
                if (JudgeUtils.isLogin(ImageDetailsActivity.this)) {
                    if (!imageViewLike.getIsChenked()) {
                        CollectionBottomDialog dialog = new CollectionBottomDialog();
                        dialog.setOnClickAddCollectionListener(new CollectionBottomDialog.OnClickAddCollectionListener() {
                            @Override
                            public void OnClickAddCollection(String ptid) {
                                addCollection(ptid);
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("pid", gallery.getId());
                        map.put("mid", Constants.MID);
                        map.put("cid", gallery.getPasterCatId());
                        //取消收藏没有传收藏夹ID
                        imageDetailActivityPrestener.collection(map);
                    }
                }
                break;
            case R.id.ivl_appreciate:
                if (JudgeUtils.isLogin(ImageDetailsActivity.this)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("pid", gallery.getId());
                    map.put("mid", Constants.MID);
                    imageDetailActivityPrestener.appreciate(map);//点赞或取消赞
                    imageViewAppreciate.setClickable(false);//
                }

                break;
            case R.id.iv_share:
                if (!OtherUtils.isWeixinAvilible(getContext())) {
                    SnackbarUtil.showPrompt(getWindow().getDecorView(), "没有安装微信！", Prompt.ERROR);
                    return;
                }
                requestSavePermissions();
                break;
            case R.id.tv_comment_bottom:
                if (editTextDialog == null){
                    editTextDialog = new EditTextDialog();
                }
                editTextDialog.setOnSubmitCommentListener(this);
                editTextDialog.show(getSupportFragmentManager());
                break;
        }
    }

    /**
     * 请求SD卡权限
     */
    private void requestSavePermissions() {
        MPermissions.requestPermissions(ImageDetailsActivity.this, Constants.REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(Constants.REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess() {
        if (gallery.getPostType() == 0){
            ShareUtils.getInstance().shareImageWithText(this,gallery.getTitle(),gallery.getContent(),getContext().getString(R.string.imgbaseurl) + gallery.getUrl(),this);
        }else if (gallery.getPostType() == 2){
            //准备分享视频
            ShareUtils.getInstance().shareVideo(this,getContext().getString(R.string.imgbaseurl) + gallery.getVideo(),gallery.getTitle(),
                    gallery.getContent(),getContext().getString(R.string.imgbaseurl) + gallery.getUrl(),this);
        }
    }

    @PermissionDenied(Constants.REQUECT_CODE_SDCARD)
    public void requestSdcardFailed() {
        MyApplication.toastor.showToast("对不起，没有读取SD卡的权限，请同意授权");
    }


    /**
     * 图文分享
     */
    @Override
    public void shareImageWithText() {
        //准备分享图文
        showPrompt();
    }
    /**
     * 分享操作
     */
    private void share() {
        shareNum = 0;
        dialog = new MaterialDialog.Builder(this)
                .title("正在准备分享的图片")
                .progress(false, imgs.size(), true)
                .cancelable(false)
                .show();

        DeleteUtils.DeleteFolder(ImageUtil.SHARE_IMGDIR);//删除旧文件资源
        //超时的处理
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!dialog.isCancelled()) {
                    MyApplication.toastor.showLongToast("分享请求超时，请检查图片是否正常加载！");
                    dialog.dismiss();
                }
            }
        }, TIME_DELAY);

        //循环下载保存图片
//                SnackbarUtil.showPrompt(getWindow().getDecorView(),"分享",Prompt.SUCCESS);
        for (int i = 0; i < imgs.size(); i++) {
            final int position;
            position = i;
            Glide.with(context)
                    .load(imgs.get(i))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ImageUtil.saveImage(context, resource, getWindow().getDecorView(), true, position);
                        }
                    });
        }
    }


    /**
     * 提示
     */
    private void showPrompt() {
        if (!config.getIsImageDetailShareShowDialog()) {
            share();
            return;
        }

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("温馨提示：")
                .content("分享按钮支持一键分享至微信朋友圈(多图+文字),请等待页面图片加载完成后再点击分享，如需保存或分享单张图片，请点击图片详情后再进行保存与分享的操作！")
                .positiveText("确定")
                .negativeText("不再提示")
                .negativeColor(getResources().getColor(R.color.line_cc))
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        share();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        config.setIsImageDetailShareShowDialog(false);
                        GreenDaoHelper.getDaoSession().getConfigDao().update(config);
                        share();
                    }
                })
                .show();
    }

    @Subscribe   //订阅事件DataChangedEvent
    public void SubscribeShareFile(DataChangedEvent imageFile) {
        if (dialog.getCurrentProgress() != dialog.getMaxProgress()) {
            dialog.incrementProgress(1);
        }
        shareNum++;
        Log.i(shareNum + "---" + imgs.size());
        if (imgs.size() == shareNum) {
            Log.i("发送");
            dialog.setContent("准备完成");
            File[] arr = new File[imgs.size()];
            for (int i = 0; i < imgs.size(); i++) {
                arr[i] = new File(ImageUtil.SHARE_IMGDIR, i + ".jpg");
            }
            shareMultiplePictureToTimeLine(arr);
            dialog.dismiss();
        } else {
            Log.i("保存");
        }
    }


    private void shareMultiplePictureToTimeLine(File[] files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File pic : files) {
//            imageUris.add(Uri.fromFile(pic));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是安卓7.0以上
                //修复微信在7.0崩溃的问题
                Uri uri = null;
                try {
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), null, null));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    MyApplication.toastor.showToast("Android7.0图片分享失败");
                }
                imageUris.add(uri);
            } else {//否则直接添加
                imageUris.add(Uri.fromFile(pic));
            }
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.putExtra("Kdescription", gallery.getContent());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        //注销到bus事件
        AppBus.getInstance().unregister(this);
    }


    /**
     * 提交评论
     */
    @Override
    public void onSubmitComment(String content) {
        if (JudgeUtils.isLogin(ImageDetailsActivity.this)) {
                    SnackbarUtil.showLoading(getWindow().getDecorView(), "提交中...");
                    Map<String, String> map = new HashMap<>();
                    map.put("pid", gallery.getId());
                    map.put("mid", Constants.MID);
                    map.put("content", content);
                    imageDetailActivityPrestener.submitComment(map);
                    editTextDialog.dismiss();
        }
    }


    /**
     * 下载视频
     */
    @Override
    public void downloadVideo() {
        downloadVideoProgressDialog = null;
        downloadVideoProgress(0, 100);
        imageDetailActivityPrestener.downloadFileWithVideo(getContext().getString(R.string.imgbaseurl) + gallery.getVideo());
    }


    /**
     * 下载进度显示
     * @param current
     * @param total
     */
    MaterialDialog downloadVideoProgressDialog;
    @Override
    public void downloadVideoProgress(int current, int total) {
        if (downloadVideoProgressDialog == null){
            downloadVideoProgressDialog = new MaterialDialog.Builder(this)
                    .title("正在下载")
                    .progress(false, total, true)
                    .cancelable(true)
                    .show();
        }
        downloadVideoProgressDialog.setProgress(current);
        if (downloadVideoProgressDialog.getCurrentProgress() == downloadVideoProgressDialog.getMaxProgress()){
            downloadVideoProgressDialog.setTitle("下载成功");
            MyApplication.toastor.showToast("视频已保存到本地");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    downloadVideoProgressDialog.dismiss();
                }
            },1000);
        }
    }

    /**
     * 下载失败
     */
    @Override
    public void downloadVideoFailure() {
        if (downloadVideoProgressDialog != null)
            downloadVideoProgressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
