package com.wisdom.nhoa.approval.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.OfficeStuffModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：文化用品选择列表的适配器
 * @time 2018/7/16 9:42
 * @change
 */
public class OfficeStuffAdapter extends BaseExpandableListAdapter {
    public static final String TAG = OfficeStuffAdapter.class.getSimpleName();
    private Context context;
    private List<OfficeStuffModel> listData;
    private List<List<OfficeStuffModel.ChildBean>> childData;
    private Boolean isFolderEmpty = false;

    public OfficeStuffAdapter(Context context, List<OfficeStuffModel> listData) {
        this.context = context;
        this.listData = listData;
        //构造子项数据源
        childData = new ArrayList<List<OfficeStuffModel.ChildBean>>();
        for (int i = 0; i < listData.size(); i++) {
            childData.add(listData.get(i).getChild());
        }
    }

    @Override
    public int getGroupCount() {
        return listData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childData.get(groupPosition).size() == 0) {
            isFolderEmpty = true;
            return 1;
        } else {
            isFolderEmpty = false;
            return childData.get(groupPosition).size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return listData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (childData != null && childData.size() > childPosition) {
            return listData.get(groupPosition).getChild();
        }
        return null;
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
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    class GroupHolder {
        TextView tv_office_stuff_parent;
    }

    class ChildHolder {
        TextView tv_office_stuff_child;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder groupHolder;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_office_stuff_parent, null);
            groupHolder.tv_office_stuff_parent = (TextView) convertView.findViewById(R.id.tv_office_stuff_parent);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        //设置相关值

        groupHolder.tv_office_stuff_parent.setText(
                listData.get(groupPosition).getName()
        );

        //对group的文件夹开合状态进行切换
        if (isExpanded) {
        } else {
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;
        if (!isFolderEmpty) {//如果文件夹不为空，那么正常加载布局，否则加载空提示布局
//            if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_office_stuff_child, null);
            childHolder.tv_office_stuff_child = (TextView) convertView.findViewById(R.id.tv_office_stuff_child);
            convertView.setTag(childHolder);
//            } else {
//                childHolder = (ChildHolder) convertView.getTag();
//            }
            childHolder.tv_office_stuff_child.setText(listData.get(groupPosition)
                    .getChild()
                    .get(childPosition)
                    .getName());
            return convertView;
        } else {
//            View noDataView = LayoutInflater.from(context).inflate(R.layout.item_folder_no_data, null);
//            return noDataView;
            return convertView;
        }
    }



}
