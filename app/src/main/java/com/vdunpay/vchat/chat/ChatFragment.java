package com.vdunpay.vchat.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.vdunpay.db.DataBaseOpenHelper;
import com.vdunpay.notificationserver.Constants;
import com.vdunpay.notificationserver.NotificationService;
import com.vdunpay.notificationserver.Notifier;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.chat.adapter.ChatListAdapter;
import com.vdunpay.vchat.chat.bean.ChatItemBean;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.vchat.chat.bean.MsgBean;
import com.vdunpay.vchat.chat.bean.PNRecviceBean;
import com.vdunpay.vchat.chat.chatinterface.DeleteChatItemListener;
import com.vdunpay.vchat.chatting.Msg;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements DeleteChatItemListener{

    private final static String TAG = "ChatActivity";
    private static List<ChatItemBean> mlist = new ArrayList<>();
    private static ChatListAdapter mAdapter = null;
    private HeaderScrollView mHeaderScrollView = null;
    public static Context mContext;
    private static Gson gson = new Gson();
    public static final String ACTION_UPDATEUI = "action.updatebottomtip";
    UpdateUIBroadcastReceiver broadcastReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chat_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        ChatListAdapter.setDeleteChatItemListener(ChatFragment.this);
        mlist = new ArrayList<ChatItemBean>();
        mlist =queryChatHistory();
        mHeaderScrollView = (HeaderScrollView) getActivity().findViewById(R.id.scrollView);
        mAdapter = new ChatListAdapter(getActivity(), mlist);
        mHeaderScrollView.setAdapter(mAdapter);

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        mContext.registerReceiver(broadcastReceiver, filter);

    }


    private void modifyMargin(int i, View view,ViewGroup viewGroup){
        View itemView = mAdapter.getView(i, view, viewGroup);
        if(null != itemView){
            View leftView = itemView.findViewWithTag(ScrollItemListView.NOR_DELETE_TAG);
            if(null != leftView) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) leftView.getLayoutParams();
                LogUtils.d(TAG, "leftMargin:" + params.leftMargin);
                params.leftMargin = 0;
                leftView.setLayoutParams(params);
            }
        }
    }

    public List<ChatItemBean> queryChatHistory(){
        List<ChatItemBean> list = new ArrayList<>();
        String queryIsExist = "select * from chathistory";
        Cursor cursor = DataBaseOpenHelper.getInstance(mContext).rawQuery(queryIsExist, null);
        LogUtils.d("cursor---"+cursor);
        while (cursor.moveToNext()){
            String friendid = cursor.getString(cursor.getColumnIndex("userid"));
            String friendUsername = cursor.getString(cursor.getColumnIndex("username"));
            String friendicon = cursor.getString(cursor.getColumnIndex("usericon"));
            ChatItemBean bean = new ChatItemBean(friendid,friendUsername,friendicon);
            list.add(bean);
        }
        return list;
    }


    @Override
    public void ItemDeleteListener(int position, View convertView, ViewGroup viewGroup) {
        modifyMargin(position, convertView ,viewGroup);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateChatListListener() {

    }


    /**
     * 定义广播接收器（内部类）
     *
     * @author lenovo
     *
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(intent.getStringExtra("msgbean"));
            ToastUtil.showToast(mContext,intent.getStringExtra("msgbean"));
        }

    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        // 注销广播
        mContext.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


   static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
         switch (msg.what){
             case 0:
                 MsgBean msgBean = gson.fromJson(msg.obj.toString(), MsgBean.class);

                 break;
         }
        }
    };

}
