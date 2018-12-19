package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.NoticeModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.adapter
 * @class describe：
 * @time 2018/3/25 14:26
 * @change
 */

public class NoticeAdapter extends BaseAdapter {
    private Context context;
    private List<NoticeModel> listData;

    public NoticeAdapter(Context context, List<NoticeModel> listData) {
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
        NoticeViewHolder holder;
        if (convertView == null) {
            holder = new NoticeViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_listview_item_notice, parent, false);
            holder.tv_title = convertView.findViewById(R.id.notice_tv_title);
            holder.tv_time = convertView.findViewById(R.id.notice_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (NoticeViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(listData.get(position).getNoticetitle());
        holder.tv_time.setText(listData.get(position).getCreatetime());
        return convertView;
    }

    class NoticeViewHolder {
        TextView tv_title;
        TextView tv_time;
    }


}
