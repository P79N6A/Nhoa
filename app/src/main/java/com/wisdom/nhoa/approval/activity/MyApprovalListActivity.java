package com.wisdom.nhoa.approval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.adapter.MySponsorListAdapter;
import com.wisdom.nhoa.approval.model.ApprovalListModel;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.mine.model.LoginModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.PullToRefreshLayout;
import com.wisdom.nhoa.widget.PullableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MyApprovalListActivity extends BaseActivity {

    @BindView(R.id.comm_head_title)
    TextView commHeadTitle;
    @BindView(R.id.puallableListView)
    PullableListView puallableListView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout pull;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.tv_error_data)
    TextView tvErrorData;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_my_approval_list);
    }

    @Override
    public void initViews() {
        getData(pull);
        setTitle(R.string.approval_mt_approval_list_title);
        pull.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
    }


    /**
     * 请求接口获得列表数据
     */
    private void getData(final PullToRefreshLayout pullToRefreshLayout) {
        U.showLoadingDialog(this);
        String token = ((LoginModel) SharedPreferenceUtil
                .getConfig(this)
                .getSerializable(ConstantString.USER_INFO))
                .getAccess_token();
        HttpParams httpParams = new HttpParams();
        httpParams.put("appkey", ConstantString.APP_KEY);
        httpParams.put("access_token", token);
        HttpUtil.httpGet(ConstantUrl.MY_APPROVAL_LIST, httpParams, new JsonCallback<BaseModel<List<ApprovalListModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                tvNoData.setVisibility(View.GONE);
                tvErrorData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(final BaseModel<List<ApprovalListModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    puallableListView.setAdapter(new MySponsorListAdapter(MyApprovalListActivity.this, listBaseModel.results));
                    //子项点击事件，跳转到详情页面
                    puallableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", listBaseModel.results.get(position));
                            Intent intent = new Intent(MyApprovalListActivity.this, MySponsorDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    tvNoData.setVisibility(View.VISIBLE);
                    tvErrorData.setVisibility(View.GONE);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });


    }

    /**
     * 加载出错的点击事件
     */
    @OnClick(R.id.tv_error_data)
    public void onViewClicked() {
        getData(pull);
    }
}
