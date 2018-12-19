package com.wisdom.nhoa.contacts.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.contacts.model.StaffListModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.adapter
 * @class describe：
 * @time 2018/3/25 15:39
 * @change
 */

public class StaffListAdapter extends BaseAdapter {
    private Context context;
    private List<StaffListModel> listData;
    public static final String TAG = StaffListAdapter.class.getSimpleName();

    public StaffListAdapter(Context context, List<StaffListModel> lisData) {
        this.context = context;
        this.listData = lisData;
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
        Log.i(TAG, "getView: " + listData.get(position).toString());
        DepartmentViewHolder holder;
        if (convertView == null) {
            holder = new DepartmentViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contract_listview_itemstaff, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_department = convertView.findViewById(R.id.tv_department);
            holder.tv_phone = convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        } else {
            holder = (DepartmentViewHolder) convertView.getTag();
        }
        Log.i(TAG, "getView: " + listData.get(position).toString());
        if (listData.get(position).getUsername() != null) {
            holder.tv_name.setText(listData.get(position).getUsername());
        }
        if (listData.get(position).getRolename() != null) {
            holder.tv_department.setText(listData.get(position).getRolename());
        }
        if (listData.get(position).getPhone() != null) {
            holder.tv_phone.setText(listData.get(position).getPhone());
        }
        return convertView;
    }

    class DepartmentViewHolder {
        TextView tv_name;
        TextView tv_department;
        TextView tv_phone;
    }
}
