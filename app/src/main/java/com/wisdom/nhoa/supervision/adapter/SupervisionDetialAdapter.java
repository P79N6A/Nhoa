package com.wisdom.nhoa.supervision.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.adapter.SendIssueTodoModelAdapter;
import com.wisdom.nhoa.sendreceive.model.SendManageModel;
import com.wisdom.nhoa.supervision.model.SupervisionModel;
import com.wisdom.nhoa.widget.CirclePrecentView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @authorzhanglichao
 * @date2018/7/26 10:51
 * @package_name com.wisdom.nhoa.supervision.adapter
 */
public class SupervisionDetialAdapter extends RecyclerView.Adapter<SupervisionDetialAdapter.ViewHolder> {
  private  List<SupervisionModel.ChildBean> mData;
   private Context mContext;
    public SupervisionDetialAdapter(Context context, List<SupervisionModel.ChildBean> childBeans) {
     mContext=context;
     mData=childBeans;
    }
    public void setItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_supervision_detailed_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_duty_content.setText(""+mData.get(position).getUsername());
        holder.tv_feedback_time_content.setText(""+mData.get(position).getEdittime());
        holder.tv_detailed_content.setText(""+mData.get(position).getContent());
        holder.tv_counts_content.setText(""+mData.get(position).getFrequency());
        holder.tv_progress_content.setText(""+mData.get(position).getUserpercentage()+"%");
        holder.cp_view.setmCurPrecent(Float.valueOf(mData.get(position).getUserpercentage()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(position,mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData==null?0:mData.size();
    }
   class ViewHolder extends RecyclerView.ViewHolder{
     AppCompatTextView tv_duty_content;
     AppCompatTextView tv_feedback_time_content;
     AppCompatTextView tv_detailed_content;
     AppCompatTextView tv_counts_content;
     AppCompatTextView tv_progress_content;
     CirclePrecentView cp_view;

     public ViewHolder(View itemView) {
         super(itemView);
         tv_duty_content=itemView.findViewById(R.id.tv_duty_content);
         tv_feedback_time_content=itemView.findViewById(R.id.tv_feedback_time_content);
         tv_detailed_content=itemView.findViewById(R.id.tv_detailed_content);
         tv_counts_content=itemView.findViewById(R.id.tv_counts_content);
         tv_progress_content=itemView.findViewById(R.id.tv_progress_content);
         cp_view=itemView.findViewById(R.id.cp_view);

     }
 }

 private onItemClickListener onItemClickListener;
    public interface onItemClickListener {
        void onItemClicked(int position ,SupervisionModel.ChildBean childBean);
    }


}
