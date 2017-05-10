package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.User;
import com.pulamsi.photomanager.bean.UserDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.interfaces.IInfoDataActivity;
import com.pulamsi.photomanager.prestener.InfoDataActivityPrestener;
import com.pulamsi.photomanager.utils.DataUtils;
import com.pulamsi.photomanager.utils.OtherUtils;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.AvatarImageView;
import com.trycatch.mysnackbar.Prompt;

import org.ayo.view.status.StatusUIManager;

import java.util.Date;
import java.util.HashMap;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-01
 * Time: 09:56
 * FIXME
 */
public class InfoDataActivity extends CommonBaseActivity implements AvatarImageView.AfterCropListener, View.OnClickListener, IInfoDataActivity {

    /**
     * 头像控件
     */
    private AvatarImageView avatarImageView;
    private RelativeLayout avatar;
    private RelativeLayout sex;
    private RelativeLayout name;
    private RelativeLayout signature;
    private RelativeLayout birthday;
    private FrameLayout exit;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_birthday;

    private InfoDataActivityPrestener infoDataActivityPrestener;

    private HashMap<String, String> map;
    private StatusUIManager statusUIManager;


    @Override
    protected void init() {
        statusUIManager = new StatusUIManager(context, findViewById(R.id.statu_content));
        infoDataActivityPrestener = new InfoDataActivityPrestener(this);
        infoDataActivityPrestener.requestData();
        showLoading();
        avatarImageView = (AvatarImageView) findViewById(R.id.avatarIv);
        sex = (RelativeLayout) findViewById(R.id.rl_sex);
        avatar = (RelativeLayout) findViewById(R.id.rl_avatar);
        name = (RelativeLayout) findViewById(R.id.rl_name);
        signature = (RelativeLayout) findViewById(R.id.rl_signature);
        birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        exit = (FrameLayout) findViewById(R.id.ll_exit);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        avatarImageView.setAfterCropListener(this);
        avatar.setOnClickListener(this);
        sex.setOnClickListener(this);
        name.setOnClickListener(this);
        signature.setOnClickListener(this);
        birthday.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_info_data;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.info_data_title;
    }


    /**
     * 剪裁之后的回调
     *
     * @param photo
     */
    @Override
    public void afterCrop(Bitmap photo) {
        if (photo != null) {
            String avatarBase64 = OtherUtils.bitmapToBase64(photo);//头像转base64
            infoDataActivityPrestener.requestAvatar(avatarBase64);
        } else {
            MyApplication.toastor.showToast("上传失败");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        if (avatarImageView != null) {
            avatarImageView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                avatarImageView.performClick();//模拟点击
                break;
            case R.id.rl_sex:
                showChooseSex();//选择性别
                break;
            case R.id.rl_name:
                inputName();//输入昵称
                break;
            case R.id.rl_signature:
                inputSignature();//输入个性签名
                break;
            case R.id.rl_birthday:
                showChooseBirthday();//选择生日
                break;
            case R.id.ll_exit:
                exit();//退出账号
                break;
        }
    }


    /**
     * 退出账号
     */
    private void exit() {
        new MaterialDialog.Builder(this)
                .content("你确定不是手滑了么？")
                .positiveText("注销")
                .negativeText("我手滑了")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                        User user = userDao.queryBuilder().unique();
                        if (user != null) {
                            userDao.deleteAll();
                            Constants.MID = null;
                            Constants.IS_LOGIN = false;
                            Constants.IMG_URL = null;
                            Constants.NAME = null;
                            Constants.AUTO_GRAPH = null;
                            Constants.RONGCLOUD_TOKEN = null;
                            Constants.USER_TYPE = 0;
                            AppBus.getInstance().post(Flag.REFRESH_CLASSIFICATION);
                            MyApplication.toastor.showToast("退出成功");
                            Intent intent = new Intent(InfoDataActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            SnackbarUtil.showPrompt(getWindow().getDecorView(), "退出账号失败", Prompt.ERROR);
                        }
                    }
                })
                .show();
    }


    /**
     * 选择生日
     */
    private void showChooseBirthday() {
        new MaterialDialog.Builder(this)
                .title("选择生日")
                .customView(R.layout.dialog_datepicker, false)
                .positiveText("确定")
                .negativeText("取消")
                .negativeColor(Color.LTGRAY)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        DatePicker datePicker = (DatePicker) materialDialog.getCustomView().findViewById(R.id.datePicker);
//                        datePicker.setMaxDate(new Date().getTime());//不能超过当前时间
//                        MyApplication.toastor.showToast(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        datePicker.setMaxDate((new Date()).getTime());
                        map = new HashMap<String, String>();
                        map.put("birth", datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        infoDataActivityPrestener.requestUpdateData(map);
                    }
                })
                .show();
    }

    /**
     * 输入个性签名
     */
    private void inputSignature() {
        new MaterialDialog.Builder(this)
                .title("修改个性签名")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 20)
                .positiveText("确定")
                .negativeText("取消")
                .negativeColor(Color.LTGRAY)
                .input("请输入新个性签名", Constants.AUTO_GRAPH, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        map = new HashMap<String, String>();
                        map.put("autograph", charSequence.toString());
                        infoDataActivityPrestener.requestUpdateData(map);
                    }
                }).show();
    }


    /**
     * 输入昵称
     */
    private void inputName() {
        new MaterialDialog.Builder(this)
                .title("修改昵称")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(1, 8)
                .positiveText("确定")
                .negativeText("取消")
                .negativeColor(Color.LTGRAY)
                .input("请输入新昵称", Constants.NAME, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        map = new HashMap<String, String>();
                        map.put("name", charSequence.toString());
                        infoDataActivityPrestener.requestUpdateData(map);
                    }
                }).show();
    }

    /**
     * 选择性别
     */
    private void showChooseSex() {
        int i = 0;//默认男
        if (!TextUtils.isEmpty(tv_sex.getText()) && tv_sex.getText().toString().equals("女"))
            i = 1;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.items(R.array.sex_choose)
                .itemsCallbackSingleChoice(i, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        map = new HashMap<String, String>();
                        map.put("sex", charSequence.toString());
                        infoDataActivityPrestener.requestUpdateData(map);
                        return false;
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .negativeColor(Color.LTGRAY)
                .show();
    }


    /**
     * 头像上传成功
     */
    @Override
    public void backHeadSuccess() {
        SnackbarUtil.showPrompt(getWindow().getDecorView(), "头像上传成功", Prompt.SUCCESS);
        infoDataActivityPrestener.requestData();
    }


    /**
     * 资料修改成功
     */
    @Override
    public void backUpdateDataSuccess() {
        infoDataActivityPrestener.requestData();
    }

    /**
     * 获取资料成功
     *
     * @param mUser
     */
    @Override
    public void backDataSuccess(User mUser) {
        Glide.with(MyApplication.context)//更改图片加载框架
                .load(Constants.IMG_URL.contains("http://") ? Constants.IMG_URL : MyApplication.context.getString(R.string.imgbaseurl) + Constants.IMG_URL)
                .asBitmap()
//              .placeholder(R.mipmap.ic_login_avatar) 设置了之后图片缓存不会立即出来
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatarImageView);

        tv_name.setText(mUser.getName());
        tv_sex.setText(mUser.getSex());
        tv_birthday.setText(DataUtils.dateToStr(DataUtils.strToDate(mUser.getBirthDay())));
        showContent();
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
}
