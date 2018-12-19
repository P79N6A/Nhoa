package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.CalendarEventModel;
import com.wisdom.nhoa.homepage.model.LeaderScheduleModel;

import java.util.List;

/**
 * Created by ldf on 17/6/14.
 */

public class LeaderScheduleAdapter extends RecyclerView.Adapter {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private  List<LeaderScheduleModel> mdata;
    private View mHeaderView;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    private final static int HEAD_COUNT = 1;
    private int opened = -1;
    public LeaderScheduleAdapter(Context context , List<LeaderScheduleModel> data) {
        mdata=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView!=null&&viewType==TYPE_HEADER){
            return new HeadHolder(mHeaderView);
        }else {
            return new ViewHolder(layoutInflater.inflate(R.layout.leader_schedule_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LeaderScheduleAdapter.HeadHolder){

        }else if (holder instanceof LeaderScheduleAdapter.ViewHolder){
            LeaderScheduleAdapter.ViewHolder holder1= (ViewHolder) holder;
            holder1.bindView(position,mdata.get(position));
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(v,mdata.get(position),position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
     int   count=( mdata == null ? 0 : mdata.size());
        if (mHeaderView != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
          return TYPE_HEADER;
        }else {
         return  TYPE_CONTENT;
        }
    }
     public void addHeadView(View view){

        ViewGroup.LayoutParams layoutParams= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         view.setLayoutParams(layoutParams);
        mHeaderView=view;
        notifyItemInserted(0);
     }
    public  class ViewHolder extends RecyclerView.ViewHolder  {
        TextView start_time;
        TextView end_time;
        TextView content;
        TextView item_title;
        TextView item_leaders_name;
        RelativeLayout rl_item_content;
        RelativeLayout  rl_item_title;
        View line_view;
        ViewHolder(View view) {
            super(view);
            item_title  = (TextView) view.findViewById(R.id.item_title);
            item_leaders_name = (TextView) view.findViewById(R.id.item_leaders_name);
            end_time = (TextView) view.findViewById(R.id.endtime_item);
            start_time = (TextView) view.findViewById(R.id.starttime_item);
            content = (TextView) view.findViewById(R.id.item_content);
            line_view=(View) view.findViewById(R.id.top_line_view);
            rl_item_content= view.findViewById(R.id.rl_item_content);
            rl_item_title= view.findViewById(R.id.rl_item_title);
//            rl_item_title.setOnClickListener(this);
        }

        void bindView(int pos, LeaderScheduleModel bean) {
            start_time.setText(bean.getStarttime());
            end_time.setText(bean.getEndtime());
//            content.setText(bean.getContent());
            item_title.setText(bean.getTitle());
            item_leaders_name.setText(bean.getOwnername());
            if (pos==0){
                line_view.setVisibility(View.VISIBLE);
            }
            if (pos == opened){
                rl_item_content.setVisibility(View.VISIBLE);
            } else{
                rl_item_content.setVisibility(View.GONE);
            }

        }


//        @Override
//        public void onClick(View v) {
//            if (opened == getAdapterPosition()) {
//                //当点击的item已经被展开了, 就关闭.
//                opened = -1;
//                notifyItemChanged(getAdapterPosition());
//            } else {
//                int oldOpened = opened;
//                opened = getAdapterPosition();
//                notifyItemChanged(oldOpened);
//                notifyItemChanged(opened);
//            }
//        }

    }

    // 头部
    private class HeadHolder extends RecyclerView.ViewHolder{
        public HeadHolder(View itemView) {
            super(itemView);
        }
    }
    public int getContentSize(){
        return mdata.size();
    }
    public void SetOnItemClickListener(OnItemClickListener onItemClick){
        onItemClickListener=onItemClick;
    }
    private static  OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
      void  OnItemClick(View view, LeaderScheduleModel data, int position);

    }

}
