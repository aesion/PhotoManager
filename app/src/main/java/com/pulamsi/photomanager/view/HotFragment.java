package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.HomeListAdapter;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.BaseFragment;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.interfaces.IListFragment;
import com.pulamsi.photomanager.prestener.ListFragmentPrestener;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.BottomDialog;
import com.squareup.otto.Subscribe;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-12-05
 * Time: 14:00
 * FIXME
 */
public class HotFragment extends BaseFragment implements IListFragment,OnRefreshListener, OnLoadMoreListener, DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener, BottomDialog.IShareListener {

    @BindView(R.id.list)
    LRecyclerView lRecyclerView;

    @BindView(R.id.view_content)
    View contentView;


    String cid;
    ListFragmentPrestener listFragmentPrestener;
    HomeListAdapter mHomeListAdapter;
    StaggeredGridLayoutManager layoutManager;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    Map<String, String> requestParameter;
    static int checkNum;
    private static ArrayList<File> arrayList;


    /**
     * 每一页展示多少条数据
     */
    public static final int REQUEST_COUNT = 10;

    private int i = 1;

    private StatusUIManager statusUIManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void lazyInitAndEvent() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.list_content_fragment;
    }

    @Override
    public Context getContext() {
        return context;
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
    public void updateListData(List<Gallery> galleryList) {
        mHomeListAdapter.addAll(galleryList);
        mLRecyclerViewAdapter.notifyDataSetChanged();

        lRecyclerView.refreshComplete(REQUEST_COUNT);//刷新或者加载更多成功都得调用

        //要放在refreshComplete后面，不然refreshComplete后会重置setNoMore的状态
        if (galleryList.size() < REQUEST_COUNT) {
            lRecyclerView.setNoMore(true);
        }
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
    public void loadingError() {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), lRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), lRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestParameter.put("pageNumber", i + "");
                listFragmentPrestener.requestLoadData(requestParameter);
            }
        });
    }


    //下拉刷新回调
    @Override
    public void onRefresh() {
        mHomeListAdapter.clear();
        mLRecyclerViewAdapter.notifyDataSetChanged();

        i = 1;//重置为1
        requestParameter.put("pageNumber", i + "");
        listFragmentPrestener.requestData(requestParameter);
    }

    //加载更多回调
    @Override
    public void onLoadMore() {
        requestParameter.put("pageNumber", ++i + "");
        listFragmentPrestener.requestLoadData(requestParameter);
    }

    @Subscribe   //订阅事件DataChangedEvent
    public void SubscribeHideDialog(String flag) {
        if (flag.equals(Flag.BACK_TOTOP)) {
            backToTop();
        }
    }


    /**
     * 回顶操作
     */
    public void backToTop() {
        lRecyclerView.scrollToPosition(0);
    }


    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        //注销到bus事件
        AppBus.getInstance().unregister(this);
    }


    @Override
    public void share() {
//        if (checkNum == 0) {
//            MyApplication.toastor.showToast("请至少选择一张");
//            return;
//        }
//        SnackbarUtil.showLoading(lRecyclerView, "加载中...");
//        arrayList = new ArrayList<>();
//        arrayList.clear();
    }


    @Override
    protected void onFirstUserVisible() {
        statusUIManager = new StatusUIManager(context, contentView);
        statusUIManager.setOnRetryClickListener(this);

        requestParameter = new HashMap<>();
        requestParameter.put("pageNumber", 1 + "");
        requestParameter.put("pageSize", REQUEST_COUNT + "");
        requestParameter.put("recommended", "recommended");

        listFragmentPrestener = new ListFragmentPrestener(this);
        listFragmentPrestener.requestData(requestParameter);
        showLoading();//第一次Loading
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        layoutManager.setItemPrefetchEnabled(false);//解决25.1.1的滑动闪退问题

        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//瀑布流解决了Item左右切换
        lRecyclerView.setLayoutManager(layoutManager);
        mHomeListAdapter = new HomeListAdapter(this.getActivity());
        lRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHomeListAdapter);//notifyDataSetChanged
        lRecyclerView.setAdapter(mLRecyclerViewAdapter);

        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);

        lRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress); //设置下拉刷新Progress的样式
        lRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                List<Gallery> dataList = mHomeListAdapter.getAllData();
                Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                intent.putExtra("pid", dataList.get(i).getId());
                context.startActivity(intent);
            }

        });
    }

    /**
     * 错误重新加载
     */
    @Override
    public void onClick() {
        lRecyclerView.forceToRefresh();//主动刷新
        showLoading();
    }

}

