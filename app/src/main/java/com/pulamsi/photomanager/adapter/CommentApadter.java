package com.pulamsi.photomanager.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.BaseListAdapter;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.CommentsBean;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.utils.JudgeUtils;
import com.pulamsi.photomanager.viewHolder.CommentItemViewHolder;
import com.pulamsi.photomanager.widght.fitsystemwindowlayout.CommentAppreciateImageView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-05
 * Time: 14:04
 * FIXME
 */
public class CommentApadter extends BaseListAdapter<CommentsBean> {

    private View convertView;
    private Activity activity;
    private CommentsBean commentsBean;
    private boolean isrequestAppreciate = true;


    public CommentApadter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        convertView = mInflater.inflate(R.layout.comment_item, null);
        if (convertView == null) {
            return null;
        }

        return new CommentItemViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder == null || !(viewHolder instanceof CommentItemViewHolder) || !(getItem(position) instanceof CommentsBean)) {
            return;
        }

        final CommentItemViewHolder holder = (CommentItemViewHolder) viewHolder;
        commentsBean = getItem(position);

//        C=A>B ? 100 :200;
//        这条语句的意思是，如果A>B的话，就将100赋给C，否则就将200赋给C；
        Glide.with(activity)//更改图片加载框架
//                .load(commentsBean.getImage())//这里拿的是微信的url
//                .load(MyApplication.context.getString(R.string.serverbaseurl) + commentsBean.getImage())
                .load(commentsBean.getImage().contains("http://") ? commentsBean.getImage() : MyApplication.context.getString(R.string.imgbaseurl) + commentsBean.getImage())
                .centerCrop()
//                .fitCenter()
                .crossFade()
//                .placeholder(R.mipmap.ic_default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img);

        holder.name.setText(commentsBean.getName());
        holder.time.setText(commentsBean.getComment_date());
        holder.content.setText(commentsBean.getComment_content());
        if (commentsBean.isGoods()){
            holder.appreciate.setIsChenked(true);
            holder.number.setTextColor(Color.RED);
        }


         final int iNumber = Integer.parseInt(commentsBean.getGoodsCount());
        holder.number.setText(iNumber + "");
        holder.appreciate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JudgeUtils.isLogin(activity) && isrequestAppreciate) {
                    requestAppreciate();//请求点赞
                } else {
                    return;
                }
                if (!((CommentAppreciateImageView) view).getIsChenked()) {
                    holder.appreciateBg.setVisibility(View.VISIBLE);
                    holder.number.setTextColor(Color.RED);
                    holder.number.setText((iNumber + 1) + "");
                    ((CommentAppreciateImageView) view).toggle();
                } else {
//                    holder.appreciateBg.setVisibility(View.GONE);
//                    holder.number.setTextColor(Color.GRAY);
//                    holder.number.setText(iNumber + "");
                    MyApplication.toastor.showToast("您已经点过赞了");
                }

            }
        });
    }

    /**
     * 请求点赞
     */
    private void requestAppreciate() {
        isrequestAppreciate = false;
        Map<String, String> map = new HashMap<>();
        map.put("cid", commentsBean.getId());
        map.put("mid", Constants.MID);
        RetrofitApi.init().requestCommentAppreciate(map).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    if (response.body())
                        Log.e("", "点赞成功");
                }
                isrequestAppreciate = true;
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("", "评论点赞失败");
                isrequestAppreciate = true;
            }
        });
    }
}
