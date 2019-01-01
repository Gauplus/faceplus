package edu.njust.cn.faceplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /** StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());*/
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
                id=edt_workID.getText().toString().trim();
                password=edt_password.getText().toString().trim();
                postRequest(id,password);
                /*if(LoginService.loginByPost(id,password)=="OK"){
                    message.what=1;
                    handler.sendMessage(message);
                }
                else{
                    message.what=0;
                    handler.sendMessage(message);
                }*/
            }

        });

        }
    private void postRequest(String strid,String strpassword)  {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormBody.Builder()
                .add("username",strid)
                .add("password",strpassword)
                .build();
        //发起请求
        final Request request = new Request.Builder()
                .url("http://192.168.43.98:3000/login")
                .post(formBody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    //回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                       Message message=new Message();
                       message.what=1;
                       handler.sendMessage(message);
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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

    private void loginThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                id=edt_workID.getText().toString();
                password=edt_password.getText().toString();
                Message message=new Message();
                HttpURLConnection connection=null;
                URL url=null;
                String path = "http://192.168.43.98:3000/login";
                try {
                    url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    Log.d("loginservice", "loginByPost: 打开连接");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    String sendData = "&passwordd=" + URLEncoder.encode(password, "UTF-8")+ "&id=" + URLEncoder.encode(id, "UTF-8");
                    connection.setRequestProperty("Content=Type", "application/x-wwww-form-urlencoded");
                    connection.setRequestProperty("Content-length", sendData.length()+"");
                    // connection.connect();
                    OutputStream outputStream=connection.getOutputStream();
                    Log.d(".Loginservice", "loginByPost: 请求");
                    outputStream.write(sendData.getBytes());
                    Log.d(".Loginservice", "loginByPost: 数据");
                    outputStream.flush();
                    InputStream inputStream=connection.getInputStream();
                    Log.d(".Loginservice", "loginByPost: 状态码");
                    if(connection.getResponseCode()==200){
                        Log.d("loginservice", "loginByPost: 输入流");
                        String text=StreamUtil.readFromStream(inputStream);
                        Log.d("Login",text+"1111111");
                        message.what=1;
                        handler.sendMessage(message);
                    }
                    else {
                        inputStream=connection.getErrorStream();
                        String text=StreamUtil.readFromStream(inputStream);
                        Log.d("loginservice", text+"loginByPost: 错误信息");
                        message.what=0;
                        handler.sendMessage(message);
                    }
                }catch (MalformedURLException e){
                    Log.d("loginservice", "loginByPost: 超时异常");
                    e.printStackTrace();
                }catch (ProtocolException e){
                    Log.d("loginservice", "loginByPost: 协议异常");
                    e.printStackTrace();
                }catch (IOException e){
                    Log.d("loginservice", "loginByPost: 输入输出异常");
                    e.printStackTrace();
                }
                /*if(LoginService.loginByPost(id,password)=="OK"){
                    message.what=1;
                    handler.sendMessage(message);
                }
                else{
                    message.what=0;
                    handler.sendMessage(message);
                }*/
            }
        }).start();
    }
}
