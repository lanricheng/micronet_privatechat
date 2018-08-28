package com.vdunpay.vchat.login.bean;

/**
 * Created by HY on 2018/8/21.
 */

public class UpdateTokenBean {
    private String openId;
    private String tokenOld;
    private String token;
    private String interfaceType;


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTokenOld() {
        return tokenOld;
    }

    public void setTokenOld(String tokenOld) {
        this.tokenOld = tokenOld;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public UpdateTokenBean(String openId, String tokenOld, String token, String interfaceType) {
        this.openId = openId;
        this.tokenOld = tokenOld;
        this.token = token;
        this.interfaceType = interfaceType;
    }
}
