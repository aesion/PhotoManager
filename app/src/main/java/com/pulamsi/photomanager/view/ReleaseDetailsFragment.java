package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.LineHomeListAdapter;
import com.pulamsi.photomanager.base.BaseFragment;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.interfaces.IReleaseDetailsFragment;
import com.pulamsi.photomanager.prestener.ReleaseDetailsFragmentPrestener;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-10
 * Time: 14:32
 * 我的发布审核界面，tab的Fragment
 * FIXME
 */
public class ReleaseDetailsFragment extends BaseFragment implements IReleaseDetailsFragment ,OnRefreshListener,OnLoadMoreListener,DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener{

    private static final int REQUEST_COUNT = 6;
    private int i = 1;

    private ReleaseDetailsFragmentPrestener releaseDetailsFragmentPrestener;
    private StatusUIManager statusUIManager;

    @BindView(R.id.list)
    LRecyclerView lRecyclerView;

    @BindView(R.id.view_content)
    View view;

    Map<String, String> requestParameter;
    LineHomeListAdapter mHomeListAdapter;

    LRecyclerViewAdapter mLRecyclerViewAdapter;

    int auditStatus;


    @Override
    protected void lazyInitAndEvent() {

    }


    public static  ReleaseDetailsFragment newInstance(String status,Integer auditStatus) {
        ReleaseDetailsFragment releaseDetailsFragment = new ReleaseDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        bundle.putInt("auditStatus", auditStatus);
        releaseDetailsFragment.setArguments(bundle);
        return releaseDetailsFragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.collection_details_activity;
    }

    @Override
    protected void onFirstUserVisible() {
        String status = getArguments().getString("status");
        auditStatus = getArguments().getInt("auditStatus");

        releaseDetailsFragmentPrestener = new ReleaseDetailsFragmentPrestener(this);

        statusUIManager = new StatusUIManager(context, view);
        statusUIManager.setOnRetryClickListener(this);

        requestParameter = new HashMap<>();
        requestParameter.put("mid", Constants.MID);
        requestParameter.put("pageNumber", 1 + "");
        requestParameter.put("pageSize", REQUEST_COUNT + "");
        requestParameter.put("status", status);
        releaseDetailsFragmentPrestener.requestData(requestParameter,auditStatus);
        showLoading();//第一次Loading


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        lRecyclerView.setLayoutManager(layoutManager);
        mHomeListAdapter = new LineHomeListAdapter(getActivity());
        mHomeListAdapter.setOnDelListener(new LineHomeListAdapter.onSwipeListener() {
            @Override
            public void onDel(final int pos) {
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .content("确定要删除这条素材？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .cancelable(false)
                        .onNegative(null)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                List<Gallery> dataList = mHomeListAdapter.getAllData();
                                releaseDetailsFragmentPrestener.deleteMaterial(dataList.get(pos - 1).getId());
                            }
                        })
                        .show();
            }

            @Override
            public void onItemClick(int position) {
                List<Gallery> dataList = mHomeListAdapter.getAllData();
                Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                intent.putExtra("pid", dataList.get(position - 1).getId());
                context.startActivity(intent);
            }
        });

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHomeListAdapter);
        lRecyclerView.setAdapter(mLRecyclerViewAdapter);

        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);

        lRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress); //设置下拉刷新Progress的样式
        lRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头

    }

    //重新加载
    @Override
    public void onClick() {
        releaseDetailsFragmentPrestener.requestData(requestParameter, auditStatus);
        showLoading();
    }


    @Override
    public void onLoadMore() {
        requestParameter.put("pageNumber", ++i + "");
        releaseDetailsFragmentPrestener.requestLoadData(requestParameter,auditStatus);
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


    /**
     * 删除成功回调
     */
    @Override
    public void deleteBack() {
        lRecyclerView.forceToRefresh();//主动刷新
        showLoading();
    }

    @Override
    public void onRefresh() {
        mHomeListAdapter.clear();
        mLRecyclerViewAdapter.notifyDataSetChanged();

        i = 1;//重置为1
        requestParameter.put("pageNumber", i + "");
        releaseDetailsFragmentPrestener.requestData(requestParameter, auditStatus);
    }

    @Override
    public Context getContext() {
        return getActivity();
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
}
