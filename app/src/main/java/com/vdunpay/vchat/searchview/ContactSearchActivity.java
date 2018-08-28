package com.vdunpay.vchat.searchview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vdunpay.vchat.searchview.adapter.SearchAdapter;
import com.vdunpay.utils.StatusBarUtils;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.chatting.ChattingActivity;
import com.vdunpay.vchat.contact.bean.User;
import com.vdunpay.utils.IntentUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactSearchActivity extends AppCompatActivity {
    private final static int NoThing = 0;
    private final static int Show = 1;
    private final static int Cant_find = 2;

    private EditText editMobile;
    private RelativeLayout rlCantFind;
    private TextView mtv_showsearchtip;
    private List<User> list = new ArrayList<>();
    private ListView mylv_search;
    private SearchAdapter myadapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_search);
        mContext = getApplication();
        StatusBarUtils.setActivityTranslucent(this);
        StatusBarUtils.setWindowStatusBarColor(ContactSearchActivity.this,getResources().getColor(R.color.black));

        initViews();
        initData();
    }

    public void initViews() {
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mylv_search = (ListView) findViewById(R.id.lv_search);
        rlCantFind = (RelativeLayout) findViewById(R.id.rl_cant_find_over);
        editMobile = (EditText) findViewById(R.id.edit_search);
        editMobile.addTextChangedListener(textWatcher);
        mtv_showsearchtip = (TextView) findViewById(R.id.tv_showsearchtip);

    }

    public void initData() {

        list = new ArrayList<>();
        list.add(new User("亳州")); // 亳[bó]属于不常见的二级汉字
        list.add(new User("大娃"));
        list.add(new User("二娃"));
        list.add(new User("三娃"));
        list.add(new User("四娃"));
        list.add(new User("五娃"));
        list.add(new User("六娃"));
        list.add(new User("七娃"));
        list.add(new User("喜羊羊"));
        list.add(new User("美羊羊"));
        list.add(new User("懒羊羊"));
        list.add(new User("沸羊羊"));
        list.add(new User("暖羊羊"));
        list.add(new User("慢羊羊"));
        list.add(new User("灰太狼"));
        list.add(new User("红太狼"));
        list.add(new User("孙悟空"));
        list.add(new User("黑猫警长"));
        list.add(new User("舒克"));
        list.add(new User("贝塔"));
        list.add(new User("海尔"));
        list.add(new User("阿凡提"));
        list.add(new User("邋遢大王"));
        list.add(new User("哪吒"));
        list.add(new User("没头脑"));
        list.add(new User("不高兴"));
        list.add(new User("蓝皮鼠"));
        list.add(new User("大脸猫"));
        list.add(new User("大头儿子"));
        list.add(new User("小头爸爸"));
        list.add(new User("蓝猫"));
        list.add(new User("淘气"));
        list.add(new User("叶峰"));
        list.add(new User("楚天歌"));
        list.add(new User("江流儿"));
        list.add(new User("Tom"));
        list.add(new User("Jerry"));
        list.add(new User("12345"));
        list.add(new User("54321"));
        list.add(new User("_(:з」∠)_"));
        list.add(new User("……%￥#￥%#"));
        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        myadapter = new SearchAdapter(mContext, list);
        mylv_search.setAdapter(myadapter);

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            myadapter.getFilter().filter(charSequence);
            if (!TextUtils.isEmpty(charSequence)) {
                if (charSequence.length() == 0) {
                    changeStates(Cant_find);
                } else {
                    changeStates(Show);
                }
                showCustomer(list);
            } else {
                changeStates(NoThing);
            }
        }


        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void changeStates(int states) {

        switch (states) {
            case NoThing:
                rlCantFind.setVisibility(View.VISIBLE);
                mylv_search.setVisibility(View.INVISIBLE);
                break;
            case Show:
                rlCantFind.setVisibility(View.INVISIBLE);
                mylv_search.setVisibility(View.VISIBLE);
                break;
            case Cant_find:
                rlCantFind.setVisibility(View.VISIBLE);
                mylv_search.setVisibility(View.INVISIBLE);
                break;


        }

    }

    public void showCustomer(List<User> list) {
        if (list == null || list.size() == 0){
            return;
        }
        myadapter.notifyDataSetChanged();
        mylv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user =(User)parent.getAdapter().getItem(position);
                IntentUtil.getInstance().showIntent(ContactSearchActivity.this, ChattingActivity.class,"username",user.getName());
                finish();
            }
        });
    }
}
