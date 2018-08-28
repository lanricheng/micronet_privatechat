package com.vdunpay.gesturelock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.leo.gesturelibrary.enums.LockMode;
import com.leo.gesturelibrary.view.CustomLockView;
import com.vdunpay.gesturelock.Contants;
import com.vdunpay.gesturelock.base.BaseActivity;
import com.vdunpay.gesturelock.util.PasswordUtil;
import com.vdunpay.vchat.R;

import static com.leo.gesturelibrary.enums.LockMode.CLEAR_PASSWORD;
import static com.leo.gesturelibrary.enums.LockMode.SETTING_PASSWORD;

public class LockActivity extends BaseActivity implements View.OnClickListener{
    TextView mtvText,mrv_back;
    CustomLockView mlvLock;
    TextView mtvHint;

    @Override
    public void beforeInitView() {
        setContentView(R.layout.activity_lock);
    }

    /**
     * 初始化View
     */
    @Override
    public void initView() {
        mrv_back = (TextView) findViewById(R.id.rv_back);
        mtvText = (TextView) findViewById(R.id.tv_text);
        mtvHint = (TextView) findViewById(R.id.tv_hint);
        mlvLock = (CustomLockView) findViewById(R.id.lv_lock);
        //显示绘制方向
        mlvLock.setShow(true);
        //允许最大输入次数
        mlvLock.setErrorNumber(3);
        //密码最少位数
        mlvLock.setPasswordMinLength(4);
        //编辑密码或设置密码时，是否将密码保存到本地，配合setSaveLockKey使用
        mlvLock.setSavePin(true);
        //保存密码Key
        mlvLock.setSaveLockKey(Contants.PASS_KEY);
    }

    /**
     * 设置监听回调
     */
    @Override
    public void initListener() {
        mlvLock.setOnCompleteListener(onCompleteListener);
        mrv_back.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //设置模式
        LockMode lockMode = (LockMode) getIntent().getSerializableExtra(Contants.INTENT_SECONDACTIVITY_KEY);
        setLockMode(lockMode);
    }


    /**
     * 密码输入模式
     */
    private void setLockMode(LockMode mode, String password, String msg) {
        mlvLock.setMode(mode);                              //密码设置模式
        mlvLock.setErrorNumber(3);                          //密码最小位数
        mlvLock.setClearPasssword(false);                   //是否立即清楚密码
        if (mode != SETTING_PASSWORD) {                                 //如果不是设置模式
            mtvHint.setText("请输入已经设置过的密码");
            mlvLock.setOldPassword(password);
        } else {
            mtvHint.setText("请输入要设置的密码");
        }
        mtvText.setText(msg);
    }


    /**
     * 密码输入监听
     */
    CustomLockView.OnCompleteListener onCompleteListener = new CustomLockView.OnCompleteListener() {
        @Override
        public void onComplete(String password, int[] indexs) {
            mtvHint.setText(getPassWordHint());

            Intent intent = new Intent();
            intent.putExtra("data_return","密码验证正确返回值");
            setResult(RESULT_OK,intent);

            finish();
        }

        @Override
        public void onError(String errorTimes) {
            mtvHint.setText("密码错误，还可以输入" + errorTimes + "次");
        }

        @Override
        public void onPasswordIsShort(int passwordMinLength) {
            mtvHint.setText("密码不能少于" + passwordMinLength + "个点");
        }

        @Override
        public void onAginInputPassword(LockMode mode, String password, int[] indexs) {
            mtvHint.setText("请再次输入密码");
        }


        @Override
        public void onInputNewPassword() {
            mtvHint.setText("请输入新密码");
        }

        @Override
        public void onEnteredPasswordsDiffer() {
            mtvHint.setText("两次输入的密码不一致");
        }

        @Override
        public void onErrorNumberMany() {
            mtvHint.setText("密码错误次数超过限制，不能再输入");
        }

    };


    /**
     * 密码相关操作完成回调提示
     */
    private String getPassWordHint() {
        String str = null;
        switch (mlvLock.getMode()) {
            case SETTING_PASSWORD:
                str = "密码设置成功";
                break;
            case EDIT_PASSWORD:
                str = "密码修改成功";
                break;
            case VERIFY_PASSWORD:
                str = "密码正确";
                break;
            case CLEAR_PASSWORD:
                str = "密码已经清除";
                break;
        }
        return str;
    }

    /**
     * 设置解锁模式
     */
    private void setLockMode(LockMode mode) {
        String str = "";
        switch (mode) {
            case CLEAR_PASSWORD:
                str = "清除密码";
                setLockMode(CLEAR_PASSWORD, PasswordUtil.getPin(this), str);
                break;
            case EDIT_PASSWORD:
                str = "修改密码";
                setLockMode(LockMode.EDIT_PASSWORD, PasswordUtil.getPin(this), str);
                break;
            case SETTING_PASSWORD:
                str = "设置密码";
                setLockMode(SETTING_PASSWORD, null, str);
                break;
            case VERIFY_PASSWORD:
                str = "验证密码";
                setLockMode(LockMode.VERIFY_PASSWORD, PasswordUtil.getPin(this), str);
                break;
        }
        mtvText.setText(str);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public void onComplete(RippleView rippleView) {
//        onBackPressed();
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return","密码验证正确返回值");
        setResult(RESULT_OK,intent);
        finish();
    }

}
