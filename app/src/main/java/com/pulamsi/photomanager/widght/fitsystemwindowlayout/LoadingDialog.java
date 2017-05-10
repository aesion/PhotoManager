package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-01
 * Time: 13:44
 * FIXME
 */
public class LoadingDialog extends Dialog {
    private TextView tvMessage;
    private String message;

    public LoadingDialog(Context context) {
        super(context, R.style.load_dialog);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvMessage.setText(message);
        init();
    }

    public void init() {
        setCancelable(false);

        Window window = this.getWindow();
        WindowManager.LayoutParams para = window.getAttributes(); // 获取对话框当前的参数值
        para.width = WindowManager.LayoutParams.WRAP_CONTENT;
//    para.width = WindowManager.LayoutParams.MATCH_PARENT;
        para.height = WindowManager.LayoutParams.WRAP_CONTENT;
//    window.getDecorView().setPadding(0, 0, 0, 0);//解决MATCH_PARENT无效的问题
        para.gravity = Gravity.CENTER;
        para.type = WindowManager.LayoutParams.TYPE_TOAST;
        window.setAttributes(para);

//    其中的关键就是
//    para.type = WindowManager.LayoutParams.TYPE_TOAST;
//    这段代码,即设置一个window级别与toast相同的弹窗.一般网上推荐使用的是
//    para.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;但在某些手机系统上即便在manifest中声明了
//            <uses-permission Android:name="android.permission.SYSTEM_ALERT_WINDOW"/>也不会生效,比如我遇到的魅族pro5就不会生效.
//            而toast级别的已经足够我们使用了,而且是不再依赖于activity的.
    }

    public void hindDialog() {
        if (this.isShowing())
            dismiss();
    }
}
