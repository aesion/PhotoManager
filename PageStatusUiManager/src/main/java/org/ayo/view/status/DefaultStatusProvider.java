package org.ayo.view.status;

import android.content.Context;
import android.view.View;

/**
 */
public class DefaultStatusProvider {

    public static int LOADING_VIEW;
    public static int EMPTY_VIEW;
    public static int ERROR_VIEW;

    public static class DefaultLoadingStatusView extends StatusProvider {

        public DefaultLoadingStatusView(Context context, String status, View contentView) {
            super(context, status, contentView);
        }

        @Override
        public View getStatusView() {
            if (LOADING_VIEW != 0) {
                try {
                    return View.inflate(mContext, LOADING_VIEW, null);
                } catch (Exception e) {
                    throw new RuntimeException("LOADING_VIEW 布局出错，请检查您的布局文件");
                }
            } else
                return View.inflate(mContext, R.layout.su_view_loading, null);
        }
    }

    public static class DefaultEmptyStatusView extends StatusProvider {

        public DefaultEmptyStatusView(Context context, String status, View contentView) {
            super(context, status, contentView);
        }

        @Override
        public View getStatusView() {
            if (EMPTY_VIEW != 0) {
                try {
                    return View.inflate(mContext, EMPTY_VIEW, null);
                } catch (Exception e) {
                    throw new RuntimeException("EMPTY_VIEW 布局出错，请检查您的布局文件");
                }
            } else
                return View.inflate(mContext, R.layout.su_view_empty, null);
        }

    }

    public static class DefaultErrorStatusView extends StatusProvider {
        public OnRetryClickListener onRetryClickListener;
        public View view;

        public DefaultErrorStatusView(Context context, String status, View contentView, OnRetryClickListener onRetryClickListener) {
            super(context, status, contentView);
            this.onRetryClickListener = onRetryClickListener;
        }

        @Override
        public View getStatusView() {
            if (ERROR_VIEW != 0) {
                try {
                    view = View.inflate(mContext, ERROR_VIEW, null);
                } catch (Exception e) {
                    throw new RuntimeException("ERROR_VIEW 布局出错，请检查您的布局文件");
                }
            }else {
                view = View.inflate(mContext, R.layout.su_view_error_local, null);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRetryClickListener != null)
                        onRetryClickListener.onClick();
                }
            });
            return view;
        }

        public interface OnRetryClickListener {
            void onClick();
        }
    }
}
