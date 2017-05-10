package com.pulamsi.photomanager.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.litesuits.common.assist.Toastor;
import com.pulamsi.photomanager.bean.Config;
import com.pulamsi.photomanager.bean.User;
import com.pulamsi.photomanager.bean.UserDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.helper.RongCloudHelper;
import com.pulamsi.photomanager.utils.OtherUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

import im.fir.sdk.FIR;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-10
 * Time: 14:46
 * FIXME
 */
public class MyApplication extends Application {

    public static RequestQueue requestQueue;
    public static Toastor toastor;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initData();
        initUmeng();
        initState();
        initRongCloud();//融云初始化
    }


    private void init() {
        FIR.init(this);//奔溃检测http://bughd.com
        requestQueue = Volley.newRequestQueue(this);
        toastor = new Toastor(this);
        context = getApplicationContext();
        GreenDaoHelper.initDatabase();
    }

    private void initData() {
        UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
        User user = userDao.queryBuilder().unique();
        if (user != null) {
            Constants.MID = user.getMid();
            Constants.IS_LOGIN = user.getIsLogin();
            Constants.IMG_URL = user.getImgUrl();
            Constants.NAME = user.getName();
            Constants.AUTO_GRAPH = user.getAutograph();
            Constants.USER_TYPE = user.getUserType();
            Constants.RONGCLOUD_TOKEN = user.getRongyunToken();
        }
        //配置参数
        Config config = GreenDaoHelper.getDaoSession().getConfigDao().queryBuilder().unique();
        if (config == null) {
            config = new Config((long) 1, true,true,true,true);
            GreenDaoHelper.getDaoSession().getConfigDao().insert(config);
        }
    }

    private void initState() {
//        PageStateLayout.Builder.setErrorView(R.layout.state_error);
//        PageStateLayout.Builder.setEmptyView(R.layout.state_empty);
    }

    private void initUmeng() {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
//目前SDK默认设置为在Token有效期内登录不进行二次授权，如果有需要每次登录都弹出授权页面，便于切换账号的开发者可以自行配置
        UMShareAPI.get(this).setShareConfig(config);
        com.umeng.socialize.Config.isJumptoAppStore = true;//对应平台没有安装的时候跳转转到应用商店下载
        PlatformConfig.setWeixin("wx521375ba1658f110", "9ec8e3dd6b6bb2a713e48101bacaa206");//不需要修改
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");//需要调试打包的应用才可以
//        PlatformConfig.setQQZone("1105712914", "Oym80JAatACn3cd9");//需要正式打包的应用才可以
        PlatformConfig.setAlipay("2015111700822536");

//      com.umeng.socialize.Config.DEBUG = true;
        //微信登录需要去开发者平台改签名
//        调试签名：9c29cc8bc30f5edc087bde312ef083a1
//        正式签名：46dcd2451b287e23d8edb3cb112e05f2
    }



    private void initRongCloud() {
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(OtherUtils.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(OtherUtils.getCurProcessName(getApplicationContext()))) {

//             IMKit SDK调用第一步 初始化
            RongIM.init(this);

            if (Constants.IS_LOGIN){
                RongCloudHelper.connect(Constants.RONGCLOUD_TOKEN);
            }

            //设置融云的用户信息提供者
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String s) {
                        RongCloudHelper.requestUserInformation(s);
                        return null;
                }
            }, true);
        }
    }

}
