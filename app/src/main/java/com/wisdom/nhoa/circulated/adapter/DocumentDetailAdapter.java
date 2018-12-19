package com.wisdom.nhoa.circulated.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.circulated.model.DocumentDetailModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.adapter
 * @class describe：
 * @time 2018/3/25 15:39
 * @change
 */

public class DocumentDetailAdapter extends BaseAdapter {
    private Context context;
    private List<DocumentDetailModel> listData;

    public DocumentDetailAdapter(Context context, List<DocumentDetailModel> lisData) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.circulated_item_document_detail, parent, false);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (DepartmentViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(listData.get(position).getUser_name());
        return convertView;
    }

    class DepartmentViewHolder {
        TextView tv_content;
    }
}
