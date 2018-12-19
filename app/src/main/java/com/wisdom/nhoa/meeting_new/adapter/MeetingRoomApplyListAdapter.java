package com.wisdom.nhoa.meeting_new.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.meeting_new.model.ApplyMeetingRoomListModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HanXueFeng
 * @ProjectName project： printer
 * @class package：com.kanglinkeji.printer.orders.adapter
 * @class describe：会议 我发起的列表
 * @time 2018/10/17 11:04
 * @change
 */
public class MeetingRoomApplyListAdapter extends RecyclerView.Adapter<MeetingRoomApplyListAdapter.ListHolder> implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<ApplyMeetingRoomListModel> listData;
    private Context context;
    private OnChildItemClickedListener listener;

    public void setListener(OnChildItemClickedListener listener) {
        this.listener = listener;
    }

    public MeetingRoomApplyListAdapter(List<ApplyMeetingRoomListModel> listData, Context context) {
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
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_apply_meeting_room, null, false);
        itemView.setOnClickListener(this);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int i) {
        holder.tvMeetingSubject.setText(listData.get(i).getTheme());
        if (listData.get(i).getStatusCode() != null) {
            //非待审状态
            switch (listData.get(i).getStatusCode()) {
                case "": {//待审状态
                    holder.tvState.setTextColor(Color.parseColor("#2196f3"));
                }
                break;
                case ConstantString.APPLY_MEETING_ROOM_STATUS_PUBLISH: {//已发布
                    //已发布
                    holder.tvState.setTextColor(Color.parseColor("#333333"));
                }
                break;
                case ConstantString.APPLY_MEETING_ROOM_STATUS_PASS: {//审批通过
                    //审批通过
                    holder.tvState.setTextColor(Color.parseColor("#00d395"));
                }
                break;
                case ConstantString.APPLY_MEETING_ROOM_STATUS_RETURN: {//审批退回
                    //审批退回
                    holder.tvState.setTextColor(Color.parseColor("#f86f6f"));
                }
                break;

            }
        } else {
            //待审状态
            holder.tvState.setTextColor(Color.parseColor("#2196f3"));
        }
        holder.tvState.setText(listData.get(i).getStatus());
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


    class ListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_tag)
        ImageView ivTag;
        @BindView(R.id.tv_meeting_subject)
        TextView tvMeetingSubject;
        @BindView(R.id.tv_state)
        TextView tvState;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 点击事件接口
     */
    public interface OnChildItemClickedListener {
        void onChildItemClick(RecyclerView recyclerView, int pos, View view, ApplyMeetingRoomListModel model);
    }


}
