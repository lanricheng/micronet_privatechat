package com.vdunpay.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.leo.gesturelibrary.util.StringUtils;
import com.vdunpay.vchat.R;

/**
 * Created by HY on 2018/8/14.
 */

public class ProgressBarUtils {

    private static final int START_DIALOG = 0;//开始对话框
    private static final int UPDATE_DIALOG = 1;//更新对话框
    private static final int STOP_DIALOG = 2;//销毁对话框
    private static AlertDialog dialog = null;
    private static TextView title = null;
    private static Context mContext = null;

    private static Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            String message = "";
            switch (msg.what) {
                case START_DIALOG:// 启动加载框
                    message = (String) msg.obj;
//                    if (dialog != null) {
//                        stopLoad();
//                        startLoad(message);
//                        return;
//                    }
                    init(message);
                    isTouchDismiss(true);
                    break;
                case UPDATE_DIALOG:// 更新加载框
                    message = (String) msg.obj;
                    if (title.VISIBLE == View.VISIBLE) {
                        if (StringUtils.isEmpty(message)) {
                            title.setVisibility(View.GONE);
                        } else {
                            title.setText(message);
                        }
                    } else {
                        if (!StringUtils.isEmpty(message)) {
                            title.setText(message);
                            title.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case STOP_DIALOG:// 停止加载框
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog.cancel();
                        dialog = null;
                        title = null;
                    }
                    break;
            }
        };
    };

    /**
     * @方法说明:加载控件与布局
     * @方法名称:init
     * @返回值:void
     */
    private static void init(String mssg) {
//        if (SystemTool.isBackground(context)) {// 如果程序在后台，则不加载
//            return;
//        }

        if (null != mContext) {
            LayoutInflater flat = LayoutInflater.from(mContext);
            View v = flat.inflate(R.layout.loading, null);
            // v.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            // 创建对话
            dialog = new AlertDialog.Builder(mContext, R.style.dialog).create();
            // 设置返回键点击消失对话框
            dialog.setCancelable(true);
            // 设置点击返回框外边不消失
            dialog.setCanceledOnTouchOutside(true);
            // 给该对话框增加系统权限
            // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            // 显示对话
            dialog.show();

            // 加载控件
            title = (TextView) v.findViewById(R.id.loading_title);

            if (StringUtils.isEmpty(mssg)) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
                title.setText(mssg);
            }

            // 必须放到显示对话框下面，否则显示不出效果
            Window window = dialog.getWindow();
            // window.getAttributes().x = 0;
            // window.getAttributes().y = 0;//设置y坐标

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            // params.alpha = 0.6f;
            window.setAttributes(params); // 加载布局组件
            dialog.getWindow().setContentView(v);
        }
    }

    /**
     * @方法说明:启动对话框
     * @方法名称:startLoad
     * @param msg
     * @返回值:void
     */
    public static void startLoad(String msg ,Context context) {
//        if (context == null) {
//            return;
//        }
//        if (SystemTool.isBackground(context)) {// 如果程序在后台，则不加载
//            return;
//        }
         mContext =context;
        Message mssage = new Message();
        mssage.what = START_DIALOG;
        mssage.obj = msg;
        handler.sendMessage(mssage);
    }


    /**
     * @方法说明:更新显示的内容
     * @方法名称:UpdateMsg
     * @param msg
     * @返回值:void
     */
    public static void UpdateMsg(String msg) {
        Message message = new Message();
        message.what = UPDATE_DIALOG;
        message.obj = msg;
        handler.sendMessage(message);
    }

    /**
     * @方法说明:允许加载条转动的时候去点击系统返回键
     * @方法名称:openCancelable
     * @param flag
     * @返回值:void
     */
    public static void openCancelable(boolean flag) {
        if (dialog != null) {
            dialog.setCancelable(flag);
        }
    }

    /**
     * @方法说明:允许点击对话框触摸消失
     * @方法名称:isTouchDismiss
     * @param isdimiss
     * @返回值:void
     */
    public static void isTouchDismiss(boolean isdimiss) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(isdimiss);
        }
    }

    /**
     * @方法说明:让警告框消失
     * @方法名称:dismiss
     * @返回值:void
     */
    public static void stopLoad() {
        handler.sendEmptyMessage(STOP_DIALOG);
    }

}
