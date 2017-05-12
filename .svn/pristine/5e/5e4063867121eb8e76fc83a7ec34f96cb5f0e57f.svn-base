package com.pulamsi.photomanager.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.trycatch.mysnackbar.Prompt;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-22
 * Time: 13:58
 * FIXME
 */
public class CreateFolderActivity extends CommonBaseActivity implements View.OnClickListener {


    private EditText createFolder;
    private Button commit;

    @Override
    protected void init() {
        createFolder = (EditText) findViewById(R.id.et_folder);
        commit = (Button) findViewById(R.id.bt_commit);
        commit.setOnClickListener(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.create_folder_activity;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.create_folder_title;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_commit:
                if (TextUtils.isEmpty(createFolder.getText().toString())){
                    SnackbarUtil.showPrompt(getWindow().getDecorView(),"名称不能为空！", Prompt.ERROR);
                }else {
                    requestCreateFolder();
                }
                break;
        }
    }

    /**
     * 创建文件夹
     */
    private void requestCreateFolder() {

        HashMap<String,String> map = new HashMap<>();
        map.put("mid", Constants.MID);
        map.put("preTypeName", createFolder.getText().toString());

        RetrofitApi.init().requestCreateCollection(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                if (response.body().getSuccessful()){
                    MyApplication.toastor.showToast("收藏夹创建成功！");
                    backSuccess();
                }else {
                    MyApplication.toastor.showToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                MyApplication.toastor.showToast("网络错误");
            }
        });
    }


    /**
     * 返回上一层
     */
    private void backSuccess() {
        setResult(RESULT_OK, getIntent()); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        finish();//此处一定要调用finish()方法
    }
}
