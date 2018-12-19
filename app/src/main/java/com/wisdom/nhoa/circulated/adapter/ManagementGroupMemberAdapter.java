package com.wisdom.nhoa.circulated.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.circulated.model.GroupAllStaffModel;
import com.wisdom.nhoa.circulated.model.GroupManagementModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.adapter
 * @class describe：公文传阅管理群成员页面
 * @time 2018/3/26 10:47
 * @change
 */

public class ManagementGroupMemberAdapter extends BaseAdapter {
    private Context context;
    private GroupManagementModel listData;
    public static Map<Integer, Boolean> isSelected;

    public ManagementGroupMemberAdapter(Context context, GroupManagementModel listData) {
        this.context = context;
        this.listData = listData;
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < listData.getUserlist().size(); i++) {
            if (ConstantString.MEMBER_IS_SELECTED_TRUE.equals(
                    listData.getUserlist().get(i).getIssave()
            )) {
                isSelected.put(i, true);
            } else {
                isSelected.put(i, false);
            }
        }
    }

    @Override
    public int getCount() {
        return listData.getUserlist().size();
    }

    @Override
    public Object getItem(int position) {
        return listData.getUserlist().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllStaffHolder holder;
        if (convertView == null) {
            holder = new AllStaffHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.circulate_management_group_item, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_staff_name);
            holder.checkBox = convertView.findViewById(R.id.cb_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (AllStaffHolder) convertView.getTag();
        }
        holder.checkBox.setChecked(isSelected.get(position));
        holder.tv_name.setText(listData.getUserlist().get(position).getUsername());

        return convertView;
    }


    public class AllStaffHolder {
        public CheckBox checkBox;
        public TextView tv_name;
    }
}
