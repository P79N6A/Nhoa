package com.wisdom.nhoa.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.model.DraftListModel;

import java.util.List;

/**
 * author：
 * date:
 */

public class DraftListAdapter extends RecyclerView.Adapter<DraftListAdapter.BaseViewHolder> {
    List<DraftListModel> mdata;
    public DraftListAdapter(List<DraftListModel>  data) {
    this.mdata=data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_list_item,parent,false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        holder.tv_draft_title.setText(mdata.get(position).getDoctitle()+"");
        holder.tv_draft_title.setText(mdata.get(position).getDocnumber()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata==null?0:mdata.size();
    }

    public   static  class  BaseViewHolder extends RecyclerView.ViewHolder{
        TextView tv_draft_title;
        TextView tv_draft_docnumber ;
       public BaseViewHolder(View itemView) {
           super(itemView);
           tv_draft_title=itemView.findViewById(R.id.tv_draft_title)  ;
           tv_draft_docnumber=itemView.findViewById(R.id.tv_draft_docnumber)  ;

       }
   }
   private OnClickListener onClickListener;
   public void setOnItemClickListener(OnClickListener listener){
       onClickListener=listener;
   }
    public interface OnClickListener{
        void onItemClick(View view,int position);
    }
}
