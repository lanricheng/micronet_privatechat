package com.vdunpay.okhttp.cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by HY on 2018/8/13.
 */

public class CookiesManager implements CookieJar {

    private static Context mContext;

    private  static  CookiesManager manager = null;
    public  static  CookiesManager getInstance(Context context){
        mContext = context;
        if (manager == null){
            manager = new CookiesManager();
        }
        return manager;
    }


    private final PersistentCookieStore cookieStore = new PersistentCookieStore(mContext);

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }


}
