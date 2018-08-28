package com.vdunpay.vchat.chatting.bean;

/**
 * Created by HY on 2018/8/16.
 */

public class SendMsgBean {
    private String id;
    private String sessionMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionMessage() {
        return sessionMessage;
    }

    public void setSessionMessage(String sessionMessage) {
        this.sessionMessage = sessionMessage;
    }

    public SendMsgBean(String id, String sessionMessage) {
        this.id = id;
        this.sessionMessage = sessionMessage;
    }
}
