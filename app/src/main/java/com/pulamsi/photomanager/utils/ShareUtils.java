package com.pulamsi.photomanager.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.interfaces.IImageDetailsActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-29
 * Time: 13:53
 * FIXME
 */
public class ShareUtils {

    private static ShareUtils instance;
    private ShareAction shareAction;
    private static ShareBoardConfig config;

    public static ShareUtils getInstance() {
        if (instance == null) {
            instance = new ShareUtils();
            config = new ShareBoardConfig();
            config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
            config.setCancelButtonVisibility(false);
            config.setTitleVisibility(false);
            config.setIndicatorVisibility(false);
        }
        return instance;
    }

    private void SnackbarUtil() {

    }

    public void shareString(Activity activity, String text) {
        shareAction = new ShareAction(activity);
        shareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .withText(text)
                .setCallback(umShareListener)
                .open(config);
    }


    /**
     * 分享视频
     * @param activity
     * @param url
     * @param title
     * @param description
     * @param imageUrl
     */
    public void shareVideo(final Activity activity, String url,String title,String description,String imageUrl, final IImageDetailsActivity iImageDetailsActivity) {
        final UMVideo umVideo = new UMVideo(url);
        UMImage thumb = new UMImage(activity, imageUrl);
        umVideo.setTitle(title);//视频的标题
        umVideo.setThumb(thumb);//视频的缩略图
        umVideo.setDescription(description);//描述
        shareAction = new ShareAction(activity);
        shareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .addButton("umeng_sharebutton_download", "umeng_sharebutton_download", "info_icon_1", "info_icon_1")
                .setCallback(umShareListener)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media == null) {
                            if (snsPlatform.mKeyword.equals("umeng_sharebutton_download")) {
                                iImageDetailsActivity.downloadVideo();
                            }
                        } else {
                            shareAction
                                    .setPlatform(share_media)
                                    .share();
                        }
                    }
                })
                .withMedia(umVideo)
                .open(config);
    }

    /**
     *
     * @param activity
     * @param title
     * @param description
     * @param imageUrl
     * @param iImageDetailsActivity
     */
    public void shareImageWithText(final Activity activity,String title,String description,String imageUrl, final IImageDetailsActivity iImageDetailsActivity) {
        UMImage thumb = new UMImage(activity, imageUrl);
        UMWeb web = new UMWeb("http://a.app.qq.com/o/simple.jsp?pkgname=com.pulamsi.photomanager");
        web.setTitle(title);//图文的标题
        web.setThumb(thumb);//图文的缩略图
        web.setDescription(description);//描述
        shareAction = new ShareAction(activity);
        shareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .setCallback(umShareListener)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                            if (snsPlatform.mKeyword.equals(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mKeyword)) {
                                iImageDetailsActivity.shareImageWithText();
                        } else {
                            shareAction
                                    .setPlatform(share_media)
                                    .share();
                        }
                    }
                })
                .withMedia(web)
                .open(config);
    }


    public void shareImage(Activity activity, Bitmap bitmap) {
        UMImage image = new UMImage(activity, bitmap);//bitmap文件
        shareAction = new ShareAction(activity);
        shareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .withMedia(image)
                .setCallback(umShareListener)
                .open(config);
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            MyApplication.toastor.showToast("准备分享...");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                MyApplication.toastor.showToast("收藏成功");
            } else {
                MyApplication.toastor.showToast("分享成功");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            MyApplication.toastor.showToast("分享失败");
            Log.e("",t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            MyApplication.toastor.showToast("用户取消");
        }
    };


    public void shareApp(Activity activity) {
        UMImage thumb = new UMImage(activity, R.mipmap.ic_launcher);
        UMWeb web = new UMWeb("http://a.app.qq.com/o/simple.jsp?pkgname=com.pulamsi.photomanager");
        web.setTitle("微商猎手");//标题
        web.setDescription("慧做微商，只为轻松创业-微商猎手");//描述
        web.setThumb(thumb);  //缩略图

        shareAction = new ShareAction(activity);
        shareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .withMedia(web)
                .setCallback(umShareListener)
                .open(config);
    }

}
