package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.InfoCollectionAdapter;
import com.pulamsi.photomanager.base.BaseFragment;
import com.pulamsi.photomanager.bean.CollectionTitle;
import com.pulamsi.photomanager.interfaces.ICollectionFragment;
import com.pulamsi.photomanager.prestener.CollectionFragmentPrestener;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.util.List;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-12-09
 * Time: 16:58
 * 收藏夹标题列表
 * FIXME
 */
public class CollectionFragment extends BaseFragment implements ICollectionFragment, DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener {

    StatusUIManager statusUIManager;

    @BindView(R.id.view_content)
    View contentView;

    @BindView(R.id.list)
    LRecyclerView lRecyclerView;

    CollectionFragmentPrestener collectionFragmentPrestener;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    InfoCollectionAdapter mInfoCollectionAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void lazyInitAndEvent() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.info_content_fragment;
    }


    private void initStatusUI() {
        statusUIManager = new StatusUIManager(context, contentView);
        statusUIManager.setOnRetryClickListener(this);
    }

    @Override
    protected void onFirstUserVisible() {
        initStatusUI();
        collectionFragmentPrestener = new CollectionFragmentPrestener(this);
        collectionFragmentPrestener.requestDate();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        lRecyclerView.setHasFixedSize(false);
        lRecyclerView.setPullRefreshEnabled(false);
        mInfoCollectionAdapter = new InfoCollectionAdapter(this.getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mInfoCollectionAdapter);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        lRecyclerView.setAdapter(mLRecyclerViewAdapter);


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(),CollectionDetailsActivity.class);
                intent.putExtra("collectionTitle",mInfoCollectionAdapter.getAllData().get(i));
                startActivity(intent);
            }

        });

    }

    @Override
    public void updateCollectionData(List<CollectionTitle> collectionTitleList) {
        mInfoCollectionAdapter.clear();
        mInfoCollectionAdapter.addAll(collectionTitleList);
        mInfoCollectionAdapter.notifyDataSetChanged();
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
    public void onClick() {
        collectionFragmentPrestener.requestDate();
    }
}
