package com.vdunpay.gesturelock.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.gesturelibrary.enums.LockMode;
import com.leo.gesturelibrary.util.StringUtils;
import com.vdunpay.gesturelock.Contants;
import com.vdunpay.gesturelock.base.BaseActivity;
import com.vdunpay.gesturelock.util.ActivityLifecycleCallbacksUtil;
import com.vdunpay.gesturelock.util.PasswordUtil;
import com.vdunpay.vchat.R;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * Created by leo on 16/4/5.
 * 主activity
 */
public class LockTestActivity extends BaseActivity implements View.OnClickListener {
    private Button mrvSetting;
    private Button mrvEdit;
    private Button mrvVerify;
    private Button mrvClear;

    private IntentFilter intentFilter;
    private ScreenBroadcast mScreenReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);

        mScreenReceiver = new ScreenBroadcast();
        registerReceiver(mScreenReceiver,intentFilter);

        ActivityLifecycleCallbacksUtil.init(getApplication());

        ActivityLifecycleCallbacksUtil.get().addListener(new ActivityLifecycleCallbacksUtil.Listener() {

                @Override
                public void onBecameForeground() {
                    Log.d(">>>>>>>>>>>>>>>>", "当前程序切换到前台");

//                    boolean IsMainActivity = true;

//                    TextView tv = findViewById(R.id.text_textview);
//                    if(tv.getText().toString().contains("密码验证正确返回值")){
//                        IsMainActivity=true;
//                    }else{
//                        IsMainActivity=false;
//                    }

 //                   Log.d(">>>>>>>>>>>>>>>>", "IsMainActivity:  "+IsMainActivity);

//                    if ( IsMainActivity) {
//                        actionSecondActivity(LockMode.VERIFY_PASSWORD);
//                        TextView tv1 = findViewById(R.id.text_textview);
//                        tv1.setText("");
//                    }
                }

                @Override
                public void onBecameBackground() {
                    Log.d(">>>>>>>>>>>>>>>>", "当前程序切换到后台");
                }
            });
        }

    @Override
    public void beforeInitView() {
        setContentView(R.layout.activity_locktest);
    }

    @Override
    public void initView() {
        mrvClear = (Button) findViewById(R.id.rv_clear);
        mrvEdit = (Button) findViewById(R.id.rv_edit);
        mrvSetting = (Button) findViewById(R.id.rv_setting);
        mrvVerify = (Button) findViewById(R.id.rv_verify);
    }

    @Override
    public void initListener() {
        mrvClear.setOnClickListener(this);
        mrvEdit.setOnClickListener(this);
        mrvSetting.setOnClickListener(this);
        mrvVerify.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_clear:
                actionSecondActivity(LockMode.CLEAR_PASSWORD);
                break;
            case R.id.rv_edit:
                actionSecondActivity(LockMode.EDIT_PASSWORD);
                break;
            case R.id.rv_setting:
                actionSecondActivity(LockMode.SETTING_PASSWORD);
                break;
            case R.id.rv_verify:
                actionSecondActivity(LockMode.VERIFY_PASSWORD);
                break;
        }
    }

    /**
     * 跳转到密码处理界面
     */
    private void actionSecondActivity(LockMode mode) {
        if (mode != LockMode.SETTING_PASSWORD) {                                      //如果不是设置密码模式
            if (StringUtils.isEmpty(PasswordUtil.getPin(this))) {
                Toast.makeText(getBaseContext(), "请先设置密码", Toast.LENGTH_SHORT).show();
                return;
            }
        }

 //       TextView tv = findViewById(R.id.text_textview);
//        tv.setText("");

        Intent intent = new Intent(this, LockActivity.class);
        intent.putExtra(Contants.INTENT_SECONDACTIVITY_KEY, mode);
    //    startActivity(intent);
        startActivityForResult(intent,1);
    }

    /**
     * 取消注册屏幕监控广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
    }

    /**
     * 屏幕监控广播
     */
    class ScreenBroadcast extends BroadcastReceiver {

        private String action = null;

        public void onReceive(Context context, Intent intent) {

            action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                //开屏

            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                // 锁屏

            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {

                // 解锁
            }
        }

    }

    /**
     * 重写onActivityResult方法获取来自下一个Intent的返回值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
//                Toast.makeText(LockTestActivity.this,
//                        data.getStringExtra("data_return"),Toast.LENGTH_SHORT).show();
            //    TextView tv = findViewById(R.id.text_textview);
            //    tv.setText(data.getStringExtra("data_return"));
            }
        }
    }


}
