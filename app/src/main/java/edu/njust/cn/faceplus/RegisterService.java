package edu.njust.cn.faceplus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterService {
    public static String registerByPost(String id, String password,String telnumber) {
        HttpURLConnection connection=null;
        URL url=null;
        String path = "http://10.30.111.245:3000/register";
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String sendData = "&password=" + URLEncoder.encode(password, "UTF-8")+ "&id=" + URLEncoder.encode(id, "UTF-8")+"&phone="+URLEncoder.encode(telnumber,"UTF-8");
            connection.setRequestProperty("Content=Type", "application/x-wwww-form-urlencoded");
            connection.setRequestProperty("Content-length", sendData.length()+"");
            OutputStream outputStream=connection.getOutputStream();
            outputStream.write(sendData.getBytes());
            int code=connection.getResponseCode();
            if(code==200){
                InputStream inputStream=connection.getInputStream();
                String text=StreamUtil.readFromStream(inputStream);
                return text;
            }
            else {
                return null;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static String registerInfoByPost(String name, String nickname,String birth,String sex,String school,String tid) {
        HttpURLConnection connection=null;
        URL url=null;
        String path = "http://10.30.111.245:3000/modifyTeacherInfo";
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String sendData = "&tname=" + URLEncoder.encode(name, "UTF-8")+ "&kname=" + URLEncoder.encode(nickname, "UTF-8")+"&birth="+URLEncoder.encode(birth,"UTF-8")+"&gender="+URLEncoder.encode(sex,"UTF-8")+"&school="+URLEncoder.encode(school,"UTF-8")+"&tid="+URLEncoder.encode(tid,"UTF-8");
            connection.setRequestProperty("Content=Type", "application/x-wwww-form-urlencoded");
            connection.setRequestProperty("Content-length", sendData.length()+"");
            OutputStream outputStream=connection.getOutputStream();
            outputStream.write(sendData.getBytes());
            int code=connection.getResponseCode();
            if(code==200){
                InputStream inputStream=connection.getInputStream();
                String text=StreamUtil.readFromStream(inputStream);
                return text;
            }
            else {
                return null;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
