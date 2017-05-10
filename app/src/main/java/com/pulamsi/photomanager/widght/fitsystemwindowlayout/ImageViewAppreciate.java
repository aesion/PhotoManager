package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-05
 * Time: 10:42
 * FIXME
 */
public class ImageViewAppreciate extends ImageView {

    private boolean isChenked;

    public ImageViewAppreciate(Context context) {
        super(context);
    }

    public ImageViewAppreciate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewAppreciate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageViewAppreciate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setIsChenked(boolean isChenked){
        this.isChenked = isChenked;
        if (isChenked){
            this.setImageResource(R.mipmap.ic_comment_appreciate_fill);
        }else {
            this.setImageResource(R.mipmap.ic_comment_appreciate);
        }
    }

    public void toggle(){
            if (isChenked){
                isChenked = false;
                this.setImageResource(R.mipmap.ic_comment_appreciate);
            }else {
                isChenked = true;
                this.setImageResource(R.mipmap.ic_comment_appreciate_fill);
                jump();
            }
    }
    public boolean getIsChenked(){
        return isChenked;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN){
//            if (isChenked){
//                isChenked = false;
//                this.setImageResource(R.mipmap.ic_comment_appreciate);
//            }else {
//                isChenked = true;
//                this.setImageResource(R.mipmap.ic_comment_appreciate_fill);
//                jump();
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    private void jump() {
        ScaleAnimation down = new ScaleAnimation(0.8f, 1f, 0.8f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//位移动画，从button的上方300像素位置开始
        down.setFillAfter(true);
        down.setInterpolator(new BounceInterpolator());//弹跳动画,要其它效果的当然也可以设置为其它的值
        down.setDuration(500);//持续时间
        this.startAnimation(down);//设置按钮运行该动画效果
    }
}
