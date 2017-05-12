package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.LineCollectionHomeListAdapter;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.CollectionTitle;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.interfaces.ICollectionDetailsActivity;
import com.pulamsi.photomanager.prestener.CollectionDetailsActivityPrestener;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-22
 * Time: 13:58
 * 收藏夹帖子列表
 * FIXME
 */
public class CollectionDetailsActivity extends CommonBaseActivity implements OnRefreshListener,OnLoadMoreListener, ICollectionDetailsActivity,DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener {

    private static final int REQUEST_COUNT = 10;
    private int i = 1;

    private CollectionDetailsActivityPrestener collectionDetailsActivityPrestener;
    private StatusUIManager statusUIManager;
    private LRecyclerView lRecyclerView;
    Map<String, String> requestParameter;
    LineCollectionHomeListAdapter mHomeListAdapter;
    LRecyclerViewAdapter mLRecyclerViewAdapter;


    @Override
    protected void init() {
        CollectionTitle collectionTitle = (CollectionTitle) getIntent().getSerializableExtra("collectionTitle");
        toolbar.setTitle(collectionTitle.getPreTypeName());

        lRecyclerView = (LRecyclerView) findViewById(R.id.list);

        collectionDetailsActivityPrestener = new CollectionDetailsActivityPrestener(this);
        statusUIManager = new StatusUIManager(context, findViewById(R.id.view_content));
        statusUIManager.setOnRetryClickListener(this);

        requestParameter = new HashMap<>();
        requestParameter.put("ptid", collectionTitle.getId());
        requestParameter.put("mid", Constants.MID);
        requestParameter.put("pageNumber", 1 + "");
        requestParameter.put("pageSize", REQUEST_COUNT + "");
        collectionDetailsActivityPrestener.requestData(requestParameter);
        showLoading();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        lRecyclerView.setLayoutManager(layoutManager);
        mHomeListAdapter = new LineCollectionHomeListAdapter(this);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHomeListAdapter);
        lRecyclerView.setAdapter(mLRecyclerViewAdapter);

        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);

        lRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress); //设置下拉刷新Progress的样式
        lRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头

        mHomeListAdapter.setOnDelListener(new LineCollectionHomeListAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
//                MyApplication.toastor.showToast("移动收藏夹");
            }

            @Override
            public void onItemClick(int position) {
                List<Gallery> dataList = mHomeListAdapter.getAllData();
                Intent intent = new Intent(CollectionDetailsActivity.this, ImageDetailsActivity.class);
                intent.putExtra("pid", dataList.get(position - 1).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.collection_details_activity;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.collection_details_title;
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


    //重新加载
    @Override
    public void onClick() {
        collectionDetailsActivityPrestener.requestData(requestParameter);
        showLoading();
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mHomeListAdapter.clear();
        mLRecyclerViewAdapter.notifyDataSetChanged();

        i = 1;//重置为1
        requestParameter.put("pageNumber", i + "");
        collectionDetailsActivityPrestener.requestData(requestParameter);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        requestParameter.put("pageNumber", ++i + "");
        collectionDetailsActivityPrestener.requestLoadData(requestParameter);
    }

    @Override
    public void loadingListData(List<Gallery> galleryList) {
        if (galleryList.size() < REQUEST_COUNT) {
            lRecyclerView.setNoMore(true);
        }

        mHomeListAdapter.addAll(galleryList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
        lRecyclerView.refreshComplete(REQUEST_COUNT);//刷新或者加载更多成功都得调用
    }

    @Override
    public void updateListData(List<Gallery> galleryList) {
        mHomeListAdapter.addAll(galleryList);
        mLRecyclerViewAdapter.notifyDataSetChanged();

        lRecyclerView.refreshComplete(REQUEST_COUNT);//刷新或者加载更多成功都得调用

        //要放在refreshComplete后面，不然refreshComplete后会重置setNoMore的状态
        if (galleryList.size() < REQUEST_COUNT) {
            lRecyclerView.setNoMore(true);
        }
    }

}
