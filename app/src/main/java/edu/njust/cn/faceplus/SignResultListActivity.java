package edu.njust.cn.faceplus;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 以学生列表形式显示签到结果的界面，分为缺勤与出勤
 */
public class SignResultListActivity extends AppCompatActivity {
    private SignExpandeAdapter signExpandeAdapter=null;
    private ExpandableListView expandableListView=null;
    private List<List<Student>> groups=new ArrayList<List<Student>>();
    private List<Student> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_result_list);
        init();

    }
    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    signExpandeAdapter = new SignExpandeAdapter(SignResultListActivity.this, groups);
                    expandableListView.setAdapter(signExpandeAdapter);
                    break;
            }
        }
    };

}
