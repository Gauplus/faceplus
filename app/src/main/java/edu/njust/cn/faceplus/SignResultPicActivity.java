package edu.njust.cn.faceplus;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 以统计图显示签到结果的界面
 */
public class SignResultPicActivity extends AppCompatActivity {
    private FloatingActionButton fab_sign_result_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_result_picture);
        fab_sign_result_list=findViewById(R.id.fab_sign_result_list);
        fab_sign_result_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignResultPicActivity.this,SignResultListActivity.class);
                startActivity(intent);
            }
        });
    }
}
