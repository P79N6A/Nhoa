package com.wisdom.nhoa.metting.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.activity.QRcodeDetailsActivity;
import com.wisdom.nhoa.metting.model.MeetingListModel;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import java.util.List;

/**
 * author：
 * date:
 */

public class MeetingListAdapter extends BaseAdapter {

    private Context context;
    private List<MeetingListModel> listData;
    public static final String TAG = MeetingListAdapter.class.getSimpleName();

    public MeetingListAdapter(Context context, List<MeetingListModel> listData) {
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
        MeetingListAdapter.NoticeViewHolder holder;
        // FIXME: 2018/3/29 暂时注销掉复用item代码，否则Glide报错：You must not call setTag() on a view Glide is targetin 现在着急要，没时间大改了。
//        if (convertView == null) {
        holder = new NoticeViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.homepage_meeting_item, parent, false);
        holder.tv_meetingTopic = convertView.findViewById(R.id.tv_meetingTopic);
        holder.img_QRcode = convertView.findViewById(R.id.img_QRcode);
        convertView.setTag(holder);
//        } else {
//            holder = (MeetingListAdapter.NoticeViewHolder) convertView.getTag();
//        }
        holder.tv_meetingTopic.setText(listData.get(position).getMeetingtopic());
        Glide.with(context).load(HttpUtil.getAbsolteUrl(listData.get(position).getQrcodeurl())).into(holder.img_QRcode);
        holder.img_QRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QRcodeDetailsActivity.class);
                Log.i(TAG, "qrcodeUrl: "+listData.get(position).getQrcodeurl());
                intent.putExtra("qrcodeUrl", HttpUtil.getAbsolteUrl(listData.get(position).getQrcodeurl()));
                intent.putExtra("mettingTitle", listData.get(position).getMeetingtopic());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class NoticeViewHolder {
        TextView tv_meetingTopic;
        ImageView img_QRcode;
    }

}
