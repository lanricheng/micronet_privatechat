package com.vdunpay.vchat.mine;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vdunpay.notificationserver.LogUtil;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.vchat.login.bean.LoginOutPut;
import com.vdunpay.utils.ImageUtils;
import com.vdunpay.utils.IntentUtil;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.dialog.DialogUtil;
import com.vdunpay.qrcore.QRcoreinterface;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.mine.mineinterface.UserinfoCallback;
import com.vdunpay.vchat.newfriend.AddNewFriendByQR;

import java.util.HashMap;
import java.util.Map;

public class MineFragment extends Fragment implements View.OnClickListener, QRcoreinterface ,UserinfoCallback {
    private LinearLayout mlay_userinfo;
    private ImageView mimv_myicon, imv_core;
    private TextView mtv_querypubkey, mtv_qqbind, mtv_wxbind, mtv_username, mtv_myid;
    private QRcoreinterface qRcoreinterface = null;
    private Context mContext;
    private static String icondbase64 ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mimv_myicon = (ImageView) getActivity().findViewById(R.id.imv_myicon);
        imv_core = (ImageView) getActivity().findViewById(R.id.imv_core);
        mtv_username = (TextView) getActivity().findViewById(R.id.tv_username);
        mtv_myid = (TextView) getActivity().findViewById(R.id.tv_myid);
        mtv_querypubkey = (TextView) getActivity().findViewById(R.id.tv_querypubkey);
        mtv_qqbind = (TextView) getActivity().findViewById(R.id.tv_qqbind);
        mtv_wxbind = (TextView) getActivity().findViewById(R.id.tv_wxbind);
        imv_core.setOnClickListener(this);
        mtv_querypubkey.setOnClickListener(this);
        mtv_qqbind.setOnClickListener(this);
        mtv_wxbind.setOnClickListener(this);

        mlay_userinfo = (LinearLayout) getActivity().findViewById(R.id.lay_userinfo);
        mlay_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map.put("icon",icondbase64);
                map.put("username",mtv_username.getText().toString());
                map.put("id",mtv_myid.getText().toString().replace("ID:",""));
                IntentUtil.getInstance().showIntent(getActivity(), SettingActivity.class,map);
            }
        });

        initData();
    }

    private void initData() {
        String username = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("username", "");
        String usericon = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("usericon", "");
            if (usericon !=null){
                Bitmap addbmp = ImageUtils.base64ToBitmap(usericon);
                icondbase64 = usericon;
                if (addbmp != null) {
                    mimv_myicon.setImageBitmap(addbmp);
                } else {
                    SharedPreferencesUtils.getInstance(mContext, "userinfo").put("isave", "false");
                    ToastUtil.showToast(mContext, "头像数据已丢失，请重新上传头像");
                }
            }
            if (username !=null){
                mtv_username.setText(username);
            }else {
                mtv_username.setText("昵称未设置");
            }
        SettingActivity.getUserInfo(MineFragment.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_core:
                String username = (String) SharedPreferencesUtils.getInstance(mContext,"userinfo").getSharedPreference("username","");
                String userid = (String) SharedPreferencesUtils.getInstance(mContext,"userinfo").getSharedPreference("userid","");
                String qrmsg = "micronet&" + username+ "&" + userid;
                DialogUtil.showqrcore(getActivity(), qrmsg);
                break;
            case R.id.tv_querypubkey:
                DialogUtil.showPubKey(getActivity(), "11111111111111111111111111111111111111111111111111111111111111111111111111111");
                break;
            case R.id.tv_qqbind:
                DialogUtil.showChangeQQBind(getActivity());
                break;
            case R.id.tv_wxbind:
                DialogUtil.showWXBind(getActivity());
                break;
//            case  R.id.tv_qrsaomiao:
//                if (qRcoreinterface == null) {
//                    qRcoreinterface = MineFragment.this;
//                }
//                WeChatCaptureActivity.setinterfaceContext(qRcoreinterface);
//                Intent intent = new Intent(getActivity(), WeChatCaptureActivity.class);
//                startActivity(intent);
//                break;
//            case  R.id.tv_addfriend:
//                DialogUtil.addFriend(getActivity());
//                break;
        }
    }

    @Override
    public void getQRcoreData(String str) {
        if (str == null || "".equals(str)) {
            ToastUtil.showToast(getActivity(), "扫描结果为空，请重新扫描。");
            return;
        }
        IntentUtil.getInstance().showIntent(getActivity(), AddNewFriendByQR.class, "qrcore", str);
    }





    @Override
    public void UserInfoCallback(LoginOutPut outPut, String logintype) {
        if (outPut !=null){
            if (outPut.getIcon() !=null){
                Bitmap addbmp = ImageUtils.base64ToBitmap(outPut.getIcon());
                icondbase64 = ImageUtils.bitmapToBase64(addbmp);
                if (addbmp != null) {
                    mimv_myicon.setImageBitmap(addbmp);
                } else {
                    SharedPreferencesUtils.getInstance(mContext, "userinfo").put("isave", "false");
                    ToastUtil.showToast(mContext, "头像数据已丢失，请重新上传头像");
                }
            }
            if (outPut.getNickname() !=null){
                mtv_username.setText(outPut.getNickname());
            }else {
                mtv_username.setText("昵称未设置");
            }
            mtv_myid.setText("ID:"+outPut.getId());
        }else {
            Message message = new Message();
            message.what= 0;
            message.obj = "数据初始化失败";
            mhandle.sendMessage(message);
        }
    }

    @Override
    public void UserIconAndUsernameCallback(){
        String base64data =(String) SharedPreferencesUtils.getInstance(mContext,"userinfo").getSharedPreference("usericon","");
        String username =(String) SharedPreferencesUtils.getInstance(mContext,"userinfo").getSharedPreference("username","");
        if (base64data !=null){
                Bitmap addbmp = ImageUtils.base64ToBitmap(base64data);
                icondbase64 = ImageUtils.bitmapToBase64(addbmp);
                if (addbmp != null) {
                    mimv_myicon.setImageBitmap(addbmp);
                } else {
                    SharedPreferencesUtils.getInstance(mContext, "userinfo").put("isave", "false");
                    ToastUtil.showToast(mContext, "头像数据已丢失，请重新上传头像");
                }
            if (username !=null){
                mtv_username.setText(username);
            }else {
                mtv_username.setText("昵称未设置");
            }
        }else {
            Message message = new Message();
            message.what= 0;
            message.obj = "头像数据初始化失败";
            mhandle.sendMessage(message);
        }
    }

    Handler mhandle = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    ToastUtil.showToast(mContext,msg.obj.toString());
//                    getActivity().finish();
                    break;
            }
        }
    };
}
