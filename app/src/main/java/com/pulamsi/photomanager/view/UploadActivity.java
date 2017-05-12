package com.pulamsi.photomanager.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.Config;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.PasterLabel;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.interfaces.IUploadActivity;
import com.pulamsi.photomanager.prestener.UploadActivityPrestener;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.FlowLayout;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.FlowRadioGroup;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.VideoSelectView;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-12-20
 * Time: 11:44
 * FIXME
 */
public class UploadActivity extends CommonBaseActivity implements DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener, SegmentControlView.OnSegmentChangedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, BGASortableNinePhotoLayout.Delegate, IUploadActivity {
//    String SAVE_IMGDIR = Environment.getExternalStorageDirectory().getPath() + "/Pulamsi/ce.mp4";

    FlowRadioGroup radioGroup;
    LayoutInflater inflater;
    FlowLayout flowLayout;
    TextView button;
    BGASortableNinePhotoLayout mPhotosSnpl;
    FrameLayout commit;
    EditText etContent;
    EditText etTitle;
    SegmentControlView segmentControlView;
    SegmentControlView SCVPostType;
    LinearLayout ll_uploadVideo,ll_uploadPhoto;
    VideoSelectView vsv_VideoSelectView;
    File videoFile;


    private int status = 0;//默认公开
    private int postType = 0;//类型 0 文字及图片 1 文字 2 视频
    //默认为文字及图片

    UploadActivityPrestener uploadActivityPrestener;
    StatusUIManager statusUIManager;

    private String classifiCation;
    private String classifiCationId;
    private StringBuilder sbTag;
    private Map<String, String> tagMap;

    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    Config config;


    @Override
    protected void init() {
        statusUIManager = new StatusUIManager(context, findViewById(R.id.view_content));
        statusUIManager.setOnRetryClickListener(this);

        uploadActivityPrestener = new UploadActivityPrestener(this);
        uploadActivityPrestener.requestData();

        inflater = this.getLayoutInflater();
        radioGroup = (FlowRadioGroup) findViewById(R.id.rg_radioGroup);
        flowLayout = (FlowLayout) findViewById(R.id.hot_flowLayout);
        commit = (FrameLayout) findViewById(R.id.fl_commit);
        etContent = (EditText) findViewById(R.id.et_content);
        etTitle = (EditText) findViewById(R.id.et_title);
        mPhotosSnpl = (BGASortableNinePhotoLayout) findViewById(R.id.snpl_moment_add_photos);
        segmentControlView = (SegmentControlView) findViewById(R.id.scv_segmentcontrolview);
        ll_uploadVideo = (LinearLayout) findViewById(R.id.ll_upload_video);
        ll_uploadPhoto = (LinearLayout) findViewById(R.id.ll_upload_photo);
        vsv_VideoSelectView = (VideoSelectView) findViewById(R.id.vsv_VideoSelectView);
        vsv_VideoSelectView.setonDeleteVideoListener(new VideoSelectView.OnDeleteVideoListener() {
            @Override
            public void onDeleteVideo() {
                videoFile = null;
            }
        });

        SCVPostType = (SegmentControlView) findViewById(R.id.scv_segmentcontrolview_type);
        SCVPostType.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int i) {
              switch (i){
                  case 0:
                    postType = 0;
                      ll_uploadPhoto.setVisibility(View.VISIBLE);
                      ll_uploadVideo.setVisibility(View.GONE);
                  break;
                  case 1:
                    postType = 2;
                      ll_uploadPhoto.setVisibility(View.GONE);
                      ll_uploadVideo.setVisibility(View.VISIBLE);
                  break;
              }
            }
        });
        segmentControlView.setOnSegmentChangedListener(this);
        button = (TextView) findViewById(R.id.bt_add_tag);
        button.setOnClickListener(this);
        commit.setOnClickListener(this);

        initTakePhoto();
    }


    /**
     * 提示
     */
    private void showPrompt() {
        config = GreenDaoHelper.getDaoSession().getConfigDao().queryBuilder().unique();

        if (!config.getIsUploadShowDialog()) {
            return;
        }

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("上传须知：")
                .content("请在上传素材的时候，认真填写分类，标签，标题与内容，如果没有适合的标签，可自行添加（不可填写与素材无关的内容),为了一打造个高品质的素材库与良好的用户体验，请认真填写，谢谢！")
                .positiveText("确定")
                .negativeText("不再提示")
                .negativeColor(getResources().getColor(R.color.line_cc))
                .cancelable(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        config.setIsUploadShowDialog(false);
                        GreenDaoHelper.getDaoSession().getConfigDao().update(config);
                    }
                })
                .show();
    }

    private void initTakePhoto() {
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
    }


    /**
     * 初始化Tag
     */
    public void initHotTag(List<PasterLabel> pasterLabelList) {
        sbTag = new StringBuilder();
        tagMap = new HashMap<>();
        List<String> list = new ArrayList();
        for (PasterLabel pasterLabel : pasterLabelList) {
            list.add(pasterLabel.getLabelName());
        }

        flowLayout.setMultiSelect(true);
        flowLayout.setColorful(false);
        flowLayout.setListData(list);
        flowLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
            @Override
            public void TagClick(TextView tv, String text) {
                if ((Boolean) tv.getTag()) {
                    tagMap.put(text, text);
                } else {
                    tagMap.remove(text);
                }
            }
        });
    }


    /**
     * 上传成功回调
     */
    @Override
    public void successBack() {
        Intent intent = new Intent(UploadActivity.this, UploadSuccessActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 初始化分类
     */
    @Override
    public void initClassify(List<Galleryclass> galleryList) {
        radioGroup.setListData(galleryList);
        radioGroup.setOnRadioClickListener(new FlowRadioGroup.OnRadioClickListener() {
            @Override
            public void RadioClick(RadioButton button, boolean checked, String text) {
                if (checked) {
                    classifiCation = text;
                    classifiCationId = (String) button.getTag();
                }
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_upload;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.upload_title;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            MyApplication.toastor.showToast(buttonView.getText().toString() + "" + buttonView.getTag().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_tag:
                showAddTag();
                break;
            case R.id.fl_commit:
                commit();
                break;
        }
    }

    private void showAddTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("添加标签");
        View view = View.inflate(context, R.layout.dialog_add_tag, null);
        final TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.til_tag);
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(inputLayout.getEditText().getText())) {
                    MyApplication.toastor.showToast("输入为空");
                } else {
//                    InputSoftUtils.hideInputSoft(UploadActivity.this);
                    flowLayout.addTag(inputLayout.getEditText().getText().toString());
                    tagMap.put(inputLayout.getEditText().getText().toString(), inputLayout.getEditText().getText().toString());
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void commit() {
        if (judge()) {
            Map<String, String> maps = new HashMap<>();
            if (!TextUtils.isEmpty(etContent.getText())) {
                maps.put("content", etContent.getText().toString());
            }
            maps.put("labels", sbTag.toString());
            maps.put("pasterCatId", classifiCationId);
            maps.put("title", etTitle.getText().toString());
            maps.put("memberId", Constants.MID);
            maps.put("status", status + "");
            maps.put("postType", postType + "");
            if (postType == 0){
                uploadActivityPrestener.commit(mPhotosSnpl.getData(), maps);
            }else if (postType == 2){
                uploadActivityPrestener.commitVideo(videoFile,maps);
            }
        }
    }


    /**
     * 判断
     *
     * @return
     */
    private boolean judge() {
        if (TextUtils.isEmpty(classifiCation)) {
            MyApplication.toastor.showToast("请选择分类");
            return false;
        } else if (tagMap == null || tagMap.size() == 0) {
            MyApplication.toastor.showToast("请选择标签");
            return false;
        } else if (TextUtils.isEmpty(etTitle.getText())) {
            MyApplication.toastor.showToast("请输入标题");
            return false;
        } else if (postType == 0 && mPhotosSnpl.getItemCount() == 0) {
                MyApplication.toastor.showToast("请至少选择一张图片");
                return false;
        }else if (postType == 2 && videoFile == null){
            MyApplication.toastor.showToast("请至少选择一个视频");
            return false;
        }
        sbTag.delete(0, sbTag.length());
        for (Map.Entry<String, String> e : tagMap.entrySet()) {
            sbTag.append(e.getValue() + ",");
        }
        sbTag.deleteCharAt(sbTag.length() - 1);
        return true;
    }


    /**
     * nine photo
     */
    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }

        if (requestCode == VideoSelectView.REQUEST_VIDEO_CODE) {
            if (resultCode == RESULT_OK) {
                vsv_VideoSelectView.onActivityResult(data);
                Uri uri = data.getData();
                videoFile = uri2File(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public Context getContext() {
        return this;
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
    }

    @Override
    public void showContent(boolean isHotTag,boolean isClassify) {
        if (isHotTag && isClassify){
            showPrompt();
            statusUIManager.showContentView();
        }
    }


    /**
     * 选项卡切换
     *
     * @param i 0为已发表，1为草稿，2为私人内容(不会被公开)
     */
    @Override
    public void onSegmentChanged(int i) {
        if (i == 0) {
            status = 0;
        } else {
            status = 2;
        }
    }

    @Override
    public void onClick() {
        uploadActivityPrestener.requestData();
    }


    /**
     * Uri转File
     * 不知道是不是都可以用
     * @param uri
     * @return
     */
    private File uri2File(Uri uri) {
        File file = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = this.managedQuery(uri, proj, null,
                null, null);
        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        file = new File(img_path);
        return file;
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
