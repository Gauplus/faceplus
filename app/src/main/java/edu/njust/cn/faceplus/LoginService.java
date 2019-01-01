package edu.njust.cn.faceplus;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginService {
    public static String loginByPost(String id,String password) {
        HttpURLConnection connection=null;
        URL url=null;
        String path = "http://192.168.43.98:3000/login";
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            Log.d("loginservice", "loginByPost: 打开连接");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String sendData = "&passwordd=" + URLEncoder.encode(password, "UTF-8")+ "&id=" + URLEncoder.encode(id, "UTF-8");
            connection.setRequestProperty("Content=Type", "application/x-wwww-form-urlencoded");
            connection.setRequestProperty("Content-length", sendData.length()+"");
           // connection.connect();
            OutputStream outputStream=connection.getOutputStream();
            //Log.d(".Loginservice", "loginByPost: 请求");
            outputStream.write(sendData.getBytes());
            //Log.d(".Loginservice", "loginByPost: 数据");
            outputStream.flush();
            InputStream inputStream=connection.getInputStream();
            Log.d(".Loginservice", "loginByPost: 状态码");
            if(connection.getResponseCode()==200){
                Log.d("loginservice", "loginByPost: 输入流");
                String text=StreamUtil.readFromStream(inputStream);
                Log.d("Login",text+"1111111");
                return text;
            }
            else {
                 inputStream=connection.getErrorStream();
                String text=StreamUtil.readFromStream(inputStream);
                Log.d("loginservice", text+"loginByPost: 错误信息");
                return null;
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
        return null;

    }
}
