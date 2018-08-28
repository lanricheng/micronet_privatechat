package com.vdunpay.vchat.contact.bean;

import android.support.annotation.NonNull;

import com.vdunpay.vchat.contact.Cn2Spell;

/**
 * Created by HY on 2018/8/9.
 */

public class ContactListBean implements Comparable<ContactListBean>{
    private String id;
    private String phoneNo;
    private String createDate;
    private String vCardNum;
    private String updatgeDate;
    private int state;//好友状态,0表示已经发起申请，1表示已经是好友关系
    private String userIcon;
    private String nickname;
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUpdatgeDate() {
        return updatgeDate;
    }

    public void setUpdatgeDate(String updatgeDate) {
        this.updatgeDate = updatgeDate;
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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }


    public ContactListBean(String id, String phoneNo, String createDate, String vCardNum, String updatgeDate, int state, String userIcon, String nickname) {
        this.id = id;
        this.phoneNo = phoneNo;
        this.createDate = createDate;
        this.vCardNum = vCardNum;
        this.updatgeDate = updatgeDate;
        this.state = state;
        this.userIcon = userIcon;
        this.nickname = nickname;

        pinyin = Cn2Spell.getPinYin(nickname); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    @Override
    public int compareTo(@NonNull ContactListBean another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }

    public ContactListBean() {
       super();
    }
}
