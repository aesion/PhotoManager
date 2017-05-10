package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.library.StatusBarUtil;
import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.BaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.base.MyApplication;
import com.pulamsi.photomanager.bean.GalleryclassDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.utils.JudgeUtils;
import com.pulamsi.photomanager.utils.ThemeUtils;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    /**
     * 栏目管理需要的常量
     */
    public final static int COLUMN_MANAGE_REQUEST = 1;
    public final static int COLUMN_MANAGE_RESULT = 2;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.dl_main_drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nv_main_navigation)
    NavigationView navigationView;

    ImageView headerImg,message;
    TextView name;
    TextView login;
    TextView members;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.iv_add_classify)
    ImageView addClassify;

    HomePhotoFragment homeFragment;


//    @BindView(R.id.iv_img)
//    ImageView heardImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    public void setStatusBar() {
//        super.setStatusBar();//透明状态栏
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, mDrawerLayout, ThemeUtils.getThemeColorRes(this));//透明状态栏
    }

    private void init() {
        Log.e("","main只实例化一次");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.abc_action_bar_home_description,
                R.string.abc_action_bar_home_description);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent(navigationView);

        disableNavigationViewScrollbars(navigationView);//隐藏滚动条
        initNavigationView();

        homeFragment = new HomePhotoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
    }

    private void initNavigationView() {
        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_bg = (LinearLayout) headerView.findViewById(R.id.rl_header);
        headerImg = (ImageView) headerView.findViewById(R.id.nav_header_img);

        name = (TextView) headerView.findViewById(R.id.tv_name);
        login = (TextView) headerView.findViewById(R.id.tv_login);
        members = (TextView) headerView.findViewById(R.id.tv_members);
        message = (ImageView) headerView.findViewById(R.id.iv_message);
        ThemeUtils.setThemeImage(getBaseContext(), nav_bg);
        headerImg.setOnClickListener(this);
        login.setOnClickListener(this);
        message.setOnClickListener(this);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                readyGo(SearchActivity.class);
                break;
            case R.id.action_settings:
                readyGo(SettingActivity.class);
                break;
            case R.id.action_about:
                readyGo(AboutActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_img);//默认选择
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    private MenuItem mPreMenuItem;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (mPreMenuItem != null)
                            mPreMenuItem.setChecked(false);
                        mDrawerLayout.closeDrawers();
                        mPreMenuItem = menuItem;
                        menuItem.setChecked(true);
                        switchClick(menuItem);
                        return true;
                    }
                });
    }

    private void switchClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_img://图片
                getSupportActionBar().setTitle(R.string.app_name);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
                break;
            case R.id.nav_voice://语音
                getSupportActionBar().setTitle("语音");
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
                break;
            case R.id.nav_theme:
                readyGo(ThemeActicity.class);
                break;
            case R.id.nav_about:
                readyGo(AboutActivity.class);
                break;
            case R.id.nav_fav:
                if (JudgeUtils.isLogin(MainActivity.this)) {
                    readyGo(FavoriteActivity.class);
                }
                break;
            case R.id.nav_dow:
                readyGo(DownloadActivity.class);
                break;
            case R.id.nav_upload:
                if (JudgeUtils.isLogin(MainActivity.this)) {
                    readyGo(UploadActivity.class);
                }
                break;
            case R.id.nav_setting:
                readyGo(SettingActivity.class);
                break;

        }
    }


    @OnClick(R.id.fab)
    public void onFab() {
        AppBus.getInstance().post(Flag.BACK_TOTOP);
    }

    @OnClick(R.id.iv_add_classify)
    public void onAddClassify() {
        //跳转至栏目管理
        startActivityForResult(new Intent(MainActivity.this, ClassificationManagementActivity.class), COLUMN_MANAGE_REQUEST);
    }

    /**
     * 隐藏滚动条
     *
     * @param navigationView
     */
    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_img:
                if (Constants.IS_LOGIN) {
                    readyGo(InfoActivity.class);
                } else {
                    readyGo(LoginActivity.class);
                }
                break;
            case R.id.tv_login:
                readyGo(LoginActivity.class);
                break;
            case R.id.iv_message:
                Map<String, Boolean> map = new HashMap<>();
                map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
                map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示
                RongIM.getInstance().startConversationList(this, map);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 头像登录的判断
         */
        if (Constants.IS_LOGIN) {
            Glide.with(MainActivity.this)//更改图片加载框架
                    .load(Constants.IMG_URL.contains("http://") ? Constants.IMG_URL : MyApplication.context.getString(R.string.imgbaseurl) + Constants.IMG_URL)
                    .centerCrop()
//                    .placeholder(R.mipmap.ic_login_avatar) 设置了之后图片缓存不会立即出来
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(headerImg);

            name.setVisibility(View.VISIBLE);
            members.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            name.setText(Constants.NAME);
//           USER_TYPE 0默认 1代理商 2经销商
            if (Constants.USER_TYPE == 0) {
                members.setText("普通会员");
            } else if (Constants.USER_TYPE == 1) {
                members.setText("代理商");
            } else if (Constants.USER_TYPE == 2) {
                members.setText("经销商");
            } else if (Constants.USER_TYPE == 3){
                members.setText("管理员");
            }
        } else {
            name.setVisibility(View.GONE);
            members.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            headerImg.setImageResource(R.mipmap.ic_login_avatar);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        AppBus.getInstance().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COLUMN_MANAGE_REQUEST:
                if (resultCode == COLUMN_MANAGE_RESULT) {
                    homeFragment.setChangeView(false);
                } else if (resultCode == RESULT_OK) {
                    int selectId = data.getIntExtra("selectId", 0);
                    homeFragment.setChangeView(false);
                    homeFragment.selectTab(selectId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Subscribe   //登录成功刷新类列表
    public void SubscribeRefreshClassification(String flag) {
        if (flag.equals(Flag.REFRESH_CLASSIFICATION)) {
            Log.e("", "登录成功刷新分类列表");
            //登录成功后要刷新列表，所以得删除分类的缓存
            GalleryclassDao galleryclassDao = GreenDaoHelper.getDaoSession().getGalleryclassDao();
            galleryclassDao.deleteAll();//删除以前的
            homeFragment.setChangeView(true);
        }
    }


}
