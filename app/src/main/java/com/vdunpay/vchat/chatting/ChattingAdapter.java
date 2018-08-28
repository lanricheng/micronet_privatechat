package com.vdunpay.vchat.chatting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vdunpay.utils.ImageUtils;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.utils.StringUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.login.LoginActivity;
import com.vdunpay.vchat.R;
import com.vdunpay.utils.ScreenUtils;
import com.vdunpay.vchat.chatting.bean.ChatMsgBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sisyphus on 2017/3/1.
 */
public class ChattingAdapter extends RecyclerView.Adapter {

    private Context context;

    private static final int ME = 0;
    private static final int OTHRE = 1;

    private List<ChatMsgBean> list = new ArrayList<>();

    public ChattingAdapter(Context context, ArrayList<ChatMsgBean> list) {
        this.context = context;
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout me;
        LinearLayout other;
        RelativeLayout rtl_me;
        RelativeLayout rtl_other;
        TextView mtv_othersendtime,mtv_mesendtime;
        ImageView mimv_myicon,mimv_friendicon;

        public ViewHolder(View itemView) {
            super(itemView);
            me = (LinearLayout) itemView.findViewById(R.id.me);
            other = (LinearLayout) itemView.findViewById(R.id.other);
            rtl_me = (RelativeLayout) itemView.findViewById(R.id.rtl_me);
            rtl_other = (RelativeLayout) itemView.findViewById(R.id.rtl_other);
            mtv_othersendtime = (TextView) itemView.findViewById(R.id.tv_othersendtime);
            mtv_mesendtime = (TextView) itemView.findViewById(R.id.tv_mesendtime);
            mimv_myicon = (ImageView) itemView.findViewById(R.id.imv_myicon);
            mimv_friendicon = (ImageView) itemView.findViewById(R.id.imv_friendicon);

            int screenwidth = ScreenUtils.getScreenWidth(context);
            if (rtl_me != null) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rtl_me.getLayoutParams();
                params.width =4*screenwidth/5 ;
                rtl_me.setLayoutParams(params);
            }

            if (rtl_other != null) {
                LinearLayout.LayoutParams otherparams = (LinearLayout.LayoutParams) rtl_other.getLayoutParams();
                otherparams.width = 4*screenwidth /5;
                rtl_other.setLayoutParams(otherparams);
            }


        }

        public LinearLayout getMe() {
            return me;
        }

        public LinearLayout getOther() {
            return other;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case ME:
                String myicon = (String) SharedPreferencesUtils.getInstance(context,"userinfo").getSharedPreference("usericon","");
                viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item2, parent, false));
                viewHolder.mimv_myicon.setImageBitmap(ImageUtils.base64ToBitmap(myicon));
                break;
            case OTHRE:
                viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false));
                byte[] data =  StringUtils.hexToBytes(ChattingActivity.friendicon);
                String base64 = new String(Base64.encode(data, Base64.DEFAULT));
                viewHolder.mimv_friendicon.setImageBitmap(ImageUtils.base64ToBitmap(base64));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        TextView tv = new TextView(context);
        tv.setTextColor(context.getResources().getColor(R.color.black));
        ChatMsgBean msg = list.get(position);

        tv.setText(msg.getSessionMessage());
        tv.setTextSize(tv.getTextSize() / (context.getResources().getDisplayMetrics().density) + 2);
        tv.setAutoLinkMask(Linkify.ALL);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        String myid = (String) SharedPreferencesUtils.getInstance(context,"userinfo").getSharedPreference("userid","");
        if (myid.equals(msg.getId())){
            if (msg.getFlag() == 0){
                viewHolder.mtv_mesendtime.setVisibility(View.VISIBLE);
                viewHolder.mtv_mesendtime.setText(msg.getCreateDate());
            }else {
                viewHolder.mtv_mesendtime.setVisibility(View.INVISIBLE);
            }
            viewHolder.getMe().removeAllViews();
            viewHolder.getMe().addView(tv);
        }else {
            if (msg.getFlag() == 0){
                viewHolder.mtv_othersendtime.setVisibility(View.VISIBLE);
                viewHolder.mtv_othersendtime.setText(msg.getCreateDate());
            }else {
                viewHolder.mtv_othersendtime.setVisibility(View.INVISIBLE);
            }
            viewHolder.getOther().removeAllViews();
            viewHolder.getOther().addView(tv);
        }


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context,"我被点击了");
            }
        });

//        switch (.MY_USERNAME) {
//            case ME:
//                /**
//                 * 当recyclerview承载的数据过多的时候，去滑动recyclerview，
//                 * 划出屏幕以外的item会重新绑定数据，如果这个时候绑定数据的方式是
//                 * viewgroup的addView（）方法的话，会出现item添加很多重复的view
//                 * 所以这之前应该执行清除里面view的操作，即removeAllViews（）
//                 */
//                viewHolder.getMe().removeAllViews();
////                tv.setBackgroundResource(R.color.light_blue);
//
//                viewHolder.getMe().addView(tv);
//                break;
//            case OTHRE:
//                viewHolder.getOther().removeAllViews();
////                tv.setBackgroundResource(R.color.zzyellow);
//                viewHolder.getOther().addView(tv);
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        String myid = (String) SharedPreferencesUtils.getInstance(context,"userinfo").getSharedPreference("userid","");
        if (myid.equals(list.get(position).getId())){
          return ME;
        }else {
            return OTHRE;
        }
    }

    public void addMsg(ChatMsgBean msg) {
        list.add(msg);
    }
}
