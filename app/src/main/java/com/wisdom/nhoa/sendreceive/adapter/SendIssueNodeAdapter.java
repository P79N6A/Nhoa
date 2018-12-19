package com.wisdom.nhoa.sendreceive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.model.SendIssueNodeModel;

import java.util.List;

/**
 * author：
 * date:
 */

public class SendIssueNodeAdapter extends BaseAdapter {
    private Context context;
    private List<SendIssueNodeModel> listData;

    public SendIssueNodeAdapter(Context context, List<SendIssueNodeModel> listData) {
        this.context = context;
        this.listData = listData;
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
        SendIssueNodeAdapter.NodeViewHolder holder;
        if (convertView == null) {
            holder = new SendIssueNodeAdapter.NodeViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.send_issue_node_item, parent, false);
            holder.img_radio = convertView.findViewById(R.id.img_radio);
            holder.tv_department = convertView.findViewById(R.id.tv_department);
            convertView.setTag(holder);
        } else {
            holder = (SendIssueNodeAdapter.NodeViewHolder) convertView.getTag();
        }

        holder.tv_department.setText(listData.get(position).getNodename());
//        if(listData.get(position).getIsChecked().equals("false")){
//            holder.img_radio.setChecked(false);
//        }else {
//            holder.img_radio.setChecked(true);
//        }

        return convertView;
    }


    class NodeViewHolder {
        RadioButton img_radio;
        TextView tv_department;
    }
}
