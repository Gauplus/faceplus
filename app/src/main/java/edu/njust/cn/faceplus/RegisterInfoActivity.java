package edu.njust.cn.faceplus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

public class RegisterInfoActivity extends AppCompatActivity {
    private EditText edt_name;
    private EditText edt_nickname;
    private EditText edt_school;
    private EditText edt_birth;
    private EditText edt_id;
    private RadioGroup rg_sex;
    private RadioGroup rg_identity;
    private RadioButton rbtn_sex;
    private RadioButton rbtn_identity;
    private Button btn_save;
    private String name;
    private String nickname;
    private String school;
    private String sex;
    private String birth;
    private String identity;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_info);
        edt_name=findViewById(R.id.edt_name);
        edt_nickname=findViewById(R.id.edt_nickname);
        edt_birth=findViewById(R.id.edt_birth);
        edt_school=findViewById(R.id.edt_school);
        edt_id=findViewById(R.id.edt_resID);
        rg_sex=findViewById(R.id.sex);
        rg_identity=findViewById(R.id.identity);
        btn_save=findViewById(R.id.btn_save);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectSex();
            }
        });
        rg_identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectIdentity();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=edt_name.getText().toString().trim();
                nickname=edt_nickname.getText().toString().trim();
                birth=edt_birth.getText().toString().trim();
                school=edt_school.getText().toString().trim();
                id=edt_id.getText().toString().trim();
                if (StringUtil.isEmpty(name)||StringUtil.isEmpty(birth)||StringUtil.isEmpty(school)){
                    Toast.makeText(RegisterInfoActivity.this,"姓名或生日或学校不能为空",Toast.LENGTH_LONG).show();
                }
                if(StringUtil.isEmpty(nickname)){
                    nickname=name;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=new Message();
                        if(RegisterService.registerInfoByPost(name,nickname,birth,sex,school,id)=="OK"){
                            if (identity=="老师"){
                                message.what=2;
                                handler.sendMessage(message);
                            }
                            else{
                                message.what=1;
                                handler.sendMessage(message);
                            }
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

    private void selectSex(){
        rbtn_sex=findViewById(rg_sex.getCheckedRadioButtonId());
        sex=rbtn_sex.getText().toString();
    }
    private void selectIdentity(){
        rbtn_identity=findViewById(rg_identity.getCheckedRadioButtonId());
        identity=rbtn_identity.getText().toString();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    Intent intent=new Intent(RegisterInfoActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent1=new Intent(RegisterInfoActivity.this,StudentActivity.class);
                    startActivity(intent1);
                case 0:
                    Toast.makeText(RegisterInfoActivity.this,"添加个人信息失败",Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };
}


