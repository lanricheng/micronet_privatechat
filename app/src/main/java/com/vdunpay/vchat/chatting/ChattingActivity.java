package com.vdunpay.vchat.chatting;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vdunpay.db.DataBaseOpenHelper;
import com.vdunpay.notificationserver.Constants;
import com.vdunpay.notificationserver.LogUtil;
import com.vdunpay.notificationserver.NotificationService;
import com.vdunpay.notificationserver.Notifier;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.utils.StringUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.chat.bean.MsgBean;
import com.vdunpay.vchat.chat.bean.PNRecviceBean;
import com.vdunpay.vchat.login.LoginActivity;
import com.vdunpay.vchat.R;
import com.vdunpay.utils.StatusBarUtils;
import com.vdunpay.vchat.chatting.bean.ChatMsgBean;
import com.vdunpay.vchat.chatting.bean.LoadChatMsgBean;
import com.vdunpay.vchat.chatting.bean.SendMsgBean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etMsg;
    private Button btnSend;
    private static RecyclerView rvChat;
    private TextView mtv_chatusername;
    private static ChattingAdapter chatAdapter;
    private static ArrayList<ChatMsgBean> list = new ArrayList<>();
    private ImageView mimv_back;
    private static String friendUsername;
    public static String friendicon;
    private static String friendid;
    private Context mContext;
    private static Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mContext = getApplication();
        StatusBarUtils.setActivityTranslucent(this);
        StatusBarUtils.setWindowStatusBarColor(ChattingActivity.this, getResources().getColor(R.color.black));

        initView();
        initData();

    }

    /**
     *
     */
    private void initData() {
        friendUsername = getIntent().getStringExtra("username");
        friendicon = getIntent().getStringExtra("usericon");
        friendid = getIntent().getStringExtra("userid").replace("ID:", "");

        LogUtils.d("username----"+friendUsername);
        LogUtils.d("usericon----"+  friendicon);
        LogUtils.d("userid----"+friendid);

        if (check() == false) {
            return;
        }
        //往数据库中保存最新的会话
        new Thread() {
            @Override
            public void run() {
                saveChating();
            }
        }.start();

        OKHttpManager.getInstance(getApplication()).getResponse("chat/info_list.do", gson.toJson(new LoadChatMsgBean(friendid)), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean != null) {
                    if ("000000".equals(bean.getErrorCode())) {
                        message.what = 1;
                        message.obj = bean.getData();
                    } else {
                        message.what = 2;
                        message.obj = bean.getMessage();
                    }
                } else {
                    message.what = 2;
                    message.obj = "请求成功，服务器返回空";
                }
                handler.sendMessage(message);
            }
        });


        String username = getIntent().getStringExtra("username");
        if (username == null || "".equals(username)) {
            mtv_chatusername.setText(R.string.unusername);
        } else {
            mtv_chatusername.setText(username + "");
        }
    }

    /**
     *
     */
    private void saveChating() {
        LogUtils.d("SQL----");
//      String sql = "select count(*) from info";
        String queryIsExist = "select * from chathistory where userid = " + "'" + friendid + "'";
        LogUtils.d("SQL----"+queryIsExist);
        Cursor cursor = DataBaseOpenHelper.getInstance(mContext).rawQuery(queryIsExist, null);
        if (cursor.getCount() == 0) {
//            String add = "insert into chathistory values(?,?,?)";
            try {
                ContentValues initialValues = new ContentValues();
                initialValues.put("userid", friendid);
                initialValues.put("username", friendUsername);
                initialValues.put("usericon", friendicon);
//                LogUtils.d("datetime"+ DateUtils.getNowDate());
//                initialValues.put("datetime", DateUtils.getNowDate());
                DataBaseOpenHelper.getInstance(mContext).insert("chathistory", initialValues);
            } catch (Exception e) {
                Log.i("RegisterActivity", "adduser-->error");
            }
        } else {
            //已存在就先删除，然后再添加，确保查询的时候最新的会话排在第一位
//            String delete = "delete from chathistory where userid = " + "'" + friendid + "'";
            DataBaseOpenHelper.getInstance(mContext).delete("chathistory", "userid=?", new String[]{friendid});

//            String add = "INSERT INTO chathistory VALUES(?,?,?)";
            try {
                ContentValues initialValues = new ContentValues();
                initialValues.put("userid", friendid);
                initialValues.put("username", friendUsername);
                initialValues.put("usericon", friendicon);
//              LogUtils.d("datetime"+ DateUtils.getNowDate());
//              initialValues.put("datetime", DateUtils.getNowDate());
                DataBaseOpenHelper.getInstance(mContext).insert("chathistory", initialValues);
            } catch (Exception e) {
                Log.i("RegisterActivity", "adduser-->error");
            }
        }
        cursor.close();
    }


    private void initView() {
        etMsg = (EditText) findViewById(R.id.et_msg);
        btnSend = (Button) findViewById(R.id.btn_send);
        mtv_chatusername = (TextView) findViewById(R.id.tv_chatusername);
        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        mimv_back = (ImageView) findViewById(R.id.imv_back);
        rvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatAdapter = new ChattingAdapter(this, list);
        rvChat.setAdapter(chatAdapter);
        btnSend.setOnClickListener(this);
        mimv_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.imv_back:
//                ChatFragment.addChatItem(friendUsername,friendid,friendicon);
                onBackPressed();
//                IntentUtil.getInstance().showIntent(ChattingActivity.this,MainActivity2.class);
                finish();
                break;
        }
    }

    private void sendMsg() {
        String message = etMsg.getText().toString();
        if (message.length() == 0) {
            ToastUtil.showToast(mContext, "请输入要发送的信息");
            return;
        }
        OKHttpManager.getInstance(mContext).getResponse("chat/send.do", gson.toJson(new SendMsgBean(friendid, message)), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean != null) {
                    message.what = 3;
                    message.obj = bean.getMessage();
                } else {
                    message.what = 2;
                    message.obj = "请求成功，服务器返回空";
                }
                handler.sendMessage(message);
            }
        });
    }


    public static class NotificationReceiver extends BroadcastReceiver {
        private Context mContext;
        private NotificationService notificationService;

        public NotificationReceiver() {
        }

        public NotificationReceiver(NotificationService notificationService) {
            this.notificationService = notificationService;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            String action = intent.getAction();

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
//             Toast.makeText(context, "789789789", Toast.LENGTH_LONG).show();
                Notifier notifier = new Notifier(context);
                notifier.notify(notificationId, notificationApiKey,
                        notificationTitle, notificationMessage, notificationUri);


//                try {
//                    String decodedString =new String(StringUtils.hexToBytes(notificationMessage));
//                    decodedString = new String(decodedString.getBytes("UTF-8"),"UTF-8");
//                    LogUtils.d("ss---"+decodedString);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                PNRecviceBean bean = gson.fromJson(notificationMessage,PNRecviceBean.class);
                switch (bean.getOp()){
                    case "1001":
                        Message message = new Message();
                        message.what=0;
                        message.obj=bean.getMsg();
                        mhandler.sendMessage(message);
                        break;
                }
            }
        }
    }

    static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String ss =  msg.obj.toString();
                    String decodedString =new String(StringUtils.hexToBytes(ss));
                    LogUtils.d("ss---"+decodedString);
                    try {
                        decodedString = URLDecoder.decode(decodedString,"UTF-8");
                        LogUtils.d("ss---"+decodedString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    MsgBean msgBean = gson.fromJson(decodedString, MsgBean.class);
                        ChatMsgBean messagebean = new ChatMsgBean();
                        messagebean.setId(msgBean.getSender());
                        messagebean.setSessionMessage(msgBean.getMsgc());
                        messagebean.setCreateDate(msgBean.getTime());
                        list.add(messagebean);
                        chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
                        rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);//插一条数据到最后一行
                    rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);//滑动到最后一条数据
                    break;
                case 1:
//                    ChatMsgBean bean = gson.fromJson(msg.obj.toString(),ChatMsgBean.class);
                    ArrayList<ChatMsgBean> listbean = gson.fromJson(msg.obj.toString(), ArrayList.class);
                    if (listbean != null) {
                        listupdate(listbean);
                    }
                    break;
                case 2:
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
                case 3:
                    ToastUtil.showToast(mContext, msg.obj.toString());
//                    if (list.size()>0){
//
////                    }
//                    String lastsenddate = list.get(list.size()-1).getCreateDate();
//                    LogUtils.d("lastsenddate----"+lastsenddate);
//                    if (lastsenddate == null){
//                        return;
//                    }
//                    Date lastdate = DateUtils.formatData(lastsenddate);
                    String myid = (String) SharedPreferencesUtils.getInstance(mContext,"userinfo").getSharedPreference("userid","");
                    ChatMsgBean messagebean = new ChatMsgBean();
                    messagebean.setId(myid);
                    messagebean.setSessionMessage(etMsg.getText().toString());
//                    if (DateUtils.compareTime(lastdate) == true){
//                        messagebean.setFlag(0);
//                    }else {
//                        messagebean.setFlag(1);
//                    }

                    messagebean.setCreateDate("");
                    list.add(messagebean);
                    chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
                    rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    etMsg.setText("");
                    break;
            }

        }
    };

    private void listupdate(ArrayList<ChatMsgBean> listbean) {

        if (listbean != null) {
            list.clear();
            for (int i = 0; i < listbean.size(); i++) {
                Object object = listbean.get(i);
                String ttt = object.toString();
                LogUtils.d("ttt-----" + ttt);
                ChatMsgBean msgBean = gson.fromJson(ttt, ChatMsgBean.class);
                msgBean.setFriendicon(friendicon);
                list.add(msgBean);
                chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);//插一条数据到最后一行
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);//滑动到最后一条数据
            }
        } else {

        }

    }


    private boolean check() {
        if (friendicon == null) {
            ToastUtil.showToast(mContext, "头像数据为空，初始化失败");
            return false;
        }
        if ("".equals(friendicon)) {
            ToastUtil.showToast(mContext, "头像数据为空，初始化失败");
            return false;
        }
        if (friendid == null) {
            ToastUtil.showToast(mContext, "好友ID为空，初始化失败");
            return false;
        }
        if ("".equals(friendid)) {
            ToastUtil.showToast(mContext, "好友ID为空，初始化失败");
            return false;
        }
        if (friendUsername == null) {
            ToastUtil.showToast(mContext, "好友昵称为空，初始化失败");
            return false;
        }
        if ("".equals(friendUsername)) {
            ToastUtil.showToast(mContext, "好友昵称为空，初始化失败");
            return false;
        }
        return true;
    }

}
