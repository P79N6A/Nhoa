package com.wisdom.nhoa.approval.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.CopyToSelectedModel;
import com.wisdom.nhoa.approval.model.OrganizationModel;
import com.wisdom.nhoa.widget.GridViewAtMost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hanxuefeng on 2018/7/24.
 * 新版本的抄送人适配器
 */

public class ChooseCopyToAdapter extends BaseAdapter {
    public static final String TAG = ChooseCopyToAdapter.class.getSimpleName();
    private Context context;
    int chooseCount = 0;//总共选中的数量总和
    int itemChooseCount = 0;//每一个小项选中的数量总和
    int headIndex = 0;//头部位置的索引
    private OnChooseCountChangeListener onChooseCountChangeListener;

    private List<OrganizationModel> listData;
    private List<Integer> listCount = new ArrayList<>();//各个类别中选中的数量集合
    private List<Map<Integer, String>> ischeck;
    private List<List<OrganizationModel.ChiledBean>> childData;
    private CopyToSelectedModel copyToSelectedModel;

    public void setOnChooseCountChangeListener(OnChooseCountChangeListener onChooseCountChangeListener) {
        this.onChooseCountChangeListener = onChooseCountChangeListener;
    }

    public ChooseCopyToAdapter(Context context, List<OrganizationModel> listData, CopyToSelectedModel copyToSelectedModel) {
        this.context = context;
        this.listData = listData;
        this.copyToSelectedModel = copyToSelectedModel;
        //构造子项数据源
        childData = new ArrayList<List<OrganizationModel.ChiledBean>>();
        for (int i = 0; i < listData.size(); i++) {
            childData.add(listData.get(i).getChiled());
        }
        //创建被选中的项纪录集合

        //如果没用户之前选择的信息的话，重新创建
        ischeck = new ArrayList<>();
        for (int j = 0; j < listData.size(); j++) {
            //创建记录每一组选中状态的数字集合
            listCount.add(new Integer(0));
            if (copyToSelectedModel == null) {
                Map<Integer, String> map = new HashMap<Integer, String>();
                for (int i = 0; i < listData.get(j).getChiled().size(); i++) {
                    map.put(i, "false");
                }
                ischeck.add(map);
            } else {
                ischeck = copyToSelectedModel.getIscheck();
            }
        }


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
    public View getView(final int positionParent, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_copy_to, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置头部
        viewHolder.tvDepartment.setText(listData.get(positionParent).getName() + "(");
        viewHolder.tvTotalNum.setText(listData.get(positionParent).getChiled().size() + ")");
        if (copyToSelectedModel == null) {
            viewHolder.tvChooseNum.setText("0/");
        } else {
            int count = 0;
            int totalCount = 0;
            //遍历列表，统计每个分组用户选中的抄送人数量。
            for (int j = 0; j < listData.get(positionParent).getChiled().size(); j++) {
                if ("true".equals(ischeck.get(positionParent).get(j))) {
                    count++;
                }
            }
            //统计用户之前选中的总数量
            //遍历列表，找到用户选中的抄送人，然后进行拼接。
            for (int i = 0; i < ischeck.size(); i++) {
                for (int k = 0; k < listData.get(i).getChiled().size(); k++) {
                    if ("true".equals(ischeck.get(i).get(k))) {
                        totalCount++;
                    }
                }
            }
            chooseCount = totalCount;
            //更新本地分组统计数据的变量,方便用户反选
            listCount.remove(positionParent);
            listCount.add(positionParent, count);
            //设置用户之前选中的每组数量总和显示
            viewHolder.tvChooseNum.setText(count + "/");
        }


        //点击头部控制子项的显隐性
        viewHolder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.gvChild.getVisibility() == View.VISIBLE) {
                    viewHolder.gvChild.setVisibility(View.GONE);
                } else {
                    viewHolder.gvChild.setVisibility(View.VISIBLE);
                }
            }
        });
        //设置头部下面的位置
        final GridViewItemAdapter viewItemAdapter = new GridViewItemAdapter(context, listData.get(positionParent).getChiled(), ischeck, positionParent);
        viewHolder.gvChild.setVisibility(View.GONE);
        viewHolder.gvChild.setAdapter(viewItemAdapter);

        viewHolder.gvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 改变界面选中效果，并纪录该Item选中状态
                GridViewItemAdapter.ViewHolder holder = (GridViewItemAdapter.ViewHolder) view.getTag();
                Log.i(TAG, "positionParent: " + positionParent);
                if (viewItemAdapter.ischeck.get(positionParent).get(position).equals("true")) {
                    viewItemAdapter.ischeck.get(positionParent).put(position, "false");
                    holder.ivCheckbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox));
                    itemChooseCount = listCount.get(positionParent) - 1;
                    listCount.remove(positionParent);
                    listCount.add(positionParent, itemChooseCount);
                    chooseCount--;
                } else {
                    viewItemAdapter.ischeck.get(positionParent).put(position, "true");
                    holder.ivCheckbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox_pre));
                    itemChooseCount = listCount.get(positionParent) + 1;
                    listCount.remove(positionParent);
                    listCount.add(positionParent, itemChooseCount);
                    chooseCount++;
                }
                onChooseCountChangeListener.OnChooseCountChange(positionParent, chooseCount, itemChooseCount, ischeck);
            }

        });
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_department)
        TextView tvDepartment;
        @BindView(R.id.tv_choose_num)
        TextView tvChooseNum;
        @BindView(R.id.tv_total_num)
        TextView tvTotalNum;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;
        @BindView(R.id.gv_child)
        GridViewAtMost gvChild;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public interface OnChooseCountChangeListener {
        void OnChooseCountChange(int groupPosition, int chooseCount, int itemChooseCount, List<Map<Integer, String>> ischeck);
    }
}
