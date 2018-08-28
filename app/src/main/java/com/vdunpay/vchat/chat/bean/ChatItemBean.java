package com.vdunpay.vchat.chat.bean;

/**
 * Created by HY on 2018/8/16.
 */

public class ChatItemBean {
    private String friendId;
    private String friendUsername;
    private String friendIcon;
    private String sessionMessage;
    private String unReanMsg;
    private String date;

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendIcon() {
        return friendIcon;
    }

    public void setFriendIcon(String friendIcon) {
        this.friendIcon = friendIcon;
    }

    public String getSessionMessage() {
        return sessionMessage;
    }

    public void setSessionMessage(String sessionMessage) {
        this.sessionMessage = sessionMessage;
    }

    public String getUnReanMsg() {
        return unReanMsg;
    }

    public void setUnReanMsg(String unReanMsg) {
        this.unReanMsg = unReanMsg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ChatItemBean(String friendId, String friendUsername, String friendIcon) {
        this.friendId = friendId;
        this.friendUsername = friendUsername;
        this.friendIcon = friendIcon;
    }

    public ChatItemBean() {
        super();
    }
}
