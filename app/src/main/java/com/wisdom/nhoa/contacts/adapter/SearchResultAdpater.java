package com.wisdom.nhoa.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.contacts.model.DepartmentListModel;
import com.wisdom.nhoa.contacts.model.SearchResultModel;

import java.util.List;

/**
 * @authorzhanglichao
 * @date2018/3/26 20:17
 * @package_name com.wisdom.nhoa.contacts.adapter
 */

public class SearchResultAdpater extends BaseAdapter{
    private Context context;
    private List<SearchResultModel> listData;

    public SearchResultAdpater(Context context, List<SearchResultModel> lisData) {
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
        SearchResultAdpater.DepartmentViewHolder holder;
        if (convertView == null) {
            holder = new SearchResultAdpater.DepartmentViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contract_listview_itemstaff, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_department = convertView.findViewById(R.id.tv_department);
            holder.tv_phone = convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        } else {
            holder = (SearchResultAdpater.DepartmentViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(listData.get(position).getUsername()+"");
        if (listData.get(position).getRolename()!=null) {
            holder.tv_department.setText(listData.get(position).getRolename()+"");
        }
        holder.tv_phone.setText(listData.get(position).getPhone()+"");
        return convertView;
    }

    class DepartmentViewHolder {
        TextView tv_name;
        TextView tv_department;
        TextView tv_phone;
    }
}
