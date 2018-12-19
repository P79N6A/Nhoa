package com.wisdom.nhoa.approval.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisdom.nhoa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveTypeAdapter extends BaseAdapter {
	private Context context;
	private JSONArray jsonArray;
	private String iscyjg = "";
	public LeaveTypeAdapter(Context context, JSONArray jsonArray) {
		super();
		this.context = context;
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount() {
		if (jsonArray.length() != 0) {
			return jsonArray.length();
		} else {
			return 0;
		}
	}

	public void iscyjg(String iscyjg){
		this.iscyjg  = iscyjg;
	}
	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {// 没有复用的item，创建item
			convertView = View.inflate(context, R.layout.item_leave_type, null);
			holder = new ViewHolder();
			holder.tv_sp_banlishixiang = (TextView) convertView.findViewById(R.id.tv_banlishixiang);// 初始化组件
			convertView.setTag(holder);// 绑定
		} else {// 有可复用的item
			holder = (ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject;
		try {
			jsonObject = jsonArray.getJSONObject(position);
			holder.tv_sp_banlishixiang.setText(jsonObject.getString("name")+ "");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_sp_banlishixiang;
	}


}
