package edu.njust.cn.faceplus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 显示教师信息的界面
 */
public class InfoPageFragment extends Fragment {
    public static final int Take_photo = 1;
    public static  final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Uri imageUri;
    private String mTempPhotoPath;
    private Button btn_takephoto;
    private Button btn_chooseFromAlbum;
    private View view;
    private Data dataApp;
    private String teacherInfo;
    private String name;
    private String nickname;
    private String school;
    private String sex;
    private String birth;
    private String phone;
    private String id;
    private TextView txt_name;
    private TextView txt_nickname;
    private TextView txt_school;
    private TextView txt_birth;
    private TextView txt_id;
    private TextView txt_tel;
    private TextView txt_sex;
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    public final static int GALLERY_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_infopage,null);
        dataApp=(Data)getActivity().getApplication();
        txt_id=view.findViewById(R.id.txt_tid);
        txt_name=view.findViewById(R.id.txt_tname);
        txt_nickname=view.findViewById(R.id.txt_kname);
        txt_birth=view.findViewById(R.id.txt_birth);
        txt_sex=view.findViewById(R.id.txt_gender);
        txt_tel=view.findViewById(R.id.txt_tel);
        txt_school=view.findViewById(R.id.txt_school);
        btn_takephoto = (Button) view.findViewById(R.id.take_photo);
        btn_chooseFromAlbum = (Button) view.findViewById(R.id.choose_from_album);
        btn_chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(getActivity(),
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
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

                }else { //权限已经被授予，在这里直接写要执行的相应方法即可
                    Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 指定照片存储位置为sd卡本目录下
                    // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
                    // File.separator为系统自带的分隔符 是一个固定的常量
                    mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
                    // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
                    /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
                    imageUri = FileProvider.getUriForFile(getContext(),
                            getActivity().getApplicationContext().getPackageName() + ".provider",
                            new File(mTempPhotoPath));
                    //下面这句指定调用相机拍照后的照片存储的路径
                    intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder reqBuild = new Request.Builder();
                HttpUrl.Builder urlBuilder =HttpUrl.parse("http://192.168.43.98:3000/getTeacherInfo")
                        .newBuilder();
                urlBuilder.addQueryParameter("tid", dataApp.getTid());
                reqBuild.url(urlBuilder.build());
                Request request = reqBuild.build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    teacherInfo = response.body().string();
                    //在线程中没有办法实现主线程操作
                    GSONTeacher(teacherInfo);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
        return view;
    }
    private void GSONTeacher(String teacherInfo){
        try {
            JSONObject jsonObject=new JSONObject(teacherInfo);
            name=jsonObject.getString("tname");
            id=jsonObject.getString("tid");
            nickname=jsonObject.getString("kname");
            phone=jsonObject.getString("tel");
            birth=jsonObject.getString("birth");
            sex=jsonObject.getString("gender");
            school=jsonObject.getString("fac");
            Message message=new Message();
            message.what=0;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    txt_id.setText(id);
                    txt_name.setText(id);
                    txt_nickname.setText(nickname);
                    txt_birth.setText(birth);
                    txt_school.setText(school);
                    txt_sex.setText(sex);
                    txt_tel.setText(phone);
                    break;
            }
        }
    };
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
                    Toast.makeText(getContext(),"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        picture = view.findViewById(R.id.upload_img);
        if (resultCode == RegisterInfoActivity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE: {
                    // 获得图片
                    try {
                        //该uri就是照片文件夹对应的uri
                        Bitmap bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
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
                            Bitmap bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
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
                imageUri = FileProvider.getUriForFile(getContext(),
                        getActivity().getApplicationContext().getPackageName() +".provider",
                        new File(mTempPhotoPath));
                //下面这句指定调用相机拍照后的照片存储的路径
                intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);

            } else
            {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
