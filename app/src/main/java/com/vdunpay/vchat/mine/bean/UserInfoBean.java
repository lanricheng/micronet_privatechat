package com.vdunpay.vchat.mine.bean;

/**
 * Created by HY on 2018/8/14.
 */

public class UserInfoBean {

    //手机号
    private String phoneNo;

    //V盾编号
    private String vCardNum;

    //登录密码
    private String password;

    private String nickname;

    private String userIcon;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
