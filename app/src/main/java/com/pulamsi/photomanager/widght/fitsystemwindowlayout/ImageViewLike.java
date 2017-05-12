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
 * Time: 10:23
 * FIXME
 */
public class ImageViewLike extends ImageView {

    private boolean isChenked;

    public ImageViewLike(Context context) {
        super(context);

    }

    public ImageViewLike(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ImageViewLike(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageViewLike(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);

    }


    public void setIsChenked(boolean isChenked) {
        this.isChenked = isChenked;
        if (isChenked) {
            this.setImageResource(R.mipmap.ic_comment_like_fill);
        } else {
            this.setImageResource(R.mipmap.ic_comment_like);
        }
    }

    public void toggle(){
        if (isChenked){
            isChenked = false;
            this.setImageResource(R.mipmap.ic_comment_like);
        }else {
            isChenked = true;
            this.setImageResource(R.mipmap.ic_comment_like_fill);
            jump();
        }
    }


    public boolean getIsChenked(){
        return isChenked;
    }

    private void jump() {
        ScaleAnimation down = new ScaleAnimation(0.8f, 1f, 0.8f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//位移动画，从button的上方300像素位置开始
        down.setFillAfter(true);
        down.setInterpolator(new BounceInterpolator());//弹跳动画,要其它效果的当然也可以设置为其它的值
        down.setDuration(500);//持续时间
        this.startAnimation(down);//设置按钮运行该动画效果
    }

}
