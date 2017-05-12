package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-04-05
 * Time: 14:53
 * 九图主要控件
 * 切图用
 * FIXME
 */
public class NineCutView extends ClipSquareImageView {

    public NineCutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

         /*
         * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
         * drawLine 绘制直线 drawPoin 绘制点
         */
        // 创建画笔
//        Paint p = new Paint();
//        p.setColor(Color.BLACK);
//        p.setStrokeWidth(10);// 画笔的粗细
//        canvas.drawLine(0, (width / 3), width, (width / 3), p);
//        canvas.drawLine(0, (width / 3) * 2, width, (width / 3) * 2, p);
//        canvas.drawLine((width / 3), 0, (width / 3), width, p);
//        canvas.drawLine((width / 3) * 2, 0, (width / 3)* 2, width, p);

    }

//        我们需要自己进行测量，即重写onMesure方法”：
//    重写之前先了解MeasureSpec的specMode,一共三种类型：
//    EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
//    AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
//    UNSPECIFIED：表示子布局想要多大就多大，很少使用
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width;
//        int height;
////        if (widthMode == MeasureSpec.EXACTLY) {
////            width = displayUtils.getWidth(getContext());
////        } else {
////            width = widthSize;
////        }
//
//        if (heightMode == MeasureSpec.EXACTLY) {
//            height = widthSize;
//        } else {
//            height = widthSize;
//        }
//
//        setMeasuredDimension(widthSize, height);
//    }
}
