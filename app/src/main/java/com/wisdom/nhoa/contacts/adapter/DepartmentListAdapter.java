package com.wisdom.nhoa.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.contacts.model.DepartmentListModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.adapter
 * @class describe：
 * @time 2018/3/25 15:39
 * @change
 */

public class DepartmentListAdapter extends BaseAdapter {
    private Context context;
    private List<DepartmentListModel> listData;

    public DepartmentListAdapter(Context context, List<DepartmentListModel> lisData) {
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
        DepartmentViewHolder holder;
        if (convertView == null) {
            holder = new DepartmentViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_listview_item_department, parent, false);
            holder.tv_name = convertView.findViewById(R.id.contact_tv_department_name);
            convertView.setTag(holder);
        } else {
            holder = (DepartmentViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(listData.get(position).getDepname());
        return convertView;
    }

    class DepartmentViewHolder {
        TextView tv_name;
    }
}
