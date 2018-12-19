package com.wisdom.nhoa.metting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.metting.model.MeetingSignUserModel;

import java.util.List;

/**
 * @author lxd
 * @ProjectName project： 签到人员适配器
 * @class package：
 * @class describe：MeetingSignUserAdapter
 * @time 16:01
 * @change
 */

public class MeetingSignUserAdapter extends BaseAdapter {
    private Context context;
    private List<MeetingSignUserModel> listData;

    public MeetingSignUserAdapter(Context context, List<MeetingSignUserModel> listData) {
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
        MeetingSignUserAdapter.signUserViewHolder holder;
        if (convertView == null) {
            holder = new MeetingSignUserAdapter.signUserViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_meeting_sign_user, parent, false);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_role_name = convertView.findViewById(R.id.tv_role_name);
            holder.tv_department = convertView.findViewById(R.id.tv_department);
            convertView.setTag(holder);
        } else {
            holder = (MeetingSignUserAdapter.signUserViewHolder) convertView.getTag();
        }
        holder.tv_username.setText(listData.get(position).getUser_name());
        holder.tv_role_name.setText(listData.get(position).getRole_name());
        holder.tv_department.setText(listData.get(position).getDep_name());
        return convertView;
    }

    class signUserViewHolder {
        TextView tv_username;
        TextView tv_role_name;
        TextView tv_department;
    }

}
