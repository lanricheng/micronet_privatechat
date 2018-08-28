package com.vdunpay.okhttp;

import android.content.Context;

import com.google.gson.Gson;
import com.vdunpay.utils.LogUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;


/**
 * Created by HY on 2018/6/15.
 */

public class OKHttpManager {
    private static Context mContext;
    public static OKHttpManager manager = null;
    private Gson gson = new Gson();
    public static  OKHttpManager getInstance(Context context){
        if (manager == null){
            manager = new OKHttpManager();
        }
        mContext = context;
        return manager;
    }


    public  void getResponse(String url,final String postdata,final OKHttpManagerCallBack CallBack){
        managerCallBack = CallBack;

        OKHttp.getInstance(mContext).ClientOKHttpToServer(url,postdata, new OKHttp.Callbacks() {
            @Override
            public void onFailure(Call call, IOException e) {
                 ResponeBean bean = new ResponeBean();
                if(e instanceof SocketTimeoutException){//判断超时异常
                      bean.setFlag(false);
                      bean.setErrorCode("201");
                      bean.setMessage("连接超时");
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    bean.setFlag(false);
                    bean.setErrorCode("202");
                    bean.setMessage("连接异常");
                }
                managerCallBack.callback(bean);
            }

            @Override
            public void onResponse(Call call, String responseToString) {
                LogUtils.d("请求成功，数据"+responseToString );
                ResponeBean bean  = gson.fromJson(responseToString,ResponeBean.class);
                managerCallBack.callback(bean);
            }
        });

    }


    public void getResponse(String url, final  OKHttpManagerCallBack managerCallBack){
        final ResponeBean bean = new ResponeBean();
        OKHttp.getInstance(mContext).ClientOKHttpToServer(url, new OKHttp.Callbacks() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponeBean bean = new ResponeBean();
                if(e instanceof SocketTimeoutException){//判断超时异常
                    bean.setFlag(false);
                    bean.setErrorCode("201");
                    bean.setMessage("连接超时");
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    bean.setFlag(false);
                    bean.setErrorCode("202");
                    bean.setMessage("连接异常");
                }
                managerCallBack.callback(bean);
            }

            @Override
            public void onResponse(Call call, String responseToString) {
                ResponeBean bean  = gson.fromJson(responseToString,ResponeBean.class);
                managerCallBack.callback(bean);
            }
        });

    }

    private OKHttpManagerCallBack managerCallBack;
    public interface OKHttpManagerCallBack{
        void callback(ResponeBean bean);
    }


}
