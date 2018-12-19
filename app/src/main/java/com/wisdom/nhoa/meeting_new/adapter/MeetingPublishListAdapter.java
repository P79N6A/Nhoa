package com.wisdom.nhoa.meeting_new.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.meeting_new.model.MeetingListModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author HanXueFeng
 * @ProjectName project： printer
 * @class package：com.kanglinkeji.printer.orders.adapter
 * @class describe：会议 发布列表
 * @time 2018/10/17 11:04
 * @change
 */
public class MeetingPublishListAdapter extends RecyclerView.Adapter<MeetingPublishListAdapter.MySponsorListHolder> implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<MeetingListModel> listData;
    private Context context;
    private OnChildItemClickedListener listener;

    public void setListener(OnChildItemClickedListener listener) {
        this.listener = listener;
    }

    public MeetingPublishListAdapter(List<MeetingListModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @NonNull
    @Override
    public MySponsorListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_publish_meeting_list, null, false);
        itemView.setOnClickListener(this);
        return new MySponsorListHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MySponsorListHolder holder, int i) {
        holder.tvMeetingSubject.setText(listData.get(i).getTheme());
        switch (listData.get(i).getStatus()) {
            case ConstantString.MEETING_PUBLISH_STATUS_PASS: {
                holder.tvState.setText("审批通过");
                holder.tvState.setTextColor(Color.parseColor("#00d395"));
            }
            break;
            case ConstantString.MEETING_PUBLISH_STATUS_PUBLISHED: {
                holder.tvState.setText("已发布");
                holder.tvState.setTextColor(Color.parseColor("#333333"));
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /**
     * 列表子项的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (listener != null) {
            int pos = recyclerView.getChildAdapterPosition(v);
            listener.onChildItemClick(recyclerView, pos, v, listData.get(pos));
        }
    }


    class MySponsorListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_tag)
        android.widget.ImageView ivTag;
        @BindView(R.id.tv_meeting_subject)
        android.widget.TextView tvMeetingSubject;
        @BindView(R.id.tv_state)
        android.widget.TextView tvState;

        public MySponsorListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 点击事件接口
     */
    public interface OnChildItemClickedListener {
        void onChildItemClick(RecyclerView recyclerView, int pos, View view, MeetingListModel model);
    }

}
