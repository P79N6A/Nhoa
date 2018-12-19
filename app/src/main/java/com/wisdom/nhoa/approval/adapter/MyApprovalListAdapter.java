package com.wisdom.nhoa.approval.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.ApprovalListModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
* 类 {类名称} 我的申请列表adapter
* @author    lxd
* @date    2018-7-24
*/
public class MyApprovalListAdapter extends BaseAdapter {
    private Context context;
    private List<ApprovalListModel> listData;

    public MyApprovalListAdapter(Context context, List<ApprovalListModel> listData) {
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

        MySponsorListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_approval_list, null, false);
            viewHolder = new MySponsorListAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MySponsorListAdapter.ViewHolder) convertView.getTag();
        }
        switch (listData.get(position).getType()) {
            case ConstantString.APPROVAL_TYPE_XJ: {
                viewHolder.tvTypeContent.setText("休假-"+listData.get(position).getTypename());
            }
            break;
            case ConstantString.APPROVAL_TYPE_CC: {
                viewHolder.tvTypeContent.setText("出差");
            }
            break;
            case ConstantString.APPROVAL_TYPE_YP: {
                viewHolder.tvTypeContent.setText("办公用品");
            }
            break;
        }
        switch (listData.get(position).getStatus()) {
            case ConstantString.APPROVAL_STATUS_CHECK: {
                viewHolder.tvStateContent.setText("待审核");
                viewHolder.tvStateContent.setTextColor(context.getResources().getColor(R.color.color_ff813d));
            }
            break;
            case ConstantString.APPROVAL_STATUS_PASS: {
                viewHolder.tvStateContent.setText("审核通过");
                viewHolder.tvStateContent.setTextColor(context.getResources().getColor(R.color.color_27e766));
            }
            break;
            case ConstantString.APPROVAL_STATUS_BACK: {
                viewHolder.tvStateContent.setText("审核退回");
                viewHolder.tvStateContent.setTextColor(context.getResources().getColor(R.color.color_f04545));
            }
            break;
        }
        viewHolder.tvApplyTimeContent.setText(listData.get(position).getApplytime());
        viewHolder.tvApplyPersonContent.setText(listData.get(position).getAuditorname());

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_type_content)
        AppCompatTextView tvTypeContent;
        @BindView(R.id.tv_state_content)
        AppCompatTextView tvStateContent;
        @BindView(R.id.tv_apply_time_content)
        AppCompatTextView tvApplyTimeContent;
        @BindView(R.id.tv_apply_person_content)
        AppCompatTextView tvApplyPersonContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
