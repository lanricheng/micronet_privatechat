package com.vdunpay.vchat.contact;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.ProgressBarUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.chatting.ChattingActivity;
import com.vdunpay.vchat.contact.adapter.SortAdapter;
import com.vdunpay.vchat.contact.bean.ContactListBean;
import com.vdunpay.vchat.contact.bean.User;
import com.vdunpay.vchat.contact.contactinterface.ReLoadContactCallback;
import com.vdunpay.vchat.newfriend.FriendRequestListActivity;
import com.vdunpay.utils.IntentUtil;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.ScreenUtils;
import com.vdunpay.vchat.newfriend.ValidateNewFriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContactFragment extends Fragment implements View.OnClickListener,ReLoadContactCallback {

    private ListView listView;
//    private EditText medt_search;
    private SideBar sideBar;
    private ArrayList<User> listt;
    private ArrayList<ContactListBean> list = new ArrayList<>();;
    private SortAdapter adapter;
    private Context mContext;

    // 1. 初始化搜索框变量
//    private LinearLayout mylayout_newfriend;
    private Gson gson = new Gson();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contact_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext =getActivity();
        initView();
        initData();
    }

    private void initView() {
        listView = (ListView) getActivity().findViewById(R.id.listView);
        sideBar = (SideBar) getActivity().findViewById(R.id.side_bar);
        adapter = new SortAdapter(getActivity(), list);
        listView.setAdapter(adapter);
//        medt_search = (EditText) getActivity().findViewById(R.id.edt_search);
//        mylayout_newfriend = (LinearLayout) getActivity().findViewById(R.id.layout_newfriend);
        int screenheight = ScreenUtils.getScreenHeight(getActivity());
        int statusHeight = ScreenUtils.getStatusHeight(getActivity());
        final android.view.ViewGroup.LayoutParams params = sideBar.getLayoutParams();
        params.height=2*(screenheight-statusHeight)/3+150;

        sideBar.setLayoutParams(params);


        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });



//        mylayout_newfriend.setOnClickListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (position == 0){
                   IntentUtil.getInstance().showIntent(getActivity(), FriendRequestListActivity.class);
               }else {
                   ContactListBean user = (ContactListBean) parent.getAdapter().getItem(position);
                   Map<String,String> map = new HashMap<>();
                   map.put("username",user.getNickname());
                   map.put("usericon",user.getUserIcon());
                   map.put("userid",user.getId());

                   IntentUtil.getInstance().showIntent(getActivity(), ChattingActivity.class,map);
               }
            }
        });

//        medt_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                mAdapter.getFilter().filter(s);
//                adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }


    private void initData() {
        ProgressBarUtils.startLoad("加载中...",getActivity());
         //设置回调
        ValidateNewFriend.TurnBackCallback(ContactFragment.this);
        FriendRequestListActivity.TurnBackCallback(ContactFragment.this);
//        ContactListBean
        OKHttpManager.getInstance(mContext).getResponse("friends/get_friend_list.do", "", new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if (bean != null) {
                    if ("000000".equals(bean.getErrorCode())){
                        message.what = 0;
                        message.obj = bean.getData();
                    }else {
                        message.what = 1;
                        message.obj = bean.getMessage();
                    }
                } else {
                    message.what = 1;
                    message.obj = "请求成功，但数据返回空。";
                }
                handler.sendMessage(message);
            }
        });



//        setListViewHeightBasedOnChildren(listView);

    }

    /*
  * 当ScrollView 与 LiseView 同时滚动计算高度的方法
  * 设置listview 的高度
  * 参数：listivew的findviewbyid
  * */
    public  void setListViewHeightBasedOnChildren(ListView listView) {
        try{
            // 获取ListView对应的Adapter
//            MyAdapter listAdapter = listView.getAdapter();
            if (adapter == null) {
                return;
            }

            int totalHeight = 0;
            for (int i = 0, len = adapter.getCount(); i < len; i++) {
                // listAdapter.getCount()返回数据项的数目
                View listItem = adapter.getView(i, null, listView);
                // 计算子项View 的宽高
                listItem.measure(0, 0);
                // 统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight+ (listView.getDividerHeight() * (adapter.getCount() - 1));
            // listView.getDividerHeight()获取子项间分隔符占用的高度
            // params.height最后得到整个ListView完整显示需要的高度
            listView.setLayoutParams(params);
        }catch (Exception e){
            LogUtils.d("setListViewHeightBasedOnChildren___"+e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_newfriend:
                IntentUtil.getInstance().showIntent(getActivity(), FriendRequestListActivity.class);
                break;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    ToastUtil.showToast(mContext, msg.obj.toString());
                    LogUtils.d("msg-----"+msg.obj.toString());
                     if(!"".equals(msg.obj.toString())){
                         ArrayList<ContactListBean> list = gson.fromJson(msg.obj.toString(),ArrayList.class);
                         LogUtils.d("listtostring-----"+list.toString());
                         listupdate(list);
                     }else {
                         ProgressBarUtils.stopLoad();
                         ContactListBean contactbean = new ContactListBean();
                         list.clear();
                         list.add(contactbean);
                         adapter.notifyDataSetChanged();
                         ToastUtil.showToast(mContext, "你还未添加任何好友");
                     }
                    break;
                case 1:
                    ProgressBarUtils.stopLoad();
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
            }
        }
    };

    private void listupdate(ArrayList<ContactListBean> listbean) {

        if (listbean!=null){
            list.clear();
            ContactListBean bean = null;
            for (int i=0;i<listbean.size();i++){
                Object object = listbean.get(i);
                String ttt = object.toString();
                LogUtils.d("ttt-----"+ttt);
                ContactListBean listBean = gson.fromJson(ttt,ContactListBean.class);
                bean = new ContactListBean(listBean.getId(),listBean.getPhoneNo(),listBean.getCreateDate(),listBean.getvCardNum(),listBean.getUpdatgeDate(),listBean.getState(),listBean.getUserIcon(),listBean.getNickname());
                list.add(bean);
                LogUtils.d("id-----"+listBean.getId());
            }
            if (list.size() == 1){
                list.add(bean);
            }
            // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
            Collections.sort(list);
            adapter.notifyDataSetChanged();
            ProgressBarUtils.stopLoad();
        }else {

        }
    }





    @Override
    public void reloadcontact() {
        initData();
    }

}
