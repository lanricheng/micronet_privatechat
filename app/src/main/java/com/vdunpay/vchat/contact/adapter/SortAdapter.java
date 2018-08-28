package com.vdunpay.vchat.contact.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.vdunpay.utils.ImageUtils;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.StringUtils;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.contact.bean.ContactListBean;
import com.vdunpay.vchat.contact.bean.User;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends BaseAdapter implements Filterable {

    private List<ContactListBean> list = new ArrayList<>();
    private Context mContext;
    private ArrayList<ContactListBean> mOriginalValues;
    private ArrayFilter mFilter;

    /**
     * This lock is also used by the filter
     * (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     * 过滤器上的锁可以同步复制原始数据。
     */
    private final Object mLock = new Object();

    public SortAdapter(Context mContext, List<ContactListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {

        LogUtils.d("listsize---"+list.size());
        LogUtils.d("listtostring---"+list.toString());
        //列表第一项
        if (position == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.addfriendtiem, null);
            return view;
        }

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            viewHolder.imv_friendicon = (ImageView) view.findViewById(R.id.imv_friendicon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

         if (viewHolder == null){
             viewHolder = new ViewHolder();
             view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
             viewHolder.name = (TextView) view.findViewById(R.id.name);
             viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
             viewHolder.imv_friendicon = (ImageView) view.findViewById(R.id.imv_friendicon);
             view.setTag(viewHolder);
        }

        LogUtils.d("listsize---"+list.size());
        LogUtils.d("position---"+position);
        final ContactListBean user = list.get(position - 1);
        if (viewHolder !=null){
            //根据position获取首字母作为目录catalog
            String catalog = list.get(position-1).getFirstLetter();
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if ((position-1) == getPositionForSection(catalog)) {
                viewHolder.catalog.setVisibility(View.VISIBLE);
                viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
            } else {
                LogUtils.d("position-----" + position);
                viewHolder.catalog.setVisibility(View.GONE);
            }
        }
        viewHolder.name.setText(this.list.get(position-1).getNickname());
        Bitmap bitmap = getBitmap(list.get(position).getUserIcon());
        viewHolder.imv_friendicon.setImageBitmap(bitmap);
        return view;
    }

    private Bitmap getBitmap(String hex) {
        byte[] data =  StringUtils.hexToBytes(hex);
        String base64 = new String(Base64.encode(data, Base64.DEFAULT));
        Bitmap bitmap =  ImageUtils.base64ToBitmap(base64);
        return bitmap;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    final static class ViewHolder {
        TextView catalog;
        TextView name;
        ImageView imv_friendicon;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
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
                    mOriginalValues = new ArrayList<>(list);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<ContactListBean> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<ContactListBean> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<ContactListBean> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final ContactListBean value = values.get(i);//从List<User>中拿到User对象
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
            list = (List<ContactListBean>) results.values;//此时，Adapter数据源就是过滤后的Results
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

}