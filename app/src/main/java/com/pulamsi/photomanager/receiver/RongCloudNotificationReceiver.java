package com.pulamsi.photomanager.receiver;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class RongCloudNotificationReceiver extends PushMessageReceiver {
 @Override
 public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
    return false;
 }

 @Override
 public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
    return false;
 }
}