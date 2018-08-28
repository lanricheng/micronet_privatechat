package com.vdunpay.vchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.gesturelibrary.enums.LockMode;
import com.leo.gesturelibrary.util.StringUtils;
import com.vdunpay.gesturelock.Contants;
import com.vdunpay.gesturelock.activity.LockActivity;
import com.vdunpay.gesturelock.activity.LockTestActivity;
import com.vdunpay.gesturelock.util.ActivityLifecycleCallbacksUtil;
import com.vdunpay.gesturelock.util.PasswordUtil;
import com.vdunpay.vchat.chat.ChatFragment;
import com.vdunpay.vchat.contact.ContactFragment;
import com.vdunpay.vchat.mine.MineFragment;
import com.vdunpay.vchat.newfriend.ValidateNewFriend;
import com.vdunpay.vchat.searchview.ContactSearchActivity;
import com.vdunpay.qrcore.QRcoreinterface;
import com.vdunpay.qrcore.WeChatCaptureActivity;
import com.vdunpay.utils.IntentUtil;
import com.vdunpay.utils.StatusBarUtils;
import com.vdunpay.utils.ToastUtil;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener, QRcoreinterface {
    private BottomNavigationView bottomNavigationView;
    private ChatFragment fragment1;
    private ContactFragment fragment2;
    private MineFragment fragment3;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment
    private Button mbtn_add, mbtn_search;
    private QRcoreinterface qRcoreinterface = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        StatusBarUtils.setActivityTranslucent(this);
        StatusBarUtils.setWindowStatusBarColor(MainActivity2.this, getResources().getColor(R.color.black));
        initFragment();

        ActivityLifecycleCallbacksUtil.init(getApplication());

        ActivityLifecycleCallbacksUtil.get().addListener(new ActivityLifecycleCallbacksUtil.Listener() {

            @Override
            public void onBecameForeground() {
                Log.d(">>>>>>>>>>>>>>>>", "当前程序切换到前台");

                boolean IsMainActivity = true;

                TextView tv = findViewById(R.id.text_textview);
                if(tv.getText().toString().contains("密码验证正确返回值")){
                    IsMainActivity=true;
                }else{
                    IsMainActivity=false;
                }

                Log.d(">>>>>>>>>>>>>>>>", "IsMainActivity:  "+IsMainActivity);

                if ( IsMainActivity) {

                    tv.setText("");

                    Intent intent = new Intent(MainActivity2.this, LockActivity.class);
                    intent.putExtra(Contants.INTENT_SECONDACTIVITY_KEY, LockMode.VERIFY_PASSWORD);
                    //    startActivity(intent);
                    startActivityForResult(intent,1);

                }
            }

            @Override
            public void onBecameBackground() {
                Log.d(">>>>>>>>>>>>>>>>", "当前程序切换到后台");
            }
        });

    }


    //初始化fragment和fragment数组
    private void initFragment() {
        mbtn_add = findViewById(R.id.btn_add);
        mbtn_search = findViewById(R.id.btn_search);
        fragment1 = new ChatFragment();
        fragment2 = new ContactFragment();
        fragment3 = new MineFragment();
        fragments = new Fragment[]{fragment1, fragment2, fragment3};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragment1).show(fragment1).commit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);


        //获取整个的NavigationView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        //这里就是获取所添加的每一个Tab(或者叫menu)，
        View tab = menuView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;

        //加载我们的角标View，新创建的一个布局
        View badge = LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false);
        TextView count = (TextView) badge.findViewById(R.id.tv_msg_count);
        //count.setText(String.valueOf(99)+"+");
        QBadgeView qBadgeView = new QBadgeView(this);
        qBadgeView.bindTarget(count).setBadgeNumber(12);
        qBadgeView.setBadgeGravity(Gravity.CENTER);
        qBadgeView.setGravityOffset(12, 2, true);
        //如果没有消息，不需要显示的时候那只需要将它隐藏即可
        //count.setVisibility(View.GONE);
        //添加到Tab上
        itemView.addView(badge);


//        int screenheight = ScreenUtils.getScreenHeight(getApplication());
//        android.view.ViewGroup.LayoutParams params = bottomNavigationView.getLayoutParams();
//        BottomNavigationViewHeight = params.height;


        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
        mbtn_add.setOnClickListener(this);
        mbtn_search.setOnClickListener(this);

    }


    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_chat: {
                    if (lastfragment != 0) {
                        fragment1 = new ChatFragment();
                        fragments = new Fragment[]{fragment1, fragment2, fragment3};
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    return true;
                }
                case R.id.navigation_contact: {
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                }
                case R.id.navigation_mine: {
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;

                    }

                    return true;
                }
            }
            return false;
        }
    };

    //切换Fragment
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.mainview, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                showPopupMenu(mbtn_add);
                break;
            case R.id.btn_search:
                IntentUtil.getInstance().showIntent(MainActivity2.this, ContactSearchActivity.class);
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(Button view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (qRcoreinterface == null) {
                    qRcoreinterface = MainActivity2.this;
                }
                WeChatCaptureActivity.setinterfaceContext(qRcoreinterface);
                Intent intent = new Intent(MainActivity2.this, WeChatCaptureActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        popupMenu.show();
    }


    @Override
    public void getQRcoreData(String str) {
        if (str == null || "".equals(str)) {
            ToastUtil.showToast(getApplication(), "扫描结果为空，请重新扫描。");
            return;
        }
        IntentUtil.getInstance().showIntent(MainActivity2.this, ValidateNewFriend.class, "qrcore", str);
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
                TextView tv = findViewById(R.id.text_textview);
                tv.setText(data.getStringExtra("data_return"));
            }
        }
    }

}