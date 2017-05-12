package org.ayo.view.status;

import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class StatusUIManager {

    private Context context;
    private View contentView;

    public StatusUIManager(Context context, View contentView) {
        this.context = context;
        this.contentView = contentView;
    }

    private Map<String, StatusProvider> map = new HashMap<>();
    private StatusProvider currentStatusProvider;


    public void addStatusProvider(StatusProvider p) {
        map.put(p.getStatus(), p);
    }

    private void show(String status) {
        if (currentStatusProvider != null) currentStatusProvider.hideStatusView();
        StatusProvider p = map.get(status);
        if (p != null) {
            p.showStatusView();
            currentStatusProvider = p;
        }
    }


    /**
     * 显示内容布局
      */
    public void showContentView() {
        if (currentStatusProvider != null) {
            currentStatusProvider.hideStatusView();
            currentStatusProvider.showContentView();
        }
    }

    /**
     * 显示正在加载布局
     */
    public void showLoading() {
        this.addStatusProvider(
                new DefaultStatusProvider.DefaultLoadingStatusView(
                        context,
                        DefaultStatus.STATUS_LOADING,
                        contentView
                        ));
        show(DefaultStatus.STATUS_LOADING);
    }

    /**
     * 显示空布局
     */
    public void showEmpty() {
        this.addStatusProvider(
                new DefaultStatusProvider.DefaultEmptyStatusView(
                        context,
                        DefaultStatus.STATUS_EMPTY,
                        contentView
                        ));
        show(DefaultStatus.STATUS_EMPTY);
    }

    /**
     * 显示错误布局
     */
    public void showError() {
        this.addStatusProvider(new DefaultStatusProvider.DefaultErrorStatusView(
                context,
                DefaultStatus.STATUS_lOCAL_ERROR,
                contentView,
                onRetryClickListener));
        show(DefaultStatus.STATUS_lOCAL_ERROR);
    }


    public DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener onRetryClickListener;
    public void  setOnRetryClickListener(DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener onRetryClickListener){
        this.onRetryClickListener = onRetryClickListener;

    }

}
