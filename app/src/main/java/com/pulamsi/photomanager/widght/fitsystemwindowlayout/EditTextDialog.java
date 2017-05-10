package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.MyApplication;

import me.shaohui.bottomdialog.BaseBottomDialog;

public class EditTextDialog extends BaseBottomDialog {

    private EditText mEditText;
    private TextView submit;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_edit_text;
    }

    @Override
    public void bindView(View v) {
        mEditText = (EditText) v.findViewById(R.id.edit_text);
        submit = (TextView) v.findViewById(R.id.tv_submit);
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEditText.getText().toString())){
                    MyApplication.toastor.showToast("不能为空");
                }else {
                    if (onSubmitCommentListener != null){
                        onSubmitCommentListener.onSubmitComment(mEditText.getText().toString());
                        mEditText.getText().clear();
                    }
                }
            }
        });
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private OnSubmitCommentListener onSubmitCommentListener;
    public void setOnSubmitCommentListener(OnSubmitCommentListener onSubmitCommentListener) {
                this.onSubmitCommentListener = onSubmitCommentListener;
    }

    public interface OnSubmitCommentListener{
        void onSubmitComment(String content);
    }
}