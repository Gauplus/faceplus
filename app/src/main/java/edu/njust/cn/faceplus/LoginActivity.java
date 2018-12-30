package edu.njust.cn.faceplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录活动
 */
public class LoginActivity extends AppCompatActivity {
    private EditText edt_workID;
    private EditText edt_password;
    private TextView btn_register;
    private Button btn_login;
    private TextView btn_forgetPassword;
    private String id;
    private String password;
    private Data dataApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dataApp=(Data)getApplication();
        edt_workID=findViewById(R.id.edt_workID);
        edt_password=findViewById(R.id.edt_password);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
        btn_forgetPassword=findViewById(R.id.btn_forgetPassword);
        btn_register.setText(getClickableSpan());
        btn_register.setMovementMethod(LinkMovementMethod.getInstance());
        btn_forgetPassword.setText(getClickableSpan1());
        btn_forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        id=edt_workID.getText().toString();
                        password=edt_password.getText().toString();
                        Message message=new Message();
                        if(LoginService.loginByPost(id,password)=="OK"){
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
        });
        }

    private SpannableString getClickableSpan(){
        SpannableString spannableString=new SpannableString("注册账号");
        spannableString.setSpan(new UnderlineSpan(),0,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        },0,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1c86ee")),0,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    private SpannableString getClickableSpan1(){
        SpannableString spannableString=new SpannableString("忘记密码?");
        spannableString.setSpan(new UnderlineSpan(),0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(LoginActivity.this, "忘记密码?", Toast.LENGTH_LONG).show();
            }
        },0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1c86ee")),0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        dataApp.setTid(id);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };
}
