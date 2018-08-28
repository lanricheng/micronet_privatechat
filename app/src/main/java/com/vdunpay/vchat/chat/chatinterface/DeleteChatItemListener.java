package com.vdunpay.vchat.chat.chatinterface;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HY on 2018/8/16.
 */

public interface DeleteChatItemListener {
    void ItemDeleteListener(int position, View convertView, ViewGroup viewGroup);
    void updateChatListListener();
}
