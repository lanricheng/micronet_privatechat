package com.vdunpay.vchat.newfriend.bean;

/**
 * Created by HY on 2018/8/14.
 */

public class ValidateBean {

    private String id;
    private String nickname;
    private String userIcon;

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

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    @Override
    public String toString() {
        return "ValidateBean{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", userIcon='" + userIcon + '\'' +
                '}';
    }
}
