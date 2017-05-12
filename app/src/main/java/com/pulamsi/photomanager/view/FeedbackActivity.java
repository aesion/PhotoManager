package com.pulamsi.photomanager.view;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.bean.DefaultResult;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.trycatch.mysnackbar.Prompt;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-07
 * Time: 16:51
 * FIXME
 */
public class FeedbackActivity extends CommonBaseActivity implements View.OnClickListener {

    private EditText content, contact;
    private Button commit;

    @Override
    protected void init() {
        content = (EditText) findViewById(R.id.et_feed_back_content);
        contact = (EditText) findViewById(R.id.et_feed_back_contact);
        commit = (Button) findViewById(R.id.bt_commit);
        commit.setOnClickListener(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.feedback_title;
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(content.getText())) {
            SnackbarUtil.showPrompt(getWindow().getDecorView(), "请输入意见", Prompt.WARNING);
            return;
        }
        if (TextUtils.isEmpty(contact.getText())) {
            SnackbarUtil.showPrompt(getWindow().getDecorView(), "请填写联系方式", Prompt.WARNING);
            return;
        }
        CommitFeedBack();
    }


    /**
     * 提交反馈
     */
    private void CommitFeedBack() {
        SnackbarUtil.showLoading(getWindow().getDecorView(), "提交中...");

        Map<String,String> map = new HashMap<>();
        map.put("opinion",content.getText().toString());
        map.put("qq",contact.getText().toString());

        RetrofitApi.init().commitFeedBack(map).enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                if (response.body().getSuccessful()){
                    SnackbarUtil.showLoadSuccess("提交成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                SnackbarUtil.showLoadError("提交失败");
            }
        });

    }
}
