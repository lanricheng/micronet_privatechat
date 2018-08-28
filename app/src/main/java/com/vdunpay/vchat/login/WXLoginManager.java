package com.vdunpay.vchat.login;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.login.base.Config;
import com.vdunpay.vchat.login.bean.LoginRequestBean;
import com.vdunpay.vchat.login.bean.LoginOutPut;


/**
 * @name WXLoginManager
 * @anthor HY
 */

public class WXLoginManager {
    private static Context mContext;
    private static WXLoginManager qqmanager = null;
    private LoginActivity.LoginCallback logincallback =null;
    private Gson gson = new Gson();
    /**
     * 微信登录相关
     */
    private IWXAPI api;

    public static WXLoginManager getInstance(Context context){
        mContext = context;
        if (qqmanager == null){
            qqmanager = new WXLoginManager();
        }
        return qqmanager;
    }


    public void doWXLogin(Activity context){
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(mContext, Config.APP_ID_WX,true);

        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
        req.state = "wechat_sdk_微信登录";
        api.sendReq(req);

    }


    public void dologin(String openid,String access_token ,String refresh_token ,LoginActivity.LoginCallback loginCallback) {
        logincallback = loginCallback;
        String pnusername = (String) SharedPreferencesUtils.getInstance(mContext, "androidpn").getSharedPreference("pnusername", "");
        if (pnusername == null) {
            ToastUtil.showToast(mContext, "pnusername == null");
            return;
        }
        if (pnusername.length() == 0) {
            ToastUtil.showToast(mContext, "pnusername == null");
            return;
        }

        String qq_openid = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("qq_openid", "");
        String qq_token = (String) SharedPreferencesUtils.getInstance(mContext, "logininfo").getSharedPreference("qq_token", "");
        if (qq_openid == null) {
            qq_openid="";
        }
        if (qq_token == null) {
            qq_token = "";
        }
        if (qq_openid == null) {
            qq_openid="";
        }
        if (qq_token == null) {
            qq_token = "";
        }
        LoginRequestBean bean = new LoginRequestBean();
        bean.setPhoneNo("18378339258");
        bean.setvCardNum("9876543210");
        bean.setPassword("654321");
        bean.setPnusername(pnusername);
        bean.setOpenId(openid);
        bean.setToken(access_token);
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
                    message.obj = "数据返回空指针异常";
                }
                mHandler.sendMessage(message);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:

                    break;
                case 3:
                    logincallback.Callback(null,msg.obj.toString());
                    break;
            }
        }
    };





}
