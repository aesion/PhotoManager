package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.bean.Galleryclass;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局的RadioGroup
 */
public class FlowRadioGroup extends RadioGroup {

    private LayoutInflater mInflater;

    public FlowRadioGroup(Context context) {
        this(context, null);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrapContent
        int width = 0;
        int height = 0;

        // 记录每一行的宽和高
        int lineWidth = 0;
        int lineHeight = 0;

        // 得到内部元素的个数
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            // 子view的占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 子view占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            // 换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft()
                    - getPaddingRight()) {

                // 对比得到最大的宽度
                width = Math.max(width, lineWidth);
                // 重置lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                // 未换行
                // 叠加行宽
                lineWidth += childWidth;
                // 得到当前最大高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 最后一个控件
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
//        Log.i("test", "sizeWidth" + sizeWidth);
//        Log.i("test", "sizeHeight" + sizeHeight);

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth
                        : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
                        + getPaddingTop() + getPaddingBottom());

//        setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(10));
    }

    // 储存所有的View
    private ArrayList<ArrayList<View>> mAllViews = new ArrayList<>();
    // 储存每一行的高度
    private ArrayList<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

        // 清除一下list集合
        mAllViews.clear();
        mLineHeight.clear();

        // 得到viewGroup当前宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        ArrayList<View> lineViews = new ArrayList<>();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width
                    - getPaddingLeft() - getPaddingRight()) {
                // 记录当前行高
                mLineHeight.add(lineHeight);
                // 记录当前行的view
                mAllViews.add(lineViews);

                // 重置行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                // 重置lineViews集合
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }
        // 处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        // 设置子view的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        // 有多少行
        int lineNum = mLineHeight.size();

        for (int i = 0; i < lineNum; i++) {

            // 获取当前行的view
            lineViews = mAllViews.get(i);
            // 当前行高
            lineHeight = mLineHeight.get(i);
            int lineViewSize = lineViews.size();
            for (int j = 0; j < lineViewSize; j++) {
                View child = lineViews.get(j);
                // 判断子view的状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + childWidth;
                int bc = tc + childHeight;

                // 为子view布局
                child.layout(lc, tc, rc, bc);

                // 同一行view坐起点坐标的变换
                left += childWidth + lp.leftMargin + lp.rightMargin;
            }
            // 换行时将left重置
            left = getPaddingLeft();
            // top要加上上一行的行高
            top += lineHeight;
        }
    }


    /**
     * 设置数据
     * @param list
     */
    public void setListData(List<Galleryclass> list) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            final RadioButton button = (RadioButton) mInflater.inflate(R.layout.activity_month_radiobutton, this,
                    false);
            button.setText(list.get(i).getCatName());
            button.setTag(list.get(i).getId());
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRadioClickListener != null) {
                        onRadioClickListener.RadioClick(button,button.isChecked(),button.getText().toString());
                    }
                }
            });
            this.addView(button);
        }
    }


    private OnRadioClickListener onRadioClickListener;

    public void setOnRadioClickListener(OnRadioClickListener onRadioClickListener) {
        this.onRadioClickListener = onRadioClickListener;
    }


    public interface OnRadioClickListener {
        void RadioClick(RadioButton button, boolean checked, String text);
    }
}