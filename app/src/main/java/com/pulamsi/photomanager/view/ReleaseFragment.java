package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.BaseFragment;
import com.pulamsi.photomanager.bean.MineCount;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-16
 * Time: 14:17
 * 发布标题列表
 * FIXME
 */
public class ReleaseFragment extends BaseFragment {

    @BindView(R.id.ll_public_release)
    LinearLayout publicRelease;

    @BindView(R.id.ll_private_release)
    LinearLayout privateRelease;

    @BindView(R.id.tv_private_number)
    TextView privateNumber;

    @BindView(R.id.tv_public_number)
    TextView publicNumber;

    @BindView(R.id.tv_private_subhead)
    TextView privateSubhead;

    @BindView(R.id.tv_public_subhead)
    TextView publicSubhead;

    MineCount mineCount;


//    public ReleaseFragment(MineCount mineCount) {
//        this.mineCount = mineCount;
//    }
//    This fragment should provide a default constructor (a public constructor with no arguments) (com.example.TestFragment)
//    Avoid non-default constructors in fragments: use a default constructor plus Fragment#setArguments(Bundle) instead
//    在增加一个空参数的构造函数可以消去第一个错误，但是第二个却不能，第二个错误说要使用默认构造函数外加setArguments(Bundle)来代替，去Android的官网上查看Fragment的例子都是下面这个样子的

    /**
     * Create a new instance of MyFragment that will be initialized
     * with the given arguments.
     */
    static ReleaseFragment newInstance(MineCount mineCount) {
        ReleaseFragment releaseFragment = new ReleaseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mineCount",mineCount);
        releaseFragment.setArguments(bundle);
        return releaseFragment;
    }




    @Override
    protected void lazyInitAndEvent() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.info_release_fragment;
    }

    @Override
    protected void onFirstUserVisible() {
        mineCount = (MineCount) getArguments().getSerializable("mineCount");

        Log.e("", mineCount.toString());
        publicNumber.setText(mineCount.getOpenCouunt() + "");
        privateNumber.setText(mineCount.getPrivateCount() + "");
        privateSubhead.setText("私密 · " + mineCount.getPrivateCount() + "个内容");
        publicSubhead.setText("公开 · " + mineCount.getOpenCouunt() + "个内容");
    }

    @OnClick(R.id.ll_public_release)
    public void onPublicRelease() {
        Intent intent = new Intent(getActivity(), InfoReleaseActivity.class);
        intent.putExtra("status", "0");
        intent.putExtra("title", "公开素材");
        startActivity(intent);
    }

    @OnClick(R.id.ll_private_release)
    public void onPrivateRelease() {
        Intent intent = new Intent(getActivity(), InfoReleaseActivity.class);
        intent.putExtra("status", "2");
        intent.putExtra("title", "私密素材");
        startActivity(intent);
    }
}
