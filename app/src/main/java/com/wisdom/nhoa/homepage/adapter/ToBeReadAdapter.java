package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.ToBeReadModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.adapter
 * @class describe：消息列表适配器
 * @time 2018/4/2 10:40
 * @change
 */

public class ToBeReadAdapter extends RecyclerView.Adapter<ToBeReadAdapter.MsgHoler> implements View.OnClickListener {
    private Context context;
    private List<ToBeReadModel> listData;
    private RecyclerView recyclerView;
    private OnToBeReadItemClickListener onToBeReadItemClickListener;

    public void setOnToBeReadItemClickListener(OnToBeReadItemClickListener onToBeReadItemClickListener) {
        this.onToBeReadItemClickListener = onToBeReadItemClickListener;
    }

    public ToBeReadAdapter(Context context, List<ToBeReadModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public MsgHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_tobe_read_recycler_item, parent, false);
        itemView.setOnClickListener(this);
        return new MsgHoler(itemView);
    }

    @Override
    public void onBindViewHolder(MsgHoler holder, int position) {
        holder.tv_content.setText(listData.get(position).getNoticetitle());
        holder.tv_group_name.setText("此文件来自传阅组\""+listData.get(position).getGroupName()+"\"");
        holder.tv_upload_time.setText(listData.get(position).getUpTime());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /**
     * 子项点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (recyclerView != null) {
            int pos = recyclerView.getChildAdapterPosition(v);
            onToBeReadItemClickListener.onToBeReadItemClicked(recyclerView, pos, v, listData.get(pos));
        }
    }

    class MsgHoler extends RecyclerView.ViewHolder {
        TextView tv_content;
        TextView tv_group_name;
        TextView tv_upload_time;
        public MsgHoler(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            tv_upload_time = itemView.findViewById(R.id.tv_upload_time);
        }
    }

    public interface OnToBeReadItemClickListener {
        void onToBeReadItemClicked(RecyclerView recyclerView, int pos, View clickedView, ToBeReadModel toBeReadModel);
    }
}
