package com.wisdom.nhoa.supervision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.supervision.model.BeSupervisedPersonFeedBackModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.supervision.adapter
 * @class describe：被督办人反馈信息详细列表 适配器
 * @time 2018/7/26 11:04
 * @change
 */
public class BeSupervisedPersonFeedBackListAdapter extends BaseAdapter {
    private Context context;
    private List<BeSupervisedPersonFeedBackModel> listData;

    public BeSupervisedPersonFeedBackListAdapter(Context context, List<BeSupervisedPersonFeedBackModel> listData) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feed_back_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTime.setText(listData.get(position).getEdittime());
        viewHolder.tvValueContent.setText(listData.get(position).getContent());
        viewHolder.tvValueStartTime.setText(listData.get(position).getWorktime());
        viewHolder.tvValuePercent.setText(listData.get(position).getUserpercentage() + "%");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_value_percent)
        TextView tvValuePercent;
        @BindView(R.id.tv_value_content)
        TextView tvValueContent;
        @BindView(R.id.tv_value_start_time)
        TextView tvValueStartTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
