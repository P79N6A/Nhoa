package com.wisdom.nhoa.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.callback.ParticpantCallBack;
import com.wisdom.nhoa.meeting_new.model.ParticpantModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxd
 * @ProjectName project：参会人员适配器
 * @class package：
 * @class describe：ParticpantAdapter
 * @time 9:00
 * @change
 */
public class ParticpantAdapter extends BaseAdapter {

    private Context context;
    private List<ParticpantModel> listData;

    public static Map<Integer, String> ischeck;
    private ParticpantCallBack mParticpantCallBack;


    public ParticpantAdapter(Context context, List<ParticpantModel> listData) {
        this.context = context;
        this.listData = listData;
        ischeck = new HashMap<Integer, String>();
        for (int i = 0; i < listData.size(); i++) {
            ischeck.put(i, "false");
        }
    }

//    public void setcheck() {
//        for (int i = 0; i < listData.size(); i++) {
//            map.put("ischeck", "false");
//            list.add(i, map);
//        }
//    }

//    public void setCallBack(ParticpantCallBack particpantCallBack) {
//        mParticpantCallBack = particpantCallBack;
//        mParticpantCallBack.getdata(listData, list);
//    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ParticpantAdapter.ParticpantHolder holder;
        if (convertView == null) {
            holder = new ParticpantAdapter.ParticpantHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.meeting_particant_item, parent, false);
            holder.tv_particant_name = convertView.findViewById(R.id.tv_particant_name);
            holder.iv_checkbox = convertView.findViewById(R.id.iv_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ParticpantAdapter.ParticpantHolder) convertView.getTag();
        }
        holder.tv_particant_name.setText(listData.get(position).getUser_name());
        if (ischeck.get(position).equals("true")) {
            holder.iv_checkbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox_pre));
        } else {
            holder.iv_checkbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox));
        }

//        holder.iv_checkbox.setTag(position);
//        holder.iv_checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = (int) holder.iv_checkbox.getTag();
//                if (list.get(pos).get("ischeck").equals("true")) {
//                    HashMap<String, Object> map = new HashMap<String, Object>();
//                    map.put("ischeck", "false");
//                    list.set(pos, map);
//                    holder.iv_checkbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox));
//                } else {
//                    HashMap<String, Object> map = new HashMap<String, Object>();
//                    map.put("ischeck", "true");
//                    list.set(pos, map);
//                    holder.iv_checkbox.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox_pre));
//                }
//            }
//        });
        return convertView;
    }

    public class ParticpantHolder {
        public ImageView iv_checkbox;
        public TextView tv_particant_name;
    }

}

