package com.wisdom.nhoa.sendreceive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.model.SendIssuePersonModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author：
 * date:
 */

public class SendIssuePersonAdapter extends BaseAdapter {
    private Context context;
    private List<SendIssuePersonModel> listData;
    public static Map<Integer, String> personIsCheck;

    public SendIssuePersonAdapter(Context context, List<SendIssuePersonModel> listData) {
        this.context = context;
        this.listData = listData;
        personIsCheck = new HashMap<Integer, String>();
        for (int i = 0; i < listData.size(); i++) {
            personIsCheck.put(i, "false");
        }
    }

    public void setListData(List<SendIssuePersonModel> listData) {
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
        SendIssuePersonAdapter.PersonViewHolder holder;
        if (convertView == null) {
            holder = new SendIssuePersonAdapter.PersonViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.send_issue_person_item, parent, false);
            holder.img_radio = convertView.findViewById(R.id.img_radio);
            holder.tv_department = convertView.findViewById(R.id.tv_department);
            convertView.setTag(holder);
        } else {
            holder = (SendIssuePersonAdapter.PersonViewHolder) convertView.getTag();
        }

        holder.tv_department.setText(listData.get(position).getUser_name());

        if (listData.get(position).getIsChecked().equals("false")) {
            holder.img_radio.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox));
        } else {
            holder.img_radio.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox_pre));
        }


        return convertView;
    }


    public class PersonViewHolder {
        public ImageView img_radio;
        public TextView tv_department;
    }
}
