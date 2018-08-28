package com.vdunpay.vchat.mine.mineinterface;

import com.vdunpay.vchat.login.bean.LoginOutPut;

/**
 * Created by HY on 2018/8/15.
 */

public interface UserinfoCallback {
        void UserInfoCallback(LoginOutPut outPut, String logintype);
        void UserIconAndUsernameCallback();
}
