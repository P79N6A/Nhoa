package com.wisdom.nhoa.meeting_new.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.meeting_new.model.MeetingMySponsorModel;

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
public class MeetingMySponsorListAdapter extends RecyclerView.Adapter<MeetingMySponsorListAdapter.MySponsorListHolder> implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<MeetingMySponsorModel> listData;
    private Context context;
    private OnChildItemClickedListener listener;

    public void setListener(OnChildItemClickedListener listener) {
        this.listener = listener;
    }

    public MeetingMySponsorListAdapter(List<MeetingMySponsorModel> listData, Context context) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_meeting_my_sponsor, null, false);
        itemView.setOnClickListener(this);
        return new MySponsorListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySponsorListHolder holder, int i) {
        // TODO: 2018/10/23 暂时没有发起部门字段
        holder.tvMeetingConvokeDepartment.setText(listData.get(i).getSureStatus());
        holder.tvMeetingMeetingTheme.setText(listData.get(i).getMeetName());
        holder.tvMeetingSetUpTime.setText(listData.get(i).getLaunchTime());
        holder.tvState2.setText(listData.get(i).getState());
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
        @BindView(R.id.tv_meeting_convoke_department)
        AppCompatTextView tvMeetingConvokeDepartment;
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
        void onChildItemClick(RecyclerView recyclerView, int pos, View view, MeetingMySponsorModel model);
    }

}
