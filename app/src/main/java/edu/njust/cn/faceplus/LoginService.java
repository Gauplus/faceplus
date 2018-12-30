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
        String path = "http://192.168.1.103:3000/login";
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String sendData = "&passwordd=" + URLEncoder.encode(password, "UTF-8")+ "&id=" + URLEncoder.encode(id, "UTF-8");
            connection.setRequestProperty("Content=Type", "application/x-wwww-form-urlencoded");
            connection.setRequestProperty("Content-length", sendData.length()+"");
            OutputStream outputStream=connection.getOutputStream();
            outputStream.write(sendData.getBytes());
            int code=connection.getResponseCode();
            if(code==200){
                InputStream inputStream=connection.getInputStream();
                String text=StreamUtil.readFromStream(inputStream);
                Log.d("Login",text+"1111111");
                return text;
            }
            else {
                return null;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }
}
