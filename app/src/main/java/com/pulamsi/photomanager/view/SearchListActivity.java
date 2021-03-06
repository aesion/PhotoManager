package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.HomeListAdapter;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.bean.Gallery;
import com.pulamsi.photomanager.interfaces.ISearchListActivity;
import com.pulamsi.photomanager.prestener.SearchListActivityPrestener;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-06
 * Time: 17:08
 * FIXME
 */
public class SearchListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, ISearchListActivity, DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener {

    @BindView(R.id.lrv_list)
    LRecyclerView searchList;


    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String search;

    private boolean isTag = false;//是否为标签搜索

    private int i = 1;

    StatusUIManager statusUIManager;
    HomeListAdapter mHomeListAdapter;
    StaggeredGridLayoutManager layoutManager;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    Map<String, String> map;
    SearchListActivityPrestener searchListActivityPrestener;


    @Override
    public void initView() {
        search = getIntent().getStringExtra("search");
        /**
         * 正则匹配##中的内容
         */
        String regex = "(?<=\\#).*(?=\\#)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(search);
        while (m.find()) {
            isTag = true;
            search = m.group();
        }

        statusUIManager = new StatusUIManager(context, searchList);
        statusUIManager.setOnRetryClickListener(this);

        searchListActivityPrestener = new SearchListActivityPrestener(this);
        map = new HashMap<>();
        map.put("pageNumber", 1 + "");
        map.put("pageSize", ListFragment.REQUEST_COUNT + "");
        map.put("name", search);
        searchListActivityPrestener.requestSearch(map, isTag);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置我们的SearchView
        initSearchView();

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        layoutManager.setItemPrefetchEnabled(false);//解决25.1.1的滑动闪退问题

        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//瀑布流解决了Item左右切换
        searchList.setLayoutManager(layoutManager);

        mHomeListAdapter = new HomeListAdapter(SearchListActivity.this);
        searchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHomeListAdapter);
        searchList.setAdapter(mLRecyclerViewAdapter);

        searchList.setOnRefreshListener(this);
        searchList.setOnLoadMoreListener(this);

        searchList.setPullRefreshEnabled(false);//禁止刷新

        searchList.setRefreshProgressStyle(ProgressStyle.SysProgress); //设置下拉刷新Progress的样式
        searchList.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                List<Gallery> dataList = mHomeListAdapter.getAllData();
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("pid", dataList.get(i).getId());
                context.startActivity(intent);
            }

        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_list;
    }


    private void initSearchView() {
        if (isTag) {
            searchView.setQueryHint("#" + search + "#");
        } else {
            searchView.setQueryHint(search);
        }

        searchView.setIconifiedByDefault(true);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        searchView.onActionViewExpanded();// 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
//        searchView.requestFocus();//输入焦点
        searchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
//        searchView.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        searchView.setIconified(false);//输入框内icon不显示
//        searchView.requestFocusFromTouch();//模拟焦点点击事件
        searchView.setFocusable(false);
        searchView.clearFocus();
//      mSearchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                map.put("pageNumber", 1 + "");
                map.put("name", query);
                searchListActivityPrestener.requestSearch(map, isTag);
                if (isTag) {
                    searchView.setQueryHint("#" + search + "#");
                } else {
                    searchView.setQueryHint(search);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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


    /**
     * 更新列表
     *
     * @param galleryList
     */
    @Override
    public void updateListData(List<Gallery> galleryList) {

        mHomeListAdapter.addAll(galleryList);
        mLRecyclerViewAdapter.notifyDataSetChanged();

        searchList.refreshComplete(ListFragment.REQUEST_COUNT);//刷新或者加载更多成功都得调用

        //要放在refreshComplete后面，不然refreshComplete后会重置setNoMore的状态
        if (galleryList.size() < ListFragment.REQUEST_COUNT) {
            searchList.setNoMore(true);
        }
    }

    @Override
    public void loadingListData(List<Gallery> galleryList) {
        if (galleryList.size() < ListFragment.REQUEST_COUNT) {
            searchList.setNoMore(true);
        }

        mHomeListAdapter.addAll(galleryList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
        searchList.refreshComplete(ListFragment.REQUEST_COUNT);//刷新或者加载更多成功都得调用
    }


    /**
     * 重新加载
     */
    @Override
    public void onClick() {
        searchList.forceToRefresh();//主动刷新
        showLoading();
    }

    //下拉刷新回调
    @Override
    public void onRefresh() {
        mHomeListAdapter.clear();
        mLRecyclerViewAdapter.notifyDataSetChanged();

        i = 1;//重置为1
        map.put("pageNumber", i + "");
        searchListActivityPrestener.requestSearch(map, isTag);
    }

    //加载更多回调
    @Override
    public void onLoadMore() {
        map.put("pageNumber", ++i + "");
        searchListActivityPrestener.requestLoadSearch(map, isTag);

    }

}
