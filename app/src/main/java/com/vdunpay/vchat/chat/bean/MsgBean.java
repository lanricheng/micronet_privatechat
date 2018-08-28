package com.vdunpay.vchat.chat.bean;

/**
 * Created by HY on 2018/8/20.
 */

public class MsgBean {
    private String sender;
    private String time;
    private String msgc;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgc() {
        return msgc;
    }

    public void setMsgc(String msgc) {
        this.msgc = msgc;
    }
}
