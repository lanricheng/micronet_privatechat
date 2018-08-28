package com.vdunpay.okhttp;

/**
 * Created by HY on 2018/8/13.
 */

public class ResponeBean {
    private Boolean flag;
    private String message;
    private String errorCode;
    private String data;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "ResponeBean{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
