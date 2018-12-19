package com.wisdom.nhoa.circulated.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.circulated.activity.DocumentReadDetailActivity;
import com.wisdom.nhoa.circulated.activity.FilePreviewActivity;
import com.wisdom.nhoa.circulated.model.CirculateConversationModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.adapter
 * @class describe：公文传阅会话界面适配器
 * @time 2018/3/26 15:43
 * @change
 */

public class CirculateConversationAdapter extends BaseAdapter {
    private Context context;
    private List<CirculateConversationModel> listData;
    public static final String TAG = CirculateConversationAdapter.class.getSimpleName();

    public CirculateConversationAdapter(Context context, List<CirculateConversationModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ConversationHolder holder;
        if (convertView == null) {
            holder = new ConversationHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.circulate_conversation_item, parent, false);
            holder.circulated_conversation_tv_time = convertView.findViewById(R.id.circulated_conversation_tv_time);
            holder.circulated_conversation_rl_left = convertView.findViewById(R.id.circulated_conversation_rl_left);
            holder.circulated_conversation_tv_dep_left = convertView.findViewById(R.id.circulated_conversation_tv_dep_left);
            holder.circulated_conversation_tv_filename_left = convertView.findViewById(R.id.circulated_conversation_tv_filename_left);
            holder.circulated_conversation_rl_right = convertView.findViewById(R.id.circulated_conversation_rl_right);
            holder.circulated_conversation_tv_detail = convertView.findViewById(R.id.circulated_conversation_tv_detail);
            holder.circulated_conversation_tv_filename_right = convertView.findViewById(R.id.circulated_conversation_tv_filename_right);
            holder.circulated_ll_right = convertView.findViewById(R.id.circulated_ll_right);
            holder.circulated_ll_left = convertView.findViewById(R.id.circulated_ll_left);
            convertView.setTag(holder);
        } else {
            holder = (ConversationHolder) convertView.getTag();
        }
        holder.circulated_conversation_tv_time.setText("" + listData.get(position).getCreatetime());

        if (ConstantString.IS_CREATER_TRUE.equals(
                listData.get(position).getIscreater()
        )) {
            //是创建者，布局显示在右侧
            holder.circulated_conversation_rl_left.setVisibility(View.GONE);
            holder.circulated_conversation_rl_right.setVisibility(View.VISIBLE);
            holder.circulated_conversation_tv_filename_right.setText("" + listData.get(position).getFilename());

        } else {
            //不是创建者，布局显示在左侧
            holder.circulated_conversation_rl_left.setVisibility(View.VISIBLE);
            holder.circulated_conversation_rl_right.setVisibility(View.GONE);
            holder.circulated_conversation_tv_dep_left.setText("" + listData.get(position).getCreater());
            holder.circulated_conversation_tv_filename_left.setText("" + listData.get(position).getFilename());
        }
        //左侧点击直接下载
        holder.circulated_ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FIXME  公文传阅的DOC预览
                Log.e("公文传阅预览路径",ConstantUrl.BASE_URL+listData.get(position).getFilepath());
                FilePreviewActivity.show(context
                        , ConstantUrl.BASE_URL+listData.get(position).getFilepath()
                        , listData.get(position).getFileid());
//                FileDisplayActivity.show(context,listData.get(position).getFilepath());

            }
        });
        //详情的点击事件
        holder.circulated_conversation_tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocumentReadDetailActivity.class);
                intent.putExtra("data", listData.get(position).getFileid());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ConversationHolder {
        TextView circulated_conversation_tv_time;
        RelativeLayout circulated_conversation_rl_left;
        TextView circulated_conversation_tv_dep_left;
        TextView circulated_conversation_tv_filename_left;
        TextView circulated_conversation_tv_filename_right;
        RelativeLayout circulated_conversation_rl_right;
        Button circulated_conversation_tv_detail;
        LinearLayout circulated_ll_right;
        LinearLayout circulated_ll_left;
    }


}
