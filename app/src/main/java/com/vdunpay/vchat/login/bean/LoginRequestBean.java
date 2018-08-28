package com.vdunpay.vchat.login.bean;

/**
 * Created by HY on 2018/8/13.
 */

public class LoginRequestBean {

    private String phoneNo;
    private String vCardNum;
    private String password;
    private String pnusername;
    private String openId;
    private String token;
    private String qqOpenId;
    private String qqtoken;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getvCardNum() {
        return vCardNum;
    }

    public void setvCardNum(String vCardNum) {
        this.vCardNum = vCardNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPnusername() {
        return pnusername;
    }

    public void setPnusername(String pnusername) {
        this.pnusername = pnusername;
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

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getQqtoken() {
        return qqtoken;
    }

    public void setQqtoken(String qqtoken) {
        this.qqtoken = qqtoken;
    }

    @Override
    public String toString() {
        return "LoginRequestBean{" +
                "phoneNo='" + phoneNo + '\'' +
                ", vCardNum='" + vCardNum + '\'' +
                ", password='" + password + '\'' +
                ", pnusername='" + pnusername + '\'' +
                ", openId='" + openId + '\'' +
                ", token='" + token + '\'' +
                ", qqOpenId='" + qqOpenId + '\'' +
                ", qqtoken='" + qqtoken + '\'' +
                '}';
    }
}
