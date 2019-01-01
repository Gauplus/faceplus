package edu.njust.cn.faceplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class SignExpandeAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater inflater=null;
    private String[] mGroupStrings=null;
    private List<List<Student>> mGroups=null;

    public SignExpandeAdapter(Context context,List<List<Student>> groups){
        mContext=context;
        mGroups=groups;
        mGroupStrings=mContext.getResources().getStringArray(R.array.signgroups);
        inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<List<Student>> groups){
        mGroups=groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).size();
    }

    @Override
    public List<Student> getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Student getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_list_sign_result,null);
        }
        GroupViewHolder groupViewHolder=new GroupViewHolder();
        groupViewHolder.group_name=convertView.findViewById(R.id.txt_sign_result_list_item);
        groupViewHolder.group_name.setText(mGroupStrings[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        Student student=getChild(groupPosition,childPosition);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.childitem_sign_list,null);
        }

        ChildViewHolder childViewHolder=new ChildViewHolder();
        childViewHolder.txt_studentName=convertView.findViewById(R.id.txt_signName);
        childViewHolder.txt_studentId=convertView.findViewById(R.id.txt_signID);
        childViewHolder.txt_studentName.setText(student.getStudentName());
        childViewHolder.txt_studentId.setText(student.getStudentId());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        TextView group_name;
    }


    class ChildViewHolder{
        TextView txt_studentName;
        TextView txt_studentId;
    }
}
