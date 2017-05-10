package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-30
 * Time: 09:28
 * FIXME
 */
public class BottomDialog extends Dialog implements View.OnClickListener {
    private LinearLayout exit;
    private LinearLayout share;

    private IShareListener iShareListener;
    private IExitListener iExitListener;

    public BottomDialog(Context context) {
        super(context, R.style.BottomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_dialog_layout);
        init();
        initView();
    }

    private void initView() {
        share = (LinearLayout) findViewById(R.id.ll_share);
        exit = (LinearLayout) findViewById(R.id.ll_exit);
        share.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void init() {
        /**
         * 位于底部
         */
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);

        //DiaLog的match_parent生效，全屏
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);//需要添加的语句

        //背景获得焦点，类似PopWindow
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }



    public void setOnShareListener(IShareListener iShareListener){
        this.iShareListener = iShareListener;
    }

    public void setOnExitListener(IExitListener iExitListener){
        this.iExitListener = iExitListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_share:
                if (iShareListener!=null)
                    iShareListener.share();
                break;
            case R.id.ll_exit:
                if (iExitListener!=null)
                    iExitListener.exit();
                dismiss();
                break;
        }
    }

    public  interface IShareListener{
        void share();
    }

    public  interface IExitListener{
        void exit();
    }

}
