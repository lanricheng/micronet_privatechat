package com.vdunpay.vchat.chatting.bean;

import java.util.Date;

/**
 * Created by HY on 2018/8/16.
 */

public class ChatMsgBean {
    private String id;
    private String idB;
    private String nickname;
    private String sessionMessage;
    private String createDate;
    private String friendicon;
    private int flag;

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

    public String getSessionMessage() {
        return sessionMessage;
    }

    public void setSessionMessage(String sessionMessage) {
        this.sessionMessage = sessionMessage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getIdB() {
        return idB;
    }

    public void setIdB(String idB) {
        this.idB = idB;
    }

    public String getFriendicon() {
        return friendicon;
    }

    public void setFriendicon(String friendicon) {
        this.friendicon = friendicon;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
