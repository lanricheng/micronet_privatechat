package com.vdunpay.application;

import android.app.Application;
import com.vdunpay.db.DataBaseOpenHelper;
import com.vdunpay.notificationserver.ServiceManager;
import com.vdunpay.vchat.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by HY on 2018/8/17.
 */

public class MyApplication extends Application{
    public final static  String DB_NAME="vchat.db";
    public final static int DB_VERSION = 1;
    public final static List<String> tableSql= new ArrayList<>();

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        String table1="create table chathistory(userid varchar(20) not null ," +
                " username varchar(20)  not null," +
                " usericon varchar(1000) not null" +
                ");";
        String table2="create table userinfo(userid varchar(20) not null ," +
                " username varchar(20)  not null," +
                " usericon varchar(1000) not null" +
                ");";
//        " datetime varchar(20) not null" +
        tableSql.add(table1);
        DataBaseOpenHelper.getInstance(getApplicationContext(),DB_NAME,DB_VERSION,tableSql);

//        new Thread(){
//            @Override
//            public void run() {
//                //启动消息推送
//                ServiceManager serviceManager = new ServiceManager(getApplicationContext());
//                serviceManager.setNotificationIcon(R.drawable.ic_launcher);
//                serviceManager.startService();
//            }
//        }.start();

        initTest();
    }

    private void initTest() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }
}
