package com.vdunpay.vchat.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.vdunpay.notificationserver.ServiceManager;
import com.vdunpay.notificationserver.XmppManager;
import com.vdunpay.notificationserver.XmppManagerCallBack;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.IntentUtil;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.ProgressBarUtils;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.utils.StatusBarUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.MainActivity2;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.login.base.Config;
import com.vdunpay.vchat.login.bean.BindUserInfo;
import com.vdunpay.vchat.login.bean.LoginRequestBean;
import com.vdunpay.vchat.login.bean.QQUser;
import com.vdunpay.vchat.login.bean.LoginOutPut;
import com.vdunpay.vchat.login.bean.UpdateTokenBean;

import org.json.JSONException;

import static com.vdunpay.vchat.login.base.Config.APP_ID_WX;

public class LoginActivity extends AppCompatActivity implements XmppManagerCallBack {
    private static int LOGINTYPE = 0;//1微信 2 QQ
    private RelativeLayout mlay_qqlogin, mlay_wxlogin;
    private Context mContext;
    private Tencent mTencent;
    private UserInfo userInfo;
    private ServiceManager serviceManager;
    /**
     * 微信登录相关
     */
    private IWXAPI api;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setActivityTranslucent(this);
        StatusBarUtils.setWindowStatusBarColor(LoginActivity.this, getResources().getColor(R.color.black));
        initView();
        initData();
    }


    private void initView() {
        mlay_wxlogin = (RelativeLayout) findViewById(R.id.imv_wxlogin);
        mlay_qqlogin = (RelativeLayout) findViewById(R.id.imv_qqlogin);
        mlay_qqlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGINTYPE = 2;
//                String isclear = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("isQQClear", "");
//                LogUtils.d("isclear---" + isclear);
//                if ("2".equals(isclear) || "".equals(isclear)) {
                if (!mTencent.isSessionValid()) {
                    //注销登录 mTencent.logout(this);
                    mTencent.login(LoginActivity.this, "all", new BaseUiListener());
                } else {
                    doAppQQLogin();
                }
//                } else {
//                    doAppQQLogin();
//                }
            }
        });

        mlay_wxlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String isclear = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("isWXClear", "");
//                LogUtils.d("isclear---" + isclear);
//                if ("1".equals(isclear) || "".equals(isclear)) {
                LogUtils.d("微信登录-----------");
                LOGINTYPE = 1;
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";//snsapi_userinfo 用户授权。
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                req.state = "wechat_sdk_微信登录";
                api.sendReq(req);
//                } else {
//                    String wx_openid = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_openid", "");
//                    String wx_access_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_access_token", "");
//                    String wx_refresh_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_refresh_token", "");
//                    doAppWXLogin(wx_openid, wx_access_token, wx_refresh_token);
//                }
            }
        });
    }


    private void initData() {
        mContext = getApplication();
        ProgressBarUtils.startLoad("正在登录推送服务器...", LoginActivity.this);
        serviceManager = new ServiceManager(mContext);
        serviceManager.setNotificationIcon(R.drawable.ic_launcher);
        serviceManager.startService();
        XmppManager.setCallBack(LoginActivity.this);
    }

    @Override
    public void getPnusername(String username) {
        ProgressBarUtils.UpdateMsg("初始化第三方登录...");
        SharedPreferencesUtils.getInstance(mContext, "androidpn").put("pnusername", username);
        String isQQclear = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("isQQClear", "");
//        if ("2".equals(isQQclear) || "".equals(isQQclear)) {
        mTencent = Tencent.createInstance(Config.QQ_LOGIN_APP_ID, this.getApplicationContext());
//        }
        String isWXclear = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("isWXClear", "");
//        if ("1".equals(isWXclear) || "".equals(isWXclear)) {
        api = WXAPIFactory.createWXAPI(LoginActivity.this, APP_ID_WX, true);
        api.registerApp(APP_ID_WX);
//        }
        ProgressBarUtils.stopLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LOGINTYPE == 1) {
            String wx_openid = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_openid", "");
            String wx_access_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_access_token", "");
            String wx_refresh_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_refresh_token", "");
            doWXUpdataToken(new String[]{wx_openid, wx_access_token, wx_refresh_token});
//            doAppWXLogin(wx_openid, wx_access_token, wx_refresh_token);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        logout();
    }

    private void doAppQQLogin() {
        ProgressBarUtils.startLoad("登录中...", LoginActivity.this);
        QQLoginManager.getInstance(getApplication()).dologin(new LoginCallback() {
            @Override
            public void Callback(LoginOutPut bean, String msg) {
                Message message = new Message();
                if (bean == null) {
                    message.what = 1;
                    message.obj = "请求成功，但服务器返回空";
                } else {
                    updateUserInfo(bean.getNickname(), bean.getIcon(), bean.getId());
                    SharedPreferencesUtils.getInstance(mContext, "userinfo").put("isQQClear", String.valueOf(bean.getClearType()));
                    message.what = 0;
                    message.obj = msg;
                }
                mhandle.sendMessage(message);
            }
        });
    }


    private void doAppWXLogin(String openid, String access_token, String refresh_token) {
        Message msg = new Message();
        msg.what = 3;
        mhandle.sendMessage(msg);
        WXLoginManager.getInstance(getApplication()).dologin(openid, access_token, refresh_token, new LoginCallback() {
            @Override
            public void Callback(LoginOutPut bean, String msg) {
                Message message = new Message();
                if (bean == null) {
                    message.what = 1;
                    message.obj = "请求成功，但服务器返回空";
                } else {
                    LogUtils.d("bean.tostring----" + bean.toString());
                    updateUserInfo(bean.getNickname(), bean.getIcon(), bean.getId());
                    SharedPreferencesUtils.getInstance(mContext, "userinfo").put("isWXClear", String.valueOf(bean.getClearType()));
                    message.what = 0;
                    message.obj = msg;
                }
                mhandle.sendMessage(message);
            }
        });
    }

    private void updateToken(String openId, String oldToken, String token, final String type) {
        OKHttpManager.getInstance(mContext).getResponse("user/update_token.do", gson.toJson(new UpdateTokenBean(openId, oldToken, token, type)), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean == null) {
                    message.what = 1;
                    message.obj = "请求成功，但服务器返回空";
                } else {
                    if ("000000".equals(bean.getErrorCode())) {
                        if ("2".equals(type)) {
                            message.what = 6;
                        } else {
                            message.what = 7;
                        }
                        message.obj = bean.getMessage();
                    } else {
                        message.what = 1;
                        message.obj = bean.getMessage();
                    }
                }
                mhandle.sendMessage(message);
            }
        });
    }

    private void updateUserInfo(String nickname, String icon, String id) {
        if (nickname != null) {
            SharedPreferencesUtils.getInstance(mContext, "userinfo").put("username", nickname);
        } else {
            SharedPreferencesUtils.getInstance(mContext, "userinfo").put("username", "");
        }
        if (icon != null) {
            SharedPreferencesUtils.getInstance(mContext, "userinfo").put("usericon", icon);
        } else {
            SharedPreferencesUtils.getInstance(mContext, "userinfo").put("usericon", "");
        }
        if (id != null) {
            SharedPreferencesUtils.getInstance(mContext, "userinfo").put("userid", id);
        } else {
            SharedPreferencesUtils.getInstance(mContext, "userinfo").put("userid", "");
        }
    }


    class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            LogUtils.d("授权:" + o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
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
                SharedPreferencesUtils.getInstance(getApplication(), "logininfo").put("qq_openid", openId);
                SharedPreferencesUtils.getInstance(getApplication(), "logininfo").put("qq_token", token);
                getQQUserInfo();
            }
        } catch (Exception e) {
        }
    }

    private void getQQUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 2;
                    mhandle.sendMessage(msg);
                }

                @Override
                public void onCancel() {
                    LogUtils.d("登录取消..");
                }
            };
            userInfo = new UserInfo(this, mTencent.getQQToken());
            userInfo.getUserInfo(listener);
        }
    }


    private void doWXUpdataToken(String[] openIdAndtoken) {
        String oldToken = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_access_token", "");
        if (oldToken != null) {
            if (!"".equals(oldToken) && !openIdAndtoken[1].equals(oldToken)) {
                updateToken(openIdAndtoken[0], oldToken, openIdAndtoken[1], "1");
            } else {
                doAppWXLogin(openIdAndtoken[0], openIdAndtoken[1], openIdAndtoken[2]);
            }
        } else {
            doAppWXLogin(openIdAndtoken[0], openIdAndtoken[1], openIdAndtoken[2]);
        }
    }

    private void doQQUpdateToken(String[] openIdAndtoken) {
        //检查更新token
        String oldToken = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("qq_token", "");
        if (oldToken != null) {
            if (!"".equals(oldToken) && !openIdAndtoken[1].equals(oldToken)) {
                updateToken(openIdAndtoken[0], oldToken, openIdAndtoken[1], "2");
            } else {
                getQQUserInfo();
            }
        } else {
            getQQUserInfo();
        }
    }


    Handler mhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ProgressBarUtils.stopLoad();
                    ToastUtil.showToast(getApplication(), msg.obj.toString());
                    IntentUtil.getInstance().showIntent(LoginActivity.this, MainActivity2.class);
                    break;
                case 1:
                    ProgressBarUtils.stopLoad();
                    ToastUtil.showToast(getApplication(), msg.obj.toString());
                    break;
                case 2:
                    JSONObject response = JSONObject.parseObject(String.valueOf(msg.obj));
                    QQUser user = JSONObject.parseObject(response.toJSONString(), QQUser.class);
                    if (user != null) {
                        LogUtils.d("userInfo:昵称：" + user.getNickname() + "  性别:" + user.getGender() + "  地址：" + user.getProvince() + user.getCity());
                        LogUtils.d("头像路径：" + user.getFigureurl_qq_2());
                        doAppQQLogin();
                    }
                    break;
                case 3:
                    ProgressBarUtils.startLoad("登录中...", LoginActivity.this);
                    break;
                case 4:
                    SharedPreferencesUtils.getInstance(mContext, "userinfo").put("ischeck", "1");
                    doQQUpdateToken(msg.obj.toString().split("&"));
                    break;
                case 6:
                    ToastUtil.showToast(getApplication(), msg.obj.toString());
                    getQQUserInfo();
                    break;
                case 7:
                    ToastUtil.showToast(getApplication(), msg.obj.toString());
                    String wx_openid = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_openid", "");
                    String wx_access_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_access_token", "");
                    String wx_refresh_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("wx_refresh_token", "");
                    doAppWXLogin(wx_openid, wx_access_token, wx_refresh_token);
                    break;
            }
        }
    };


    public LoginCallback callback;

    public interface LoginCallback {
        void Callback(LoginOutPut bean, String message);
    }


}


