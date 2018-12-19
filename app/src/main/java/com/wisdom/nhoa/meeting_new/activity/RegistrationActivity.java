package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.meeting_new.adapter.RegistrationAdapter;
import com.wisdom.nhoa.meeting_new.model.RegistrationModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 类 {类名称}  { 会议签到页}
 *
 * @author lxd
 * @date 2018-10-29
 */
public class RegistrationActivity extends BaseActivity implements RegistrationAdapter.OnChildItemClickedListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    RegistrationAdapter adapter;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_registration);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_sign_meeting);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();

            }
        });
        refreshLayout.autoRefresh();
    }

    /**
     * 取得服务器数据适配到界面
     */
    private void getData() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GET_MEETING_LIST_URL, params, new JsonCallback<BaseModel<List<RegistrationModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<RegistrationModel>> model, Call call, Response response) {
                refreshLayout.finishRefresh();
                if (model.results.size() > 0) {
                    //有数据项
                    tv_no_data.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    adapter = new RegistrationAdapter(model.results, RegistrationActivity.this);
                    adapter.setListener(RegistrationActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                refreshLayout.finishRefresh();
                tv_no_data.setVisibility(View.VISIBLE);
            }


        });
    }

    @Override
    public void onChildItemClick(RecyclerView recyclerView, int pos, View view, RegistrationModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        Intent intent = new Intent(this, MeetingSignDetailActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            refreshLayout.autoRefresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

