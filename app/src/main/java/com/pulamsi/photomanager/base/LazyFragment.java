package com.pulamsi.photomanager.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Fragment 懒加载
 */
public abstract class LazyFragment extends Fragment {

    /**
     * Fragment是否可见
     */
    protected boolean isVisible;

    private boolean isPrepared;
    /**
     * 是否为第一次可见
     */
    private boolean isFirstVisible = true;
    /** 
     * 在这里实现Fragment数据的缓加载. 
     * @param isVisibleToUser 
     */  
    @Override  
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);  
        if(getUserVisibleHint()) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            }
            isVisible = true;
            onVisible();  
        } else {  
            isVisible = false;  
            onInvisible();  
        }  
    }  
  
    protected void onVisible(){  
        lazyLoad();  
    }

    /**
     * 懒加载，延迟加载。Fragment可见时调用
     */
    protected abstract void lazyLoad();


    /**
     *Fragment不可见时调用
     */
    protected void onInvisible(){}



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }


    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 懒加载，延迟加载。Fragment可见时调用
     * 只加载第一次
     */
    protected abstract void onFirstUserVisible();


}