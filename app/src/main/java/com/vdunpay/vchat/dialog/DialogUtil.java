package com.vdunpay.vchat.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vdunpay.vchat.R;
import com.vdunpay.qrcore.ZXingUtils;
import com.vdunpay.utils.ScreenUtils;

/**
 * Created by HY on 2018/6/6.
 */

public class DialogUtil extends Activity{


    public static void showqrcore(final  Context context,String value) {
        Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_showcore, null);
        ImageView content = v.findViewById(R.id.imv_showcore);
        TextView username = v.findViewById(R.id.tv_username);
        TextView mtv_myid = v.findViewById(R.id.tv_myid);

        String[] group = value.split("&");
        username.setText(group[1]);
        mtv_myid.setText(group[2]);
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

         ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
         params.width =4*screenWidth/5;
         params.height =1*screenHeight/2-150;

        Bitmap bitmap = ZXingUtils.createQRImage(value, 4*screenWidth/5, 4*screenWidth/5);
        content.setImageBitmap(bitmap);
        content.setLayoutParams(params);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
    }

    public static void showPubKey(final Context context,String value) {
       final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_showpubkey, null);
        TextView content = v.findViewById(R.id.tv_showpubkey);
        Button mbtn_pubok = v.findViewById(R.id.btn_pubok);
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

  //      ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
  //      params.width =4*screenWidth/5;
 //       params.height =1*screenHeight/5;

  //      content.setText(value);
  //      content.setLayoutParams(params);
  //      int screenWidth = ScreenUtils.getScreenWidth(context);
  //      int screenHeight = ScreenUtils.getScreenHeight(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置

        mbtn_pubok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /***
     * 第一次绑定qq
     * */
    public static void showQQBind(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_showqqnewbind, null);
        EditText mqqnumber = v.findViewById(R.id.edt_qqnumber);
        EditText mreqqnumber = v.findViewById(R.id.edt_reqqnumber);
        Button mbtn_bingqqok = v.findViewById(R.id.btn_bingqqok);


        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
//        params.width =4*screenWidth/5;
//        params.height =1*screenHeight/3;

//        content.setLayoutParams(params);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        mbtn_bingqqok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
            }
        });
    }

    /**
     * 绑定新的qq号
     * */
   public static void showChangeQQBind(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_showqqmessage, null);        //R.layout.dialog_showqqbindedit
  //      EditText oldqqNumber = v.findViewById(R.id.edt_oldqqnumber);
  //      EditText qqNewNumber = v.findViewById(R.id.edt_newqqnumber);
  //      EditText reNewqqNumber = v.findViewById(R.id.edt_newreqqnumber);
        Button ok = v.findViewById(R.id.btn_bindqqok);
        Button cancel = v.findViewById(R.id.btn_bindqqcancel);

        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

 //      RelativeLayout Add_Account = v.findViewById(R.id.dialog_showqqbindedit_Add_Account);
  //     Add_Account.setOnClickListener(new View.OnClickListener() {
  //         @Override
  //         public void onClick(View view) {

 //          }

  //     });

//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
//        params.width =4*screenWidth/5;
//        params.height =1*screenHeight/3;

//        content.setLayoutParams(params);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置

   //    ok.setOnClickListener(new View.OnClickListener() {
   //        @Override
    //       public void onClick(View v) {
//
    //       }
    //   });

     //  cancel.setOnClickListener(new View.OnClickListener() {
    //       @Override
   //        public void onClick(View v) {
   //           dialog.dismiss();
    //       }
    //   });

    }


    /***
     * 第一次绑定wx
     * */
    public static void showWXBind(final Context context) {
        Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_showwxmessage, null);               //R.layout.dialog_showwxnewbind
     //   EditText mwxnumber = v.findViewById(R.id.edt_wxnumber);
     //   EditText mrewxnumber = v.findViewById(R.id.edt_rewxnumber);
    //    Button mbtn_wxbindok = v.findViewById(R.id.btn_wxbindok);


        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
//        params.width =4*screenWidth/5;
//        params.height =1*screenHeight/3;

//        content.setLayoutParams(params);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        //mbtn_wxbindok.setOnClickListener(new View.OnClickListener() {
        //    @Override
       //     public void onClick(View v) {

       //     }
       // });
    }


    /**
     * 绑定新的wx号
     * */
    public static void showChangeWXBind(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_showwxbindedit, null);
   //    EditText oldwxNumber = v.findViewById(R.id.edt_oldqqnumber);
   //     EditText wxNewNumber = v.findViewById(R.id.edt_newqqnumber);
   //     EditText reNewwxNumber = v.findViewById(R.id.edt_newreqqnumber);
        Button ok = v.findViewById(R.id.btn_bindwxok);
        Button cancel = v.findViewById(R.id.btn_bindwxcancel);

        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
//        params.width =4*screenWidth/5;
//        params.height =1*screenHeight/3;

//        content.setLayoutParams(params);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 添加好友
     * */
    public static void addFriend(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_addfriend, null);
        EditText friendid = v.findViewById(R.id.edt_friendid);
        EditText username = v.findViewById(R.id.edt_friendusername);
        Button ok = v.findViewById(R.id.btn_addok);
        Button cancel = v.findViewById(R.id.btn_addcancel);

        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) content.getLayoutParams();
//        params.width =4*screenWidth/5;
//        params.height =1*screenHeight/3;

//        content.setLayoutParams(params);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);//点击dialog外面不会消失
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
