package com.pulamsi.photomanager.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.GalleryclassDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.interfaces.IClassificationManagementActivity;
import com.pulamsi.photomanager.model.GridRecyclerViewAdapter;
import com.pulamsi.photomanager.model.RecyclerItemTouchHelperCallback;
import com.pulamsi.photomanager.prestener.ClassificationManagementActivityPrestener;
import com.pulamsi.photomanager.utils.OtherUtils;

import org.ayo.view.status.DefaultStatusProvider;
import org.ayo.view.status.StatusUIManager;

import java.util.List;

import butterknife.BindView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-11
 * Time: 17:55
 * FIXME
 */
public class ClassificationManagementActivity extends BaseActivity implements IClassificationManagementActivity, DefaultStatusProvider.DefaultErrorStatusView.OnRetryClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    StatusUIManager statusUIManager;

    GridRecyclerViewAdapter adapter;

    ClassificationManagementActivityPrestener classificationManagementActivityPrestener;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_classification_management;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setStatusBar() {
//        super.setStatusBar();
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);//透明状态栏
    }

    @Override
    public void initView() {
        statusUIManager = new StatusUIManager(context, recyclerView);
        classificationManagementActivityPrestener = new ClassificationManagementActivityPrestener(this);
        classificationManagementActivityPrestener.requestData();
        statusUIManager.setOnRetryClickListener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading() {
        statusUIManager.showLoading();
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        statusUIManager.showEmpty();
    }

    @Override
    public void showError() {
        statusUIManager.showError();
    }

    @Override
    public void showContent() {
        statusUIManager.showContentView();
        recyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * 重试
     */
    @Override
    public void onClick() {
        classificationManagementActivityPrestener.requestData();
    }


    /**
     * 接收返回来的数据
     *
     * @param body
     */
    @Override
    public void dataBack(final List<Galleryclass> body) {
        GalleryclassDao galleryclassDao = GreenDaoHelper.getDaoSession().getGalleryclassDao();
        List<Galleryclass> myChannels = galleryclassDao.loadAll();


        Galleryclass otherEntity = new Galleryclass();
        otherEntity.setId("000");
        otherEntity.setCatName("最新动态");
        myChannels.add(0, otherEntity);
        body.add(0, otherEntity);


        List<Galleryclass> different = OtherUtils.getDifferent(myChannels, body);//取出差集


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter = new GridRecyclerViewAdapter(myChannels, different, itemTouchHelper);

        View view = View.inflate(this, R.layout.keep_head_tag, null);
        view.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到对应的page页
                saveColumn();
                Intent intent = getIntent();
                intent.putExtra("selectId", 0);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        adapter.setHeaderView(view);


        adapter.setOnItemClickListener(new GridRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到对应的page页
                saveColumn();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Intent intent = getIntent();
                intent.putExtra("selectId", position - 1);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int realPosition = adapter.getItemViewType(position);
                if (realPosition == GridRecyclerViewAdapter.MY_CHANNEL_NAME || realPosition == GridRecyclerViewAdapter.OTHER_CHANNEL_NAME) {
                    return 4;
                } else {
                    return 1;
                }

            }
        });
        recyclerView.setLayoutManager(manager);

    }


    @Override
    public void onBackPressed() {
        saveColumn();
        Intent intent = getIntent();
        intent.putExtra("selectId", 0);
        setResult(MainActivity.COLUMN_MANAGE_RESULT, intent);
        finish();
        super.onBackPressed();
    }


    /**
     * 保存分类到本地
     */
    private void saveColumn() {
        if (adapter == null)
            return;
        List<Galleryclass> myChannels = adapter.getMyChannels();
        if (myChannels == null || myChannels.size() == 0)
            return;
        //保存分类缓存
        GalleryclassDao galleryclassDao = GreenDaoHelper.getDaoSession().getGalleryclassDao();
        galleryclassDao.deleteAll();//插入之前删除以前的
        myChannels.remove(0);//移除热门分类
        galleryclassDao.insertInTx(myChannels);//插入所有的

        if (Constants.IS_LOGIN) {
            saveTheServer(myChannels);//保存至服务器
        }
    }

    /**
     * 保存到服务器
     *
     * @param myChannels
     */
    private void saveTheServer(List<Galleryclass> myChannels) {
        StringBuffer stringBuffer = new StringBuffer();

        for (Galleryclass myChannel : myChannels) {
            stringBuffer.append(myChannel.getId() + ",");
        }
        classificationManagementActivityPrestener.saveTheServer(stringBuffer);
    }

}
