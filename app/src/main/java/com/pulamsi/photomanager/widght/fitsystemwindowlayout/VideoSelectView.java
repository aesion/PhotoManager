package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.pulamsi.photomanager.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-04-22
 * Time: 10:50
 * FIXME
 */
public class VideoSelectView extends FrameLayout {

    public static final int REQUEST_VIDEO_CODE = 10;
    Activity activity;
    VideoView videoView;
    RelativeLayout videoGroup;
    ImageView back;


    public VideoSelectView(Context context) {
        super(context);
        init();
    }

    public VideoSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Context context = this.getContext();

        if(context instanceof Activity)
        {
            activity = (Activity)context;
        }


        View view = View.inflate(getContext(), R.layout.video_select_view, this);
        ImageView add = (ImageView) view.findViewById(R.id.iv_add_video);
        videoGroup = (RelativeLayout) view.findViewById(R.id.rl_video_group);
        videoView = (VideoView) findViewById(R.id.videoview);
        back = (ImageView) findViewById(R.id.iv_back);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                videoGroup.setVisibility(GONE);
                if (onDeleteVideoListener != null)
                    onDeleteVideoListener.onDeleteVideo();
            }
        });

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent, REQUEST_VIDEO_CODE);
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onActivityResult(Intent data) {
        videoGroup.setVisibility(VISIBLE);

        Uri uri = data.getData();
        videoView.setVideoURI(uri);
        //设置视频控制器
//        videoView.setMediaController(new MediaController(activity));
        //开始播放视频
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
    }


    public interface OnDeleteVideoListener{
        void onDeleteVideo();
    }

    OnDeleteVideoListener onDeleteVideoListener;
    public void setonDeleteVideoListener(OnDeleteVideoListener onDeleteVideoListener){
        this.onDeleteVideoListener = onDeleteVideoListener;
    }
}
