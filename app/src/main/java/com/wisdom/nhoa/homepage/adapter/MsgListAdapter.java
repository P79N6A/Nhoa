package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.util.ToastUtil;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.adapter
 * @class describe：消息列表适配器
 * @time 2018/4/2 10:40
 * @change
 */

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgHoler> implements View.OnClickListener {
    private Context context;
    private List<MsgListModel> listData;
    private RecyclerView recyclerView;
    private OnMsgItemClickListener onMsgItemClickListener;
    private OnMsgItemLongClickListener onMsgItemLongClickListener;

    public void setOnMsgItemClickListener(OnMsgItemClickListener onMsgItemClickListener) {
        this.onMsgItemClickListener = onMsgItemClickListener;
    }
    public void setOnMsgItemLongClickListener(OnMsgItemLongClickListener onMsgItemLongClickListener) {
        this.onMsgItemLongClickListener = onMsgItemLongClickListener;
    }
    public MsgListAdapter(Context context, List<MsgListModel> listData) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_msg_recycler_item, parent, false);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onMsgItemLongClickListener!=null){
                    int pos = recyclerView.getChildAdapterPosition(v);
                    onMsgItemLongClickListener.onMsgItemLongClicked(recyclerView, pos, v, listData.get(pos));
                }
                return true;
            }
        });
        return new MsgHoler(itemView);
    }

    @Override
    public void onBindViewHolder(MsgHoler holder, int position) {
        holder.tv_content.setText(listData.get(position).getMsgtitle());
        holder.tv_content.setTextColor(context.getResources().getColor(R.color.textcolor));
        if (listData.get(position).getIsread()==1){
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.grey_line));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /**
     * 子项点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (recyclerView != null) {
            int pos = recyclerView.getChildAdapterPosition(v);
            onMsgItemClickListener.onMsgItemClicked(recyclerView, pos, v, listData.get(pos));
        }
    }

    class MsgHoler extends RecyclerView.ViewHolder {
        TextView tv_content;

        public MsgHoler(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    public interface OnMsgItemClickListener {
        void onMsgItemClicked(RecyclerView recyclerView, int pos, View clickedView, MsgListModel msgListModel);
    }
    public interface OnMsgItemLongClickListener {
        void onMsgItemLongClicked(RecyclerView recyclerView, int pos, View clickedView, MsgListModel msgListModel);
    }
}
