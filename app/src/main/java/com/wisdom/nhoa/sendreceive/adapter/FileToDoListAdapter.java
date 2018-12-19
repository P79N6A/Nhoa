package com.wisdom.nhoa.sendreceive.adapter;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.sendreceive.model.FileProcessingModel;
import com.wisdom.nhoa.util.RegularUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：
 * @time 2018/3/14 14:01
 * @change
 */

public class FileToDoListAdapter extends BaseAdapter {
    private Context context;
    private List<FileProcessingModel> listData;
    private String nowTime = "";
    public static final String TAG = FileToDoListAdapter.class.getSimpleName();

    public FileToDoListAdapter(Context context, List<FileProcessingModel> listData) {
        this.context = context;
        this.listData = listData;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        nowTime = sdf.format(new Date());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        FileTodoViewHolder holder;
//        if (convertView == null) {
            holder = new FileTodoViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.approval_listview_item_file_todo_list, parent, false);
            holder.img_sign = convertView.findViewById(R.id.img_sign);
            holder.tv_sign = convertView.findViewById(R.id.tv_sign);
            holder.et_sign = convertView.findViewById(R.id.et_sign);
            holder.tv_form_name = convertView.findViewById(R.id.approval_listview_item_tv_subject);
            convertView.setTag(holder);
//        }
// else {
//            holder = (FileTodoViewHolder) convertView.getTag();
//        }
        holder.tv_form_name.setText(listData.get(position).getForm_name());
//        如果表格的标题是包含时间关键字的，弹出时间选择空间
        Log.i(TAG, "表格的标题: " + listData.get(position).getForm_name());

        //其它可读可写判断
        if (ConstantString.FILE_SIGN_TYPE_TEXTW.equals(listData.get(position).getValue_type())) {
            holder.et_sign.setVisibility(View.VISIBLE);
            holder.tv_sign.setVisibility(View.GONE);
            holder.img_sign.setVisibility(View.GONE);
            holder.et_sign.setText(listData.get(position).getForm_value());
            if (listData.get(position).getForm_name().contains("时间")) {
                Log.i(TAG, "时间: " + nowTime);
                holder.et_sign.setText(nowTime);
            }
        } else if (ConstantString.FILE_SIGN_TYPE_TEXTR.equals(listData.get(position).getValue_type())) {
            holder.et_sign.setVisibility(View.GONE);
            holder.tv_sign.setVisibility(View.VISIBLE);
            holder.img_sign.setVisibility(View.GONE);
            holder.tv_sign.setText(listData.get(position).getForm_value());
        } else if (ConstantString.FILE_SIGN_TYPE_URLR.equals(listData.get(position).getValue_type())) {
            holder.et_sign.setVisibility(View.GONE);
            holder.tv_sign.setVisibility(View.GONE);
            holder.img_sign.setVisibility(View.VISIBLE);

        } else {
            holder.et_sign.setVisibility(View.GONE);
            holder.tv_sign.setVisibility(View.VISIBLE);
            holder.img_sign.setVisibility(View.GONE);
        }
        if (!"".equals(listData.get(position).getForm_value())&&
                RegularUtil.isWebUrl(listData.get(position).getForm_value())) {
            holder.et_sign.setVisibility(View.GONE);
            holder.tv_sign.setVisibility(View.GONE);
            holder.img_sign.setVisibility(View.VISIBLE);
            Glide.with(context).load(listData.get(position).getForm_value()).into(holder.img_sign);
        }

        return convertView;
    }


    class FileTodoViewHolder {
        ImageView img_sign;
        TextView tv_sign;
        EditText et_sign;
        TextView tv_form_name;
    }

}
