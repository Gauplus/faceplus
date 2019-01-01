package edu.njust.cn.faceplus;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 以学生列表显示状态检测结果的界面
 */
public class StatusResultListActivity extends AppCompatActivity {
    private StatusExpandeAdapter statusExpandeAdapter=null;
    private ExpandableListView expandableListView=null;
    private List<List<Student>> groups=new ArrayList<List<Student>>();
    private List<Student> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_result_list);
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
                    statusExpandeAdapter = new StatusExpandeAdapter(StatusResultListActivity.this, groups);
                    expandableListView.setAdapter(statusExpandeAdapter);
                    break;
            }
        }
    };
}
