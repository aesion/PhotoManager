package com.pulamsi.photomanager.view;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

import com.pulamsi.photomanager.R;
import com.pulamsi.photomanager.base.CommonBaseActivity;
import com.pulamsi.photomanager.base.Constants;
import com.pulamsi.photomanager.utils.OtherUtils;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends CommonBaseActivity {
    private String mTargetId,title;

    boolean isFromPush = false;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;


    @Override
    protected void init() {
        Intent intent = getIntent();
        getIntentDate(intent);
        isReconnect(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.conversation;
    }

    @Override
    protected int getToolBarTitleID() {
        return R.string.image_details;
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        title = intent.getData().getQueryParameter("title");
        toolbar.setTitle("与 "+title+" 对话中");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

//        enterFragment(mConversationType, mTargetId);不能重复调用
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {
        if (getApplicationInfo().packageName.equals(OtherUtils.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                }

                @Override
                public void onSuccess(String s) {
                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
        }
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {
        if (!Constants.IS_LOGIN)
            return;

        String token = Constants.RONGCLOUD_TOKEN;

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
            isFromPush = true;
            Log.e("","isFromPush");
            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
        if(!fragment.onBackPressed()) {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && isFromPush) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}