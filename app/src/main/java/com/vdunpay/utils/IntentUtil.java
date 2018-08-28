package com.vdunpay.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by HY on 2018/5/18.
 */

public class IntentUtil extends Activity{

    private static IntentUtil intentUtil = null;



    public static IntentUtil getInstance(){

        if (intentUtil == null){
            intentUtil = new IntentUtil();
        }
     return intentUtil;
    }

    public void showIntent(Activity context, Class<?> clzz, Map<String,String> values){
        Intent intent = new Intent(context, clzz);
        Iterator<Map.Entry<String, String>> entries = values.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        context.startActivity(intent);
    }

    public void showIntent(Activity context, Class<?> clzz, String[] keys, String[] values){
        Intent intent = new Intent(context, clzz);
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                if (!TextUtils.isEmpty(keys[i]) && !TextUtils.isEmpty(values[i])) {
                    intent.putExtra(keys[i], values[i]);
                }
            }
        }
        context.startActivity(intent);
    }


    public void showIntent(Activity context, Class<?> clzz,String key,String values){
        Intent intent = new Intent(context, clzz);
        if (values != null && values.length() > 0) {
                    intent.putExtra(key,values);
            }
        context.startActivity(intent);
    }

    public void showIntent(Activity context, Class<?> clzz){
        Intent intent = new Intent(context, clzz);
        context.startActivity(intent);
    }

}
