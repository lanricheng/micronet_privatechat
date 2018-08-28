/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vdunpay.notificationserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import com.google.gson.Gson;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.chat.ChatFragment;
import com.vdunpay.vchat.chat.bean.PNRecviceBean;

/** 
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml. 
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class NotificationReceiver extends BroadcastReceiver {
    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationReceiver.class);
    private Context mContext;
//    static MakeCallInterface mMakeCall  = null;
//    static AnswerCallInterface mAnswerCall  = null;
        private NotificationService notificationService;

    public NotificationReceiver() {
    }

        public NotificationReceiver(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);

        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
            String notificationId = intent
                    .getStringExtra(Constants.NOTIFICATION_ID);
            String notificationApiKey = intent
                    .getStringExtra(Constants.NOTIFICATION_API_KEY);
            String notificationTitle = intent
                    .getStringExtra(Constants.NOTIFICATION_TITLE);
            String notificationMessage = intent
                    .getStringExtra(Constants.NOTIFICATION_MESSAGE);
            String notificationUri = intent
                    .getStringExtra(Constants.NOTIFICATION_URI);
            ToastUtil.showToast(context,"notificationMessage=" + notificationMessage);
            Log.d(LOGTAG, "notificationId=" + notificationId);
            Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
            Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
            Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
            Log.d(LOGTAG, "notificationUri=" + notificationUri);
//             Toast.makeText(context, "789789789", Toast.LENGTH_LONG).show();
            Notifier notifier = new Notifier(context);
            notifier.notify(notificationId, notificationApiKey,
                    notificationTitle, notificationMessage, notificationUri);

            Gson gson = new Gson();
            PNRecviceBean bean = gson.fromJson(notificationMessage,PNRecviceBean.class);
            switch (bean.getOp()){
                case "1001":
                    final Intent intent2chatgragment = new Intent();
                    intent2chatgragment.setAction(ChatFragment.ACTION_UPDATEUI);
                    intent2chatgragment.putExtra("msgbean", bean.getMsg());
                    ChatFragment.mContext.sendBroadcast(intent2chatgragment);
                    break;
            }
//            PushMsg bean = gson.fromJson(notificationMessage,PushMsg.class);
//            Log.d("推送服务器返回的数据", "bean: "+bean.toString());
//            switch (bean.getOp()){
//                case "1001":
////                    BaseCommon.getInstance(context).setIsreload(true);
//                    break;
//                case "1002":
//
//                    break;
//                case "1003":
////                    CallManager.getInstance(mContext).AnswerCall(bean.getMsg());
//                    break;
//
//                case "1004":
////                    mMakeCall.startMakeCall(bean.getMsg());
//                    break;
//
//                case "1005":
////                    mMakeCall.hangup();
//                    break;
//
//            }
        }
    }
}
