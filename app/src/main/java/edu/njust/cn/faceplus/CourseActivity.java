package edu.njust.cn.faceplus;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 某门课程界面，显示学生列表，和签到、检测状态按钮
 */
public class CourseActivity extends AppCompatActivity {
    private TextView txt_courseTitle;
    private ListView student_list;
    private FloatingActionButton fab_signIn;       //签到按钮
    private FloatingActionButton fab_checkStatus; //状态检测按钮
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        txt_courseTitle=findViewById(R.id.txt_courseTitle);
        student_list=findViewById(R.id.list_student);
        fab_signIn=findViewById(R.id.fab_signIn);
        fab_checkStatus=findViewById(R.id.fab_checkStatus);
        studentAdapter=new StudentAdapter(CourseActivity.this,R.layout.item_list_students,studentList);
        student_list.setAdapter(studentAdapter);

        fab_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"调用摄像头拍照",Toast.LENGTH_LONG).show();
                /**
                 * 调用摄像头拍照
                 */
            }
        });
        fab_checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"调用摄像头拍照",Toast.LENGTH_LONG).show();
                /**
                 * 调用摄像头拍照
                 */
            }
        });
    }
}
