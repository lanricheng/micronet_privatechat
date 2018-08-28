package com.vdunpay.vchat.login.bean;

/**
 * Created by HY on 2018/8/13.
 */

public class LoginOutPut {

    private String id;
    private String nickname;
    private String iconUrl;
    private String icon;
    private String state;
    private int clearType;//0 表示不清除 1 代表是微信关联登录 2 代表是QQ关联登录

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getClearType() {
        return clearType;
    }

    public void setClearType(int clearType) {
        this.clearType = clearType;
    }


    @Override
    public String toString() {
        return "LoginOutPut{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", icon='" + icon + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
