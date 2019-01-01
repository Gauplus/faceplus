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

public class StudentAdapter extends ArrayAdapter<Student> {
    int resourceId;
    public StudentAdapter(Context context, int textViewResourceId, List<Student> studentList){
        super(context,textViewResourceId,studentList);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Student student=getItem(position);
        View view;
        StudentAdapter.ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.txt_studentName=view.findViewById(R.id.txt_studentName);
            viewHolder.txt_studentID=view.findViewById(R.id.txt_studentID);
            viewHolder.txt_studentName.setText(student.getStudentName());
            viewHolder.txt_studentID.setText(student.getStudentId());
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(StudentAdapter.ViewHolder)view.getTag();
        }
        return view;
    }
    class ViewHolder{
        TextView txt_studentName;
        TextView txt_studentID;
    }
}
