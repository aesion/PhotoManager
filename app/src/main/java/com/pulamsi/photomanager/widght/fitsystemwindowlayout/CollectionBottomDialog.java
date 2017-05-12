package com.pulamsi.photomanager.widght.fitsystemwindowlayout;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.adapter.DialogCollectionAdapter;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.CollectionTitle;
import com.pulamsi.photomanager.helper.retrofit.RetrofitApi;
import com.pulamsi.photomanager.view.CreateFolderActivity;

import org.ayo.view.status.StatusUIManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import me.shaohui.bottomdialog.BaseBottomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionBottomDialog extends BaseBottomDialog implements View.OnClickListener {

    View contentView;
    StatusUIManager statusUIManager;
    LRecyclerView lRecyclerView;
    TextView diss;
    TextView create;

    DialogCollectionAdapter mInfoCollectionAdapter;
    private static int REQUESTCODE = 1;
    private static int RESULTCODE = 7;


    @Override
    public int getLayoutRes() {
        return R.layout.collection_dialog_layout;
    }

    @Override
    public void bindView(View v) {
        // do any thing you want
        contentView = v.findViewById(R.id.view_content);
        lRecyclerView = (LRecyclerView) v.findViewById(R.id.list);
        diss = (TextView) v.findViewById(R.id.tv_diss);
        create = (TextView) v.findViewById(R.id.tv_create_folder);
        diss.setOnClickListener(this);
        create.setOnClickListener(this);
        statusUIManager = new StatusUIManager(getActivity(), contentView);

        requestDate();//请求数据
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //添加分割线
        lRecyclerView.setHasFixedSize(false);
        lRecyclerView.setPullRefreshEnabled(false);
        mInfoCollectionAdapter = new DialogCollectionAdapter(this.getActivity());
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mInfoCollectionAdapter);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        lRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                requestAddCollection(mInfoCollectionAdapter.getAllData().get(i).getId());//添加至收藏夹
                dismiss();
            }

        });
    }


    /**
     * 添加至收藏夹
     */
    private void requestAddCollection(String ptid) {
        if (onClickAddCollectionListener != null)
            onClickAddCollectionListener.OnClickAddCollection(ptid);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    /**
     * 获取收藏夹列表
     */
    public void requestDate() {
        statusUIManager.showLoading();

        HashMap<String, String> map = new HashMap<>();
        map.put("mid", Constants.MID);

        RetrofitApi.init().requestCollection(map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("", response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    List<CollectionTitle> collectionTitleList = new Gson().fromJson(jsonObject.get("list").toString(), new TypeToken<List<CollectionTitle>>() {
                    }.getType());
                    if (collectionTitleList.size() == 0) {
                        statusUIManager.showEmpty();
                    } else {
                        updateCollectionData(collectionTitleList);
                        statusUIManager.showContentView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("", "收藏夹列表包装出错");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                statusUIManager.showError();
            }
        });
    }

    private void updateCollectionData(List<CollectionTitle> collectionTitleList) {
        mInfoCollectionAdapter.clear();
        mInfoCollectionAdapter.addAll(collectionTitleList);
        mInfoCollectionAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_diss:
                dismiss();
                break;
            case R.id.tv_create_folder:
                createFolder();//创建新收藏夹
                break;
        }
    }

    /**
     * 创建文件夹
     */
    private void createFolder() {
        Intent intent = new Intent(getActivity(), CreateFolderActivity.class);
        startActivityForResult(intent, REQUESTCODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == getActivity().RESULT_OK) {
            requestDate();
        }
    }


    OnClickAddCollectionListener onClickAddCollectionListener;

    public void setOnClickAddCollectionListener(OnClickAddCollectionListener onClickAddCollectionListener) {
        this.onClickAddCollectionListener = onClickAddCollectionListener;
    }


    public interface OnClickAddCollectionListener {
        void OnClickAddCollection(String ptid);
    }
}