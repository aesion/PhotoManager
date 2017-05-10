package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.NineCutMaskListAdapter;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.FilterEffect;
import com.pulamsi.photomanager.bean.ImagePiece;
import com.pulamsi.photomanager.bean.SerializableData;
import com.pulamsi.photomanager.utils.DataHandler;
import com.pulamsi.photomanager.utils.GPUImageFilterTools;
import com.pulamsi.photomanager.utils.ImageUtil;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.NineCutView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-04-10
 * Time: 09:56
 * FIXME
 */
public class NineCutToolActivity extends CommonBaseActivity implements View.OnClickListener,NineCutMaskListAdapter.OnRecyclerViewItemClickListener{

    private NineCutView nineCutView;
    private LinearLayout save;
    private LinearLayout mask;
    private LinearLayout fiter;
    private RecyclerView maskList;
    private Integer[] CutMask = new Integer[]{R.mipmap.cut_thumb_normal,R.mipmap.cut_thumb_star,R.mipmap.cut_thumb_flower,R.mipmap.cut_thumb_heart
            ,R.mipmap.cut_thumb_circle,R.mipmap.cut_thumb_bear,R.mipmap.cut_thumb_cat,R.mipmap.cut_thumb_cat_2,R.mipmap.cut_thumb_apple
            ,R.mipmap.cut_thumb_dog,R.mipmap.cut_thumb_qq,R.mipmap.cut_thumb_mice,R.mipmap.cut_thumb_shit};

    private Integer[] CutFiter = new Integer[]{R.mipmap.filter_normal,R.mipmap.filter_xproii,R.mipmap.filter_brannan,R.mipmap.filter_hudson,
            R.mipmap.filter_lomofi,R.mipmap.filter_lord_kelvin,R.mipmap.filter_amaro,};
    private NineCutMaskListAdapter nineCutMaskListAdapter;

    private Bitmap bitmap,bitmapWithFilterApplied;//获得相册的图片


    private ListTag listTag = ListTag.MASK;//初始是图形



    enum ListTag{
        MASK,FITER
    }

    @Override
    protected void init() {
        Intent intent=getIntent();
        Uri uri = intent.getParcelableExtra("uri");
        nineCutView = (NineCutView) findViewById(R.id.nineCutView);
        save = (LinearLayout) findViewById(R.id.ll_save);
        mask = (LinearLayout) findViewById(R.id.ll_mask);
        fiter = (LinearLayout) findViewById(R.id.ll_fiter);
        maskList = (RecyclerView) findViewById(R.id.rv_mask);
        save.setOnClickListener(this);
        mask.setOnClickListener(this);
        fiter.setOnClickListener(this);

        maskList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        nineCutMaskListAdapter = new NineCutMaskListAdapter(this,CutMask);
        maskList.setAdapter(nineCutMaskListAdapter);

        nineCutMaskListAdapter.setOnItemClickListener(this);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Log.v("前", uri.toString() + "<前>" + bitmap.getByteCount());

            //图片压缩一半防止内存溢出
            Matrix matrix = new Matrix();
            matrix.setScale(0.5f, 0.5f);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);

            Log.v("后",uri.toString()+"<后>"+bitmap.getByteCount());

            nineCutView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            MyApplication.toastor.showToast("获取图片失败");
        }
    }



    @Override
    protected int getContentViewID() {
        return R.layout.activity_nine_cut;
    }



    @Override
    protected int getToolBarTitleID() {
        return R.string.ninecut_title;
    }
    int FITER;
    int MASK;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_save:
                Bitmap clipBitMap = nineCutView.clip();//截取放大的部分
                nineCilp(clipBitMap);//九图分割
                break;
            case R.id.ll_mask:
                listTag = ListTag.MASK;

                FITER = nineCutMaskListAdapter.layoutPosition;
                nineCutMaskListAdapter = new NineCutMaskListAdapter(this,CutMask);
                maskList.setAdapter(nineCutMaskListAdapter);
                nineCutMaskListAdapter.layoutPosition = MASK;
                nineCutMaskListAdapter.setOnItemClickListener(this);
                nineCutMaskListAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_fiter:
                listTag = ListTag.FITER;

                MASK = nineCutMaskListAdapter.layoutPosition;
                nineCutMaskListAdapter = new NineCutMaskListAdapter(this,CutFiter);
                maskList.setAdapter(nineCutMaskListAdapter);
                nineCutMaskListAdapter.layoutPosition = FITER;
                nineCutMaskListAdapter.setOnItemClickListener(this);
                nineCutMaskListAdapter.notifyDataSetChanged();
                break;
        }

    }

    /**
     * 九图分割剪裁
     */

    File[] nineCilpFile;
    private void nineCilp(Bitmap bitmap) {
        List<ImagePiece> split = split(bitmap, 3, 3);
//        Log.e("", "bitmap总数：" + split.size());
        nineCilpFile = new File[split.size()];

        ImageUtil.saveNineCutFristAndEnd(this, ImageUtil.NineCutTag.FRIST);
        for (ImagePiece imagePiece : split) {
            File file = ImageUtil.saveImage(this, imagePiece.bitmap,imagePiece.index);
            nineCilpFile[imagePiece.index] = file;
        }
        ImageUtil.saveNineCutFristAndEnd(this, ImageUtil.NineCutTag.END);

        startResult(nineCilpFile);//跳转成功界面
    }


    public void startResult(File[] nineCilpFile){
        Intent intent = new Intent(NineCutToolActivity.this,NineCutToolResultActivity.class);
        Bundle bundle = new Bundle() ;
        SerializableData serializableData = new SerializableData();
        serializableData.setFiles(nineCilpFile);
        bundle.putSerializable("SerializableData", serializableData);
        intent.putExtras(bundle);
        startActivity(intent) ;
    }



    /**
     * 图片切割方法
     * @param bitmap 图片
     * @param xPiece 行
     * @param yPiece 列
     * @return
     */
    public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {
        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / 3;
        int pieceHeight = height / 3;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                ImagePiece piece = new ImagePiece();
                piece.index = j + i * xPiece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                pieces.add(piece);
            }
        }
        return pieces;
    }



    @Override
    public void onItemClick(View view, int position) {
        if (listTag == ListTag.MASK){
            nineCutView.setMaskPosition(position);
        }else if (listTag == ListTag.FITER){
            setFiterPosition(position);
        }
    }


    /**
     * GPUImage开源项目
     * 设置图片的滤镜
     * @param position
     */
    public void setFiterPosition(int position) {
        List<FilterEffect> filters = DataHandler.filters;
        GPUImage mGPUImage = new GPUImage(this);
        mGPUImage.setImage(bitmap); // this loads image on the current thread, should be run in a thread
        GPUImageFilter filter = GPUImageFilterTools.createFilterForType(
                this, filters.get(position).getType());
        mGPUImage.setFilter(filter);
        bitmapWithFilterApplied = mGPUImage.getBitmapWithFilterApplied();
        nineCutView.setImageBitmap(bitmapWithFilterApplied);
    }


}
