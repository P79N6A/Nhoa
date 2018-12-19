package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.LatelyPersonModel;

import java.util.List;

/**
 * author：
 * date:
 */

public class LatelyPersonAdapter extends BaseAdapter {
    private Context context;
    private List<LatelyPersonModel> listData;


    public LatelyPersonAdapter(Context context, List<LatelyPersonModel> list) {
        this.context = context;
        this.listData = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView == null) {
            holder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_addressbook, null);
            holder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_zhiwei = (TextView) convertView.findViewById(R.id.tv_zhiwei);
            holder.tv_phone=convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tv_name.setText(listData.get(position).getUsername());
        holder.tv_zhiwei.setText(listData.get(position).getRolename());
        holder.tv_phone.setText(""+listData.get(position).getPhone());
        return convertView;
    }

    class MyHolder {
        ImageView img_head;
        TextView tv_name;
        TextView tv_zhiwei;
        TextView tv_phone;
    }
}
