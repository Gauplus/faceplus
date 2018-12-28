package edu.njust.cn.faceplus;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText edt_tel;
    private Button btn_register;
    private Button btn_cancle;
    private String telNumber;
    private EditText edt_id;
    private EditText edt_password;
    private String id;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_tel);
        edt_tel=findViewById(R.id.edt_tel);
        edt_id=findViewById(R.id.edt_id);
        edt_password=findViewById(R.id.edt_RegPassword);
        btn_register=findViewById(R.id.btn_VerifyRegister);
        btn_cancle=findViewById(R.id.btn_cancleRegister);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telNumber=edt_tel.getText().toString().trim();
                id=edt_id.getText().toString().trim();
                password=edt_password.getText().toString().trim();
                if(StringUtil.isEmpty(telNumber)||StringUtil.isEmpty(id)||StringUtil.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "手机号或密码或工号不能为空", Toast.LENGTH_LONG).show();
                }
                if(!StringUtil.isPhoneNumberValid(telNumber)){
                    Toast.makeText(RegisterActivity.this, "手机号有误", Toast.LENGTH_LONG).show();
                }
                if(StringUtil.isSpecialChar(id)||StringUtil.isSpecialChar(password)){
                    Toast.makeText(RegisterActivity.this, "密码或工号格式有误", Toast.LENGTH_SHORT).show();
                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            if(RegisterService.registerByPost(id,password,telNumber)=="OK"){
                                message.what=1;
                                handler.sendMessage(message);
                            }
                            else{
                                message.what=0;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                }


            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent=new Intent(RegisterActivity.this,RegisterInfoActivity.class);
                    startActivity(intent);
                    break;
                case 0:
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };
}
