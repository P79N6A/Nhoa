package com.wisdom.nhoa.approval.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.model.SendManageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * author：
 * date:
 */

public class BacklogELAdapter extends BaseExpandableListAdapter {
    public String[] groupStrings = {"发文待办", "收文待办"};
    private List<SendManageModel> sendlist = new ArrayList<>();
    private List<SendManageModel> recrvicelist = new ArrayList();
    private List list = new ArrayList();
    private Context context;

    public BacklogELAdapter(Context context) {
        this.context = context;
    }

    public void setSend(List<SendManageModel> sendlist) {
        this.sendlist = sendlist;
        list.add(sendlist);
    }

    public void setRecevice(List<SendManageModel> recrvicelist) {
        this.recrvicelist = recrvicelist;
        list.add(recrvicelist);
    }

    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        if (groupPosition == 0) {
            size = sendlist.size();
        } else {
            size = recrvicelist.size();
        }
        return size;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return sendlist;
        } else {
            return recrvicelist;
        }
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.homepage_expandable_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_department = (TextView) convertView.findViewById(R.id.tv_department);
            groupViewHolder.iv_unfold = (ImageView) convertView.findViewById(R.id.iv_unfold);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tv_department.setText(groupStrings[groupPosition]);
        if (isExpanded) {
            groupViewHolder.iv_unfold.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pickup));
        } else {
            groupViewHolder.iv_unfold.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_unfold));
        }

        return convertView;
    }

    //        获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.approval_item_issue_todo, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_title = convertView.findViewById(R.id.approval_item_tv_law_title);
            childViewHolder.tv_subject = convertView.findViewById(R.id.approval_item_tv_law_subject);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            childViewHolder.tv_title.setText(sendlist.get(childPosition).getDoctitle());
            childViewHolder.tv_subject.setText(sendlist.get(childPosition).getDocnumber());
        } else {
            childViewHolder.tv_title.setText(recrvicelist.get(childPosition).getDoctitle());
            childViewHolder.tv_subject.setText(recrvicelist.get(childPosition).getDocnumber());
        }

        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tv_department;
        ImageView iv_unfold;
    }

    static class ChildViewHolder {
        TextView tv_title;
        TextView tv_subject;
    }

}
