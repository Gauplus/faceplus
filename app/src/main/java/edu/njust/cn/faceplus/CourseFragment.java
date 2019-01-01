package edu.njust.cn.faceplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 显示课程列表的界面
 */
public class CourseFragment extends Fragment {
    private ListView course_list;
    private View view;
    private ArrayList<Course> courseList=new ArrayList<>();
    private CourseAdapter courseAdapter;
    private String coursesData;
    private Data dataApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_course,null);
        dataApp=(Data)getActivity().getApplication();
        course_list=view.findViewById(R.id.list_course);
        init();
        course_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courseAdapter.getItem(position);
                Intent intent=new Intent(getContext(),CourseActivity.class);
                intent.setAction("action");
                intent.putExtra("tid",dataApp.getTid());
                intent.putExtra("cid", course.getCourseId());
                intent.putExtra("place",course.getCoursePlace());
                intent.putExtra("ctime",course.getCourseTime());
                intent.putExtra("cname",course.getCourseName());
                getActivity().startActivity(intent);

            }
        });
        return view;
    }
    private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder reqBuild = new Request.Builder();
                HttpUrl.Builder urlBuilder =HttpUrl.parse("http://192.168.43.98:3000/getCourses")
                        .newBuilder();
                urlBuilder.addQueryParameter("tid", dataApp.getTid());
                reqBuild.url(urlBuilder.build());
                Request request=reqBuild.build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    coursesData = response.body().string();
                    Log.d("coursefragment",coursesData+"课程数据");
                    //在线程中没有办法实现主线程操作
                    GSONCourses(coursesData);
                }catch (IOException e){

                    e.printStackTrace();
                }
            }
        }).start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    courseAdapter=new CourseAdapter(view.getContext(),R.layout.item_list_course,courseList);
                    course_list.setAdapter(courseAdapter);
                    break;
            }
        }
    };
    private void GSONCourses(String coursesData){
        String cid,cname,place,ctime ;
        try {
            JSONArray jsonArray=new JSONArray(coursesData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Course course=new Course();
                cid = jsonObject.getString("cid");
                Log.d("coursefragment",cid+"课程号");
                cname = jsonObject.getString("cname");
                Log.d("coursefragment",cname+"课程名");
                place = jsonObject.getString("place");
                Log.d("coursefragment",cid+"课程地点");
                ctime = jsonObject.getString("ctime");
                Log.d("coursefragment",cid+"课程时间");
                course.setCourseId(cid);
                course.setCourseName(cname);
                course.setCoursePlace(place);
                course.setCourseTime(ctime);
                Log.d("coursefragment",courseList+"课程列表");
                courseList.add(course);
            }
            Message message=new Message();
            message.what=0;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
