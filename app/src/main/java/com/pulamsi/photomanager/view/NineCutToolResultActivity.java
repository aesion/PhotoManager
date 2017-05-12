package com.pulamsi.photomanager.view;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.SerializableData;
import com.pulamsi.photomanager.utils.OtherUtils;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.trycatch.mysnackbar.Prompt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-04-12
 * Time: 18:56
 * FIXME
 */
public class NineCutToolResultActivity extends CommonBaseActivity {

    TextView back;
    LinearLayout share;
    SerializableData serializableData;
    File[] nineCilpFile;

    @Override
    protected void init() {
        back = (TextView) findViewById(R.id.tv_back_home);
        share = (LinearLayout) findViewById(R.id.ll_share);

        serializableData = (SerializableData) getIntent().getExtras().getSerializable("SerializableData");
        nineCilpFile = serializableData.getFiles();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NineCutToolResultActivity.this, HomeTabActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清除除了顶部其他栈
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!OtherUtils.isWeixinAvilible(context)) {
                    SnackbarUtil.showPrompt(getWindow().getDecorView(), "没有安装微信！", Prompt.ERROR);
                    return;
                }

                if (nineCilpFile != null || nineCilpFile.length > 0){
                    try {
                        shareMultiplePictureToTimeLine(nineCilpFile);
                    }catch (Exception e){
                        MyApplication.toastor.showToast("分享失败,请确保打开了微信");
                    }
                }else {
                    MyApplication.toastor.showToast("分享失败");
                }
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_nine_cut_result;
    }


    @Override
    protected int getToolBarTitleID() {
        return R.string.ninecut_result_title;
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
//        intent.putExtra("Kdescription", gallery.getContent());
        startActivity(intent);
    }
}
