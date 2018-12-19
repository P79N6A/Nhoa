package com.wisdom.nhoa.supervision.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.ApprovalListModel;
import com.wisdom.nhoa.supervision.model.SupervisionModel;
import com.wisdom.nhoa.widget.CirclePrecentView;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
public class SupervisionAdapter extends BaseAdapter {
    private Context context;
    private List<SupervisionModel> listData;

    public SupervisionAdapter(Context context, List<SupervisionModel> listData) {
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
        ViewHolder    viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_supervision_list, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (listData.get(position).getStatus().equals("")){
            viewHolder.tvStatusContent.setText("无反馈");
            viewHolder.tvStatusContent.setTextColor(context.getResources().getColor(R.color.color_ff813d));
        }else {
            switch (listData.get(position).getStatus()) {
                case "0": {
                    viewHolder.tvStatusContent.setText("进行");
                    viewHolder.tvStatusContent.setTextColor(context.getResources().getColor(R.color.color_ff813d));
                }
                break;
                case "1": {
                    viewHolder.tvStatusContent.setText("完成");
                    viewHolder.tvStatusContent.setTextColor(context.getResources().getColor(R.color.color_27e766));
                }
                break;
                case "2": {
                    viewHolder.tvStatusContent.setText("超额完成");
                    viewHolder.tvStatusContent.setTextColor(context.getResources().getColor(R.color.color_f04545));
                }
                break;
            }
        }
        viewHolder.precentView.setmCurPrecent(Float.valueOf(listData.get(position).getPercentage()));
        viewHolder.tvNumberContent.setText(position+1+"");
        viewHolder.tvNameContent.setText(listData.get(position).getName());
        viewHolder.tvBeginTimeContent.setText(""+listData.get(position).getBegintime());
        viewHolder.tvEndTtimeContent.setText(""+listData.get(position).getEndtime());
        viewHolder.tvProgressContent.setText(""+listData.get(position).getPercentage()+"%");
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_number_content)
        AppCompatTextView tvNumberContent;
        @BindView(R.id.tv_name_content)
        AppCompatTextView tvNameContent;
        @BindView(R.id.tv_begin_time_content)
        AppCompatTextView tvBeginTimeContent;
        @BindView(R.id.tv_end_time_content)
        AppCompatTextView tvEndTtimeContent;
        @BindView(R.id.tv_status_content)
        AppCompatTextView tvStatusContent;
        @BindView(R.id.tv_progress_content)
        AppCompatTextView tvProgressContent;
        @BindView(R.id.cp_view)
        CirclePrecentView precentView;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}