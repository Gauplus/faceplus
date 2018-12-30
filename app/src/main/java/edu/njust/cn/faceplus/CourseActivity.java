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
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
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

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 某门课程界面，显示学生列表，和签到、检测状态按钮
 */
public class CourseActivity extends AppCompatActivity {
    private TextView txt_courseTitle;
    private ListView student_list;
    private FloatingActionButton fab_signIn;       //签到按钮
    private FloatingActionButton fab_checkStatus; //状态检测按钮
    private StudentAdapter studentAdapter;
    private ArrayList<Student> studentList;
    public static final int Take_photo = 1;
    public static  final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Uri imageUri;
    private String studentsData;
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
                Toast.makeText(view.getContext(),"调用摄像头拍照",Toast.LENGTH_LONG).show();
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
                    imageUri = FileProvider.getUriForFile(CourseActivity.this, "edu.njust.cn.faceplus.provider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Take_photo);
            }
        });
        fab_checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"调用摄像头拍照",Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(CourseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CourseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {
                    openAlbum();
                }
            }
        });
    }
    private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tid=null,cid=null,place=null,ctime=null;
                Intent intent = getIntent();
                if("action".equals(intent.getAction())) {
                    tid = intent.getStringExtra("tid");
                    cid=intent.getStringExtra("cid");
                    place=intent.getStringExtra("place");
                    ctime=intent.getStringExtra("ctime");
                }
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder reqBuild = new Request.Builder();
                HttpUrl.Builder urlBuilder =HttpUrl.parse("http://10.30.111.245:3000/getStudentList")
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
                    studentAdapter=new StudentAdapter(CourseActivity.this,R.layout.item_list_students,studentList);
                    student_list.setAdapter(studentAdapter);
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
