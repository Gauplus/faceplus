package edu.njust.cn.faceplus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 课程列表适配器
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;
    public CourseAdapter(Context context, int textViewResourceId, List<Course> courseList){
        super(context,textViewResourceId,courseList);
        resourceId=textViewResourceId;
    }


    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {
        Course course=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.txt_courseName=view.findViewById(R.id.txt_courseName);
            viewHolder.txt_courseTime=view.findViewById(R.id.txt_courseTime);
            viewHolder.txt_coursePlace=view.findViewById(R.id.txt_coursePlace);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        return view;
    }
    class ViewHolder{
        TextView txt_courseName;
        TextView txt_courseTime;
        TextView txt_coursePlace;
    }
}
