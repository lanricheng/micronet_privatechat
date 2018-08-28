package com.vdunpay.vchat.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.vdunpay.utils.ProgressBarUtils;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.login.base.Config;
import com.vdunpay.vchat.login.bean.LoginRequestBean;
import com.vdunpay.vchat.login.bean.QQUser;
import com.vdunpay.vchat.login.bean.LoginOutPut;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.utils.LogUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;


/**
 * @name QQLoginManager
 * @anthor HY
 */

public class QQLoginManager extends AppCompatActivity {
    private static Tencent mTencent;
    private UserInfo userInfo;
    private static Context mContext;
    private static QQLoginManager qqmanager = null;
    private BaseUiListener listener = new BaseUiListener();
    private Gson gson = new Gson();
    private LoginActivity.LoginCallback logincallback =null;
    public static QQLoginManager getInstance(Context context) {
        mContext = context;
        mTencent = Tencent.createInstance(Config.QQ_LOGIN_APP_ID, mContext);
        if (qqmanager == null) {
            qqmanager = new QQLoginManager();
        }
        return qqmanager;
    }


    public void dologin(LoginActivity.LoginCallback loginCallback) {
        logincallback = loginCallback;
        String pnusername = (String) SharedPreferencesUtils.getInstance(getApplication(), "androidpn").getSharedPreference("pnusername", "");

        checkPnusername(pnusername);

        String qq_openid = (String) SharedPreferencesUtils.getInstance(getApplication(), "logininfo").getSharedPreference("qq_openid", "");
        String qq_token = (String) SharedPreferencesUtils.getInstance(getApplication(), "logininfo").getSharedPreference("qq_token", "");
        String wx_openid = (String) SharedPreferencesUtils.getInstance(getApplication(), "logininfo").getSharedPreference("wx_openid", "");
        String wx_token = (String) SharedPreferencesUtils.getInstance(getApplication(), "logininfo").getSharedPreference("wx_access_token", "");
        if (wx_openid == null) {
            wx_openid = "";
        }
        if (wx_token == null) {
            wx_token ="";
        }
        LoginRequestBean bean = new LoginRequestBean();
        bean.setPhoneNo("18378339259");
        bean.setvCardNum("1234567890");
        bean.setPassword("123456");
        bean.setPnusername(pnusername);
        bean.setOpenId(wx_openid);
        bean.setToken(wx_token);
        bean.setQqOpenId(qq_openid);
        bean.setQqtoken(qq_token);
        LogUtils.d("beantistring----"+bean.toString());
        OKHttpManager.getInstance(mContext).getResponse("user/login.do", gson.toJson(bean), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean !=null){
                    switch (bean.getErrorCode()){
                        case "000000":
                            message.what = 1;
                            message.obj = bean.getData();
                            LoginOutPut outputbean = gson.fromJson(bean.getData(),LoginOutPut.class);
                            logincallback.Callback(outputbean,bean.getMessage());
                            break;
                        default:
                            message.what = 3;
                            message.obj = bean.getMessage();
                            break;
                    }
                }else {
                    message.what = 3;
                    message.obj = "服务器返回空";
                }
                mHandler.sendMessage(message);
            }
        });
    }

    private void checkPnusername(String pnusername) {
        if (pnusername == null) {
            ToastUtil.showToast(mContext, "pnusername == null");
            return;
        }
        if (pnusername.length() == 0) {
            ToastUtil.showToast(mContext, "pnusername == null");
            return;
        }
    }


    public void doQQLogin(Activity context) {
        LogUtils.d("开始QQ登录..");
        if (!mTencent.isSessionValid())
        {
            //注销登录 mTencent.logout(this);
            mTencent.login(context, "all", listener);
        }
    }


    class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            LogUtils.d("授权:" + o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                getUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {
            LogUtils.d("onError:code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            LogUtils.d("onCancel");
        }
    }


    /**
     * 获取登录QQ腾讯平台的权限信息(用于访问QQ用户信息)
     *
     * @param jsonObject
     */
    public void initOpenidAndToken(org.json.JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }


    private void getUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    LogUtils.e("................" + response.toString());
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onCancel() {
                    LogUtils.d("登录取消..");
                }
            };
            userInfo = new UserInfo(mContext, mTencent.getQQToken());
            userInfo.getUserInfo(listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    JSONObject response = JSONObject.parseObject(String.valueOf(msg.obj));
                    LogUtils.d("UserInfo:" + JSON.toJSONString(response));
                    QQUser user = JSONObject.parseObject(response.toJSONString(), QQUser.class);
                    if (user != null) {
                        LogUtils.d("userInfo:昵称：" + user.getNickname() + "  性别:" + user.getGender() + "  地址：" + user.getProvince() + user.getCity());
                        LogUtils.d("头像路径：" + user.getFigureurl_qq_2());
                    }
                    break;
                case 1:
                    break;
                case 3:
                    QQLoginManager.this.logincallback.Callback(null,msg.obj.toString());
                    break;
            }
        }
    };




}
