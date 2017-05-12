package com.pulamsi.photomanager.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.MyApplication;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-04-10
 * Time: 09:56
 * FIXME
 */
public class NineCutToolChooseActivity extends CommonBaseActivity {

    private TextView choose;

    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    @Override
    protected void init() {
        choose = (TextView) findViewById(R.id.tv_choose);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_nine_cut_choose;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.ninecut_title;
    }


    public void choose(View view){
        choosePhoto();//选择图片
    }


    /**
     * 选择图片
     */
    private void choosePhoto() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);

//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent,
//                IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                    Intent intent=new Intent(NineCutToolChooseActivity.this,NineCutToolActivity.class);
                    intent.putExtra("uri",uri);
                    startActivity(intent);
            }else {
                MyApplication.toastor.showToast("图片获取失败");
            }
        }
    }


}
