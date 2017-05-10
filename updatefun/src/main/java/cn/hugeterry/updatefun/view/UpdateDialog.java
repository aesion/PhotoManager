package cn.hugeterry.updatefun.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.hugeterry.updatefun.R;
import cn.hugeterry.updatefun.config.DownloadKey;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/7/16 19:25
 */
public class UpdateDialog extends Activity {

    private TextView yes, no;
    private TextView tv_version, tv_changelog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);

        yes = (TextView) findViewById(R.id.updatedialog_yes);
        no = (TextView) findViewById(R.id.updatedialog_no);
        tv_version = (TextView) findViewById(R.id.title);
        tv_changelog = (TextView) findViewById(R.id.updatedialog_text_changelog);

        tv_version.setText(" 版本更新 v" + DownloadKey.version);
        tv_changelog.setText("新特性：\n" + DownloadKey.changeLog);

        yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DownloadKey.TOShowDownloadView = 2;
                finish();
            }
        });

        no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DownloadKey.TOShowDownloadView = 1;
                if (DownloadKey.ISManual) {
                    DownloadKey.LoadManual = false;
                }
                finish();
            }
        });
    }


}
