package com.wisdom.nhoa.meeting_new.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.meeting_new.model.MyCheckedApplyMeetingRoomModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HanXueFeng
 * @ProjectName project： printer
 * @class package：com.kanglinkeji.printer.orders.adapter
 * @class describe：我审核的列表
 * @time 2018/10/17 11:04
 * @change
 */
public class MyCheckedMeetingRoomListAdapter extends RecyclerView.Adapter<MyCheckedMeetingRoomListAdapter.MySponsorListHolder> implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<MyCheckedApplyMeetingRoomModel> listData;
    private Context context;
    private OnChildItemClickedListener listener;

    public void setListener(OnChildItemClickedListener listener) {
        this.listener = listener;
    }

    public MyCheckedMeetingRoomListAdapter(List<MyCheckedApplyMeetingRoomModel> listData, Context context) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_my_checked_meeting_room, null, false);
        itemView.setOnClickListener(this);
        return new MySponsorListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySponsorListHolder holder, int i) {
        holder.tvMeetingRoom.setText(listData.get(i).getBdrmName());
        holder.tvMeetingMeetingTheme.setText(listData.get(i).getTheme());
        holder.tvMeetingSetUpTime.setText(listData.get(i).getStaTime());
        switch (listData.get(i).getStatus()) {
            case ConstantString.APPLY_MEETING_ROOM_STATUS_RETURN: {
                //审批退回
                holder.tvState2.setText("审核退回");
            }
            break;
            case ConstantString.APPLY_MEETING_ROOM_STATUS_PASS: {
                //审核通过
                holder.tvState2.setText("审核通过");
            }
            break;
            case ConstantString.APPLY_MEETING_ROOM_STATUS_PUBLISH: {
                //审核通过并已发布会议
                holder.tvState2.setText("已发布");
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
        @BindView(R.id.tv_meeting_meeting_theme)
        AppCompatTextView tvMeetingMeetingTheme;
        @BindView(R.id.tv_meeting_room)
        AppCompatTextView tvMeetingRoom;
        @BindView(R.id.tv_meeting_set_up_time)
        AppCompatTextView tvMeetingSetUpTime;
        @BindView(R.id.tv_state2)
        AppCompatTextView tvState2;

        public MySponsorListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 点击事件接口
     */
    public interface OnChildItemClickedListener {
        void onChildItemClick(RecyclerView recyclerView, int pos, View view, MyCheckedApplyMeetingRoomModel model);
    }

}
