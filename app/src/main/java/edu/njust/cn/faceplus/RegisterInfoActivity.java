package edu.njust.cn.faceplus;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterInfoActivity extends AppCompatActivity {
    public static final int Take_photo = 1;
    public static  final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Uri imageUri;
    private String mTempPhotoPath;
    private Button btn_takephoto;
    private Button btn_chooseFromAlbum;
    private EditText edt_name;
    private EditText edt_nickname;
    private EditText edt_school;
    private EditText edt_birth;
    private RadioGroup rg_sex;
    private RadioGroup rg_identity;
    private RadioButton rbtn_sex;
    private RadioButton rbtn_identity;
    private Button btn_save;
    private String name;
    private String nickname;
    private String school;
    private String sex;
    private String birth;
    private String identity;
    //拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    public final static int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_info);
        edt_name=findViewById(R.id.edt_name);
        edt_nickname=findViewById(R.id.edt_nickname);
        edt_birth=findViewById(R.id.edt_birth);
        edt_school=findViewById(R.id.edt_school);
        rg_sex=findViewById(R.id.sex);
        rg_identity=findViewById(R.id.identity);
        btn_save=findViewById(R.id.btn_save);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectSex();
            }
        });
        rg_identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectIdentity();
            }
        });
        btn_takephoto = (Button) findViewById(R.id.take_photo);
        btn_chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        btn_chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**if (ContextCompat.checkSelfPermission(RegisterInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {
                    openAlbum();
                }*/
                if (ContextCompat.checkSelfPermission(RegisterInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(RegisterInfoActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

                }else { //权限已经被授予，在这里直接写要执行的相应方法即可
                    Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                    // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
                    intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
                    startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
                }
            }
        });
        btn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(RegisterInfoActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

                }else { //权限已经被授予，在这里直接写要执行的相应方法即可
                    Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 指定照片存储位置为sd卡本目录下
                    // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
                    // File.separator为系统自带的分隔符 是一个固定的常量
                    mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
                    // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
                    /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
                    imageUri = FileProvider.getUriForFile(RegisterInfoActivity.this,
                            RegisterInfoActivity.this.getApplicationContext().getPackageName() + ".provider",
                            new File(mTempPhotoPath));
                    //下面这句指定调用相机拍照后的照片存储的路径
                    intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=edt_name.getText().toString().trim();
                nickname=edt_nickname.getText().toString().trim();
                birth=edt_birth.getText().toString().trim();
                school=edt_school.getText().toString().trim();
                if (StringUtil.isEmpty(name)||StringUtil.isEmpty(birth)||StringUtil.isEmpty(school)){
                    Toast.makeText(RegisterInfoActivity.this,"姓名或生日或学校不能为空",Toast.LENGTH_LONG).show();
                }
                if(StringUtil.isEmpty(nickname)){
                    nickname=name;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=new Message();
                        if(RegisterService.registerInfoByPost(name,nickname,birth,sex,school)=="OK"){
                            if (identity=="老师"){
                                message.what=2;
                                handler.sendMessage(message);
                            }
                            else{
                                message.what=1;
                                handler.sendMessage(message);
                            }
                        }
                        else{
                            message.what=0;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("images/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }
    public void onRequestPermissionResult(int requestCode,String[] permissions,int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else
                {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      picture = findViewById(R.id.upload_img);
      if (resultCode == RegisterInfoActivity.RESULT_OK) {
          switch (requestCode) {
              case CAMERA_REQUEST_CODE: {
                  // 获得图片
                  try {
                      //该uri就是照片文件夹对应的uri
                      Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                      // 给相应的ImageView设置图片 未裁剪
                      picture.setImageBitmap(bit);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  break;
              }
              case GALLERY_REQUEST_CODE: {
                  // 获取图片
                  try {
                      //该uri是上一个Activity返回的
                      imageUri = data.getData();
                      if(imageUri!=null) {
                          Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                          Log.i("bit", String.valueOf(bit));
                          picture.setImageBitmap(bit);
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  break;
              }
          }
      }
      super.onActivityResult(requestCode, resultCode, data);
  }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == CAMERA_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定照片存储位置为sd卡本目录下
                // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
                // File.separator为系统自带的分隔符 是一个固定的常量
                mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
                // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
                /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
                imageUri = FileProvider.getUriForFile(RegisterInfoActivity.this,
                        RegisterInfoActivity.this.getApplicationContext().getPackageName() +".provider",
                        new File(mTempPhotoPath));
                //下面这句指定调用相机拍照后的照片存储的路径
                intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);

            } else
            {
                // Permission Denied
                Toast.makeText(RegisterInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
                intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
                startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
            } else
            {
                // Permission Denied
                Toast.makeText(RegisterInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectSex(){
        rbtn_sex=findViewById(rg_sex.getCheckedRadioButtonId());
        sex=rbtn_sex.getText().toString();
    }
    private void selectIdentity(){
        rbtn_identity=findViewById(rg_identity.getCheckedRadioButtonId());
        identity=rbtn_identity.getText().toString();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    Intent intent=new Intent(RegisterInfoActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent1=new Intent(RegisterInfoActivity.this,StudentActivity.class);
                    startActivity(intent1);
                case 0:
                    Toast.makeText(RegisterInfoActivity.this,"添加个人信息失败",Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };
}


