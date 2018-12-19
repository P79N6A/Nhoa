package com.wisdom.nhoa.meeting_new.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.activity.QRcodeDetailsActivity;
import com.wisdom.nhoa.meeting_new.model.RegistrationModel;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类 {类名称}  {会议签到列表}
 *
 * @author lxd
 * @date 2018-10-29
 */
public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationListHolder> implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<RegistrationModel> listData;
    private Context context;
    private OnChildItemClickedListener listener;
    public static final String TAG = RegistrationAdapter.class.getSimpleName();

    public void setListener(OnChildItemClickedListener listener) {
        this.listener = listener;
    }

    public RegistrationAdapter(List<RegistrationModel> listData, Context context) {
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
    public RegistrationAdapter.RegistrationListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_registration, null, false);
        itemView.setOnClickListener(this);
        return new RegistrationAdapter.RegistrationListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RegistrationAdapter.RegistrationListHolder holder, final int i) {
        holder.tv_meeting_subject.setText(listData.get(i).getMeetingtopic());
        if (ConstantString.MEETING_STATE_READY.equals(listData.get(i).getState())) {
            holder.tv_state.setText("未开始");
        } else if (ConstantString.MEETING_STATE_DOING.equals(listData.get(i).getState())) {
            holder.tv_state.setText("进行中");
        } else if (ConstantString.MEETING_STATE_OVER.equals(listData.get(i).getState())) {
            holder.tv_state.setText("已结束");
        }
        Glide.with(context).load(HttpUtil.getAbsolteUrl(listData.get(i).getQrcodeurl())).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
               holder.img_QRcode.setClickable(false);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.img_QRcode.setClickable(true);
                return false;
            }
        }).into(holder.img_QRcode);
        holder.img_QRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QRcodeDetailsActivity.class);
                Log.i(TAG, "qrcodeUrl: " + listData.get(i).getQrcodeurl());
                intent.putExtra("qrcodeUrl", HttpUtil.getAbsolteUrl(listData.get(i).getQrcodeurl()));
                intent.putExtra("mettingTitle", listData.get(i).getMeetingtopic());
                context.startActivity(intent);
            }
        });
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


    class RegistrationListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_meeting_subject)
        TextView tv_meeting_subject;
        @BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.img_QRcode)
        ImageView img_QRcode;

        public RegistrationListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 点击事件接口
     */
    public interface OnChildItemClickedListener {
        void onChildItemClick(RecyclerView recyclerView, int pos, View view, RegistrationModel model);
    }

}
