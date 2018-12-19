package com.wisdom.nhoa.sendreceive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.model.SendManageModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：
 * @time 2018/3/8 10:37
 * @change
 */

public class SendIssueTodoModelAdapter extends RecyclerView.Adapter<SendIssueTodoModelAdapter.IssueTodoHolder> implements View.OnClickListener {
    private Context context;
    private List<SendManageModel> listData;
    private RecyclerView recyclerView;
    private IssueTodoItemClickListener issueTodoItemClickListener;
    public static final String TAG = SendIssueTodoModelAdapter.class.getSimpleName();

    public void setIssueTodoItemClickListener(IssueTodoItemClickListener issueTodoItemClickListener) {
        this.issueTodoItemClickListener = issueTodoItemClickListener;
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

    public SendIssueTodoModelAdapter(Context context, List<SendManageModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public IssueTodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.approval_item_issue_todo, parent, false);
        view.setOnClickListener(this);
        return new IssueTodoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueTodoHolder holder, int position) {
        holder.tv_title.setText(listData.get(position).getDoctitle());
        if (listData.get(position).getDocnumber() == null || "".equals(listData.get(position).getDocnumber())) {
            //未分配文号
            holder.tv_subject.setText("未分配文号");
            holder.tv_subject.setTextColor(context.getResources().getColor(R.color.chartreuse));
        } else {
            //分配了文号
            holder.tv_subject.setText(listData.get(position).getDocnumber());
            holder.tv_subject.setTextColor(context.getResources().getColor(R.color.crimson));
        }

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
        if (issueTodoItemClickListener != null && recyclerView != null) {
            int pos = recyclerView.getChildAdapterPosition(v);
            issueTodoItemClickListener.onIssueTodoItemClicked(recyclerView, pos, v, listData.get(pos));
        }
    }

    class IssueTodoHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_subject;

        public IssueTodoHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.approval_item_tv_law_title);
            tv_subject = itemView.findViewById(R.id.approval_item_tv_law_subject);
        }
    }

    public interface IssueTodoItemClickListener {
        void onIssueTodoItemClicked(RecyclerView recyclerView, int pos, View clickView, SendManageModel sendManageModel);
    }

}
