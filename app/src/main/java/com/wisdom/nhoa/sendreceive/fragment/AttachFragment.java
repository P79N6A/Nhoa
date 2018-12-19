package com.wisdom.nhoa.sendreceive.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.AttachListModel;
import com.wisdom.nhoa.sendreceive.adapter.AttachListAdapter;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.ListViewForScrollView;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.fragment
 * @class describe： 附件材料
 * @time 2018/3/13 15:57
 * @change
 */

public class AttachFragment extends Fragment {
    private ListViewForScrollView listViewForScrollView;
    private String insid = "";
    private MyReceiver receiver;
    private TextView tv_no_data_hint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.approval_fragment_attach_list, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化界面控件
     */
    private void initView(View view) {
        listViewForScrollView = view.findViewById(R.id.approval_lv_attach_list);
        tv_no_data_hint = view.findViewById(R.id.tv_no_data_hint);
        receiver = new MyReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter("com.sendrecevice.fragment.AttachFragment"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            insid = intent.getStringExtra("insid");
            getdata();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁广播
        getContext().unregisterReceiver(receiver);
    }

    //获取附件材料
    private void getdata() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(getContext()).getAccess_token());
        params.put("insid", insid);
        HttpUtil.httpGet(ConstantUrl.BACKLOGDETAILS_FILE, params, new JsonCallback<BaseModel<List<AttachListModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<AttachListModel>> meetingSignModelBaseModel, Call call, Response response) {
                if (0 == meetingSignModelBaseModel.results.size()) {
                    //暂时没有文件
                    tv_no_data_hint.setVisibility(View.VISIBLE);
                    listViewForScrollView.setVisibility(View.GONE);
                } else {
                    //有可以预览的文件
                    tv_no_data_hint.setVisibility(View.GONE);
                    listViewForScrollView.setVisibility(View.VISIBLE);
                    AttachListAdapter adapter = new AttachListAdapter(getContext(), meetingSignModelBaseModel.results);
                    listViewForScrollView.setAdapter(adapter);
                }
                U.closeLoadingDialog();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }
}
