package com.vdunpay.vchat.login.bean;

/**
 * Created by HY on 2018/8/21.
 */

public class BindUserInfo {
    private String openIdOld;
    private String tokenOld;
    private String tokenTypeOld;
    private String openId;
    private String token;
    private String tokenType;


    public String getOpenIdOld() {
        return openIdOld;
    }

    public void setOpenIdOld(String openIdOld) {
        this.openIdOld = openIdOld;
    }

    public String getTokenOld() {
        return tokenOld;
    }

    public void setTokenOld(String tokenOld) {
        this.tokenOld = tokenOld;
    }

    public String getTokenTypeOld() {
        return tokenTypeOld;
    }

    public void setTokenTypeOld(String tokenTypeOld) {
        this.tokenTypeOld = tokenTypeOld;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public BindUserInfo(String openIdOld, String tokenOld, String tokenTypeOld, String openId, String token, String tokenType) {
        this.openIdOld = openIdOld;
        this.tokenOld = tokenOld;
        this.tokenTypeOld = tokenTypeOld;
        this.openId = openId;
        this.token = token;
        this.tokenType = tokenType;
    }
}
