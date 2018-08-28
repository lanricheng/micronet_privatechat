package com.vdunpay.vchat.newfriend.bean;

/**
 * Created by HY on 2018/8/9.
 */

public class GetAddFriendRequestBean {
    private String id;
    private String name;
    private String phoneNo;
    private String createDate;
    private String vCardNum;
    private int state;//0发送请求，待确认，1 表示已经确定,2 表示已忽略， 3 拒绝添加
    private String userIcon;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getvCardNum() {
        return vCardNum;
    }

    public void setvCardNum(String vCardNum) {
        this.vCardNum = vCardNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
