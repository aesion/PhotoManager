package com.pulamsi.photomanager.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-12-14
 * Time: 10:14
 * FIXME
 */
public abstract class BaseFragment extends LazyFragment {

    public Context context;

    /**
     * 标志位，标志已经初始化完成。
     */
    private boolean isPrepared;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            view = inflater.inflate(getContentViewLayoutID(), null);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);//ButterKnife在FragMent初始化
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        //延迟加载的数据在这里写
        lazyInitAndEvent();
    }


    /**
     * 延迟加载
     */
    protected abstract void lazyInitAndEvent();


    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


}
