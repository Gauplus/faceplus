package edu.njust.cn.faceplus;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 登录成功后的跳转界面，默认首先显示课程列表界面
 */
public class MainActivity extends AppCompatActivity {
    private CourseFragment courseFragment;
    private InfoPageFragment infoPageFragment;
    private Button btn_course;
    private Button btn_infopage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_course=findViewById(R.id.btn_course);
        btn_infopage=findViewById(R.id.btn_infopage);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frmlayout_main,courseFragment);
        transaction.commit();
        btn_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                if(!courseFragment.isAdded()){
                    transaction.hide(infoPageFragment).add(R.id.frmlayout_main,courseFragment).show(courseFragment);
                    transaction.commit();
                }
                else {
                    transaction.hide(infoPageFragment).show(courseFragment);
                    transaction.commit();
                }
            }
        });
        btn_infopage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                if(!infoPageFragment.isAdded()){
                    transaction.hide(courseFragment).add(R.id.frmlayout_main,infoPageFragment).show(infoPageFragment);
                    transaction.commit();
                }
                else {
                    transaction.hide(courseFragment).show(infoPageFragment);
                    transaction.commit();
                }
            }
        });
    }
}
