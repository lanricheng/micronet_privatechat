package com.vdunpay.vchat.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vdunpay.utils.DateUtils;
import com.vdunpay.utils.IntentUtil;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.chat.ChatFragment;
import com.vdunpay.vchat.chat.ScrollItemListView;
import com.vdunpay.vchat.chat.bean.ChatItemBean;
import com.vdunpay.vchat.chat.chatinterface.DeleteChatItemListener;
import com.vdunpay.vchat.chatting.ChattingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by HY on 2018/8/16.
 */

public class ChatListAdapter extends BaseAdapter {
    private Activity mContext;
    private List<ChatItemBean> mlist = new ArrayList<>();
    private static DeleteChatItemListener listener;

    public  ChatListAdapter(Activity context, List<ChatItemBean> list) {
        mContext = context;
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
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        final  ChatItemBean bean = mlist.get(position);
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.rlName = (RelativeLayout) convertView.findViewById(R.id.rl_name);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvNews = (TextView) convertView.findViewById(R.id.tv_news);
            viewHolder.imvusericon = (ImageView) convertView.findViewById(R.id.imv_usericon);
            viewHolder.tvshowdate = (TextView) convertView.findViewById(R.id.tv_showdate);

            new QBadgeView(mContext).bindTarget(viewHolder.imvusericon).setBadgeNumber(12);

            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            viewHolder.rlName.setTag(ScrollItemListView.NOR_DELETE_TAG);
            viewHolder.btnDelete.setTag(ScrollItemListView.DELETE_TAG);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        Toast.makeText(getActivity(), "onClick", Toast.LENGTH_LONG).show();in
                    Map<String,String> map = new HashMap<>();
                    map.put("username",bean.getFriendUsername());
                    map.put("usericon",bean.getFriendIcon());
                    map.put("userid",bean.getFriendId());
                    IntentUtil.getInstance().showIntent(mContext, ChattingActivity.class,map);
                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tvName.setText(bean.getFriendUsername());
        viewHolder.tvNews.setText("你最帅");
        final View itemView = convertView;
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, bean.getFriendUsername()+ " delete", Toast.LENGTH_SHORT).show();
                mlist.remove(position);
                listener.ItemDeleteListener(position,itemView,viewGroup);
            }
        });


        String date =  DateUtils.formatDataForDisplay("2018-08-10 10:23:56");
        viewHolder.tvshowdate.setText(date+"");
        return convertView;
    }


    private class ViewHolder {
        RelativeLayout rlName;
        TextView tvName;
        TextView tvNews, tvshowdate;
        Button btnDelete;
        ImageView imvusericon;
    }


    public static void setDeleteChatItemListener(DeleteChatItemListener itemListener){
        listener = itemListener;
    }

}
