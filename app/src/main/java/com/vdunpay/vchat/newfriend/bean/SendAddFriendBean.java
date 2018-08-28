package com.vdunpay.vchat.newfriend.bean;

/**
 * Created by HY on 2018/8/15.
 */

public class SendAddFriendBean {

    private String id;
    private String remark;
    private String message;//验证消息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
