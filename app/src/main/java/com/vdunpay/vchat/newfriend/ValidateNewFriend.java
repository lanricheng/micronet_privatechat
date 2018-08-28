package com.vdunpay.vchat.newfriend;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.ImageUtils;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.contact.contactinterface.ReLoadContactCallback;
import com.vdunpay.vchat.newfriend.bean.SendAddFriendBean;
import com.vdunpay.vchat.newfriend.bean.ValidateBean;

public class ValidateNewFriend extends AppCompatActivity implements View.OnClickListener {
    private Button mbtn_addnewfriend;
    private TextView mtv_myid, mtv_username;
    private ImageView mimv_userinfoback, mimv_newfriendicon, mimg_delete_sendremark, mimg_delete_myremark;
    private EditText medit_sendremark, medit_myremark;
    private Context mContext;
    private Gson gson = new Gson();
    private static ReLoadContactCallback Callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_new_friend);
        mContext = getApplication();
        initView();
        initData();
    }


    private void initView() {
        mbtn_addnewfriend = (Button) findViewById(R.id.btn_addnewfriend);
        mtv_myid = (TextView) findViewById(R.id.tv_myid);
        mtv_username = (TextView) findViewById(R.id.tv_username);
        mimv_userinfoback = (ImageView) findViewById(R.id.imv_userinfoback);
        mimv_newfriendicon = (ImageView) findViewById(R.id.imv_newfriendicon);
        mimg_delete_sendremark = (ImageView) findViewById(R.id.img_delete_sendremark);
        mimg_delete_myremark = (ImageView) findViewById(R.id.img_delete_myremark);
        medit_sendremark = (EditText) findViewById(R.id.edit_sendremark);
        medit_myremark = (EditText) findViewById(R.id.edit_myremark);

        mbtn_addnewfriend.setOnClickListener(this);
        mimg_delete_sendremark.setOnClickListener(this);
        mimg_delete_myremark.setOnClickListener(this);
        mimv_userinfoback.setOnClickListener(this);
    }

    private void initData() {
        String UsernameAndId = getIntent().getStringExtra("qrcore");
        String[] group = UsernameAndId.split("&");
        ValidateBean bean = new ValidateBean();
        bean.setId(group[2]);
        bean.setNickname(group[1]);
        OKHttpManager.getInstance(mContext).getResponse("user/get_info.do", gson.toJson(bean), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean != null) {
                    if ("000000".equals(bean.getErrorCode())) {
                        message.what = 0;
                        message.obj = bean.getData();
                    } else {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addnewfriend:
                doAddNewFriend();
                break;
            case R.id.img_delete_sendremark:
                String sendremark = medit_sendremark.getText().toString();
                medit_sendremark.setText(deletestr(sendremark));
                break;
            case R.id.img_delete_myremark:
                String myremark = medit_myremark.getText().toString();
                medit_myremark.setText(deletestr(myremark));
                break;
            case R.id.imv_userinfoback:
                Callback.reloadcontact();
                onBackPressed();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        Callback.reloadcontact();
        super.onBackPressed();
        finish();
    }


    private void doAddNewFriend() {

        SendAddFriendBean bean = new SendAddFriendBean();
        bean.setId(mtv_myid.getText().toString().replace("ID:", ""));
        bean.setMessage(medit_sendremark.getText().toString());
        bean.setRemark(medit_myremark.getText().toString());
        OKHttpManager.getInstance(mContext).getResponse("friends/init_add.do", gson.toJson(bean), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean != null) {
                        message.what = 1;
                        message.obj = bean.getMessage();
                    LogUtils.d("发送好友请求结果---"+bean.getMessage());
                } else {
                    message.what = 1;
                    message.obj = "请求成功，但数据返回空。";
                }
                handler.sendMessage(message);
            }
        });


    }

    private String deletestr(String str) {
        str = str.substring(0, str.length() - 1);
        return str;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ValidateBean bean = (ValidateBean) gson.fromJson(msg.obj.toString(), ValidateBean.class);
                    showIcon(bean);
                    break;
                case 1:
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
            }
        }
    };



    private void showIcon(ValidateBean bean) {
        Bitmap addbmp = ImageUtils.base64ToBitmap(bean.getUserIcon());
        if (addbmp != null) {
            mtv_username.setText(bean.getNickname());
            mtv_myid.setText("ID:" + bean.getId());
            mimv_newfriendicon.setImageBitmap(addbmp);
        } else {
            ToastUtil.showToast(mContext, "头像数据有误，无法生成头像");
        }
    }


    public static void TurnBackCallback(ReLoadContactCallback callback){
        Callback = callback;
    }
}
