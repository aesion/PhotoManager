package com.pulamsi.photomanager.view;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.AppBus;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.base.Flag;
import com.pulamsi.photomanager.bean.User;
import com.pulamsi.photomanager.bean.UserDao;
import com.pulamsi.photomanager.dao.GreenDaoHelper;
import com.pulamsi.photomanager.utils.GlideCacheUtil;
import com.pulamsi.photomanager.utils.ShareUtils;
import com.pulamsi.photomanager.utils.SnackbarUtil;
import com.trycatch.mysnackbar.Prompt;

import cn.hugeterry.updatefun.UpdateFunGO;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-29
 * Time: 14:47
 * FIXME
 */
public class SettingActivity extends CommonBaseActivity {

    private SettingsFragment mSettingsFragment;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.setting_title;
    }

    @Override
    protected void init() {
        mSettingsFragment = new SettingsFragment();
        replaceFragment(R.id.settings_container, mSettingsFragment);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        Preference cache;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            initEvent();
        }

        private void initEvent() {
            Preference exit = findPreference("exit");
            Preference about = findPreference("about");
            Preference update = findPreference("update");
            Preference feedback = findPreference("feedback");
            Preference share = findPreference("share");
            cache = findPreference("cache");
            exit.setOnPreferenceClickListener(this);
            about.setOnPreferenceClickListener(this);
            update.setOnPreferenceClickListener(this);
            feedback.setOnPreferenceClickListener(this);
            share.setOnPreferenceClickListener(this);
            cache.setOnPreferenceClickListener(this);

            cache.setSummary(GlideCacheUtil.getInstance().getCacheSize(getActivity()));
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals("exit")) {
                exit();
                return true;
            } else if (preference.getKey().equals("about")) {
                about();
                return true;
            } else if (preference.getKey().equals("update")) {
                UpdateFunGO.manualStart(getActivity());
                return true;
            } else if (preference.getKey().equals("feedback")) {
                readyGo(FeedbackActivity.class);
                return true;
            } else if (preference.getKey().equals("cache")) {
                cache();
                return true;
            }else if (preference.getKey().equals("share")) {
                share();
                return true;
            }
            return true;
        }


        /**
         * 分享
         */
        private void share() {
            ShareUtils.getInstance().shareApp(getActivity());
        }


        /**
         * 清理缓存
         */
        private void cache() {
            SnackbarUtil.showLoading(getActivity().getWindow().getDecorView(), "清理中...");
            GlideCacheUtil.getInstance().clearImageAllCache(getActivity());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SnackbarUtil.showLoadSuccess("清理完成");
                    cache.setSummary(GlideCacheUtil.getInstance().getCacheSize(getActivity()));
                }
            }, 3000);
        }


        /**
         * 退出账号
         */
        private void exit() {
            if (!Constants.IS_LOGIN){
                SnackbarUtil.showPrompt(getActivity().getWindow().getDecorView(),"未登录", Prompt.WARNING);
            }else {
                UserDao userDao = GreenDaoHelper.getDaoSession().getUserDao();
                User user = userDao.queryBuilder().unique();
                if (user != null) {
                    userDao.deleteAll();
                    Constants.MID = null;
                    Constants.IS_LOGIN = false;
                    Constants.IMG_URL = null;
                    Constants.NAME = null;
                    Constants.AUTO_GRAPH = null;
                    Constants.RONGCLOUD_TOKEN = null;
                    Constants.USER_TYPE = 0;
                    AppBus.getInstance().post(Flag.REFRESH_CLASSIFICATION);
                    SnackbarUtil.showPrompt(getActivity().getWindow().getDecorView(),"退出成功", Prompt.SUCCESS);
                }else {
                    SnackbarUtil.showPrompt(getActivity().getWindow().getDecorView(),"退出账号失败", Prompt.ERROR);
                }
            }
        }


        /**
         * 关于
         */
        private void about() {
            readyGo(AboutActivity.class);
        }

        protected void readyGo(Class<?> clazz) {
            Intent intent = new Intent(getActivity(), clazz);
            startActivity(intent);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

}
