package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.model.SendManageModel;

import java.util.List;

/**
 * author：
 * date:
 */

public class BacklogTabAdapter extends BaseAdapter {

    private Context context;
    private List<SendManageModel> listData;

    public BacklogTabAdapter(Context context, List<SendManageModel> lisData) {
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
        BacklogTabAdapter.DepartmentViewHolder holder;
        if (convertView == null) {
            holder = new BacklogTabAdapter.DepartmentViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.circulated_item_document_detail, parent, false);
            holder.tv_title = convertView.findViewById(R.id.approval_item_tv_law_title);
            holder.tv_subject = convertView.findViewById(R.id.approval_item_tv_law_subject);
            convertView.setTag(holder);
        } else {
            holder = (BacklogTabAdapter.DepartmentViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(listData.get(position).getDoctitle());
        if (listData.get(position).getDocnumber() == null || "".equals(listData.get(position).getDocnumber())) {
            //未分配文号
            holder.tv_subject.setText("未分配文号");
            holder.tv_subject.setTextColor(context.getResources().getColor(R.color.chartreuse));
        } else {
            //分配了文号
            holder.tv_subject.setText(listData.get(position).getDocnumber());
            holder.tv_subject.setTextColor(context.getResources().getColor(R.color.crimson));
        }
        return convertView;
    }

    class DepartmentViewHolder {
        TextView tv_title;
        TextView tv_subject;
    }
}
