package com.wisdom.nhoa.sendreceive.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.sendreceive.adapter.SendIssueTodoModelAdapter;
import com.wisdom.nhoa.sendreceive.model.SendManageModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.activity
 * @class describe：
 * @time 2018/3/8 10:49
 * @change
 */

public class SendIssueTodoListActivity extends BaseActivity implements SendIssueTodoModelAdapter.IssueTodoItemClickListener {
    @BindView(R.id.approval_recycler_view)
    RecyclerView approvalRecyclerView;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    public static final String TAG = SendIssueTodoListActivity.class.getSimpleName();
    private List<SendManageModel> list = new ArrayList<>();

    @Override
    public void initViews() {
        setTitle(R.string.approval_issue_todo);
        getdata();
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.approval_activity_issue_todo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            getdata();
        }
    }

    /**
     * 列表的子项点击事件
     *
     * @param recyclerView
     * @param pos
     * @param clickView
     * @param
     */

    @Override
    public void onIssueTodoItemClicked(RecyclerView recyclerView, int pos, View clickView, SendManageModel sendManageModel) {
        Intent intent = new Intent(this, SendIssueTodoDetailActivity.class);
        intent.putExtra("insid", list.get(pos).getInsid());
        intent.putExtra("doctitle", list.get(pos).getDoctitle());
        intent.putExtra("docnumber", list.get(pos).getDocnumber());
        startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
    }

    private void getdata() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(SendIssueTodoListActivity.this).getAccess_token());
        params.put("listtype", ConstantString.SEND_RECEIVE_LIST_TYPE_SEND);
        params.put("doctype", ConstantString.SEND_RECEIVE_DOC_TYPE_SEND);
        params.put("pagenum", 1);
        HttpUtil.httpGet(ConstantUrl.RECEIVE_SEND_ISSUE_MANAGMENT, params, new JsonCallback<BaseModel<List<SendManageModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<SendManageModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                list = listBaseModel.results;
                if (list.size() == 0) {
                    tv_hint.setVisibility(View.VISIBLE);
                    approvalRecyclerView.setVisibility(View.GONE);
                } else {
                    tv_hint.setVisibility(View.GONE);
                    approvalRecyclerView.setVisibility(View.VISIBLE);
                    SendIssueTodoModelAdapter adapter = new SendIssueTodoModelAdapter(SendIssueTodoListActivity.this, list);
                    approvalRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setIssueTodoItemClickListener(SendIssueTodoListActivity.this);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

}
