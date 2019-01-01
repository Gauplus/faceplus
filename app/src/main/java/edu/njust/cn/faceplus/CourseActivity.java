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
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 某门课程界面，显示学生列表，和签到、检测状态按钮
 */
public class CourseActivity extends AppCompatActivity {
    private TextView txt_courseTitle;
    private ListView student_list;
    private Uri imageUri;
    private String mTempPhotoPath;
    private FloatingActionButton fab_signIn;       //签到按钮
    private FloatingActionButton fab_checkStatus; //状态检测按钮
    private StudentAdapter studentAdapter;
    private ArrayList<Student> studentList=new ArrayList<>();
    public static final int Take_photo = 1;
    public static  final int CHOOSE_PHOTO=2;
    private String studentsData;
    private String cid=null;
    private String tid=null;
    private String cname=null;
    private String place=null;
    private String ctime=null;
    private String photo_path;
    private final OkHttpClient client = new OkHttpClient();
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    public final static int GALLERY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        txt_courseTitle=findViewById(R.id.txt_courseTitle);
        student_list=findViewById(R.id.list_student);
        fab_signIn=findViewById(R.id.fab_signIn);
        fab_checkStatus=findViewById(R.id.fab_checkStatus);
        init();
        fab_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"调用摄像头拍照",Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(CourseActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CourseActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

                }else {
                    Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
                    imageUri = FileProvider.getUriForFile(CourseActivity.this,
                            getApplicationContext().getPackageName() + ".provider",
                            new File(mTempPhotoPath));
                    intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
                }
            }
        });
    }
    private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                if("action".equals(intent.getAction())) {
                    tid = intent.getStringExtra("tid");
                    cid=intent.getStringExtra("cid");
                    place=intent.getStringExtra("place");
                    ctime=intent.getStringExtra("ctime");
                }
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder reqBuild = new Request.Builder();
                HttpUrl.Builder urlBuilder =HttpUrl.parse("http://192.168.43.98:3000/getStudentList")
                        .newBuilder();
                urlBuilder.addQueryParameter("tid", tid);
                urlBuilder.addQueryParameter("cid", cid);
                urlBuilder.addQueryParameter("place", place);
                urlBuilder.addQueryParameter("ctime", ctime);
                reqBuild.url(urlBuilder.build());
                Request request = reqBuild.build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    studentsData = response.body().string();
                    Log.d("courseactivity",studentsData+"学生列表");
                    //在线程中没有办法实现主线程操作
                    GSONStudents(studentsData);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void GSONStudents(String studentsData){
        String sid,sname;
        try {
            JSONArray jsonArray=new JSONArray(studentsData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Student student=new Student();
                sid = jsonObject.getString("sid");
                sname = jsonObject.getString("sname");
                student.setStudentId(sid);
                student.setStudentName(sname);
                studentList.add(student);
            }
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
                    txt_courseTitle.setText(cname);
                    studentAdapter=new StudentAdapter(CourseActivity.this,R.layout.item_list_students,studentList);
                    student_list.setAdapter(studentAdapter);
                    break;
            }
        }
    };

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == CAMERA_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
                imageUri = FileProvider.getUriForFile(CourseActivity.this,
                        getApplicationContext().getPackageName() +".provider",
                        new File(mTempPhotoPath));
                intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);

            } else
            {
                Toast.makeText(CourseActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
                intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
            } else
            {
                Toast.makeText(CourseActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CourseActivity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE: {
                    // 获得图片
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                       photo_path = ImageUtil.getPath(this, imageUri);
                        //选取完图片后调用上传方法，将图片路径放入参数中
                        sendStudentInfoToServer(photo_path);
                        // 给相应的ImageView设置图片 未裁剪
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
    private void sendStudentInfoToServer(String path) {
        //接口地址
        final File file=new File(path);
        final String urlAddress = "http://192.168.43.98:3000/attendence";
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", "sign_image", fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(urlAddress)
                        .post(requestBody)
                        .build();
                Response response;
                try {
                    response = client.newCall(request).execute();
                    String jsonString = response.body().string();
                    Log.d("courseactivity","返回数据"+jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("courseactivity","upload IOException ",e);
                }
            }
        }).start();
    }

}
