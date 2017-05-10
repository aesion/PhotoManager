package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-28
 * Time: 17:02
 * FIXME
 */
public class CommentAppreciateImageView extends ImageView {

    private boolean isChenked;


    public CommentAppreciateImageView(Context context) {
        super(context);
        this.setColorFilter(Color.GRAY);
    }

    public CommentAppreciateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setColorFilter(Color.GRAY);
    }

    public CommentAppreciateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setColorFilter(Color.GRAY);
    }


    public void setIsChenked(boolean isChenked){
        this.isChenked = isChenked;
        if (isChenked){
            this.setColorFilter(Color.GRAY);
        }else {
            this.setImageResource(R.mipmap.ic_comment_appreciate);
        }
    }

    public void toggle(){
        if (isChenked){
            isChenked = false;
            this.setColorFilter(Color.GRAY);
        }else {
            isChenked = true;
            this.setColorFilter(Color.RED);
            jump();
        }
    }


    public boolean getIsChenked(){
        return isChenked;
    }

    private void jump() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.comment_appreciate_anim);
        this.startAnimation(animation);
    }
}
