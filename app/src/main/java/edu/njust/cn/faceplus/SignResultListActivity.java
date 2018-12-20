package edu.njust.cn.faceplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 以学生列表形式显示签到结果的界面，分为缺勤与出勤
 */
public class SignResultListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_result_list);
    }
}
