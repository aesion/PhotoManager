package com.pulamsi.photomanager.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.litesuits.common.utils.RandomUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.bean.DataChangedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gaohailong on 2016/5/20.
 */
public class ImageUtil {

    public static String SAVE_IMGDIR = Environment.getExternalStorageDirectory().getPath() + "/Pulamsi";
    public static String SHARE_IMGDIR = Environment.getExternalStorageDirectory().getPath() + "/Pulamsi" + "/shareCache";

    public static void saveImage(final Context context, final Bitmap bitmap, final View view, final boolean isShare, final int position) {
//        Log.e("", position + "<<<<<<<");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //保存路径
                String imgDir;
                if (isShare){
                    imgDir = SHARE_IMGDIR;
                }else {
                    imgDir = SAVE_IMGDIR;
                }
                String fileName;
                if (isShare){
                    fileName = position + ".jpg";
                }else {
                    fileName = RandomUtil.getRandomLetters(6) + ".jpg";
                }
                //创建文件路径
                final File fileDir = new File(imgDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                    //创建文件
                    final File imageFile = new File(fileDir, fileName);
                    try {
                        FileOutputStream fos = new FileOutputStream(imageFile);
                        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        if (compress) {
                            new Handler(context.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isShare) {
                                        AppBus.getInstance().post(new DataChangedEvent(position,imageFile));
                                    } else {
                                        SnackbarUtil.showLoadSuccess("保存成功");
                                    }
                                }
                            });
                        } else {
                            new Handler(context.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isShare) {

                                    } else {
                                        SnackbarUtil.showLoadError("保存失败");
                                    }
                                }
                            });
                        }
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Uri uri = Uri.fromFile(imageFile);
                if (!isShare){
                    //发送广播，通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                }
            }
            }

            ).

            start();
        }


    /**
     * 本地保存无需开启线程
     * @param context
     * @param bitmap
     * @param index
     */
    public static File saveImage(Context context,Bitmap bitmap, int index) {
        boolean compress = false;
        String imgDir = SAVE_IMGDIR;
        File imageFile;
        //创建文件路径
        final File fileDir = new File(imgDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String fileName = RandomUtil.getRandomLetters(6)+ index + ".jpg";
        //创建文件
        imageFile = new File(fileDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //发送广播，通知图库更新
        Uri uri = Uri.fromFile(imageFile);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        if (compress){
            return imageFile;
        }
        return null;
    }


    /**
     * 九图第一张
     * @param context
     */
    public static void saveNineCutFristAndEnd(Context context,NineCutTag nineCutTag) {
        String imgDir = SAVE_IMGDIR+ "/cut";
        File imageFile;
        //创建文件路径
        final File fileDir = new File(imgDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String fileName = RandomUtil.getRandomLetters(6) + ".jpg";
        //创建文件
        imageFile = new File(fileDir, fileName);
        Bitmap bitmap;
        if (nineCutTag == NineCutTag.FRIST){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_ninecut_one);
        }else{
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_ninecut_nine);
        }
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //发送广播，通知图库更新
        Uri uri = Uri.fromFile(imageFile);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    public enum NineCutTag{
        FRIST,END
    }


    /**
     * 压缩Bitmap
     * @param absolutePath
     * @return
     */
    private Bitmap adjustImage(String absolutePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(absolutePath, opt);

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > DensityUtil.getWidth())
                opt.inSampleSize = picWidth / DensityUtil.getWidth();
        } else {
            if (picHeight > DensityUtil.getHeight())

                opt.inSampleSize = picHeight / DensityUtil.getHeight();
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm= BitmapFactory.decodeFile(absolutePath, opt);
        // 用imageview显示出bitmap
        return bm;
    }

}
