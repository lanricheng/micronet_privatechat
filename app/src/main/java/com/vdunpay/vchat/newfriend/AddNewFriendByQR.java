package com.vdunpay.vchat.newfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.R;

public class AddNewFriendByQR extends AppCompatActivity implements View.OnClickListener{
   private TextView mtv_friendnicheng,mtv_friendid,mtv_input,mtv_finish;
   private EditText medit_newfriendremark;
   private ImageView mimg_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend_by_qr);
        mtv_finish = (TextView) findViewById(R.id.tv_finish);
        mtv_friendnicheng = (TextView) findViewById(R.id.tv_friendnicheng);
        mtv_friendid = (TextView) findViewById(R.id.tv_friendid);
        mtv_input = (TextView) findViewById(R.id.tv_input);
        medit_newfriendremark = (EditText) findViewById(R.id.edit_newfriendremark);
        mimg_delete = (ImageView) findViewById(R.id.img_delete);

        mtv_finish.setOnClickListener(this);
        mimg_delete.setOnClickListener(this);

        String UsernameAndId = getIntent().getStringExtra("qrcore");
        String[] group = UsernameAndId.split("&");
        mtv_friendnicheng.setText(group[1]);
        mtv_friendid.setText(group[2]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_finish:

                break;
            case R.id.img_delete:

                break;
        }
    }
}
