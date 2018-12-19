package com.wisdom.nhoa.approval.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.OfficeStuffModel;
import com.wisdom.nhoa.approval.model.OrganizationModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：弹出选择审批人的滚轮中左侧的分类列表适配器
 * @time 2018/7/17 9:14
 * @change
 */
public class PopWheelPeopleTreeAdapter extends BaseAdapter {
    private Context context;
    private List<OrganizationModel> listData;
    private int selectedPosition =-1;// 选中的位置

    public PopWheelPeopleTreeAdapter(Context context, List<OrganizationModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_stuff_subject, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvItemSubject.setText(listData.get(position).getName());
        if (position % 2 == 0) {
            if (selectedPosition == position) {
                convertView.setSelected(true);
                convertView.setPressed(true);
                convertView.setBackgroundColor(Color.parseColor("#F0F0F0"));
//                viewHolder.iv.setImageResource(R.drawable.sex_down);
            } else {
                convertView.setSelected(false);
                convertView.setPressed(false);
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//                holder.iv.setImageResource(R.drawable.sex_nor);
            }
        } else {
            if (selectedPosition == position) {
                convertView.setSelected(true);
                convertView.setPressed(true);
//                ViewHolder.iv.setImageResource(R.drawable.sex_down);
                convertView.setBackgroundColor(Color.parseColor("#F0F0F0"));
            } else {
                convertView.setSelected(false);
                convertView.setPressed(false);
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//                ViewHolder.iv.setImageResource(R.drawable.sex_nor);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_subject)
        TextView tvItemSubject;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
