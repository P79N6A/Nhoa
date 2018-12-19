package com.wisdom.nhoa.approval.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.OrganizationModel;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：
 * @time 2018/7/24 9:30
 * @change
 */
public class GridViewItemAdapter extends BaseAdapter {
    public List<Map<Integer, String>> ischeck;
    private Context context;
    private List<OrganizationModel.ChiledBean> chiledBeanList;
    public static final String TAG = GridViewItemAdapter.class.getSimpleName();
    private int groupId;

    public GridViewItemAdapter(Context context, List<OrganizationModel.ChiledBean> chiledBeanList, List<Map<Integer, String>> ischeck, int groupId) {
        this.ischeck = ischeck;
        this.context = context;
        this.chiledBeanList = chiledBeanList;
        this.groupId = groupId;
    }

    public GridViewItemAdapter(Context context, List<OrganizationModel.ChiledBean> chiledBeanList) {
        this.context = context;
        this.chiledBeanList = chiledBeanList;
//            ischeck = new HashMap<Integer, String>();
//            Log.i(TAG, "GridViewItemAdapter: **********************");
//            for (int i = 0; i < chiledBeanList.size(); i++) {
//                ischeck.put(i, "false");
//            }
    }

    @Override
    public int getCount() {
        return chiledBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return chiledBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_copy_to_person, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(chiledBeanList.get(position).getName());
        if (ischeck.get(groupId).get(position).equals("true")) {
            viewHolder.ivCheckbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox_pre));
        } else {
            viewHolder.ivCheckbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox));
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_checkbox)
        ImageView ivCheckbox;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
