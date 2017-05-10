package com.pulamsi.photomanager.prestener;

import android.os.Handler;

import com.pulamsi.photomanager.interfaces.IDownloadActivity;
import com.pulamsi.photomanager.utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:04
 * FIXME
 */
public class DownloadActivityPrestener extends BasePrestener {

    private IDownloadActivity iDownloadActivity;
    private Handler handler = new Handler();

    public DownloadActivityPrestener(IDownloadActivity iDownloadActivity) {
        this.iDownloadActivity = iDownloadActivity;
    }

    /**
     * 请求本地图片
     */
    public void requestDown() {
        iDownloadActivity.showLoading();
        new Thread(new Runnable() {//耗时任务放在子线程
            @Override
            public void run() {
                File imgFile = new File(ImageUtil.SAVE_IMGDIR);
                final List<String> imags = new ArrayList<>();
                if (!imgFile.exists()){//如果不存在
                    iDownloadActivity.imagesBack(imags);
                    return;
                }
                //获取SD卡目录列表
                File[] files =imgFile.listFiles();
                for (File file : files) {
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath.contains(".jpg")){
                        imags.add(absolutePath);
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDownloadActivity.imagesBack(imags);
                        iDownloadActivity.showContent();
                    }
                });
            }
        }).start();

    }
}
