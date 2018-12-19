package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.CalendarEventModel;

import java.util.List;

/**
 * Created by ldf on 17/6/14.
 */

public class CalendarEventAdapter extends RecyclerView.Adapter {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private  List<CalendarEventModel> mdata;
    private View mHeaderView;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    private final static int HEAD_COUNT = 1;
    public CalendarEventAdapter(Context context , List<CalendarEventModel> data) {
        mdata=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView!=null&&viewType==TYPE_HEADER){
            return new HeadHolder(mHeaderView);
        }else {
            return new ViewHolder(layoutInflater.inflate(R.layout.calendar_event_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CalendarEventAdapter.HeadHolder){

        }else if (holder instanceof CalendarEventAdapter.ViewHolder){
            CalendarEventAdapter.ViewHolder holder1= (ViewHolder) holder;
            holder1.start_time.setText(mdata.get(position).getStarttime());
            holder1.end_time.setText(mdata.get(position).getEndtime());
            holder1.schdule_title.setText(mdata.get(position).getTitle());
            if (position==0){
                holder1.line_view.setVisibility(View.VISIBLE);
            }
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v,mdata,position);
                    }
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView start_time;
        TextView end_time;
        TextView schdule_title;
        View line_view;
        ViewHolder(View view) {
            super(view);
            start_time = (TextView) view.findViewById(R.id.starttime_item);
            end_time = (TextView) view.findViewById(R.id.endtime_item);
            schdule_title = (TextView) view.findViewById(R.id.schdule_title);
            line_view=(View) view.findViewById(R.id.top_line_view);

        }
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
      void  OnItemClick(View view,List<CalendarEventModel> data,int position);

    }
}
