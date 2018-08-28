package com.vdunpay.vchat.newfriend.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.ImageUtils;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.StringUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.newfriend.bean.ConfirmBean;
import com.vdunpay.vchat.newfriend.bean.GetAddFriendRequestBean;
import com.vdunpay.vchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HY on 2018/8/9.
 */

public class AddNewFriendAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<GetAddFriendRequestBean> mlist = new ArrayList<>();
    private ArrayList<GetAddFriendRequestBean> mOriginalValues;
    private ArrayFilter mFilter;
    private Gson gson = new Gson();
    private ViewHolder viewHolder;
    /**
     * This lock is also used by the filter
     * (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     * 过滤器上的锁可以同步复制原始数据。
     */
    private final Object mLock = new Object();


    public AddNewFriendAdapter(Context application, List<GetAddFriendRequestBean> list) {
        mContext = application;
        mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        viewHolder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_addnewfrienditem, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.tv_agreeold = (TextView) view.findViewById(R.id.tv_agreeold);
            viewHolder.tv_agreenow = (Button) view.findViewById(R.id.btn_agreenow);
            viewHolder.mimv_friendicon = (ImageView) view.findViewById(R.id.imv_friendicon);
            switch (mlist.get(position).getState()) {
                case 1:
                    viewHolder.tv_agreenow.setVisibility(View.GONE);
                    viewHolder.tv_agreeold.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    viewHolder.tv_agreenow.setVisibility(View.GONE);
                    viewHolder.tv_agreeold.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    viewHolder.tv_agreenow.setVisibility(View.VISIBLE);
                    viewHolder.tv_agreeold.setVisibility(View.GONE);
                    break;
                case 3:
                    viewHolder.tv_agreenow.setVisibility(View.GONE);
                    viewHolder.tv_agreeold.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    viewHolder.tv_agreenow.setVisibility(View.GONE);
                    viewHolder.tv_agreeold.setVisibility(View.VISIBLE);
                    break;
            }
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(mlist.get(position).getNickname());
        String hex = mlist.get(position).getUserIcon();
        byte[] data = StringUtils.hexToBytes(hex);
        String base64 = new String(Base64.encode(data, Base64.DEFAULT));
        Bitmap bitmap = ImageUtils.base64ToBitmap(base64);
        viewHolder.mimv_friendicon.setImageBitmap(bitmap);
        switch (mlist.get(position).getState()) {
            case 0:
                viewHolder.tv_agreeold.setText("");
                break;
            case 3:
                viewHolder.tv_agreeold.setText("未同意");
                break;
            case 1:
                viewHolder.tv_agreeold.setText("已同意");
                break;
            case 2:
                viewHolder.tv_agreeold.setText("已忽略");
                break;
            case 4:
                viewHolder.tv_agreeold.setText("等待验证");
                break;
        }

        viewHolder.tv_agreenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmBean confirmBean = new ConfirmBean();
                confirmBean.setId(mlist.get(position).getId());
                LogUtils.d("id-----" + mlist.get(position).getId());
                OKHttpManager.getInstance(mContext).getResponse("friends/confirm.do", gson.toJson(confirmBean), new OKHttpManager.OKHttpManagerCallBack() {
                    @Override
                    public void callback(ResponeBean bean) {
                        Message message = new Message();
                        if (bean != null) {
                            if ("000000".equals(bean.getErrorCode())) {
                                message.what = 2;
                                message.obj = bean.getMessage();
                            } else {
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
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    static class ViewHolder {
        TextView name, tv_agreeold;
        Button tv_agreenow;
        ImageView mimv_friendicon;
    }


    /**
     * 过滤数据的类
     */
    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     * <p/>
     * 一个带有首字母约束的数组过滤器，每一项不是以该首字母开头的都会被移除该list。
     */
    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mlist);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<GetAddFriendRequestBean> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<GetAddFriendRequestBean> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<GetAddFriendRequestBean> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final GetAddFriendRequestBean value = values.get(i);//从List<User>中拿到User对象
//                    final String valueText = value.toString().toLowerCase();
                    final String valueText = value.getNickname().toString().toLowerCase();//User对象的name属性作为过滤的参数
                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString) || valueText.indexOf(prefixString.toString()) != -1) {//第一个字符是否匹配
                        newValues.add(value);//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            mlist = (List<GetAddFriendRequestBean>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated

                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
                case 1:
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
                case 2:
                    updateUI();
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
            }
        }
    };

    private void updateUI() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewHolder.tv_agreenow.setVisibility(View.GONE);
                viewHolder.tv_agreeold.setVisibility(View.VISIBLE);
                viewHolder.tv_agreeold.setText("已同意");
            }
        }, 0);
    }

}
