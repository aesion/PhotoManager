package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.DownloadListAdapter;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.interfaces.IDownloadActivity;
import com.pulamsi.photomanager.prestener.DownloadActivityPrestener;

import org.ayo.view.status.StatusUIManager;

import java.io.File;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-22
 * Time: 13:58
 * FIXME
 */
public class DownloadActivity extends CommonBaseActivity implements IDownloadActivity {


    EasyRecyclerView dowList;
    DownloadActivityPrestener downloadActivityPrestener;
    DownloadListAdapter downloadListAdapter;
    StatusUIManager statusUIManager;


    @Override
    protected void init() {
        dowList = (EasyRecyclerView) findViewById(R.id.dow_list);
        statusUIManager = new StatusUIManager(context,dowList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//瀑布流解决了Item左右切换
        dowList.setLayoutManager(staggeredGridLayoutManager);
        downloadListAdapter = new DownloadListAdapter(context);
        dowList.setAdapter(downloadListAdapter);

        downloadActivityPrestener = new DownloadActivityPrestener(this);
        downloadActivityPrestener.requestDown();

        downloadListAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                //使用Intent,打开系统相册
//                http://www.jianshu.com/p/3f9e3fc38eae  Android7.0须知--应用间共享文件
                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(downloadListAdapter.getAllData().get(i))), "image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(context, "com.pulamsi.photomanager.fileprovider", new File(downloadListAdapter.getAllData().get(i)));
                    intent.setDataAndType(contentUri, "image/*");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(downloadListAdapter.getAllData().get(i))), "image/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.download_activity;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.fav_download;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading() {
        statusUIManager.showLoading();
    }

    @Override
    public void showEmpty() {
        statusUIManager.showEmpty();
    }

    @Override
    public void showError() {
        statusUIManager.showError();
    }

    @Override
    public void showContent() {
        statusUIManager.showContentView();
    }

    @Override
    public void imagesBack(List<String> imags) {
        if (imags == null || imags.size() == 0){
            showEmpty();
            return;
        }
        downloadListAdapter.addAll(imags);
        downloadListAdapter.notifyDataSetChanged();
    }
}
