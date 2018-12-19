package edu.njust.cn.faceplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private EditText edt_tel;
    private EditText edt_verifyCode;
    private Button btn_getVerifyCode;
    private Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tel);
        edt_tel=findViewById(R.id.edt_tel);
        edt_verifyCode=findViewById(R.id.edt_verifyCode);
        btn_getVerifyCode=findViewById(R.id.btn_getVerifyCode);
        btn_next=findViewById(R.id.btn_next);
        btn_getVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
