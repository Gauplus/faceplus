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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterInfoActivity extends AppCompatActivity {
    public static final int Take_photo = 1;
    public static  final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Uri imageUri;
    private EditText edt_name;
    private EditText edt_nickname;
    private  EditText edt_birth;
    private EditText edt_school;
    private EditText edt_studentId;
    private  RadioButton rbtnMale,rbtnFamale;
    private  RadioButton rbtntech,rbtnstu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_info);
        init();
        Button takephoto = (Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);

        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {
                    openAlbum();
                }
            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(RegisterInfoActivity.this, "edu.njust.cn.faceplus.provider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Take_photo);

            }
        });
    }
    private void init()
    {

        edt_name = findViewById(R.id.edt_name);
        edt_nickname=findViewById(R.id.edt_nickname);
        edt_birth=findViewById(R.id.edt_birth);
        edt_school=findViewById(R.id.edt_school);
        edt_studentId=findViewById(R.id.edt_studentId);
        rbtnMale=findViewById(R.id.rbtn_male);
        rbtnMale=findViewById(R.id.rbtn_female);
        rbtntech=findViewById(R.id.rbtn_teacher);
        rbtnstu=findViewById(R.id.rbtn_student);
        Button edt_register=findViewById(R.id.save_regist_info);

    }
    public void onClick(View view){
        if (view.getId()==R.id.save_regist_info)
        {

            String name=edt_name.getText().toString();
            if(TextUtils.isEmpty(name))
            {
                Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            String nickname=edt_nickname.getText().toString();
            if(TextUtils.isEmpty(nickname))
            {
                Toast.makeText(this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            String birth=edt_birth.getText().toString();
            if(TextUtils.isEmpty(birth))
            {
                Toast.makeText(this,"出生日期不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            String school=edt_school.getText().toString();
            if(TextUtils.isEmpty(school))
            {
                Toast.makeText(this,"学校不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            String studentId=edt_studentId.getText().toString();
            if(TextUtils.isEmpty(studentId))
            {
                Toast.makeText(this,"学号不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            String sex;
            if(rbtnMale.isChecked()){
                sex=rbtnMale.getText().toString();
            }else {
                sex=rbtnFamale.getText().toString();
            }
            String identity;
            if(rbtntech.isChecked())
            {
                identity=rbtntech.getText().toString();
            }else{
                identity=rbtnstu.getText().toString();
            }

        }
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



    protected void  onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case Take_photo:
                if (requestCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19)
                    {
                        //4.4及以上系统是用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);

                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data)
    {
        String imagePath=null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的URI，则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads//public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的URI，直接获取图片路径即可
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private  void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private  String getImagePath(Uri uri,String selection){
        String path=null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private  void displayImage(String imagePath)
    {
        if(imagePath!=null){
            Bitmap bitmap =BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}


