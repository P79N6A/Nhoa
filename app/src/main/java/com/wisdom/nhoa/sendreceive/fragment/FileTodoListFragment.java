package com.wisdom.nhoa.sendreceive.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.sendreceive.adapter.FileToDoListAdapter;
import com.wisdom.nhoa.sendreceive.model.FileProcessingModel;
import com.wisdom.nhoa.sendreceive.model.FormIdValueModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.fragment
 * @class describe：文件处理单
 * @time 2018/3/13 15:33
 * @change
 */

public class FileTodoListFragment extends Fragment {
    private ListViewForScrollView listViewForScrollView;
    private String TAG = FileTodoListFragment.class.getSimpleName();
    private String insid = "";
    private String processkey = "";
    private String nodeid = "";
    private MyReceiver receiver;
    private String stringJsonResult;
    String resultJson;
    private RefreshReceiver refreshReceiver;
    private List<FileProcessingModel> listData = new ArrayList<>();
    FileToDoListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.approval_fragment_file_todo_list, container, false);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            insid = bundle.getString("insid");
            processkey = bundle.getString("processkey");
            nodeid = bundle.getString("nodeid");
        }
        receiver = new MyReceiver();
        refreshReceiver = new RefreshReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(ConstantString.BROADCAST_INSID_TAG));
        getActivity().registerReceiver(refreshReceiver, new IntentFilter(ConstantString.BROADCAST_REFRESH_TAG));
        initViews(view);
        //getdata();
        return view;
    }


    private class MyReceiver extends BroadcastReceiver {
        /**
         * 接数据参数的Receiver
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: 接收到广播");
            insid = intent.getStringExtra("insid");
            processkey = intent.getStringExtra("processkey");
            nodeid = intent.getStringExtra("nodeid");
            getdata();
        }
    }

    /**
     * 接到广播，刷新界面数据
     */
    private class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getdata();
        }
    }

    /**
     * 初始化类内相关控件
     *
     * @param view
     */
    private void initViews(View view) {
        listViewForScrollView = view.findViewById(R.id.approval_lv_list);
        ConstantString.listViewForScrollView = this.listViewForScrollView;
        Log.i(TAG, "listViewForScrollView初始化后: " + ConstantString.listViewForScrollView);
    }

    //获取文件处理
    private void getdata() {
//        U.showLoadingDialog(getContext());
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(getContext()).getAccess_token());
        params.put("insid", insid);
        params.put("processkey", processkey);
        params.put("nodeid", nodeid);
        HttpUtil.httpGet(ConstantUrl.FILEHANDINGSHEET, params, new JsonCallback<BaseModel<List<FileProcessingModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<FileProcessingModel>> listBaseMdoel, Call call, Response response) {
                listData = listBaseMdoel.results;
                adapter = new FileToDoListAdapter(getContext(), listBaseMdoel.results);
                listViewForScrollView.setAdapter(adapter);
                U.closeLoadingDialog();
                getFormIds(listBaseMdoel.results);
                ConstantString.fileProcessingModelList = listData;
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

    /**
     * 遍历数据源，拼接满足条件的formId，将拼接好的id
     * 给签字上传图片接口
     *
     * @param results
     */
    private void getFormIds(List<FileProcessingModel> results) {
        String formIds = "";
        for (int j = 0; j < results.size(); j++) {
            if (ConstantString.FILE_SIGN_TYPE_URLW.equals(results.get(j).getValue_type())) {
                formIds += results.get(j).getForm_id() + ",";
            }
        }
        if (!"".equals(formIds)) {
            formIds = formIds.substring(0, formIds.length() - 1);
        }
        ConstantString.FORM_ID = formIds;
    }

    @Override
    public void onDestroyView() {
        BroadCastManager.getInstance().unregisterReceiver(getActivity(), receiver);
        BroadCastManager.getInstance().unregisterReceiver(getActivity(), refreshReceiver);
        super.onDestroyView();
    }

    public String getUserJson() {
//        return makeUserJson(listData);
        return makeUserJson(ConstantString.fileProcessingModelList);
    }

    /**
     * 构造发送接口数据源
     */
    public String makeUserJson(List<FileProcessingModel> listModel) {
        List<FormIdValueModel> formIdValueModels = new ArrayList<>();
        Log.i(TAG, "listModel: " + listModel.toString());
        String result1 = "";
        for (int k = 0; k < listModel.size(); k++) {
            Log.i(TAG, "TYPE: " + listModel.get(k).getValue_type());
            if (ConstantString.FILE_SIGN_TYPE_TEXTW.equals(
                    listModel.get(k).getValue_type())) {
                FormIdValueModel model = new FormIdValueModel();
                //用户填写的可写状态
                View view = getViewByPosition(k, ConstantString.listViewForScrollView);
                EditText editText = view.findViewById(R.id.et_sign);
                model.setForm_value(StrUtils.getEdtTxtContent(editText));
                model.setForm_id(listModel.get(k).getForm_id());
                formIdValueModels.add(model);
            }
        }
        Log.i(TAG, "formIdValueModels.size: " + formIdValueModels.size());
        if (formIdValueModels.size() == 0) {
            //没有可供用户填写的东西
            result1 = "space";
        } else {
            //有可供用户填写的东西
            result1 = new Gson().toJson(formIdValueModels).toString();
        }
        Log.i(TAG, "makeUserJson: " + result1);
        return result1;
    }


    /**
     * 根据索引获得listView的某一条目
     *
     * @param pos
     * @param listView
     * @return
     */
    public View getViewByPosition(int pos, ListView listView) {
        Log.i(TAG, "listView: " + listView);
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


}
