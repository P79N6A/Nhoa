package com.wisdom.nhoa.sendreceive.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.AttachListModel;
import com.wisdom.nhoa.sendreceive.activity.ReceiveIssueTodoDetailActivity;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.widget.fileview.FileDisplayActivity;
import com.wisdom.nhoa.widget.fileview.PreviwPicActivity;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：
 * @time 2018/3/14 15:25
 * @change
 */

public class AttachListAdapter extends BaseAdapter {
    private Context context;
    private List<AttachListModel> listData;
    public static final String TAG = ReceiveIssueTodoDetailActivity.class.getSimpleName();

    public AttachListAdapter(Context context, List<AttachListModel> listData) {
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
        AttachListHolder holder;
        if (convertView == null) {
            holder = new AttachListHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.approval_listview_item_attach_list, parent, false);
            holder.approval_todo_attach_btn_delete = convertView.findViewById(R.id.approval_todo_attach_btn_delete);
            holder.approval_todo_attach_btn_preview = convertView.findViewById(R.id.approval_todo_attach_btn_preview);
            holder.approval_todo_attach_tv_file_name = convertView.findViewById(R.id.approval_todo_attach_tv_file_name);
            holder.approval_todo_attach_tv_upload_author = convertView.findViewById(R.id.approval_todo_attach_tv_upload_author);
            holder.approval_todo_attach_tv_upload_time = convertView.findViewById(R.id.approval_todo_attach_tv_upload_time);
            convertView.setTag(holder);
        } else {
            holder = (AttachListHolder) convertView.getTag();
        }
        holder.approval_todo_attach_tv_file_name.setText(listData.get(position).getFile_real_name());
        holder.approval_todo_attach_tv_upload_time.setText(listData.get(position).getCreatetime());
        holder.approval_todo_attach_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除点击事件
                // TODO: 2018/3/14
            }
        });
        // holder.approval_todo_attach_btn_preview.setTag(position);
        holder.approval_todo_attach_btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int pos = (int) v.getTag();
                //预览
                String fileName = listData.get(position).getFile_url();
                String fileNameArray[] = fileName.split("\\.");
                Log.i(TAG, "onClick: 截取地址-----" + fileNameArray.length);
                if (fileNameArray.length > 1) {
                    String fileLastName = fileNameArray[fileNameArray.length - 1];
                    if (fileLastName.endsWith("jpg") ||
                            fileLastName.endsWith("png") ||
                            fileLastName.endsWith("jpeg") ||
                            fileLastName.endsWith("bmp") ||
                            fileLastName.endsWith("gif")
                            ) {//等待预览的文件是图片资源
                        Intent intent = new Intent(context,
                                PreviwPicActivity.class);
                        intent.putExtra("title", fileName);
                        intent.putExtra("url", listData.get(position).getFile_url());
                        Log.i(TAG, "onClick: 截取地址-----" + listData.get(position).getFile_url());
                        context.startActivity(intent);
                    } else if (fileLastName.endsWith("txt")
                            || fileLastName.endsWith("rar")
                            || fileLastName.endsWith("zip")) {
                        ToastUtil.showToast("暂不支持预览此格式的文件");
                    } else {
                        FileDisplayActivity.show(context, listData.get(position).getFile_url());
                    }
                } else {//没有拿到文件的后缀名称
                    ToastUtil.showToast("暂不支持预览此格式的文件");
                }
                // 预览点击事件
                // TODO: 2018/3/14
            }
        });

        return convertView;
    }

    class AttachListHolder {
        private TextView approval_todo_attach_tv_file_name;
        private TextView approval_todo_attach_tv_upload_time;
        private TextView approval_todo_attach_tv_upload_author;
        private Button approval_todo_attach_btn_delete;
        private Button approval_todo_attach_btn_preview;
    }


}
