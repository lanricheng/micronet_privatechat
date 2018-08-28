package com.vdunpay.vchat.chat;

/**
 * Created by HY on 2018/8/16.
 */

public class User {


    public User(String name){
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof User && ((User)o).name != null){
            return ((User)o).name.equals(this.name);
        }
        return false;
    }

}
