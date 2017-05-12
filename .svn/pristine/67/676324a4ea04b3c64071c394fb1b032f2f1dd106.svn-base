package com.pulamsi.photomanager.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.bean.PasterLabel;
import com.pulamsi.photomanager.bean.SearchHistory;
import com.pulamsi.photomanager.bean.SearchHistoryDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.interfaces.ISearchActivity;
import com.pulamsi.photomanager.prestener.SearchActivityPrestener;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-24
 * Time: 14:05
 * FIXME
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, ISearchActivity {

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.hot_flowLayout)
    FlowLayout hotFlowLayout;

    @BindView(R.id.his_flowLayout)
    FlowLayout hisFlowLayout;

    @BindView(R.id.ll_history)
    LinearLayout lHistory;

    @BindView(R.id.delete)
    ImageView delete;

    SearchHistoryDao historyDao;


    @Override
    public void initView() {
        SearchActivityPrestener searchActivityPrestener = new SearchActivityPrestener(this);
        searchActivityPrestener.requestHotTag();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        historyDao = GreenDaoHelper.getDaoSession().getSearchHistoryDao();
        //设置我们的SearchView
        initSearchView();
        delete.setOnClickListener(this);

    }

    private void initSearchView() {
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
                readyGo(query);
                //插入之前先查询，如果有相同的就不在插入进去
                SearchHistory unique = historyDao.queryBuilder().where(SearchHistoryDao.Properties.SearchContent.eq(query)).unique();
                if (unique == null) {
                    historyDao.insert(new SearchHistory(null, query));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    /**
     * 跳转搜索
     * @param query
     */
    private void readyGo(String query) {
        Intent intent = new Intent(SearchActivity.this,SearchListActivity.class);
        intent.putExtra("search", query);
        readyGoAndIntent(intent);
    }

    /**
     * 历史搜索
     */
    private void initHistoryTag() {
        hisFlowLayout.cleanTag();

        List<SearchHistory> searchHistories = historyDao.loadAll();
        if (historyDao != null && searchHistories != null && searchHistories.size() != 0) {
            List<String> historyList = new ArrayList<>();
            for (SearchHistory searchHistory : searchHistories) {
                historyList.add(searchHistory.getSearchContent());
            }
            lHistory.setVisibility(View.VISIBLE);
            hisFlowLayout.setListData(historyList);
            hisFlowLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
                @Override
                public void TagClick(TextView tv, String text) {
                    readyGo(text);
                }
            });
        }
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHistoryTag();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确定要删除全部历史记录？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lHistory.setVisibility(View.GONE);
                        hisFlowLayout.cleanTag();
                        historyDao.deleteAll();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
        }
    }



    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showContent() {

    }


    /**
     * 设置热门标签
     * @param pasterLabelList
     */
    @Override
    public void HotTagBack(List<PasterLabel> pasterLabelList) {
        List<String> list = new ArrayList();
        for (PasterLabel pasterLabel : pasterLabelList) {
            list.add(pasterLabel.getLabelName());
        }
        hotFlowLayout.setColorful(true);
        hotFlowLayout.setListData(list);
        hotFlowLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
            @Override
            public void TagClick(TextView tv, String text) {
                readyGo("#"+text+"#");//标签必须加上
            }
        });
    }
}
