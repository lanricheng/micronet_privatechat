package com.vdunpay.okhttp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.google.gson.Gson;
import com.vdunpay.okhttp.cookie.CookiesManager;
import com.vdunpay.utils.LogUtils;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HY on 2018/2/6.
 */

public class OKHttp extends AppCompatActivity {
    private final String TAG = "OKHttp";
    private static Gson gson = new Gson();
    private static Context mContext;
    public static OKHttp okhttpInstance;// 192.168.31.169  192.168.10.129   192.168.10.170
    public static String URL_HEAD = "http://192.168.10.129/micronetCount/";

    public OKHttp(Context context) {
        this.mContext = context;
    }

    public static OKHttp getInstance(Context context) {
        mContext = context;
        if (okhttpInstance == null) {
            synchronized (OKHttp.class) {
                if (okhttpInstance == null) {
                    okhttpInstance = new OKHttp(context);
                }
            }
        }
        return okhttpInstance;
    }

    public  void ClientOKHttpToServer( final  String url, final Callbacks callbacks) {
       final String newurl = URL_HEAD + url;
        LogUtils.d("url--" +newurl );
        Thread thread = new Thread(){
            public  void run(){
                try {
                    OkHttpClient client = new OkHttpClient
                            .Builder()
                            .connectTimeout(8, TimeUnit.SECONDS)//连接超时设置
                            .writeTimeout(8, TimeUnit.SECONDS)//写入超时设置，
                            .readTimeout(8, TimeUnit.SECONDS)//读取超时设置
//                                .addInterceptor(getIns())//拦截器
                            .cookieJar(CookiesManager.getInstance(mContext))
                            .build();


                    Request request = new Request.Builder()
                            .url(newurl)
                            .build();
                    //异步，需要设置一个回调接口
                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            callbacks.onFailure(call,e);
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                            callbacks.onResponse(call, response.body().string());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public  void ClientOKHttpToServer(final  String url, final String json, final Callbacks callbacks) {
        final String newurl = URL_HEAD + url;
        LogUtils.d("url--" +newurl );
        Thread thread = new Thread(){
            public  void run(){
                try {
                    OkHttpClient client = new OkHttpClient
                            .Builder()
                            .connectTimeout(8, TimeUnit.SECONDS)//连接超时设置
                            .writeTimeout(8, TimeUnit.SECONDS)//写入超时设置，
                            .readTimeout(8, TimeUnit.SECONDS)//读取超时设置
//                                .addInterceptor(getIns())//拦截器
                            .cookieJar(CookiesManager.getInstance(mContext))
                            .build();

//                        OkHttpClient client = new OkHttpClient();
                    //MediaType  设置Content-Type 标头中包含的媒体类型值
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                            , json);

                    FormBody formBody = new FormBody.Builder()
                            .add("data", json)
                            .build();


                    Request request = new Request.Builder()
                            .url(newurl)
                            .post(requestBody)
                            .build();
                    //异步，需要设置一个回调接口
                    client.newCall(request).enqueue(new Callback() {

                        @Override public void onFailure(Call call, IOException e) {
                            LogUtils.d("请求失败" );
                            callbacks.onFailure(call,e);
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
//                            if (!response.isSuccessful()) {
//                                callbacks.onResponse(call, "Unexpected code " + response);
//                            }
                            LogUtils.d("请求成功" );
                            callbacks.onResponse(call, response.body().string());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }



    /**
     * Synchronous Get（同步Get）
     * */
    public static void Synchronous(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Headers responseHeaders = response.headers();
//        for (int i = 0; i < responseHeaders.size(); i++) {
//            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//        }

        System.out.println(response.body().string());
    }




    /**
     * Asynchronous Get（异步Get）
     * */
    public static void Asynchronous() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        //异步，需要设置一个回调接口
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        });
    }


    public interface Callbacks {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, String responseToString);
    }

}
