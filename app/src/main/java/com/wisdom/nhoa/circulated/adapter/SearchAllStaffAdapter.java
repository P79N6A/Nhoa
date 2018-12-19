package com.wisdom.nhoa.circulated.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.circulated.model.GroupAllStaffModel;
import com.wisdom.nhoa.contacts.adapter.DepartmentListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.adapter
 * @class describe：创建群页面员工列表
 * @time 2018/3/26 10:47
 * @change
 */

public class SearchAllStaffAdapter extends BaseAdapter {
    private Context context;
    private List<GroupAllStaffModel> listData;
    public static Map<Integer, Boolean> isSelected;

    public SearchAllStaffAdapter(Context context, List<GroupAllStaffModel> listData) {
        this.context = context;
        this.listData = listData;
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < listData.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.circulate_create_group_item, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_staff_name);
            holder.checkBox = convertView.findViewById(R.id.cb_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (AllStaffHolder) convertView.getTag();
        }
        holder.checkBox.setChecked(isSelected.get(position));
        holder.tv_name.setText(listData.get(position).getUser_name());
        return convertView;
    }


    public class AllStaffHolder {
       public  CheckBox checkBox;
        public TextView tv_name;
    }
}
