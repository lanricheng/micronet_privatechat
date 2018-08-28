package com.vdunpay.vchat.newfriend;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vdunpay.vchat.contact.contactinterface.ReLoadContactCallback;
import com.vdunpay.vchat.newfriend.adapter.AddNewFriendAdapter;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.ProgressBarUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.R;
import com.vdunpay.utils.StatusBarUtils;
import com.vdunpay.vchat.newfriend.bean.GetAddFriendRequestBean;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestListActivity extends AppCompatActivity {
    private TextView mtv_nonewfriendtip,mtv_addnewfriend;
    private ListView mylv_newfriend;
    private EditText medt_seachnewfriend;
    private ImageView mimv_addfriendback;
    private Context mContext;
    private AddNewFriendAdapter adapter;
    private List<GetAddFriendRequestBean> list = new ArrayList<>();
    private Gson gson = new Gson();
    private static ReLoadContactCallback Callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        StatusBarUtils.setActivityTranslucent(this);
        StatusBarUtils.setWindowStatusBarColor(FriendRequestListActivity.this,getResources().getColor(R.color.black));
        mContext = getApplication();
        initView();
        initData();
    }


    @Override
    public void onBackPressed() {
        Callback.reloadcontact();
        super.onBackPressed();
        finish();
    }

    private void initData() {
        ProgressBarUtils.startLoad("加载中...", FriendRequestListActivity.this);
        adapter = new AddNewFriendAdapter(mContext,list);
        mylv_newfriend.setAdapter(adapter);
        OKHttpManager.getInstance(mContext).getResponse("friends/get_friend_request_list.do","", new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean != null) {
                    if ("000000".equals(bean.getErrorCode())){
                        message.what = 0;
                        message.obj = bean.getData();
                    }else {
                        message.what = 1;
                        message.obj = bean.getMessage();
                    }
                } else {
                    message.what = 1;
                    message.obj = "请求成功，但数据返回空。";
                }
                handler.sendMessage(message);
            }
        });



    }

    private void initView() {
        mtv_nonewfriendtip = (TextView) findViewById(R.id.tv_nonewfriendtip) ;
        mtv_addnewfriend = (TextView) findViewById(R.id.tv_addnewfriend) ;
        mylv_newfriend = (ListView) findViewById(R.id.lv_newfriend) ;
        medt_seachnewfriend = (EditText) findViewById(R.id.edt_seachnewfriend) ;
        mimv_addfriendback= (ImageView) findViewById(R.id.imv_addfriendback) ;
        medt_seachnewfriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mimv_addfriendback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback.reloadcontact();
                onBackPressed();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ProgressBarUtils.stopLoad();
                    LogUtils.d("msg.obj.toString()"+msg.obj.toString());
                    ArrayList<GetAddFriendRequestBean> list = gson.fromJson(msg.obj.toString(),ArrayList.class);
                    if (list !=null){
                        listupdate(list);
                    }
                    break;
                case 1:
                    ProgressBarUtils.stopLoad();
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
            }
        }
    };

    private void listupdate(ArrayList<GetAddFriendRequestBean> listbean) {
        if (listbean!=null){
               list.clear();
          for (int i=0;i<listbean.size();i++){
              Object object = listbean.get(i);
              String ttt = object.toString();
              GetAddFriendRequestBean FriendListOut = gson.fromJson(ttt,GetAddFriendRequestBean.class);
              list.add(FriendListOut);
          }
            if (list == null || list.size() == 0){
                mtv_nonewfriendtip.setVisibility(View.VISIBLE);
                mylv_newfriend.setVisibility(View.GONE);
            }else {
                mtv_nonewfriendtip.setVisibility(View.GONE);
                mylv_newfriend.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }else {

        }
    }


    public static void TurnBackCallback(ReLoadContactCallback callback){
        Callback = callback;
    }

}
