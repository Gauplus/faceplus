package edu.njust.cn.faceplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示课程列表的界面
 */
public class CourseFragment extends Fragment {
    private ListView course_list;
    private View view;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_course,null);
        course_list=view.findViewById(R.id.list_course);
        courseAdapter=new CourseAdapter(view.getContext(),R.layout.item_list_course,courseList);
        course_list.setAdapter(courseAdapter);
        course_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courseAdapter.getItem(position);
                Intent intent=new Intent(getContext(),CourseActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
