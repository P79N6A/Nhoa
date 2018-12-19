package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author：
 * date:
 */

public class AddressBookAdapter extends BaseAdapter {
    private Context context;
    private List<String> list = new ArrayList<>();
    private String isclick;
    public AddressBookAdapter(Context context, List<String> list,String isclick) {
        this.context = context;
        this.list = list;
        this.isclick = isclick;
    }

    /**
     * 刷新数据源
     *
     * @param list
     */
    public void onRefreshDataSource(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 追加数据源
     *
     * @param listNew
     */
    public void addDataSource(List<String> listNew) {
        for (int i = 0; i < listNew.size(); i++) {
            list.add(listNew.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView == null) {
            holder = new MyHolder();
            if(isclick.equals("false")){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_addressbook, null);
                holder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_zhiwei = (TextView) convertView.findViewById(R.id.tv_zhiwei);
                convertView.setTag(holder);
            }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.ll_department_item, null);
                holder.tv_department = (TextView) convertView.findViewById(R.id.tv_department);
            }
        } else {
            holder = (MyHolder) convertView.getTag();
        }

        return convertView;
    }

    class MyHolder {
        ImageView img_head;
        TextView tv_name;
        TextView tv_zhiwei;
        TextView tv_department;
    }
}
