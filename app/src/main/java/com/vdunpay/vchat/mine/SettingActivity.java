package com.vdunpay.vchat.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vdunpay.okhttp.OKHttpManager;
import com.vdunpay.okhttp.ResponeBean;
import com.vdunpay.utils.LogUtils;
import com.vdunpay.utils.PhotoUtils;
import com.vdunpay.utils.SharedPreferencesUtils;
import com.vdunpay.vchat.mine.bean.UserInfoBean;
import com.vdunpay.utils.ImageUtils;
import com.vdunpay.utils.ToastUtil;
import com.vdunpay.vchat.R;
import com.vdunpay.vchat.dialog.DialogUtil;
import com.vdunpay.vchat.mine.mineinterface.UserinfoCallback;
import com.vdunpay.vchat.mine.bottomdialog.MyDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,
        MyDialog.OnButtonClickListener {
    private LinearLayout mlay_usericon,mlay_username,mlay_userqrcore;
    private ImageView mimv_back,mimv_userinfoicon;
    private MyDialog dialog;// 图片选择对话框
    private EditText medt_myusername;
    private TextView mtv_save;//mtv_myid,
    private Context mContext;
    private Gson gson = new Gson();
    private static String imagebase64 ;
    private static UserinfoCallback Callback;
    private static Boolean flag = false;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = getApplication();
        initView();
        initDate();
    }

    private void initView() {
        mlay_usericon = (LinearLayout) findViewById(R.id.lay_usericon);
        mlay_username = (LinearLayout) findViewById(R.id.lay_username);
        mlay_userqrcore = (LinearLayout) findViewById(R.id.lay_userqrcore);
        mimv_back = (ImageView) findViewById(R.id.imv_userinfiback);
        mimv_userinfoicon= (ImageView) findViewById(R.id.imv_userinfoicon);
        medt_myusername= (EditText) findViewById(R.id.edt_myusername);
//        mtv_myid = (TextView) findViewById(R.id.tv_myid);
        mtv_save = (TextView) findViewById(R.id.tv_save);
        medt_myusername.setEnabled(false);

        mtv_save.setOnClickListener(this);
        mlay_usericon.setOnClickListener(this);
        mlay_username.setOnClickListener(this);
        medt_myusername.setOnClickListener(this);
        mlay_userqrcore.setOnClickListener(this);
        mimv_back.setOnClickListener(this);

        dialog = new MyDialog(this);
        dialog.setOnButtonClickListener(this);

    }



    private void initDate() {
        flag = false;
//        String icondata = getIntent().getStringExtra("icon");
//        String username = getIntent().getStringExtra("username");
        String username = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("username", "");
        String usericon = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("usericon", "");
        if (check(usericon,username) == true){
            Bitmap addbmp = ImageUtils.base64ToBitmap(usericon);
            mimv_userinfoicon.setImageBitmap(addbmp);
            medt_myusername.setText(username);
        }
//        mtv_myid.setText(id);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lay_usericon:
                dialog.show();
                break;
            case R.id.lay_username:
                medt_myusername.setEnabled(true);
                break;
            case R.id.edt_myusername:
                medt_myusername.setEnabled(true);
                break;
            case R.id.lay_userqrcore:
                String userid = (String) SharedPreferencesUtils.getInstance(mContext, "userinfo").getSharedPreference("userid", "");
                if (userid == null){
                    ToastUtil.showToast(mContext,"用户id为空");
                    return;
                }
                if (userid.length()==0){
                    ToastUtil.showToast(mContext,"用户id为空");
                    return;
                }
                String qrmsg = "micronet&"+medt_myusername.getText().toString()+"&"+userid;
                DialogUtil.showqrcore(SettingActivity.this,qrmsg);
                break;
            case R.id.imv_userinfiback:
                 if (flag = true){
                    Callback.UserIconAndUsernameCallback();
                 }
                onBackPressed();
                finish();
                break;
                case R.id.tv_save:
                    upLoadUserInfo();
                    break;
        }
    }

    private void upLoadUserInfo() {
      final  String username = medt_myusername.getText().toString();
        if (username == null){
            ToastUtil.showToast(mContext,"昵称不能为空");
            return;
        }
        if ("".equals(username)){
            ToastUtil.showToast(mContext,"昵称不能为空");
            return;
        }
        UserInfoBean bean = new UserInfoBean();
        bean.setNickname(username);
        bean.setUserIcon(imagebase64);
        OKHttpManager.getInstance(mContext).getResponse("user/infoAdd.do", gson.toJson(bean), new OKHttpManager.OKHttpManagerCallBack() {
            @Override
            public void callback(ResponeBean bean) {
                Message message = new Message();
                if ("000000".equals(bean.getErrorCode())){
                    SharedPreferencesUtils.getInstance(mContext,"userinfo").put("username",username);
                    SharedPreferencesUtils.getInstance(mContext,"userinfo").put("usericon",imagebase64);
                    message.what = 0;
                    message.obj = bean.getMessage();
                }else {
                    message.what = 1;
                    message.obj = bean.getMessage();
                }
                mhandle.sendMessage(message);
            }
        });
    }



    @Override
    public void camera() {
        autoObtainCameraPermission();
    }

    @Override
    public void gallery() {
        autoObtainStoragePermission();
    }

    @Override
    public void cancel() {
        dialog.cancel();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
            dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.vdunpay.vchat", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtil.showToast(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        imagebase64 = ImageUtils.bitmapToBase64(bitmap);
                        mimv_userinfoicon.setImageBitmap(bitmap);
                    }
                    break;
                default:
            }
        }
    }


    private boolean check(String icondata, String username) {
        if (icondata == null){
//            ToastUtil.showToast(mContext,"头像数据为空，初始化失败");
            return false;
        }
        if ("".equals(icondata)){
//            ToastUtil.showToast(mContext,"头像数据为空，初始化失败");
            return false;
        }
        if (username == null){
//            ToastUtil.showToast(mContext,"好友昵称为空，初始化失败");
            return false;
        }
        if ("".equals(username)){
//            ToastUtil.showToast(mContext,"好友昵称为空，初始化失败");
            return false;
        }
        return true;
    }


    Handler mhandle = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    ToastUtil.showToast(mContext,msg.obj.toString());
                    flag =true;
//                    SharedPreferencesUtils.getInstance(mContext,"userinfo").put("isave","true");
//                    SharedPreferencesUtils.getInstance(mContext,"userinfo").put("username",mtv_myusername.getText().toString());
                    break;
                case 1:
                    ToastUtil.showToast(mContext,msg.obj.toString());
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag = true){
            Callback.UserIconAndUsernameCallback();
        }
        finish();
    }

    public static void getUserInfo(UserinfoCallback mycallback) {
        Callback = mycallback;
    }


    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtil.showToast(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(SettingActivity.this, "com.vdunpay.vchat", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.showToast(this, "设备没有SD卡！");
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(SettingActivity.this, "com.vdunpay.vchat", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showToast(this, "设备没有SD卡！");
                    }
                } else {
                    ToastUtil.showToast(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtil.showToast(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
