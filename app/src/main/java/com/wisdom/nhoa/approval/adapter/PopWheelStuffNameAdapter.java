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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：弹出选择申请用品的滚轮中右侧的分类列表适配器
 * @time 2018/7/17 9:14
 * @change
 */
public class PopWheelStuffNameAdapter extends BaseAdapter {
    private Context context;
    private List<OfficeStuffModel.ChildBean> listData;

    public PopWheelStuffNameAdapter(Context context, List<OfficeStuffModel.ChildBean> listData) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_stuff_name, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvItemSubject.setText(listData.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvItemSubject;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
